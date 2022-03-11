package de.xxschrandxx.wsc.bungee.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.xxschrandxx.wsc.bungee.MinecraftLinkerBungee;

public class CommandHandler implements HttpHandler {

    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HashMap<String, Object> response = new HashMap<String, Object>();

        // TODO
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String request = new String(requestBytes, StandardCharsets.UTF_8);

        MinecraftLinkerBungee.getInstance().getLogger().log(Level.INFO, request);

        String json = this.gson.toJson(response);
        byte[] jsonBytes = json.getBytes();
        try {
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, jsonBytes.length);
            exchange.getResponseBody().write(jsonBytes);
            exchange.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
