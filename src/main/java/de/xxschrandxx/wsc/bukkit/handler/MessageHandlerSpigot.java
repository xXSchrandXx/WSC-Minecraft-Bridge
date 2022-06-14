package de.xxschrandxx.wsc.bukkit.handler;

import java.util.HashMap;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class MessageHandlerSpigot {
    public static void sendMessage(Player player, HashMap<String, String> request) {
        TextComponent message = new TextComponent(request.get("message"));
        if (!request.get("hover").isBlank()) {
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(request.get("hover"))));
        }
        player.spigot().sendMessage(message);
    }
}
