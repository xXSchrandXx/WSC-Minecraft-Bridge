package de.xxschrandxx.wsc.bungee.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.xxschrandxx.wsc.bungee.MinecraftLinkerBungee;

public class VersionHandler implements HttpHandler {

    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("name", MinecraftLinkerBungee.getInstance().getDescription().getName());
        response.put("version", MinecraftLinkerBungee.getInstance().getDescription().getVersion());
        response.put("author", MinecraftLinkerBungee.getInstance().getDescription().getAuthor());
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
