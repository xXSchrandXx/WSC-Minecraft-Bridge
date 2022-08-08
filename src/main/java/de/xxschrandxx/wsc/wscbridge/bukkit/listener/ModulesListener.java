package de.xxschrandxx.wsc.wscbridge.bukkit.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.xxschrandxx.wsc.wscbridge.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.wscbridge.bukkit.api.MinecraftBridgeModulesEvent;
import de.xxschrandxx.wsc.wscbridge.core.MinecraftBridgeVars;

public class ModulesListener implements Listener {
    @EventHandler
    public void onStatus(MinecraftBridgeModulesEvent e) {
        if (MinecraftBridgeBukkit.getInstance().getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.modules.groupsync.enabled))
            e.addModule("permission");
    }
}
