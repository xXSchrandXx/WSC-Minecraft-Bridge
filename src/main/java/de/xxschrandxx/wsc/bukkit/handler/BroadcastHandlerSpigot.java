package de.xxschrandxx.wsc.bukkit.handler;

import java.util.HashMap;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class BroadcastHandlerSpigot {
    public static void broadcast(HashMap<String, String> request) {
        TextComponent message = new TextComponent(request.get("message"));
        if (!request.get("hover").isBlank()) {
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(request.get("hover"))));
        }
        Bukkit.spigot().broadcast(message);
    }
}
