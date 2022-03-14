package de.xxschrandxx.wsc.core;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class AbstractHttpHandler implements HttpHandler {

    protected final Gson gson = new Gson();

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
        getLogger().log(Level.INFO, "WebServer: Incomming '" + exchange.getRequestMethod() + "' Request from: " + exchange.getRemoteAddress().getAddress());

        HashMap<String, Object> response = run(exchange);
        String json = this.gson.toJson(response);
        byte[] jsonBytes = json.getBytes();

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders((int) response.get("statusCode"), jsonBytes.length);
        exchange.getResponseBody().write(jsonBytes);
        exchange.close();
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
