package de.xxschrandxx.wsc.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.xxschrandxx.wsc.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.bukkit.api.MinecraftBridgeEvent;
import de.xxschrandxx.wsc.bukkit.handler.*;
import de.xxschrandxx.wsc.core.MinecraftBridgeHandler;

public class HandlerListener implements Listener {
    @EventHandler
    public void onEnable(MinecraftBridgeEvent event) {
        MinecraftBridgeHandler handler = MinecraftBridgeBukkit.getInstance().getHandler();
        handler.addPasswordHandler("/", new StatusHandler());
        handler.addPasswordHandler("/list", new PlayerListHandler());
        handler.addPasswordHandler("/command", new CommandHandler());
        handler.addPasswordHandler("/sendCode", new SendCodeHandler());
    }
}
