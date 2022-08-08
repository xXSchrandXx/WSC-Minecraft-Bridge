package de.xxschrandxx.wsc.wscbridge.bungee.handler;

import java.net.HttpURLConnection;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.wscbridge.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.api.AbstractBungeeHttpHandler;
import de.xxschrandxx.wsc.wscbridge.bungee.api.MinecraftBridgeModulesEvent;

public class StatusHandler extends AbstractBungeeHttpHandler {
    @Override
    public HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (exchange.getRequestURI().getPath().equals("/")) {
            if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                response.put("status", "OK");
                response.put("statusCode", HttpURLConnection.HTTP_OK);
                response.put("name", MinecraftBridgeBungee.getInstance().getDescription().getName());
                response.put("version", MinecraftBridgeBungee.getInstance().getDescription().getVersion());
                response.put("applicationAuthor", MinecraftBridgeBungee.getInstance().getDescription().getAuthor());
                response.put("applicationDescription", MinecraftBridgeBungee.getInstance().getDescription().getDescription());
                response.put("platformName", MinecraftBridgeBungee.getInstance().getProxy().getName());
                response.put("platformVersion", MinecraftBridgeBungee.getInstance().getProxy().getVersion());
                MinecraftBridgeModulesEvent event = new MinecraftBridgeModulesEvent();
                MinecraftBridgeBungee.getInstance().getProxy().getPluginManager().callEvent(event);
                response.put("modules", event.getModules());
            }
            else {
                response.put("status", "Method Not Allowed.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_METHOD);
            }
        }
        else {
            response.put("status", "Not Found.");
            response.put("statusCode", HttpURLConnection.HTTP_NOT_FOUND);
        }
        return response;
    }
}
