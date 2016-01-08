/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.nznetwork.kickserver;

import java.util.logging.Level;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author NZNetwork
 */
public class KickServer extends JavaPlugin implements Listener {

    String message = "There is no real server here";
    
    @Override
    public void onEnable() {
        if(getConfig().isString("message")) {
            message = formatMessage(getConfig().getString("message"));
            CommandSender sender = getServer().getConsoleSender();
            sender.sendMessage(ChatColor.GREEN+"Message loaded from config, message is:");
            sender.sendMessage(message);
        }
        getServer().getPluginManager().registerEvents(this, this);
        try {
        getServer().getWorlds().stream().forEach((w) -> {
            getServer().unloadWorld(w, true);
        }); } catch (Exception e) {}
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        if(!event.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) return;
        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(message);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reloadmessage")) {
            if(getConfig().isString("message")) {
                message = formatMessage(getConfig().getString("message"));
                sender.sendMessage(ChatColor.GREEN+"Message loaded from config, new message is:");
                sender.sendMessage(message);
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("setmessage")) {
            if(args.length < 1) {
                sender.sendMessage(ChatColor.RED+"You did not type a message");
                return true;
            }
            String unformated = getReason(args, 0);
            message = formatMessage(unformated);
            getConfig().set("message", unformated);
            saveConfig();
            sender.sendMessage(ChatColor.GREEN+"Message saved to config, new message is:");
            sender.sendMessage(message);
            return true;
        }
        return false;
    }
    private String formatMessage(String message) {
        message = message.replaceAll("&0", ChatColor.BLACK.toString());
        message = message.replaceAll("&1", ChatColor.DARK_BLUE.toString());
        message = message.replaceAll("&2", ChatColor.DARK_GREEN.toString());
        message = message.replaceAll("&3", ChatColor.DARK_AQUA.toString());
        message = message.replaceAll("&4", ChatColor.DARK_RED.toString());
        message = message.replaceAll("&5", ChatColor.DARK_PURPLE.toString());
        message = message.replaceAll("&6", ChatColor.GOLD.toString());
        message = message.replaceAll("&7", ChatColor.GRAY.toString());
        message = message.replaceAll("&8", ChatColor.DARK_GRAY.toString());
        message = message.replaceAll("&9", ChatColor.BLUE.toString());
        message = message.replaceAll("&a", ChatColor.GREEN.toString());
        message = message.replaceAll("&b", ChatColor.AQUA.toString());
        message = message.replaceAll("&c", ChatColor.RED.toString());
        message = message.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString());
        message = message.replaceAll("&e", ChatColor.YELLOW.toString());
        message = message.replaceAll("&f", ChatColor.WHITE.toString());
        return message;
    }
    private String getReason(String[] args, int startIndex) {
        String reason = "";
        if(args.length > startIndex) {
            StringBuilder build = new StringBuilder();
            build.append(args[startIndex]);
            for(int i = startIndex+1; i < args.length; i++) {
                build.append(" ");
                build.append(args[i]);
            }
            reason = build.toString();
        }
        return reason;
    }
}
