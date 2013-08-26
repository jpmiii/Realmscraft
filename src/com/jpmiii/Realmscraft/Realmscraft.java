package com.jpmiii.Realmscraft;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;


import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;



import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


import com.trc202.CombatTag.CombatTag;
import com.trc202.CombatTagApi.CombatTagApi;

import tk.manf.InventorySQL.manager.DatabaseManager;


public final class Realmscraft extends JavaPlugin {
	    public Permission perms = null;
	    public static Chat chat = null;
	    public static CombatTagApi combatApi = null;
	    public static DatabaseManager dbm = null;
	    public Location portalLoc = null;

		
		public void onEnable(){
			//getLogger().info("onEnable has been invoked!");
			getServer().getPluginManager().registerEvents(new RealmscraftListener(this), this);
	        setupPermissions();
	        
	        this.saveDefaultConfig();

	        setupCombatApi();
	        dbm = DatabaseManager.getInstance();
			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			getLogger().info( "[Bungee] Plugin channel registered!");
			BukkitTask task = new RealmscraftTask(this).runTaskTimer(this, 1200, 1200)  ;       //     .runTaskLater(this, 20);


		}
	 
		public void onDisable(){
			getLogger().info("onDisable has been invoked!");
		}

	    private boolean setupCombatApi() {
	    	if(getServer().getPluginManager().getPlugin("CombatTag") != null){
	    		combatApi = new CombatTagApi((CombatTag)getServer().getPluginManager().getPlugin("CombatTag")); 
	    		
	    	}
	    	return combatApi != null;
	    }

	    private boolean setupPermissions() {
	        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
	        perms = rsp.getProvider();
	        return perms != null;
	    }

		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
			if(cmd.getName().equalsIgnoreCase("rc")){ 
				// doSomething

		        
				Player player = (Player) sender;
				if(args.length > 0){

					if(args[0].equalsIgnoreCase("s")){
						if(perms.has(player, "realmscraft.s")) {
				        
						    dbm.savePlayer(player);
						    ByteArrayOutputStream b = new ByteArrayOutputStream();
						    DataOutputStream out = new DataOutputStream(b);
						 
					    	try {
						        out.writeUTF("Connect");
						        out.writeUTF(this.getConfig().getString("portalServer")); // Target Server
						    } catch (IOException e) {
						    // Can never happen
						    }
						    player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
						}

						return true;
					}
					
		        
		            
				}
		            
		           
		        
		        

				//getLogger().info(args[0]);
				
			} //If this has happened the function will return true. 
		        // If this hasn't happened the a value of false will be returned.
			return false; 
		}
}
