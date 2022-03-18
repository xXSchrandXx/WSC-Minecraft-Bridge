package de.xxschrandxx.wsc.bukkit.handler;

import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import com.sun.net.httpserver.HttpExchange;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.xxschrandxx.wsc.bukkit.api.AbstractBukkitHttpHandler;

public class UserListHandler extends AbstractBukkitHttpHandler {
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
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        HashMap<UUID, String> map = new HashMap<UUID, String>();
        if (players.isEmpty()) {
            response.put("user", map);
        }
        else {
            for (Player player : players) {
                map.put(player.getUniqueId(), player.getName());
            }
            response.put("user", map);
        }
        return response;
    }
}
