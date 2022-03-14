package de.xxschrandxx.wsc.bukkit.handler;

import java.net.HttpURLConnection;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bukkit.MinecraftLinkerBukkit;
import de.xxschrandxx.wsc.bukkit.api.AbstractBukkitHttpHandler;

public class StatusHandler extends AbstractBukkitHttpHandler {
    @Override
    public HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
            response.put("status", "OK");
            response.put("statusCode", HttpURLConnection.HTTP_OK);
            response.put("name", MinecraftLinkerBukkit.getInstance().getDescription().getName());
            response.put("version", MinecraftLinkerBukkit.getInstance().getDescription().getVersion());
            response.put("applicationAuthor", MinecraftLinkerBukkit.getInstance().getDescription().getAuthors().get(0));
            response.put("applicationDescription", MinecraftLinkerBukkit.getInstance().getDescription().getDescription());
            response.put("platformName", MinecraftLinkerBukkit.getInstance().getServer().getName());
            response.put("platformVersion", MinecraftLinkerBukkit.getInstance().getServer().getVersion());
        }
        else {
            response.put("status", "Method Not Allowed.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_METHOD);
        }
        return response;
    }
}
