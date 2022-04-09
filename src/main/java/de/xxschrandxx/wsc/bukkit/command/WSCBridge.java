package de.xxschrandxx.wsc.bukkit.command;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import de.xxschrandxx.wsc.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.bukkit.api.MinecraftBridgeCommandSender;
import de.xxschrandxx.wsc.core.MinecraftBridgeVars;

public class WSCBridge implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("debug")) {
                return debug(sender);
            }
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                return reload(sender, args);
            }
            if (args[0].equalsIgnoreCase("whitelist")) {
                return whitelist(sender, args);
            }
            if (args[0].equalsIgnoreCase("blacklist")) {
                return blacklist(sender, args);
            }
        }
        PluginDescriptionFile d = MinecraftBridgeBukkit.getInstance().getDescription();
        sender.sendMessage(d.getName() + " " + d.getVersion() + " by " + d.getAuthors());
        return true;
    }

    public boolean reload(CommandSender sender, String[] args) {
        if (sender instanceof MinecraftBridgeCommandSender) {
            sender.sendMessage("Not supported.");
        }
        else if (!sender.hasPermission("rcon.command.reload")) {
            sender.sendMessage("You don't have permission to do that.");
        }
        else {
            boolean saveLists = false;
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("saveLists")) {
                    saveLists = true;
                }
            }
            MinecraftBridgeBukkit instance = MinecraftBridgeBukkit.getInstance();
            instance.stopHandler(sender, saveLists);
            if (!instance.reloadConfiguration()) {
                sender.sendMessage("Could not reload Plugin. Error while loading 'config.xml'. Read logs for more intormation.");
                return true;
            }
            sender.sendMessage("Reloaded config.");
            if (!instance.setHandler(sender)) {
                sender.sendMessage("Could not initialate handler. Read logs for more information.");
                return true;
            }
            instance.startHandler(sender);
        }
        return true;
    }

    public boolean whitelist(CommandSender sender, String[] args) {
        if (sender instanceof MinecraftBridgeCommandSender) {
            sender.sendMessage("Not supported.");
        }
        else if (!sender.hasPermission("rcon.command.whitelist")) {
            sender.sendMessage("You don't have permission to do that.");
        }
        else if (MinecraftBridgeBukkit.getInstance().getHandler().whitelist == null) {
            sender.sendMessage("Whitelist not set.");
        }
        else if (args.length == 1) {
            sender.sendMessage("Usage: /wsclinker whitelist <add/remove/list> [address]");
        }
        else if (args[1].equalsIgnoreCase("add")) {
            try {
                InetAddress address = InetAddress.getByName(args[2]);
                if (MinecraftBridgeBukkit.getInstance().getHandler().whitelist.add(address)) {
                    sender.sendMessage("Added.");
                }
                else {
                    sender.sendMessage("Not added. Already in.");
                }
            }
            catch (UnknownHostException e) {
                sender.sendMessage("Unknown Host.");
            }
        }
        else if (args[1].equalsIgnoreCase("remove")) {
            try {
                InetAddress address = InetAddress.getByName(args[2]);
                if (MinecraftBridgeBukkit.getInstance().getHandler().whitelist.remove(address)) {
                    sender.sendMessage("Removed.");
                }
                else {
                    sender.sendMessage("Not removed. Not in list.");
                }
            }
            catch (UnknownHostException e) {
                sender.sendMessage("Unknown Host.");
            }
        }
        else if (args[1].equalsIgnoreCase("list")) {
            String result = "Whitelist:";
            for (InetAddress address : MinecraftBridgeBukkit.getInstance().getHandler().whitelist) {
                result += "\r\n" + address;
            }
            sender.sendMessage(result);
        }
        else {
            sender.sendMessage("Usage: /wsclinker whitelist <add/remove/list> [address]");
        }
        return true;
    }

    public boolean blacklist(CommandSender sender, String[] args) {
        if (sender instanceof MinecraftBridgeCommandSender) {
            sender.sendMessage("Not supported.");
        }
        else if (!sender.hasPermission("rcon.command.blacklist")) {
            sender.sendMessage("You don't have permission to do that.");
        }
        else if (MinecraftBridgeBukkit.getInstance().getHandler().blacklist == null) {
            sender.sendMessage("Blacklist not set.");
        }
        else if (args.length == 1) {
            sender.sendMessage("Usage: /wsclinker blacklist <add/remove/list> [address]");
        }
        else if (args[1].equalsIgnoreCase("add")) {
            try {
                InetAddress address = InetAddress.getByName(args[2]);
                if (MinecraftBridgeBukkit.getInstance().getHandler().blacklist.add(address)) {
                    sender.sendMessage("Added.");
                }
                else {
                    sender.sendMessage("Not added. Already in.");
                }
            }
            catch (UnknownHostException e) {
                sender.sendMessage("Unknown Host.");
            }
        }
        else if (args[1].equalsIgnoreCase("remove")) {
            try {
                InetAddress address = InetAddress.getByName(args[2]);
                if (MinecraftBridgeBukkit.getInstance().getHandler().blacklist.remove(address)) {
                    sender.sendMessage("Removed.");
                }
                else {
                    sender.sendMessage("Not removed. Not in list.");
                }
            }
            catch (UnknownHostException e) {
                sender.sendMessage("Unknown Host.");
            }
        }
        else if (args[1].equalsIgnoreCase("list")) {
            String result = "Blacklist:";
            for (InetAddress address : MinecraftBridgeBukkit.getInstance().getHandler().blacklist) {
                result += "\r\n" + address;
            }
            sender.sendMessage(result);
        }
        else {
            sender.sendMessage("Usage: /wsclinker blacklist <add/remove/list> [address]");
        }
        return true;
    }

    private final SimpleDateFormat format = new SimpleDateFormat();
    public boolean debug(CommandSender sender) {
        MinecraftBridgeBukkit instance = MinecraftBridgeBukkit.getInstance();
        sender.sendMessage("Debug information:");
        sender.sendMessage("Hostname: " + instance.getConfiguration().getString(MinecraftBridgeVars.Configuration.server.hostname));
        sender.sendMessage("Port: " + instance.getConfiguration().getInt(MinecraftBridgeVars.Configuration.server.port));
        sender.sendMessage("SSL: " + instance.getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.server.ssl.enabled));
        if (instance.getHandler().whitelist == null) {
            sender.sendMessage("Whitelist: Not enabled.");
        }
        else {
            try {
                Class.forName("org.bukkit.command.CommandSender.Spigot");
                WSCBridgeSigot.sendWhitelistMessage(sender);
            }
            catch (ClassNotFoundException e) {
                sender.sendMessage("Whitelist: Enabled. (List with '/wsclinker whitelist list')");
            }
        }
        if (instance.getHandler().blacklistEnabled) {
            try {
                Class.forName("org.bukkit.command.CommandSender.Spigot");
                WSCBridgeSigot.sendBlacklistMessage(sender);
            }
            catch (ClassNotFoundException e) {
                sender.sendMessage("Blacklist: Enabled. (List with '/wsclinker blacklist list')");
            }
        }
        else {
            sender.sendMessage("Blacklist: Not enabled.");
        }
        if (instance.getHandler().maxTries <= -1) {
            sender.sendMessage("Floodgate disabled.");
        }
        else {
            sender.sendMessage("Floodgate:");
            sender.sendMessage("  maxTries: " + instance.getHandler().maxTries);
            sender.sendMessage("  resetTime: " + instance.getHandler().resetTime);
            sender.sendMessage("  maxOverruns: " + instance.getHandler().maxOverruns);
        if (instance.getHandler().tries.isEmpty()) {
                sender.sendMessage("  Map: empty");
            }
            else {
                sender.sendMessage("  Map:");
                for (Entry<InetAddress, Integer> entry : instance.getHandler().tries.entrySet()) {
                    sender.sendMessage("    " + entry.getKey().toString());
                    sender.sendMessage("      tries: " + entry.getValue());
                    Date lastReset = new Date(instance.getHandler().times.get(entry.getKey()));
                    sender.sendMessage("      last reset: " + format.format(lastReset));
                    Date nextReset = new Date(lastReset.getTime() + instance.getHandler().resetTime);
                    sender.sendMessage("      next reset: " + format.format(nextReset));
                    if (instance.getHandler().blacklist.contains(entry.getKey()))
                        sender.sendMessage("      will reset: blacklisted.");
                    else
                        sender.sendMessage("      will reset: " + nextReset.before(new Date()));
                }
            }
        }
        return true;
    }
}
