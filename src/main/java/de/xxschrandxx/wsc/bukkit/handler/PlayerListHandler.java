package de.xxschrandxx.wsc.bukkit.handler;

import java.net.HttpURLConnection;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.xxschrandxx.wsc.bukkit.api.AbstractBukkitHttpHandler;

public class PlayerListHandler extends AbstractBukkitHttpHandler {
    @Override
    public HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            response.put("status", "Method not allowed.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_METHOD);
            return response;
        }
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        for (Player player : Bukkit.getOnlinePlayers()) {
            response.put(player.getName(), player.getUniqueId());
        }
        return response;
    }
}
