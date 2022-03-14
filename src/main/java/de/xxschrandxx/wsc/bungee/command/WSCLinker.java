package de.xxschrandxx.wsc.bungee.command;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import de.xxschrandxx.wsc.bungee.MinecraftLinkerBungee;
import de.xxschrandxx.wsc.bungee.api.MinecraftLinkerCommandSender;
import de.xxschrandxx.wsc.core.MinecraftLinkerVars;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginDescription;

public class WSCLinker extends Command {

    public WSCLinker() {
        super("wsclinkerbungee");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                reload(sender, args);
                return;
            }
            if (args[0].equalsIgnoreCase("debug")) {
                debug(sender);
                return;
            }
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("whitelist")) {
                whitelist(sender, args);
                return;
            }
            if (args[0].equalsIgnoreCase("blacklist")) {
                blacklist(sender, args);
                return;
            }
        }
        PluginDescription d = MinecraftLinkerBungee.getInstance().getDescription();
        sender.sendMessage(new TextComponent(d.getName() + " " + d.getVersion() + " by " + d.getAuthor()));
    }

    public void reload(CommandSender sender, String[] args) {
        if (sender instanceof MinecraftLinkerCommandSender) {
            sender.sendMessage(new TextComponent("Not supported."));
        }
        else if (!sender.hasPermission("rcon.command.reload")) {
            sender.sendMessage(new TextComponent("You don't have permission to do that."));
        }
        else {
            MinecraftLinkerBungee instance = MinecraftLinkerBungee.getInstance();
            instance.stopHandler(sender);
            if (!instance.reloadConfiguration()) {
                sender.sendMessage(new TextComponent("Could not reload Plugin. Error while loading 'config.xml'. Read logs for more intormation."));
                return;
            }
            sender.sendMessage(new TextComponent("Reloaded config."));
            if (!instance.setHandler(sender)) {
                sender.sendMessage(new TextComponent("Could not initialate handler. Read logs for more information."));
                return;
            }
            instance.startHandler(sender);
        }
    }

    public void whitelist(CommandSender sender, String[] args) {
        if (sender instanceof MinecraftLinkerCommandSender) {
            sender.sendMessage(new TextComponent("Not supported."));
        }
        else if (!sender.hasPermission("rcon.command.whitelist")) {
            sender.sendMessage(new TextComponent("You don't have permission to do that."));
        }
        else if (MinecraftLinkerBungee.getInstance().getHandler().whitelist == null) {
            sender.sendMessage(new TextComponent("Whitelist not set."));
        }
        else if (args.length == 1) {
            sender.sendMessage(new TextComponent("Usage: /wsclinkerbungee whitelist <add/remove/list> [address]"));
        }
        else if (args[1].equalsIgnoreCase("add")) {
            try {
                InetAddress address = InetAddress.getByName(args[2]);
                if (MinecraftLinkerBungee.getInstance().getHandler().whitelist.add(address)) {
                    sender.sendMessage(new TextComponent("Added."));
                }
                else {
                    sender.sendMessage(new TextComponent("Not added. Already in."));
                }
            }
            catch (UnknownHostException e) {
                sender.sendMessage(new TextComponent("Unknown Host."));
            }
        }
        else if (args[1].equalsIgnoreCase("remove")) {
            try {
                InetAddress address = InetAddress.getByName(args[2]);
                if (MinecraftLinkerBungee.getInstance().getHandler().whitelist.remove(address)) {
                    sender.sendMessage(new TextComponent("Removed."));
                }
                else {
                    sender.sendMessage(new TextComponent("Not removed. Not in list."));
                }
            }
            catch (UnknownHostException e) {
                sender.sendMessage(new TextComponent("Unknown Host."));
            }
        }
        else if (args[1].equalsIgnoreCase("list")) {
            String result = "Whitelist:";
            for (InetAddress address : MinecraftLinkerBungee.getInstance().getHandler().whitelist) {
                result += "\r\n" + address;
            }
            sender.sendMessage(new TextComponent(result));
        }
        else {
            sender.sendMessage(new TextComponent("Usage: /wsclinkerbungee whitelist <add/remove/list> [address]"));
        }
    }

    public void blacklist(CommandSender sender, String[] args) {
        if (sender instanceof MinecraftLinkerCommandSender) {
            sender.sendMessage(new TextComponent("Not supported."));
        }
        else if (!sender.hasPermission("rcon.command.blacklist")) {
            sender.sendMessage(new TextComponent("You don't have permission to do that."));
        }
        else if (MinecraftLinkerBungee.getInstance().getHandler().blacklist == null) {
            sender.sendMessage(new TextComponent("Blacklist not set."));
        }
        else if (args.length == 1) {
            sender.sendMessage(new TextComponent("Usage: /wsclinkerbungee blacklist <add/remove/list> [address]"));
        }
        else if (args[1].equalsIgnoreCase("add")) {
            try {
                InetAddress address = InetAddress.getByName(args[2]);
                if (MinecraftLinkerBungee.getInstance().getHandler().blacklist.add(address)) {
                    sender.sendMessage(new TextComponent("Added."));
                }
                else {
                    sender.sendMessage(new TextComponent("Not added. Already in."));
                }
            }
            catch (UnknownHostException e) {
                sender.sendMessage(new TextComponent("Unknown Host."));
            }
        }
        else if (args[1].equalsIgnoreCase("remove")) {
            try {
                InetAddress address = InetAddress.getByName(args[2]);
                if (MinecraftLinkerBungee.getInstance().getHandler().blacklist.remove(address)) {
                    sender.sendMessage(new TextComponent("Removed."));
                }
                else {
                    sender.sendMessage(new TextComponent("Not removed. Not in list."));
                }
            }
            catch (UnknownHostException e) {
                sender.sendMessage(new TextComponent("Unknown Host."));
            }
        }
        else if (args[1].equalsIgnoreCase("list")) {
            String result = "Blacklist:";
            for (InetAddress address : MinecraftLinkerBungee.getInstance().getHandler().blacklist) {
                result += "\r\n" + address;
            }
            sender.sendMessage(new TextComponent(result));
        }
        else {
            sender.sendMessage(new TextComponent("Usage: /wsclinkerbungee blacklist <add/remove/list> [address]"));
        }
    }

    private final SimpleDateFormat format = new SimpleDateFormat();
    public void debug(CommandSender sender) {
        MinecraftLinkerBungee instance = MinecraftLinkerBungee.getInstance();
        sender.sendMessage(new TextComponent("Debug information:"));
        sender.sendMessage(new TextComponent("Hostname: " + instance.getConfiguration().getString(MinecraftLinkerVars.Configuration.server.hostname)));
        sender.sendMessage(new TextComponent("Port: " + instance.getConfiguration().getInt(MinecraftLinkerVars.Configuration.server.port)));
        sender.sendMessage(new TextComponent("SSL: " + instance.getConfiguration().getBoolean(MinecraftLinkerVars.Configuration.server.ssl.enabled)));
        if (instance.getHandler().whitelist == null) {
            sender.sendMessage(new TextComponent("Whitelist: Not enabled."));
        }
        else {
            TextComponent whitelist = new TextComponent("Whitelist: Enabled.");
            whitelist.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wsclinkerbungee whitelist list"));
            whitelist.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click for more information.")));
            sender.sendMessage(whitelist);
        }
        if (instance.getHandler().blacklist == null) {
            sender.sendMessage(new TextComponent("Blacklist: Not enabled."));
        }
        else {
            TextComponent blacklist = new TextComponent("Blacklist: Enabled.");
            blacklist.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wsclinkerbungee blacklist list"));
            blacklist.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click for more information.")));
            sender.sendMessage(blacklist);
        }
        if (instance.getHandler().maxTries <= -1) {
            sender.sendMessage(new TextComponent("Floodgate disabled."));
        }
        else {
            sender.sendMessage(new TextComponent("Floodgate:"));
            sender.sendMessage(new TextComponent("  maxTries: " + instance.getHandler().maxTries));
            sender.sendMessage(new TextComponent("  resetTime: " + instance.getHandler().resetTime));
        if (instance.getHandler().tries.isEmpty()) {
                sender.sendMessage(new TextComponent("  Map: empty"));
            }
            else {
                sender.sendMessage(new TextComponent("  Map:"));
                for (Entry<InetAddress, Integer> entry : instance.getHandler().tries.entrySet()) {
                    sender.sendMessage(new TextComponent("    " + entry.getKey().toString()));
                    sender.sendMessage(new TextComponent("      tries: " + entry.getValue()));
                    Date lastReset = new Date(instance.getHandler().times.get(entry.getKey()));
                    sender.sendMessage(new TextComponent("      last reset: " + format.format(lastReset)));
                    Date nextReset = new Date(lastReset.getTime() + instance.getHandler().resetTime);
                    sender.sendMessage(new TextComponent("      next reset: " + format.format(nextReset)));
                    if (instance.getHandler().blacklist.contains(entry.getKey()))
                        sender.sendMessage(new TextComponent("      will reset: blacklisted."));
                    else
                        sender.sendMessage(new TextComponent("      will reset: " + nextReset.before(new Date())));
                }
            }
        }
    }
}
