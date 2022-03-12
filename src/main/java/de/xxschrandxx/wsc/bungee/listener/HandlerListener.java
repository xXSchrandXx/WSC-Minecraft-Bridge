package de.xxschrandxx.wsc.bungee.listener;

import de.xxschrandxx.wsc.bungee.MinecraftLinkerBungee;
import de.xxschrandxx.wsc.bungee.api.MinecraftLinkerEvent;
import de.xxschrandxx.wsc.bungee.handler.CommandHandler;
import de.xxschrandxx.wsc.bungee.handler.StatusHandler;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class HandlerListener implements Listener {
    @EventHandler
    public void onEnable(MinecraftLinkerEvent event) {
        MinecraftLinkerBungee.getInstance().getHandler().addHandler("/", new StatusHandler());
        MinecraftLinkerBungee.getInstance().getHandler().addPasswordHandler("/command", new CommandHandler());
    }
}
