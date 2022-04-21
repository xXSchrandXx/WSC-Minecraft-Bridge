package de.xxschrandxx.wsc.core;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class AbstractHttpHandler implements HttpHandler {

    protected final Gson gson = new Gson();

    protected final Type typeListString = new TypeToken<List<String>>(){}.getType();

    protected final Type typeStringObject = new TypeToken<HashMap<String, Object>>(){}.getType();
    protected final Type typeStringString = new TypeToken<HashMap<String, String>>(){}.getType();
    protected final Type typeStringInteger = new TypeToken<HashMap<String, Integer>>(){}.getType();
    protected final Type typeStringUUID = new TypeToken<HashMap<String, UUID>>(){}.getType();
    protected final Type typeStringListObjects = new TypeToken<HashMap<String, List<Object>>>(){}.getType();
    protected final Type typeStringListStrings = new TypeToken<HashMap<String, List<String>>>(){}.getType();
    protected final Type typeStringListIntegers = new TypeToken<HashMap<String, List<Integer>>>(){}.getType();
    protected final Type typeStringListUUIDs = new TypeToken<HashMap<String, List<UUID>>>(){}.getType();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        getLogger().log(Level.INFO, "WebServer: Incomming '" + exchange.getRequestMethod() + " " + exchange.getRequestURI() + "' Request from: " + exchange.getRemoteAddress().getAddress());

        HashMap<String, Object> response = run(exchange);
        String json = this.gson.toJson(response);
        byte[] jsonBytes = json.getBytes();

        if (!response.containsKey("statusCode")) {
            throw new IOException("WebServer: Reponse does not contain statusCode.");
        }
        if (!(response.get("statusCode") instanceof Integer)) {
            throw new IOException("WebServer: Response statusCode is not an integer.");
        }
        if (!response.containsKey("status")) {
            throw new IOException("WebServer: Reponse does not contain status.");
        }
        if (!(response.get("status") instanceof String)) {
            throw new IOException("WebServer: Response status is not a string.");
        }

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders((int) response.get("statusCode"), jsonBytes.length);
        exchange.getResponseBody().write(jsonBytes);
        exchange.close();
    }

    protected List<String> readRequestBodyListString(HttpExchange exchange) throws IOException, JsonParseException, JsonSyntaxException { 
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String requestString = new String(requestBytes, StandardCharsets.UTF_8);
        List<String> request = this.gson.fromJson(requestString, typeListString);
        return request;
    }

    protected HashMap<String, Object> readRequestBodyObject(HttpExchange exchange) throws IOException, JsonParseException, JsonSyntaxException {
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String requestString = new String(requestBytes, StandardCharsets.UTF_8);
        HashMap<String, Object> request = this.gson.fromJson(requestString, typeStringObject);
        return request;
    }

    protected HashMap<String, String> readRequestBodyString(HttpExchange exchange) throws IOException, JsonParseException, JsonSyntaxException {
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String requestString = new String(requestBytes, StandardCharsets.UTF_8);
        HashMap<String, String> request = this.gson.fromJson(requestString, typeStringString);
        return request;
    }

    protected HashMap<String, List<String>> readRequestBodyStringListStrings(HttpExchange exchange) throws IOException, JsonParseException, JsonSyntaxException {
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String requestString = new String(requestBytes, StandardCharsets.UTF_8);
        HashMap<String, List<String>> request = this.gson.fromJson(requestString, typeStringListStrings);
        return request;
    }

    protected abstract Logger getLogger();

    /**
     * Runs while handeled by {@link #handle(HttpExchange)}.
     * @see de.xxschrandxx.wsc.bungee.handler.StatusHandler#run(HttpExchange)
     * @see de.xxschrandxx.wsc.bukkit.handler.StatusHandler#run(HttpExchange)
     * @param exchange the exchange containing the request from the client
     * @return a {@link HashMap} containing the response for the client.
     *         Contained keys should be in <a href="https://en.wikipedia.org/wiki/Camel_case">Camel case</a>
     *         The response must have the key 'status' as {@link String} and 'statusCode' as {@link Integer} set.
     */
    protected abstract HashMap<String, Object> run(HttpExchange exchange);
}
