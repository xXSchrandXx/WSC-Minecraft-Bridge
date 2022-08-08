package de.xxschrandxx.wsc.wscbridge.bungee.listener;

import de.xxschrandxx.wsc.wscbridge.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.api.MinecraftBridgeModulesEvent;
import de.xxschrandxx.wsc.wscbridge.core.MinecraftBridgeVars;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ModulesListener implements Listener {
    @EventHandler
    public void onStatus(MinecraftBridgeModulesEvent e) {
        if (MinecraftBridgeBungee.getInstance().getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.modules.groupsync.enabled))
            e.addModule("permission");
    }
}
