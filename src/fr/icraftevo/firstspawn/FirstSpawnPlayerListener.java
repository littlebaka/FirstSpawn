package fr.icraftevo.firstspawn;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FirstSpawnPlayerListener extends PlayerListener {
	
	public static final Logger log = Logger.getLogger( "Minecraft" );
	HashMap<String, Integer> login = new HashMap<String, Integer>();
	private FirstSpawn plugin;
		
	
	public FirstSpawnPlayerListener(FirstSpawn plugin){
		this.plugin = plugin;
	}
	
	//Quand le joueur se connecte
	public void onPlayerLogin ( PlayerLoginEvent e ) {
		Player p = e.getPlayer();
		String name = p.getName();
		//start modif
		File groupsfile = new File( "plugins/PermissionsBukkit/config.yml");
		 users.name.groups
		//END MODIF
		boolean exists = file.exists();
		boolean alreadyspawned = ;
		if ( exists && !alreadyspawned ) { //Si le joueur est dans le group X et s'il n'est pas dans le dossier "AlreadySpawned"
			login.put( name, 1 );
			System.out
					.println( "[HeroSpawn] "
							+ name
							+ ": logged in for first time. Teleporting them to First Spawn" );

		} else {

			if ( login.containsKey( name ) )
				login.remove( name );
		}
	}

}
