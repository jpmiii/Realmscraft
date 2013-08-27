package com.jpmiii.Realmscraft;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.jpmiii.Realmscraft.Realmscraft;
public class RealmscraftListener  implements Listener {
	private Realmscraft plugin;
	

    public RealmscraftListener(Realmscraft plugin) {
    	this.plugin = plugin;
	}
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void spawn(CreatureSpawnEvent event) {
    	Location el = event.getLocation();
    	//SpawnReason sr = event.getSpawnReason();
    	EntityType et = event.getEntity().getType();
    	if (el.getWorld().getName().equalsIgnoreCase("world")){
    		
    		//if(sr == SpawnReason.NATURAL || sr == SpawnReason.DEFAULT || sr == SpawnReason.CHUNK_GEN) {
    			

    	        if (et == EntityType.CREEPER || et == EntityType.ENDERMAN || 
    	        		et == EntityType.SPIDER || et == EntityType.SKELETON ||
    	        		et == EntityType.ZOMBIE) {
    	        	if (el.getY() > 55 || el.getBlock().getLightLevel() > 11) {
		                event.setCancelled(true);
		            }


     		  //  }   	   
    	    }
    	    
    	}
    	if (et == EntityType.BAT ||
    			et == EntityType.ENDER_DRAGON ||
    			et == EntityType.IRON_GOLEM ||
    			et == EntityType.OCELOT ||
    			et == EntityType.SILVERFISH ||
    			et == EntityType.VILLAGER ||
    			et == EntityType.WITCH ||
    			et == EntityType.WOLF ){
    		event.setCancelled(true);
    	}

    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void normalLogin(PlayerLoginEvent event) {


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
    		plugin.portalLoc = new  Location(event.getPlayer().getWorld(),0,61, 0) ;
    		if (event.getClickedBlock().getLocation().distance(plugin.portalLoc) <=  5 && !plugin.hotPlayers.containsKey(event.getPlayer().getName())){
    			if(plugin.perms.has(event.getPlayer(), "realmscraft.portal")) {
    		        Realmscraft.dbm.savePlayer(event.getPlayer());
    		        
    		        plugin.hotPlayers.put(event.getPlayer().getName(), System.currentTimeMillis( ));
    		
			        ByteArrayOutputStream b = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(b);
			 
			        try {
			            out.writeUTF("Connect");
			            out.writeUTF(plugin.getConfig().getString("portalServer")); // Target Server
			        } catch (IOException e) {
			        // Can never happen
			        }
			        event.getPlayer().sendPluginMessage(this.plugin, "BungeeCord", b.toByteArray());
    		        this.plugin.getLogger().info(event.getClickedBlock().getLocation().toString());
    		    }
    		}
    	}

  	
 
        
    }
	
    @EventHandler
    public void PlayerBed(PlayerBedEnterEvent event) {
    	String[] msg = {"Would you like to goto", "Dream Land?"};
        
    	event.getPlayer().sendMessage(msg);
    	//plugin.getLogger().info(event.getPlayer().getName() + " lay in bed");
    }
    @EventHandler
    public void PlayerYes(AsyncPlayerChatEvent event) {
    	if (plugin.perms.has(event.getPlayer(), "realmscraft.sleep")) {
    		//plugin.getLogger().info(event.getPlayer().getName() + " has permission");
    	
    	    if (event.getPlayer().isSleeping()) {
    	    	//plugin.getLogger().info(event.getPlayer().getName() + " has said" + event.getMessage());

    	    	if(event.getMessage().equalsIgnoreCase("yes")) {
    		
    	    		plugin.getLogger().info(event.getPlayer().getName() + " has said" + event.getMessage());
    	    		ByteArrayOutputStream b = new ByteArrayOutputStream();
    		        DataOutputStream out = new DataOutputStream(b);
    		 
    	    	    try {
    		            out.writeUTF("Connect");
    		            out.writeUTF(plugin.getConfig().getString("sleepServer")); // Target Server
    		        } catch (IOException e) {
    		    // Can never happen
    		        }
    		        event.getPlayer().sendPluginMessage(this.plugin, "BungeeCord", b.toByteArray());
    		    
    	    	}
    	    }
    	}
    	
    }
}
