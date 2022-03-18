package de.xxschrandxx.wsc.bungee.handler.permission;

import java.net.HttpURLConnection;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bungee.api.AbstractBungeeHttpHandler;

public class DefaultPermissionHandler extends AbstractBungeeHttpHandler {

    @Override
    protected HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("status", "No supported Permission Plugin found.");
        response.put("statusCode", HttpURLConnection.HTTP_INTERNAL_ERROR);
        return response;
    }

}
