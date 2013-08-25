package com.jpmiii.Realmscraft;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.Location;

import com.jpmiii.Realmscraft.Realmscraft;
public class RealmscraftListener  implements Listener {
	private Realmscraft plugin;
	

    public RealmscraftListener(Realmscraft plugin) {
    	this.plugin = plugin;
	}
    

    
    @EventHandler(priority = EventPriority.MONITOR)
    public void normalLogin(PlayerLoginEvent event) {

        //Realmscraft.combatApi.tagPlayer(event.getPlayer().getDisplayName());
    	try {
			Realmscraft.dbm.loadPlayer(event.getPlayer());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void portalStep(PlayerInteractEvent event) {
    	
    	if (event.getAction() == Action.PHYSICAL) {
    		Location portalLoc = new  Location(event.getPlayer().getWorld(),0.0,61.0, 0.0) ;
    		if (event.getClickedBlock().getLocation() ==  portalLoc){//.getBlockX() == 150 && event.getClickedBlock().getLocation().getBlockY() == 61 && event.getClickedBlock().getLocation().getBlockZ() == 150  ) {
    		
    		    Realmscraft.dbm.savePlayer(event.getPlayer());
    		
			    ByteArrayOutputStream b = new ByteArrayOutputStream();
			    DataOutputStream out = new DataOutputStream(b);
			 
			    try {
			        out.writeUTF("Connect");
			        out.writeUTF("w3"); // Target Server
			    } catch (IOException e) {
			        // Can never happen
			    }
			    event.getPlayer().sendPluginMessage(this.plugin, "BungeeCord", b.toByteArray());
    		    this.plugin.getLogger().info(event.getClickedBlock().getLocation().toString());
    		}
    	}

       // Realmscraft.combatApi.tagPlayer(event.getPlayer().getDisplayName());
    	
 
        
    }
	

}
