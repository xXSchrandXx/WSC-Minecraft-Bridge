package de.xxschrandxx.wsc.wscbridge.bungee.api;

import java.util.logging.Logger;

import de.xxschrandxx.wsc.wscbridge.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.wscbridge.core.AbstractHttpHandler;

public abstract class AbstractBungeeHttpHandler extends AbstractHttpHandler {
    @Override
    protected Logger getLogger() {
        return MinecraftBridgeBungee.getInstance().getLogger();
    }
}
