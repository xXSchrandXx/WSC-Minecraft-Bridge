package de.xxschrandxx.wsc.wscbridge.core.authenticator;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.wscbridge.core.MinecraftBridgeHandler;

public class PasswordAuthenticator extends BasicAuthenticator {

    private final MinecraftBridgeHandler handler;

    private final String user;

    private final String password;

    public PasswordAuthenticator(MinecraftBridgeHandler handler, String user, String password) {
        super("wsclinker");
        this.handler = handler;
        this.user = user;
        this.password = password;
    }

    @Override
    public Result authenticate(HttpExchange exch) {
        Result result = this.handler.authenticate(exch);
        if (result instanceof Success) {
            return super.authenticate(exch);
        }
        else {
            return result;
        }
    }

    @Override
    public boolean checkCredentials(String user, String password) {
        if (user.equals(this.user) && password.equals(this.password)) {
            return true;
        }
        else {
            return false;
        }
    }    
}
