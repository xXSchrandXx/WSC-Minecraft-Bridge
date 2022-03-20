package de.xxschrandxx.wsc.bukkit.handler.permission;

import java.net.HttpURLConnection;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bukkit.api.AbstractBukkitHttpHandler;

public class DefaultPermissionHandler extends AbstractBukkitHttpHandler {

    @Override
    protected HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("status", "No supported Permission Plugin found.");
        response.put("statusCode", HttpURLConnection.HTTP_NO_CONTENT);
        return response;
    }

}
