package de.xxschrandxx.wsc.core.authenticator;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.core.MinecraftBridgeHandler;

public class Floodgate extends Authenticator {

    private final MinecraftBridgeHandler handler;

    public Floodgate(MinecraftBridgeHandler handler) {
        this.handler = handler;
    }

    @Override
    public Result authenticate(HttpExchange exch) {
        return this.handler.authenticate(exch);
    }
}
