package de.xxschrandxx.wsc.bungee.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bungee.api.AbstractHttpHandler;
import de.xxschrandxx.wsc.bungee.api.MinecraftLinkerCommandSender;

public class CommandHandler extends AbstractHttpHandler {

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
            String command = request.get("command");
            MinecraftLinkerCommandSender sender = new MinecraftLinkerCommandSender();
            sender.setIP(exchange.getRemoteAddress().getAddress());
            if (sender.dispatchCommand(command)) {
                response.put("response", sender.flush());
                response.put("status", "OK");
                response.put("statusCode", HttpURLConnection.HTTP_OK);
            }
            else {
                response.put("status", "Command not found.");
                response.put("statusCode", HttpURLConnection.HTTP_INTERNAL_ERROR);
            }
        }
        catch (JsonSyntaxException e) {
            response.put("status", "JsonSyntaxException. " + e.getMessage());
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            e.printStackTrace();
        }
        catch (IOException e) {
            response.put("status", "Internal Server Error.");
            response.put("statusCode", HttpURLConnection.HTTP_INTERNAL_ERROR);
            e.printStackTrace();
        }
        return response;
    }
}
