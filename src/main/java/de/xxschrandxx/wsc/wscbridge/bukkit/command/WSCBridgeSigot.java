package de.xxschrandxx.wsc.wscbridge.bukkit.command;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class WSCBridgeSigot {
    public static void sendWhitelistMessage(CommandSender sender) {
        TextComponent whitelist = new TextComponent("Whitelist: Enabled.");
        whitelist.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wsclinkerbungee whitelist list"));
        whitelist.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click for more information.")));
        sender.spigot().sendMessage(whitelist);
    }
    public static void sendBlacklistMessage(CommandSender sender) {
        TextComponent whitelist = new TextComponent("Blacklist: Enabled.");
        whitelist.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/wsclinkerbungee blacklist list"));
        whitelist.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click for more information.")));
        sender.spigot().sendMessage(whitelist);
    }
}
