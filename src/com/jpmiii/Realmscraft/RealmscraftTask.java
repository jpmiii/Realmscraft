package com.jpmiii.Realmscraft;


import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;



import com.jpmiii.Realmscraft.Realmscraft;
public class RealmscraftTask extends BukkitRunnable {
    private final Realmscraft plugin;
    
    public RealmscraftTask(Realmscraft plugin) {
        this.plugin = plugin;
    }
 
    public void run() {

    	for (Map.Entry<String, Long> entry : plugin.hotPlayers.entrySet()) {
    	    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
    		if (entry.getValue()+180000 > System.currentTimeMillis()){
    			plugin.hotPlayers.remove(entry.getKey());
    		}
    	}
    }
}
