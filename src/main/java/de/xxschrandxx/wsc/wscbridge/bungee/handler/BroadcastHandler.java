package de.xxschrandxx.wsc.wscbridge.bungee.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.wscbridge.bungee.api.AbstractBungeeHttpHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class BroadcastHandler extends AbstractBungeeHttpHandler {
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
            TextComponent message = new TextComponent(request.get("message"));
            if (!request.get("hover").isBlank()) {
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(request.get("hover"))));
            }
            ProxyServer.getInstance().broadcast(message);
            response.put("status", "OK.");
            response.put("statusCode", HttpURLConnection.HTTP_OK);
        }
        catch (JsonSyntaxException e) {
            response.put("status", "Could not parse JSON.");
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
