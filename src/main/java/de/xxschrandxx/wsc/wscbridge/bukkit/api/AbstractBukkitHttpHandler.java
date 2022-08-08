package de.xxschrandxx.wsc.wscbridge.bukkit.api;

import java.util.logging.Logger;

import de.xxschrandxx.wsc.wscbridge.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.wscbridge.core.AbstractHttpHandler;

public abstract class AbstractBukkitHttpHandler extends AbstractHttpHandler {
    @Override
    protected Logger getLogger() {
        return MinecraftBridgeBukkit.getInstance().getLogger();
    }
}
