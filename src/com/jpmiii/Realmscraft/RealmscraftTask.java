package com.jpmiii.Realmscraft;


import org.bukkit.scheduler.BukkitRunnable;

import com.jpmiii.Realmscraft.Realmscraft;
public class RealmscraftTask extends BukkitRunnable {
    private final Realmscraft plugin;
    
    public RealmscraftTask(Realmscraft plugin) {
        this.plugin = plugin;
    }
 
    public void run() {
        // What you want to schedule goes here
        //plugin.getServer().broadcastMessage("Welcome to Bukkit! Remember to read the documentation!");
    	
    }
}
