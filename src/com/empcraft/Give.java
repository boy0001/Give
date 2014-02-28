package com.empcraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 */
/**
 * @author Jesse Boyd
 *
 */


public final class Give extends JavaPlugin implements Listener {
    public boolean checkperm(Player player,String perm) {
    	if (player==null) {
    		return true;
    	}
    	boolean hasperm = false;
    	String[] nodes = perm.split("\\.");
    	
    	String n2 = "";
    	if (player.hasPermission(perm)) {
    		hasperm = true;
    	}
    	else if (player.isOp()==true) {
    		hasperm = true;
    	}
    	else {
    		for(int i = 0; i < nodes.length-1; i++) {
    			n2+=nodes[i]+".";
            	if (player.hasPermission(n2+"*")) {
            		hasperm = true;
            	}
    		}
    	}
		return hasperm;
    }
    public Material LevensteinDistance(String string) {
    	try {
    		return  Material.getMaterial(Integer.parseInt(string));
    	}
    	catch (Exception e) {
    		
    	}
    	string = StringUtils.replace(string.toString(), " ", "_").toUpperCase();
    	string = StringUtils.replace(string.toString(), "SHOVEL", "SPADE");
    	string = StringUtils.replace(string.toString(), "SLAB", "STEB");
    	string = StringUtils.replace(string.toString(), "REPEATER", "DIODE");
    	string = StringUtils.replace(string.toString(), "STONEBRICK", "SMOOTH_BRICK");
    	string = StringUtils.replace(string.toString(), "STONE_BRICK", "SMOOTH_BRICK");
    	
    	if (string.contains("STAINED")==false) {
	    	string = StringUtils.replace(string.toString(), "GLASSPANE", "THIN_GLASS");
	    	string = StringUtils.replace(string.toString(), "GLASS_PANE", "THIN_GLASS");
    	}
    	Material[] materials = Material.values();
    	int smallest = -1;
    	Material lastmaterial = null;
    	for (Material mymaterial:materials) {
    		String current = mymaterial.toString();
    		if (smallest == -1) {
    			lastmaterial = mymaterial;
    			int distance;
    			if (current.contains(string)) {
    				distance = StringUtils.getLevenshteinDistance(string.toUpperCase(), current)-4;
    				if (distance==-1) {
    					distance = 0;
    				}
    			}
    			else {
    				distance = StringUtils.getLevenshteinDistance(string.toUpperCase(), current)+Math.abs(string.length()-current.length());
    			}
    			smallest = distance;
    		}
    		else {
    			int distance;
    			if (current.contains(string)) {
    				distance = StringUtils.getLevenshteinDistance(string.toUpperCase(), current)-4;
    				if (distance==-1) {
    					distance = 0;
    				}
    			}
    			else {
    				distance = StringUtils.getLevenshteinDistance(string.toUpperCase(), current)+Math.abs(string.length()-current.length());
    			}
    			if (distance<smallest) {
    				smallest = distance;
    				lastmaterial = mymaterial;
    			}
    		}
    	}
    	return lastmaterial;
    }
    public Player matchplayer(String arg) {
		List<Player> matches = getServer().matchPlayer(arg);
		if (matches.isEmpty()) {
			return null;
		}
		else if (matches.size() > 1) {
			return null;
		}
		else {
			return matches.get(0);
		}
    }
    public String colorise(String mystring) {
    	String[] codes = {"&1","&2","&3","&4","&5","&6","&7","&8","&9","&0","&a","&b","&c","&d","&e","&f","&r","&l","&m","&n","&o","&k"};
    	for (String code:codes) {
    		mystring = mystring.replace(code, "§"+code.charAt(1));
    	}
    	return mystring;
    }
    public void msg(Player player,String mystring) {
    	if (player==null) {
    		getServer().getConsoleSender().sendMessage(colorise(mystring));
    	}
    	else if (player instanceof Player==false) {
    		getServer().getConsoleSender().sendMessage(colorise(mystring));
    	}
    	else {
    		player.sendMessage(colorise(mystring));
    	}

    }
    
    
    @Override
    public void onDisable() {
    	System.out.println("[Grant] Shutting down");
    	this.reloadConfig();
    	this.saveConfig();
        System.out.println("DONE!");
    }
    
    
	@Override
    public void onEnable(){
		getConfig().options().copyDefaults(true);
        
        final Map<String, Object> options = new HashMap<String, Object>();
        getConfig().set("version", "0.2.1");
        //TODO config
        
        options.put("overwrite",false);
        
        
        for (final Entry<String, Object> node : options.entrySet()) {
       	 if (!getConfig().contains(node.getKey())) {
       		 getConfig().set(node.getKey(), node.getValue());
       	 }
       }
    	saveConfig();
    	this.saveDefaultConfig();
    	getServer().getPluginManager().registerEvents(this, this);
	}
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	Player player;
    	if (sender instanceof Player==false) {
    		player = null;
    	}
    	else {
    		player = (Player) sender;
    	}
    	String line = "";
    	for (String i:args) {
    		line+=i+" ";
    	}
    	if (cmd.getName().equalsIgnoreCase("enchantments")) {
    		msg(player,"&6===ENCHANTMENTS===");
			String allenchants = "";
			for (Enchantment i:Enchantment.values()) {
				msg(player,"&7 - &a"+i.getName()+": "+i.getId());
			}
    	}
    	else if(((cmd.getName().equalsIgnoreCase("g"))||(cmd.getName().equalsIgnoreCase("grant")))||((cmd.getName().equalsIgnoreCase("o"))||(cmd.getName().equalsIgnoreCase("overwrite")))){
    		boolean overwrite = false;
    		if (((cmd.getName().equalsIgnoreCase("o"))||(cmd.getName().equalsIgnoreCase("overrite")))) {
    			overwrite = true;
    		}
    		if (checkperm(player,"give.grant")) {
    			if (args.length>0) {
        			Player receiver = player;
        			byte damage = 0;
        			int id = 0;
        			int amount = 1;
        			String itemdb = args[0];
        			String rename = "";
        			String addlore = "";
        			int slot = -1;
        			int equip = -1;
        			String last = "";
        			String color = "";
        			String enchants = "";
        			String reason = "";
        			boolean all = false;
        			for (String current:args) {
        				String arg;
        				String start;
        				try {
        					start = current.substring(0,2);
        					arg = current.substring(2,current.length());
        				}
        				catch (Exception e) {
        					arg = current;
        					start = current;
        				}
        					if (arg.equals("")) {
        						if (start.equalsIgnoreCase("p:")) {
                					last = "p:";
	            				}
	            				else if (start.equalsIgnoreCase("n:")) {
	            					last = "n:";
	            				}
	            				else if (start.equalsIgnoreCase("r:")) {
	            					last = "r:";
	            				}
	            				else if (start.equalsIgnoreCase("l:")) {
	            					last = "l:";
	            				}
	            				else if (start.equalsIgnoreCase("s:")) {
	            					last = "s:";
	            				}
	            				else if (start.equalsIgnoreCase("a:")) {
	            					last = "a:";
	            				}
	            				else if (start.equalsIgnoreCase("c:")) {
	            					last = "c:";
	            				}
	            				else if (start.equalsIgnoreCase("e:")) {
	            					last = "e:";
	            				}
	            				else {
	            					if (last.equals("p:")) {
		        						last = "";
		        						if (current.equals("*")==false) {
		        							try { receiver = matchplayer(current); }catch(Exception e) { reason="&7Cannot find player: &c"+current+"&7."; }
			        						if (receiver == null) {reason="&7Cannot find player: &c"+current+"&7.";}	
		        						}
		        						else {
		        							all = true;
		        						}
		        					}
		        					else if (last.equals("n:")) {
		        						last = "";
		        						try { amount=Integer.parseInt(current); }catch(Exception e) {reason="&7Invalid amount: &c"+current+"&7.";}
		        					}
		        					else if (last.equals("r:")) {
		        						try { rename+=current+" "; }catch(Exception e) {reason="&7Invalid item name: &c"+current+"&7.";}
		        					}
		        					else if (last.equals("l:")) {
		        						try { addlore+=current+" "; }catch(Exception e) {reason="&7Invalid lore: &c"+current+"&7.";}
		        					}
		        					else if (last.equals("e:")) {
		        						try { enchants+=current+","; }catch(Exception e) {reason="&7Invalid enchantment: &c"+current+"&7.";}
		        					}
		        					else if (last.equals("s:")) {
		        						last = "";
		        						try { slot=Integer.parseInt(current); }catch(Exception e) {reason="&7Invalid slot number: &c"+current+"&7.";}
		        					}
		        					else if (last.equals("a:")) {
		        						last = "";
		        						try { equip=Integer.parseInt(current); }catch(Exception e) {reason="&7Invalid equipment slot: &c"+current+"&7. (1-4)";}
		        					}
		        					else if (last.equals("c:")) {
		        						last = "";
		        						color += current + " ";
		        					}
		        					else {
		
		        					}
	            				}
        				}
        				else {
            				if (start.equalsIgnoreCase("p:")) {
            					if (arg.equals("*")==false) {
			    					try { receiver = matchplayer(arg); }catch(Exception e) { reason="&7Cannot find player: &c"+current+"&7."; }
			    					if (receiver == null) {reason="&7Cannot find player: &c"+current+"&7.";}
            					}
        						else {
        							all = true;
        						}
        				}
        				else if (start.equalsIgnoreCase("n:")) {
        					try { amount=Integer.parseInt(arg); }catch(Exception e) {reason="&7Invalid amount: &c"+current+"&7.";}
        				}
        				else if (start.equalsIgnoreCase("r:")) {
        					try { rename+=arg+" "; last = "r:"; }catch(Exception e) {reason="&7Invalid item name: &c"+current+"&7.";}
        				}
        				else if (start.equalsIgnoreCase("l:")) {
        					try { addlore+=arg+" "; last = "l:";}catch(Exception e) {reason="&7Invalid lore: &c"+current+"&7.";}
        				}
        				else if (start.equalsIgnoreCase("e:")) {
        					try { enchants+=arg+" "; last = "e:";}catch(Exception e) {reason="&7Invalid enchantment: &c"+current+"&7.";}
        				}
        				else if (start.equalsIgnoreCase("s:")) {
        					try { slot=Integer.parseInt(arg); }catch(Exception e) {reason="&7Invalid slot number: &c"+current+"&7.";}
        				}
        				else if (start.equalsIgnoreCase("a:")) {
        					try { equip=Integer.parseInt(arg); }catch(Exception e) {reason="&7Invalid equipment slot: &c"+current+"&7. (1-4)";}
        				}
        				else if (start.equalsIgnoreCase("c:")) {
        					try { 
        						color+=arg+" ";
        					}catch(Exception e) {}
        				}
        				else {
	        					if (last.equals("p:")) {
	        						last = "";
	        						if (current.equals("*")==false) {
		        						try { receiver = matchplayer(current); }catch(Exception e) { reason="&7Cannot find player: &c"+current+"&7."; }
		        						if (receiver == null) {reason="&7Cannot find player: &c"+current+"&7.";}
	        						}
	        						else {
	        							all = true;
	        						}
	        					}
	        					else if (last.equals("n:")) {
	        						last = "";
	        						try { amount=Integer.parseInt(current); }catch(Exception e) { reason="&7Invalid amount: &c"+current+"&7."; }
	        					}
	        					else if (last.equals("r:")) {
	        						try { rename+=current+" "; }catch(Exception e) { reason="&7Invalid item name: &c"+current+"&7."; }
	        					}
	        					else if (last.equals("l:")) {
	        						try { addlore+=current+" "; }catch(Exception e) { reason="&7Invalid lore: &c"+current+"&7."; }
	        					}
	        					else if (last.equals("e:")) {
	        						try { enchants+=current+" "; }catch(Exception e) { reason="&7Invalid lore: &c"+current+"&7."; }
	        					}
	        					else if (last.equals("s:")) {
	        						last = "";
	        						try { slot=Integer.parseInt(current); }catch(Exception e) { reason="&7Invalid slot number: &c"+current+"&7."; }
	        					}
	        					else if (last.equals("a:")) {
	        						last = "";
	        						try { equip=Integer.parseInt(current); }catch(Exception e) { reason="&7Invalid equipment slot: &c"+current+"&7. (1-4)"; }
	        					}
	        					else if (last.equals("c:")) {
	        						last = "";
	        						color += current + " ";
	        					}
	        					else {
	        						
	        					}
        					}
        				}
        			}
        			addlore = addlore.trim();
        			rename = rename.trim();
        			try { 
        				damage = Byte.parseByte(itemdb.split(":")[1]); 
        				itemdb = itemdb.split(":")[0];
        			}
        			catch (Exception e) { }
        			try {
        				id = LevensteinDistance(itemdb).getId();
        			}
        			catch (Exception e) {
        				try {
        					id = Integer.parseInt(itemdb);
        				}
        				catch (Exception e2) {
        					msg(player,"&7Cannot find item: &c"+itemdb+"&7.");
        				}
        			}
        			try {
        				List<String> lore = new LinkedList<String>();
        				ItemStack item = new ItemStack(id, amount, (byte) damage);
        				if (!rename.equals("")) {
        				try {
        				ItemMeta meta = item.getItemMeta();
        				meta.setDisplayName(colorise(rename));
        				item.setItemMeta(meta);
        				}
        				catch (Exception e) {
        					msg(player,"&7Failed to &crename&7 item.");
        				}
        				}

        				if (enchants.equals("")==false) {
            			try {
            				String[] enchantments = enchants.trim().split(",");
            				ItemMeta meta = item.getItemMeta();
            				for (String current:enchantments) {
            					String eid = current.split(":")[0];
            					int edamage = Integer.parseInt(current.split(":")[1]);
            					try {
            						meta.addEnchant(Enchantment.getById(Integer.parseInt(eid)), edamage, true);
            						item.setItemMeta(meta);
            					}
            					catch (Exception e1) {
            						try {
            							Enchantment.getByName(eid.toUpperCase()).getName();
            							meta.addEnchant(Enchantment.getByName(eid.toUpperCase()), edamage, true);
            							item.setItemMeta(meta);
            						}
            						catch (Exception e2) {
            							msg(player,"&7Failed to &cenchant&7 item. For a list use &c/enchantments&7.");
            						}
            					}
            				}
            				
            				
            			}
            			catch (Exception e) {
            				msg(player,"&7Failed to &cenchant&7 item. For a list use &c/enchantments&7.");
            				msg(player,"&7To enchant use e: <Enchant>:<Level>,<Enchant2>:<Level>.");
            			}
        				}
        				if (!addlore.equals("")) {
        					String[] toadd = addlore.split("//");
        					for (String current:toadd) {
        						lore.add(colorise(current));
        						ItemMeta meta = item.getItemMeta();
        						meta.setLore(lore);
        						item.setItemMeta(meta);
        					}
        				}
        				if (!color.trim().equals("")) {
        					try {
        					color = color.trim();
        					Color mycolor;
        					LeatherArmorMeta i2 = (LeatherArmorMeta)item.getItemMeta();
        					if (color.contains(",")) {
        						mycolor = Color.fromRGB(Integer.parseInt(color.split(",")[0]),Integer.parseInt(color.split(",")[1]),Integer.parseInt(color.split(",")[2]));
        					}
        					else if (color.equalsIgnoreCase("AQUA")) {
        						mycolor = Color.AQUA;
        					}
        					else if (color.equalsIgnoreCase("BLACK")) {
        						mycolor = Color.BLACK;
        					}
        					else if (color.equalsIgnoreCase("BLUE")) {
        						mycolor = Color.BLUE;
        					}
        					else if (color.equalsIgnoreCase("FUCHSIA")) {
        						mycolor = Color.FUCHSIA;
        					}
        					else if (color.equalsIgnoreCase("GRAY")) {
        						mycolor = Color.GRAY;
        					}
        					else if (color.equalsIgnoreCase("LIME")) {
        						mycolor = Color.LIME;
        					}
        					else if (color.equalsIgnoreCase("MAROON")) {
        						mycolor = Color.MAROON;
        					}
        					else if (color.equalsIgnoreCase("NAVY")) {
        						mycolor = Color.NAVY;
        					}
        					else if (color.equalsIgnoreCase("OLIVE")) {
        						mycolor = Color.OLIVE;
        					}
        					else if (color.equalsIgnoreCase("ORANGE")) {
        						mycolor = Color.ORANGE;
        					}
        					else if (color.equalsIgnoreCase("PURPLE")) {
        						mycolor = Color.PURPLE;
        					}
        					else if (color.equalsIgnoreCase("RED")) {
        						mycolor = Color.RED;
        					}
        					else if (color.equalsIgnoreCase("SILVER")) {
        						mycolor = Color.SILVER;
        					}
        					else if (color.equalsIgnoreCase("TEAL")) {
        						mycolor = Color.TEAL;
        					}
        					else if (color.equalsIgnoreCase("WHITE")) {
        						mycolor = Color.WHITE;
        					}
        					else if (color.equalsIgnoreCase("YELLOW")) {
        						mycolor = Color.YELLOW;
        					}
        					else {
        						mycolor = null;
        					}
        					if (mycolor!=null) {
	        					i2.setColor(mycolor);
	        					item.setItemMeta(i2);
        					}
        					else {
        						msg(player,"&7Invalid color: &c"+color+"&7. try: WHITE,SILVER,GRAY,BLACK,RED,MAROON,YELLOW,OLIVE,LIME,GREEN,AQUA,TEAL,BLUE,NAVY,FUCHSIA,PURPLE,ORANGE");
        					}
        					}
        					catch (Exception e) {
        						msg(player,"&7Colors can only be applied to &cleather armor&7.");
        					}
        				}
        				//TODO slots
        				
        				
        				
        				if (receiver!=null) {
                			List<Player> players2 = new ArrayList<Player>();
                			if (all) {
                				for (Player current: Bukkit.getServer().getOnlinePlayers()) {
                					players2.add(current);
                				}
                			}
                			else {
                				players2.add(receiver);
                			}
                			for (Player user:players2) {
                				boolean hasperm = true;
            					boolean full = false;
            					if (user.getInventory().firstEmpty()==-1) {
            						full = true;
            						if (checkperm(player,"give.overwrite")) {
            							overwrite = true;
            						}
            					}
            				if (equip!=-1) {
            					if (equip==1) {
            						ItemStack tempitem = user.getInventory().getHelmet();
            						if (tempitem!=null) {
                    					if (full&&(overwrite==false)) { hasperm = false; }
            						}
            						if (hasperm) {user.getInventory().setHelmet(item);
            						if ((tempitem!=null)&&(overwrite==false)) {
            							user.getInventory().addItem(tempitem);
            						}
            						}
            					}
            					else if (equip==2) {
            						ItemStack tempitem = user.getInventory().getChestplate();
            						if (tempitem!=null) {
                    					if (full&&(overwrite==false)) { hasperm = false; }
            						}
            						if (hasperm) {user.getInventory().setChestplate(item);
            						if ((tempitem!=null)&&(overwrite==false)) {
            							user.getInventory().addItem(tempitem);
            						}
            						}
            					}
            					else if (equip==3) {
            						ItemStack tempitem = user.getInventory().getLeggings();
            						if (tempitem!=null) {
                    					if (full&&(overwrite==false)) { hasperm = false; }
            						}
            						if (hasperm) {user.getInventory().setLeggings(item);
            						if ((tempitem!=null)&&(overwrite==false)) {
            							user.getInventory().addItem(tempitem);
            						}
            						}
            					}
            					else if (equip==4) {
            						ItemStack tempitem = user.getInventory().getBoots();
            						if (tempitem!=null) {
                    					if (full&&(overwrite==false)) { hasperm = false; }
            						}
            						if (hasperm) {user.getInventory().setBoots(item);
            						if ((tempitem!=null)&&(overwrite==false)) {
            							user.getInventory().addItem(tempitem);
            						}
            					}
            					}
            					else {
            						msg(player,"&cInvalid armor slot!");
            					}
            				}
            				else if (slot!=-1) {
            					try {
            						ItemStack tempitem = user.getInventory().getItem(slot-1);
            						if (tempitem!=null) {
                    					if (full&&(overwrite==false)) { hasperm = false; }
            						}
            						if (hasperm) {user.getInventory().setItem(slot-1, item);
    	        						if ((tempitem!=null)&&(overwrite==false)) {
    	        							user.getInventory().addItem(tempitem);
    	        						}
            						}
            					}
            					catch (Exception e) {
            						msg(player,"&7Slots must be from 0-9, not: &c"+slot+"&7.");
            					}
            				}
            				else {
            					if (full&&(overwrite==false)) { hasperm = false; }
            					if (hasperm) {user.getInventory().addItem(item);}
            				}
            				if (hasperm) {
            					if (full) {
            						msg(player,"&c[INFO] Inventory is full.");
            					}
            					msg(player,"&7Item &9"+item.getType()+"&7 Granted to &a"+user.getName()+"&7.");
            				}
            				else {
            					msg(player,"&cYou do not have permission to overwrite an item.");
            				}
                			}
        					
        			
        				}
        				else {
        					if (reason.equals("")) {
        						msg(player,"&7Use: &c/g <itemid> p: <player> n: <amount> l: <lore> r: <rename> c: <color> s: <slot> a: <equip>");
        					}
        					else {
        						msg(player,reason);
        					}
        				}
        			}
        			catch (Exception e) {
        				msg(player,reason);
        				// invalid ID/AMOUNT/DAMAGE
        			}

    				
    			}
    			else {
    				msg(player,"&7Use: &c/g <itemid> p: <player> n: <amount> l: <lore> r: <rename> c: <color> s: <slot> a: <equip>");
    			}
    		}
    		else {
    			msg(player,"&7You lack the permission: &cgive.grant&7.");
    		}
    	}
    	else {
    		msg(player,"&7Unknown command.");
    	}
		return true;
    }
    
}