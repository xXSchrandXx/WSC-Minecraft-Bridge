package de.xxschrandxx.wsc.bungee.listener;

import de.xxschrandxx.wsc.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.bungee.api.MinecraftBridgeEvent;

import de.xxschrandxx.wsc.bungee.handler.*;
import de.xxschrandxx.wsc.core.MinecraftBridgeHandler;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class HandlerListener implements Listener {
    @EventHandler
    public void onEnable(MinecraftBridgeEvent event) {
        MinecraftBridgeHandler handler = MinecraftBridgeBungee.getInstance().getHandler();
        handler.addPasswordHandler("/", new StatusHandler());
        handler.addPasswordHandler("/list", new PlayerListHandler());
        handler.addPasswordHandler("/command", new CommandHandler());
        handler.addPasswordHandler("/sendCode", new SendCodeHandler());
    }
}
