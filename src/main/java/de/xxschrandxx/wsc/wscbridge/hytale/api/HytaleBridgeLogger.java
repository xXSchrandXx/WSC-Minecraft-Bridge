package de.xxschrandxx.wsc.wscbridge.hytale.api;

import com.hypixel.hytale.logger.HytaleLogger;

import de.xxschrandxx.wsc.wscbridge.core.api.IBridgeLogger;

public class HytaleBridgeLogger implements IBridgeLogger {

    private HytaleLogger logger;

    public HytaleBridgeLogger(HytaleLogger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String s) {
        this.logger.atInfo().log(s);
    }

    @Override
    public void warn(String s) {
        this.logger.atWarning().log(s);
    }

    @Override
    public void warn(String s, Throwable t) {
        this.logger.atWarning().log(s, t);
    }

    @Override
    public void severe(String s) {
        this.logger.atSevere().log(s);
    }

    @Override
    public void severe(String s, Throwable t) {
        this.logger.atSevere().log(s, t);
    }
}
