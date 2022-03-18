package de.xxschrandxx.wsc.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.xxschrandxx.wsc.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.bukkit.api.MinecraftBridgeModulesEvent;
import de.xxschrandxx.wsc.core.MinecraftBridgeVars;

public class ModulesListener implements Listener {
    @EventHandler
    public void onStatus(MinecraftBridgeModulesEvent e) {
        if (MinecraftBridgeBukkit.getInstance().getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.modules.permission))
            e.addModule("permission");
    }
}
