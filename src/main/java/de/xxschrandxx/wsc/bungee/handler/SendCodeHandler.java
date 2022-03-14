package de.xxschrandxx.wsc.bungee.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bungee.api.AbstractHttpHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SendCodeHandler extends AbstractHttpHandler {
    @Override
    public HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            response.put("status", "Method not allowed.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_METHOD);
            return response;
        }
        try {
            byte[] requestBytes = exchange.getRequestBody().readAllBytes();
            String requestString = new String(requestBytes, StandardCharsets.UTF_8);
            HashMap<String, String> request = this.gson.fromJson(requestString, typeStringString);
            if (!request.containsKey("uuid")) {
                response.put("status", "No uuid set.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            }
            if (!request.containsKey("code")) {
                response.put("status", "No code set.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            }
            if (!request.containsKey("message")) {
                response.put("status", "No message set.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            }
            if (!request.containsKey("hover")) {
                response.put("status", "No hover set.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            }
            UUID uuid = UUID.fromString(request.get("uuid"));
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
            if (player == null) {
                response.put("status", "Player not found.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            }
            if (!player.isConnected()) {
                response.put("status", "Player not connected.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            }
            TextComponent message = new TextComponent(request.get("message"));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, request.get("code")));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(request.get("hover"))));
            player.sendMessage(message);
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