package de.xxschrandxx.wsc.bukkit.handler;

import java.net.HttpURLConnection;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.bukkit.api.AbstractBukkitHttpHandler;

public class StatusHandler extends AbstractBukkitHttpHandler {
    @Override
    public HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
            response.put("status", "OK");
            response.put("statusCode", HttpURLConnection.HTTP_OK);
            response.put("name", MinecraftBridgeBukkit.getInstance().getDescription().getName());
            response.put("version", MinecraftBridgeBukkit.getInstance().getDescription().getVersion());
            response.put("applicationAuthor", MinecraftBridgeBukkit.getInstance().getDescription().getAuthors().get(0));
            response.put("applicationDescription", MinecraftBridgeBukkit.getInstance().getDescription().getDescription());
            response.put("platformName", MinecraftBridgeBukkit.getInstance().getServer().getName());
            response.put("platformVersion", MinecraftBridgeBukkit.getInstance().getServer().getVersion());
        }
        else {
            response.put("status", "Method Not Allowed.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_METHOD);
        }
        return response;
    }
}
