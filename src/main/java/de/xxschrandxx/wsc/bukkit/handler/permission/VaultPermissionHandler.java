package de.xxschrandxx.wsc.bukkit.handler.permission;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.core.permission.AbstractPermsMethods;
import de.xxschrandxx.wsc.core.permission.PermissionMethodEnum;
import net.milkbowl.vault.permission.Permission;

public class VaultPermissionHandler extends AbstractPermsMethods {

    private final PermissionMethodEnum method;
    protected Permission api;
    protected String world = "world";

    public VaultPermissionHandler(PermissionMethodEnum method) {
        this.method = method;
        this.api = Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        this.plugin = this.api.getName();
        this.version = "unknown";
        for (World world : Bukkit.getWorlds()) {
            this.world = world.getName();
            break;
        }
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
    @Override
    public HashMap<String, Object> groupList() {
        HashMap<String, Object> response = new HashMap<String, Object>();
        String[] groups = this.api.getGroups();
        ArrayList<String> groupList = new ArrayList<String>();
        for (String group : groups) {
            groupList.add(group);
        }
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        response.put("groups", groupList);
        return response;
    }

    @Override
    public HashMap<String, Object> getUserGroups(HashMap<String, String> requestBody) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (!requestBody.containsKey("uuid")) {
            response.put("status", "No UUID given.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(requestBody.get("uuid"));
        }
        catch (IllegalArgumentException e) {
            response.put("status", "UUID could not be parsed.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        String[] groups = this.api.getPlayerGroups(this.world, player);
        ArrayList<String> groupList = new ArrayList<String>();
        for (String group : groups) {
            groupList.add(group);
        }
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        response.put("groups", groupList);
        return response;
    }

    @Override
    public HashMap<String, Object> addUserToGroup(HashMap<String, String> requestBody) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (!requestBody.containsKey("uuid")) {
            response.put("status", "No UUID given.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        if (!requestBody.containsKey("group")) {
            response.put("status", "No group givem.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        String[] groups = this.api.getGroups();
        ArrayList<String> groupList = new ArrayList<String>();
        for (String group : groups) {
            groupList.add(group);
        }
        if (!groupList.contains(requestBody.get("group"))) {
            response.put("status", "Group not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(requestBody.get("uuid"));
        }
        catch (IllegalArgumentException e) {
            response.put("status", "UUID could not be parsed.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        if (this.api.playerInGroup(this.world, player, requestBody.get("group"))) {
            response.put("status", "User already in group.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        this.api.playerAddGroup(this.world, player, requestBody.get("group"));
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        return response;
    }

    @Override
    public HashMap<String, Object> removeUserFromGroup(HashMap<String, String> requestBody) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (!requestBody.containsKey("uuid")) {
            response.put("status", "No UUID given.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        if (!requestBody.containsKey("group")) {
            response.put("status", "No group givem.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        String[] groups = this.api.getGroups();
        ArrayList<String> groupList = new ArrayList<String>();
        for (String group : groups) {
            groupList.add(group);
        }
        if (!groupList.contains(requestBody.get("group"))) {
            response.put("status", "Group not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(requestBody.get("uuid"));
        }
        catch (IllegalArgumentException e) {
            response.put("status", "UUID could not be parsed.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        if (!this.api.playerInGroup(this.world, player, requestBody.get("group"))) {
            response.put("status", "User not in group.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        this.api.playerRemoveGroup(this.world, player, requestBody.get("group"));
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        return response;
    }
}
