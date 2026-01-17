package de.xxschrandxx.wsc.wscbridge.bukkit.api.command;

import org.bukkit.command.CommandSender;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class SenderSpigot extends SenderBukkit {

    public SenderSpigot(CommandSender sender, IBridgePlugin<?> instance) {
        super(sender, instance);
    }

    @Override
    public void sendMessage(String message, String hoverMessage, String copyText) {
        TextComponent text = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        if (hoverMessage != null) {
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverMessage)));
        }
        if (copyText != null) {
            if (copyText.startsWith("http") || copyText.startsWith("https")) {
                text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, copyText));
            }
            else {
                text.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyText));
            }
        }
        this.sender.spigot().sendMessage(text);
    }
}
