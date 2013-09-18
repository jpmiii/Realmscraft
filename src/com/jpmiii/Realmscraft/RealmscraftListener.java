package com.jpmiii.Realmscraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;

import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import com.vexsoftware.votifier.model.VotifierEvent;
import com.vexsoftware.votifier.model.Vote;

import com.jpmiii.Realmscraft.Realmscraft;

public class RealmscraftListener implements Listener {
	private Realmscraft plugin;

	public RealmscraftListener(Realmscraft plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void normalLogin(PlayerLoginEvent event) {
		if (plugin.getConfig().getBoolean("useSQL")) {

			try {
				Realmscraft.dbm.loadPlayer(event.getPlayer());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		event.getPlayer().setCompassTarget(plugin.portalLoc);
		plugin.lastPlayer = event.getPlayer();

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void portalStep(PlayerInteractEvent event) {

		if (event.getAction() == Action.PHYSICAL
				&& !plugin.getConfig().getString("portalServer").isEmpty()) {

			Location l = event.getClickedBlock().getLocation();
			if (l.getWorld().getName() == plugin.getConfig().getString(
					"worldName")) {
				if (l.distance(plugin.portalLoc) <= 5
						&& !plugin.hotPlayers.containsKey(event.getPlayer()
								.getName())) {
					if (plugin.perms.has(event.getPlayer().getPlayer(),
							"realmscraft.portal")) {
						plugin.getServer()
								.getPlayer(event.getPlayer().getName())
								.updateInventory();
						if (plugin.getConfig().getBoolean("useSQL")) {
							Realmscraft.dbm.savePlayer(event.getPlayer());
						}
						plugin.hotPlayers.put(event.getPlayer().getName(),
								System.currentTimeMillis());

						ByteArrayOutputStream b = new ByteArrayOutputStream();
						DataOutputStream out = new DataOutputStream(b);

						try {
							out.writeUTF("Connect");
							out.writeUTF(plugin.getConfig().getString(
									"portalServer")); // Target Server
						} catch (IOException e) {
							// Can never happen
						}
						event.getPlayer().sendPluginMessage(this.plugin,
								"BungeeCord", b.toByteArray());
						this.plugin.getLogger().info(
								event.getClickedBlock().getLocation()
										.toString());
						if (plugin.getConfig().getBoolean("useSQL")) {
							Realmscraft.dbm.savePlayer(event.getPlayer());
						}
					}
				}
			}
		}

	}

	@EventHandler
	public void PlayerBed(PlayerBedEnterEvent event) {
		if (plugin.perms.has(event.getPlayer(), "realmscraft.sleep")
				|| ((plugin.getCustomConfig().getLong(
						"players." + event.getPlayer().getName()) + 86400000) < System
						.currentTimeMillis())
				&& !plugin.getConfig().getString("sleepServer").isEmpty()) {
			String[] msg = { plugin.getConfig().getString("sleepMsg") };
			event.getPlayer().sendMessage(msg);

		}

	}

	@EventHandler
	public void PlayerYes(AsyncPlayerChatEvent event) {
		if (plugin.perms.has(event.getPlayer(), "realmscraft.sleep")
				|| ((plugin.getCustomConfig().getLong(
						"players." + event.getPlayer().getName()) + 86400000) < System
						.currentTimeMillis())
				&& !plugin.getConfig().getString("sleepServer").isEmpty()) {
			// plugin.getLogger().info(event.getPlayer().getName() +
			// " has permission");

			if (event.getPlayer().isSleeping()) {
				// plugin.getLogger().info(event.getPlayer().getName() +
				// " has said" + event.getMessage());

				if (event.getMessage().equalsIgnoreCase("yes")) {

					plugin.combatApi.untagPlayer(event.getPlayer().getName());
					ByteArrayOutputStream b = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(b);

					try {
						out.writeUTF("Connect");
						out.writeUTF(plugin.getConfig()
								.getString("sleepServer")); // Target Server
					} catch (IOException e) {
						// Can never happen
					}
					event.getPlayer().sendPluginMessage(this.plugin,
							"BungeeCord", b.toByteArray());

				}
			}
		}

	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onVotifierEvent(VotifierEvent event) {
		plugin.getServer().getLogger().info("votifier event");
		if (plugin.getConfig().getBoolean("voteServer")) {
			Vote vote = event.getVote();
			if (!(vote.getUsername() == null)) {
				String uname = vote.getUsername();
				uname = uname.replace(" ", "");
				if (!(plugin.getServer().getPlayer(uname) == null)
						&& !plugin.getCustomConfig().contains(uname)) {
					plugin.dreamPlayers.put(uname,
							(Object) System.currentTimeMillis());
					plugin.getCustomConfig().createSection("players",
							plugin.dreamPlayers);
					plugin.saveCustomConfig();
					plugin.getServer().getPlayer(uname)
							.sendMessage("sleep added");
					plugin.getLogger().info("sleep added " + uname);

				} else if (plugin.getServer().getOfflinePlayer(uname)
						.hasPlayedBefore()) {
					plugin.dreamPlayers.put(uname,
							(Object) System.currentTimeMillis());
					plugin.getCustomConfig().createSection("players",
							plugin.dreamPlayers);
					plugin.saveCustomConfig();
					plugin.getLogger().info(
							"sleep added offline player " + uname);
				} else {
					plugin.getLogger().warning(uname + ":  player not found");
				}

			} else {
				plugin.getLogger().warning("no player for sleep");
			}

		}

	}
}
