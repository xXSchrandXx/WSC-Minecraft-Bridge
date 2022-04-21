package de.xxschrandxx.wsc.bungee.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bungee.api.AbstractBungeeHttpHandler;
import de.xxschrandxx.wsc.bungee.api.MinecraftBridgeCommandSender;

public class CommandHandler extends AbstractBungeeHttpHandler {
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
            if (!request.containsKey("command")) {
                response.put("status", "No command found.");
                response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
                return response;
            }
            String command = request.get("command");
            MinecraftBridgeCommandSender sender = new MinecraftBridgeCommandSender();
            sender.setIP(exchange.getRemoteAddress().getAddress());
            if (sender.dispatchCommand(command)) {
                response.put("response", sender.flush());
                response.put("status", "OK");
                response.put("statusCode", HttpURLConnection.HTTP_OK);
            }
            else {
                response.put("response", sender.flush());
                response.put("status", "Command not found.");
                response.put("statusCode", HttpURLConnection.HTTP_INTERNAL_ERROR);
            }
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
