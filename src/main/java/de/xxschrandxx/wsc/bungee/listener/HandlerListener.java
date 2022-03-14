package de.xxschrandxx.wsc.bungee.listener;

import de.xxschrandxx.wsc.bungee.MinecraftLinkerBungee;
import de.xxschrandxx.wsc.bungee.api.MinecraftLinkerEvent;

import de.xxschrandxx.wsc.bungee.handler.*;
import de.xxschrandxx.wsc.core.MinecraftLinkerHandler;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class HandlerListener implements Listener {
    @EventHandler
    public void onEnable(MinecraftLinkerEvent event) {
        MinecraftLinkerHandler handler = MinecraftLinkerBungee.getInstance().getHandler();
        handler.addHandler("/", new StatusHandler());
        handler.addPasswordHandler("/list", new PlayerListHandler());
        handler.addPasswordHandler("/command", new CommandHandler());
        handler.addPasswordHandler("/sendCode", new SendCodeHandler());
    }
}
