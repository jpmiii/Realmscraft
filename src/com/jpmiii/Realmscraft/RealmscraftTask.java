package com.jpmiii.Realmscraft;


import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;



import com.jpmiii.Realmscraft.Realmscraft;
public class RealmscraftTask extends BukkitRunnable {
    private final Realmscraft plugin;
    
    public RealmscraftTask(Realmscraft plugin) {
        this.plugin = plugin;
    }
 
    public void run() {
    	//plugin.getLogger().info("portal moved " + plugin.portalLoc.toString());

    	for (Map.Entry<String, Long> entry : plugin.hotPlayers.entrySet()) {
    	    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
    		if (entry.getValue()+180000 > System.currentTimeMillis()){
    			plugin.hotPlayers.remove(entry.getKey());
    		}
    	}
    	if(Math.random() > plugin.getConfig().getDouble("portalMoveChance")){
    		plugin.randLoc();
    		for (Player ply : plugin.getServer().getWorld(plugin.getConfig().getString("worldName")).getPlayers()){
    			ply.setCompassTarget(plugin.portalLoc);
    		}
    		plugin.getLogger().info("portal moved " + plugin.portalLoc.toString());
    	}
    }
}
