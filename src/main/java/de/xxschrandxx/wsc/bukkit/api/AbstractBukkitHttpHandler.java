package de.xxschrandxx.wsc.bukkit.api;

import java.util.logging.Logger;

import de.xxschrandxx.wsc.bukkit.MinecraftLinkerBukkit;
import de.xxschrandxx.wsc.core.AbstractHttpHandler;

public abstract class AbstractBukkitHttpHandler extends AbstractHttpHandler {
    @Override
    protected Logger getLogger() {
        return MinecraftLinkerBukkit.getInstance().getLogger();
    }
}
