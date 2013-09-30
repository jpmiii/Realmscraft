 package com.jpmiii.Realmscraft;



import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.jpmiii.Realmscraft.Realmscraft;

public class RealmscraftTask extends BukkitRunnable {
	private final Realmscraft plugin;

	public RealmscraftTask(Realmscraft plugin) {
		this.plugin = plugin;
	}

	public void run() {
		// plugin.getLogger().info("portal moved " +
		// plugin.portalLoc.toString());
		


		if (Math.random() > plugin.getConfig().getDouble("portalMoveChance")) {
			plugin.randLoc();
			for (Player ply : plugin.getServer()
					.getWorld(plugin.getConfig().getString("worldName"))
					.getPlayers()) {
				ply.setCompassTarget(plugin.portalLoc);
			}
			plugin.getLogger().info(
					"portal moved " + plugin.portalLoc.toString());
		}
	}
}
