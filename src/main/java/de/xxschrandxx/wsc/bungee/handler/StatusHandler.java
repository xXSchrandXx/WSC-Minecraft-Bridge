package de.xxschrandxx.wsc.bungee.handler;

import java.net.HttpURLConnection;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bungee.MinecraftLinkerBungee;
import de.xxschrandxx.wsc.bungee.api.AbstractHttpHandler;

public class StatusHandler extends AbstractHttpHandler {

    @Override
    public HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
            response.put("status", "OK");
            response.put("statusCode", HttpURLConnection.HTTP_OK);
            response.put("name", MinecraftLinkerBungee.getInstance().getDescription().getName());
            response.put("version", MinecraftLinkerBungee.getInstance().getDescription().getVersion());
            response.put("applicationAuthor", MinecraftLinkerBungee.getInstance().getDescription().getAuthor());
            response.put("applicationDescription", MinecraftLinkerBungee.getInstance().getDescription().getDescription());
            response.put("platformName", MinecraftLinkerBungee.getInstance().getProxy().getName());
            response.put("platformVersion", MinecraftLinkerBungee.getInstance().getProxy().getVersion());
        }
        else {
            response.put("status", "Method Not Allowed.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_METHOD);
        }
        return response;
    }

}
