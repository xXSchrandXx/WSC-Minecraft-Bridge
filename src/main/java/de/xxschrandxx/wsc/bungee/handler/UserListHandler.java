package de.xxschrandxx.wsc.bungee.handler;

import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bungee.api.AbstractBungeeHttpHandler;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class UserListHandler extends AbstractBungeeHttpHandler {
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
        Collection<ProxiedPlayer> players = ProxyServer.getInstance().getPlayers();
        HashMap<UUID, String> map = new HashMap<UUID, String>();
        if (players.isEmpty()) {
            response.put("user", map);
        }
        else {
            for (ProxiedPlayer player : players) {
                map.put(player.getUniqueId(), player.getName());
            }
            response.put("user", map);
        }
        return response;
    }
}
