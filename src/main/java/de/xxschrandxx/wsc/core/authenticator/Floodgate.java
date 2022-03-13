package de.xxschrandxx.wsc.core.authenticator;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.core.MinecraftLinkerHandler;

public class Floodgate extends Authenticator {

    private final MinecraftLinkerHandler handler;

    public Floodgate(MinecraftLinkerHandler handler) {
        this.handler = handler;
    }

    @Override
    public Result authenticate(HttpExchange exch) {
        return this.handler.authenticate(exch);
    }
}
