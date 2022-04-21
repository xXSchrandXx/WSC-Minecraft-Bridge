package de.xxschrandxx.wsc.bukkit.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.xxschrandxx.wsc.bukkit.api.AbstractBukkitHttpHandler;

public class SendCodeHandler extends AbstractBukkitHttpHandler {
    @Override
    public HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            response.put("status", "Method not allowed.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_METHOD);
            return response;
        }
        try {
            HashMap<String, String> request = readRequestBodyString(exchange);
            if (!request.containsKey("uuid")) {
                response.put("status", "No uuid set.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
                return response;
            }
            if (!request.containsKey("code")) {
                response.put("status", "No code set.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
                return response;
            }
            if (!request.containsKey("message")) {
                response.put("status", "No message set.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
                return response;
            }
            if (!request.containsKey("hover")) {
                response.put("status", "No hover set.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
                return response;
            }
            UUID uuid = UUID.fromString(request.get("uuid"));
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                response.put("status", "Player not found.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
                return response;
            }
            if (!player.isOnline()) {
                response.put("status", "Player not connected.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
                return response;
            }
            try {
                Class.forName("org.bukkit.entity.Player.Spigot");
                SendCodeHandlerSpigot.sendCodeMessage(player, request);
            }
            catch (ClassNotFoundException e) {
                player.sendMessage(request.get("message"));
            }
            response.put("status", "OK.");
            response.put("statusCode", HttpURLConnection.HTTP_OK);
        }
        catch (JsonSyntaxException e) {
            response.put("status", "Could not parse JSON.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
        }
        catch (IllegalArgumentException e) {
            response.put("status", "Could not parse UUID.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
        }
        catch (IOException e) {
            response.put("status", "Internal Server Error.");
            response.put("statusCode", HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();
        }
        return response;
    }
}
