package de.xxschrandxx.wsc.core.permission;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.milkbowl.vault.permission.Permission;

public abstract class VaultMethods extends AbstractPermsMethods {

    protected Permission api;

    public VaultMethods(Permission api) {
        this.api = api;
        this.plugin = this.api.getName();
        this.version = "unknown";
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
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        String[] groups = this.api.getPlayerGroups(player);
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
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        if (this.api.playerInGroup(player, requestBody.get("group"))) {
            response.put("status", "User already in group.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        this.api.playerAddGroup(player, requestBody.get("group"));
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
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        if (!this.api.playerInGroup(player, requestBody.get("group"))) {
            response.put("status", "User not in group.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        this.api.playerRemoveGroup(player, requestBody.get("group"));
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        return response;
    }
}
