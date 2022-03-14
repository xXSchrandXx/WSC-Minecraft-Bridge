package de.xxschrandxx.wsc.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.xxschrandxx.wsc.bukkit.MinecraftLinkerBukkit;
import de.xxschrandxx.wsc.bukkit.api.MinecraftLinkerEvent;
import de.xxschrandxx.wsc.bukkit.handler.*;
import de.xxschrandxx.wsc.core.MinecraftLinkerHandler;

public class HandlerListener implements Listener {
    @EventHandler
    public void onEnable(MinecraftLinkerEvent event) {
        MinecraftLinkerHandler handler = MinecraftLinkerBukkit.getInstance().getHandler();
        handler.addHandler("/", new StatusHandler());
        handler.addPasswordHandler("/list", new PlayerListHandler());
        handler.addPasswordHandler("/command", new CommandHandler());
        handler.addPasswordHandler("/sendCode", new SendCodeHandler());
    }
}
