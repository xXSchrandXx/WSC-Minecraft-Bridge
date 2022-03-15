package de.xxschrandxx.wsc.bungee.api;

import java.util.logging.Logger;

import de.xxschrandxx.wsc.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.core.AbstractHttpHandler;

public abstract class AbstractBungeeHttpHandler extends AbstractHttpHandler {
    @Override
    protected Logger getLogger() {
        return MinecraftBridgeBungee.getInstance().getLogger();
    }
}
