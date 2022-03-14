package de.xxschrandxx.wsc.bungee.api;

import java.util.logging.Logger;

import de.xxschrandxx.wsc.bungee.MinecraftLinkerBungee;
import de.xxschrandxx.wsc.core.AbstractHttpHandler;

public abstract class AbstractBungeeHttpHandler extends AbstractHttpHandler {
    @Override
    protected Logger getLogger() {
        return MinecraftLinkerBungee.getInstance().getLogger();
    }
}
