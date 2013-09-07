package com.jpmiii.Realmscraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.trc202.CombatTag.CombatTag;
import com.trc202.CombatTagApi.CombatTagApi;

import tk.manf.InventorySQL.manager.DatabaseManager;

public final class Realmscraft extends JavaPlugin implements
		PluginMessageListener {
	public Permission perms = null;
	public static Chat chat = null;
	public CombatTagApi combatApi = null;
	public Player lastPlayer = null;

	public static DatabaseManager dbm = null;
	public Location portalLoc = null;
	public HashMap<String, Long> hotPlayers = new HashMap<String, Long>();

	public void onEnable() {
		// getLogger().info("onEnable has been invoked!");

		this.saveDefaultConfig();

		this.getServer().getMessenger()
				.registerIncomingPluginChannel(this, "BungeeCord", this);

		getServer().getPluginManager().registerEvents(
				new RealmscraftListener(this), this);
		setupPermissions();

		setupCombatApi();
		if (this.getConfig().getBoolean("useSQL")) {
			dbm = DatabaseManager.getInstance();
		}

		getServer().getMessenger().registerOutgoingPluginChannel(this,
				"BungeeCord");
		getLogger().info("[Bungee] Plugin channel registered!");
		BukkitTask task = new RealmscraftTask(this).runTaskTimer(this, 1200,
				1200); // .runTaskLater(this, 20);
		randLoc();

	}

	public void onDisable() {
		getLogger().info("onDisable has been invoked!");
	}

	public static String playerName; // Example: using the GetServer subchannel

	@Override
	public void onPluginMessageReceived(String channel, Player player,
			byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(
				message));
		String subchannel;
		try {
			subchannel = in.readUTF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			subchannel = "inv";
		}
		if (subchannel.equals("inv")) {
			try {
				playerName = in.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (this.getConfig().getBoolean("useSQL")) {
					dbm.loadPlayer(getServer().getPlayer(playerName));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void randLoc() {
		World wld = getServer().getWorld(
				this.getConfig().getString("worldName"));
		Integer xloc = (this.getConfig().getInt("minX") + (int) (Math.random() * ((this
				.getConfig().getInt("maxX") - this.getConfig().getInt("minX")) + 1)));
		Integer zloc = (this.getConfig().getInt("minZ") + (int) (Math.random() * ((this
				.getConfig().getInt("maxZ") - this.getConfig().getInt("minZ")) + 1)));
		Integer yloc = wld.getHighestBlockYAt(xloc, zloc);

		this.portalLoc = new Location(wld, xloc, yloc, zloc);
	}

	private boolean setupCombatApi() {
		if (getServer().getPluginManager().getPlugin("CombatTag") != null) {
			combatApi = new CombatTagApi((CombatTag) getServer()
					.getPluginManager().getPlugin("CombatTag"));

		}
		return combatApi != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer()
				.getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("rc")) {
			// doSomething

			if (!(sender instanceof Player)) {
				this.reloadConfig();
				getLogger().info("config reloaded");
				return true;
			}

			Player player = (Player) sender;
			if (args.length > 0) {

				if (args[0].equalsIgnoreCase("s")) {
					if (perms.has(player, "realmscraft.s")
							&& !this.getConfig().getString("portalServer")
									.isEmpty()) {

						getServer().getPlayer(player.getName())
								.updateInventory();

						ByteArrayOutputStream b = new ByteArrayOutputStream();
						DataOutputStream out = new DataOutputStream(b);

						try {
							out.writeUTF("Connect");
							out.writeUTF(this.getConfig().getString(
									"portalServer")); // Target Server
						} catch (IOException e) {
							// Can never happen
						}
						player.sendPluginMessage(this, "BungeeCord",
								b.toByteArray());
						if (this.getConfig().getBoolean("useSQL")) {
							dbm.savePlayer(player);
						}
					}

					return true;
				}

				if (args[0].equalsIgnoreCase("c")) {
					player.setCompassTarget(this.portalLoc);

					return true;
				}
				if (args[0].equalsIgnoreCase("x")) {
					if (player.isOp()) {
						player.teleport(this.lastPlayer);
					}
					return true;
				}

			}

			// getLogger().info(args[0]);

		} // If this has happened the function will return true.
			// If this hasn't happened the a value of false will be returned.
		return false;
	}
}
