package de.xxschrandxx.wsc.wscbridge.bukkit.handler.permission;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.wscbridge.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.wscbridge.core.permission.LuckPermsMethods;
import de.xxschrandxx.wsc.wscbridge.core.permission.PermissionMethodEnum;
import net.luckperms.api.LuckPermsProvider;

public class LuckPermsPermissionHandler extends LuckPermsMethods {

    private final PermissionMethodEnum method;

    public LuckPermsPermissionHandler(PermissionMethodEnum method) {
        super(LuckPermsProvider.get());
        this.method = method;
    }

    @Override
    protected Logger getLogger() {
        return MinecraftBridgeBukkit.getInstance().getLogger();
    }

    @Override
    protected HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        try {
            if (method == PermissionMethodEnum.status) {
                if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                    return status();
                }
            }
            else if (method == PermissionMethodEnum.groupList) {
                if (exchange.getRequestMethod().equalsIgnoreCase("get")) {
                    return groupList();
                }
            }
            else if (method == PermissionMethodEnum.getUserGroups) {
                if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                    return getUserGroups(readRequestBodyString(exchange));
                }
            }
            else if (method == PermissionMethodEnum.getUsersGroups) {
                if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                    return getUsersGroups(readRequestBodyListString(exchange));
                }
            }
            else if (method == PermissionMethodEnum.addUserToGroup) {
                if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                    return addUserToGroup(readRequestBodyString(exchange));
                }
            }
            else if (method == PermissionMethodEnum.addUsersToGroups) {
                if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                    return addUsersToGroups(readRequestBodyStringListStrings(exchange));
                }
            }
            else if (method == PermissionMethodEnum.removeUserFromGroup) {
                if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                    return removeUserFromGroup(readRequestBodyString(exchange));
                }
            }
            else if (method == PermissionMethodEnum.removeUsersFromGroups) {
                if (exchange.getRequestMethod().equalsIgnoreCase("post")) {
                    return removeUsersFromGroups(readRequestBodyStringListStrings(exchange));
                }
            }
            else {
                return this.notFound;
            }
        }
        catch (IOException e) {
            response.put("status", "Could not read request body.");
            response.put("statusCode", HttpURLConnection.HTTP_INTERNAL_ERROR);
            return response;
        }
        catch (JsonSyntaxException e) {
            response.put("status", "Malformed JSON element.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        catch (JsonParseException e) {
            response.put("status", "Could parse JSON.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        response.put("status", "Method Not Allowed.");
        response.put("statusCode", HttpURLConnection.HTTP_BAD_METHOD);
        return response;
    }
}
