package de.xxschrandxx.wsc.bungee.listener;

import de.xxschrandxx.wsc.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.bungee.api.MinecraftBridgeModulesEvent;
import de.xxschrandxx.wsc.core.MinecraftBridgeVars;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ModulesListener implements Listener {
    @EventHandler
    public void onStatus(MinecraftBridgeModulesEvent e) {
        if (MinecraftBridgeBungee.getInstance().getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.modules.permission))
            e.addModule("permission");
    }
}
