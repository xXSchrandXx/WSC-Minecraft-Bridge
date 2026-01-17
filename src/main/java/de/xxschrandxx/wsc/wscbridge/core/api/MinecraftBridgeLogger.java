package de.xxschrandxx.wsc.wscbridge.core.api;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MinecraftBridgeLogger implements IBridgeLogger {

    private Logger logger;

    public MinecraftBridgeLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String s) {
        this.logger.log(Level.INFO, s);
    }

    @Override
    public void warn(String s) {
        this.logger.log(Level.WARNING, s);
    }

    @Override
    public void warn(String s, Throwable t) {
        this.logger.log(Level.WARNING, s, t);
    }

    @Override
    public void severe(String s) {
        this.logger.log(Level.SEVERE, s);
    }

    @Override
    public void severe(String s, Throwable t) {
        this.logger.log(Level.SEVERE, s, t);
    }
}
