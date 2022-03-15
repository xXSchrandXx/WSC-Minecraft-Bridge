package de.xxschrandxx.wsc.bungee.handler;

import java.net.HttpURLConnection;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.bungee.api.AbstractBungeeHttpHandler;

public class StatusHandler extends AbstractBungeeHttpHandler {
    @Override
    public HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
            response.put("status", "OK");
            response.put("statusCode", HttpURLConnection.HTTP_OK);
            response.put("name", MinecraftBridgeBungee.getInstance().getDescription().getName());
            response.put("version", MinecraftBridgeBungee.getInstance().getDescription().getVersion());
            response.put("applicationAuthor", MinecraftBridgeBungee.getInstance().getDescription().getAuthor());
            response.put("applicationDescription", MinecraftBridgeBungee.getInstance().getDescription().getDescription());
            response.put("platformName", MinecraftBridgeBungee.getInstance().getProxy().getName());
            response.put("platformVersion", MinecraftBridgeBungee.getInstance().getProxy().getVersion());
        }
        else {
            response.put("status", "Method Not Allowed.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_METHOD);
        }
        return response;
    }
}
