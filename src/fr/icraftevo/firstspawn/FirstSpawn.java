package fr.icraftevo.firstspawn;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.Map;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

public class FirstSpawn extends JavaPlugin {
	
	public static final String PERM_TP = "FirstSpawn.tp";
	public static final String PERM_SET = "FirstSpawn.set";
	static String mainDirectory = "plugins/FirstSpawn";
	File file = new File (mainDirectory + File.separator + "config.yml");
	
	private final FirstSpawnPlayerListener playerListener = new FirstSpawnPlayerListener(this);
	
	Logger log = Logger.getLogger("Minecraft");
	
	public static Permission permission = null;
	private String group;
	public boolean UsePermissions;
	
	@Override
	public void onDisable() {
		log.info("[FirstSpawn] Version 0.1 Disabled");

	}

	@Override
	public void onEnable() {
		setupPermissions();
		
		//Creation du fichier config.yml
		if (!new File(getDataFolder(), "config.yml").exists()) {
			getDataFolder().mkdirs();
			getServer().getLogger().info("[FirstSpawn] Generating default configuration");
			writeDefaultConfiguration();
		}
		
		//Events
		group = read ("group");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent( Event.Type.PLAYER_LOGIN, playerListener,Event.Priority.Highest, this );
		pm.registerEvent( Event.Type.PLAYER_JOIN, playerListener,Event.Priority.Highest, this );
		pm.registerEvent( Event.Type.PLAYER_RESPAWN, playerListener,Event.Priority.Highest, this );
		
		//enable OK
		log.info("[FirstSpawn] Version 0.1 Enabled");

	}
	
	public void write ( String root, Object x ) {
		Configuration config = load();
		config.setProperty( root, x );
		config.save();
	}

	public String read ( String root ) {
		Configuration config = load();
		return config.getString( root );
	}

	public Configuration load () {

		try {
			Configuration config = new Configuration( file );
			config.load();
			return config;

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void setupPermissions () {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
            UsePermissions = true;
            System.out.println( "[FirstSpawn] Permissions system detected!" );
        } else {
        	UsePermissions = false;
        	log.info( "Permission system not detected, defaulting to OP" );
        }
	}
	
	public boolean canSetSpawn ( Player p ) {
		if ( UsePermissions ) {
			return permission.has( p, PERM_SET );
		}
		return p.isOp();
	}

	public boolean canTP ( Player p ) {
		if ( UsePermissions ) {
			return permission.has( p, PERM_TP );
		}
		return p.isOp();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
	           sender.sendMessage(ChatColor.RED + "Cette commande peut uniquement etre execute par un joueur");
	           return true;
	        }
	        Player player = (Player) sender;
		
		if (cmd.getName().equalsIgnoreCase("sf")){
			{
				if ( !canTP( player ) ) {
					player.sendMessage( ChatColor.RED
							+ "You don't have permission to do that!" );
					return true;
				}
				double z = Double.parseDouble( read( player.getWorld()
						.getName() + ".Z" ) );
				double x = Double.parseDouble( read( player.getWorld()
						.getName() + ".X" ) );
				double y = Double.parseDouble( read( player.getWorld()
						.getName() + ".Y" ) );
				Location loc = new Location( player.getWorld(), x, y, z );
				player.teleport( loc );
				player.sendMessage( "¤f[¤9HeroSpawn¤f]¤e Welcome to First Spawn" );
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("setsf")){
			{
				if ( !canSetSpawn( player ) ) {
					player.sendMessage( ChatColor.RED
							+ "You don't have permission to do that!" );
					return true;
				}
				String x1 = Double.toString( player.getLocation().getX() );
				String y1 = Double.toString( player.getLocation().getY() );
				String z1 = Double.toString( player.getLocation().getZ() );
				write( player.getWorld().getName() + ".X", x1 );
				write( player.getWorld().getName() + ".Y", y1 );
				write( player.getWorld().getName() + ".Z", z1 );
				player.sendMessage( ChatColor.BLUE + "¤f[¤9FirstSpawn¤f] "
						+ ChatColor.YELLOW
						+ "Les joeurs vont maintenant spawner ici lors de leur premier login" );
			}
			return true;
		}
		return false; 
	}
	
	public Group getGroup(String groupName) {
        if (getNode("groups") != null) {
            for (String key : getNode("groups").getKeys()) {
                if (key.equalsIgnoreCase(groupName)) {
                    return new Group(this, key);
                }
            }
        }
        return null;
    }
	
	protected ConfigurationNode getNode(String child) {
        return getNode("", child);
    }
	
	/**
     * Returns a list of groups a player is in.
     * @param playerName The name of the player.
     * @return The groups this player is in. May be empty.
     */
    public List<Group> getGroups(String playerName) {
        ArrayList<Group> result = new ArrayList<Group>();
        if (getNode("users." + playerName) != null) {
            for (String key : getNode("users." + playerName).getStringList("groups", new ArrayList<String>())) {
                result.add(new Group(this, key));
            }
        } else {
            result.add(new Group(this, "default"));
        }
        return result;
    }
	
	 private void writeDefaultConfiguration() {
	        HashMap<String, Object> user = new HashMap<String, Object>();

	        users.put("littlebaka", user);

	        getConfiguration().setProperty("users", users);

	        getConfiguration().setHeader(
	            "# Liste des joueurs qui se sont deja connecté en tant que : ",
	            "# ",
	            "");
	        getConfiguration().save();
	    }
	
	public String getGroup () {
		return group;
	}

}
