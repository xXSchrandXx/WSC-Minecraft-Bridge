package de.xxschrandxx.wsc.core.permission;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public abstract class LuckPermsMethods extends AbstractPermsMethods {

    protected LuckPerms api;

    public LuckPermsMethods(LuckPerms api) {
        this.api = api;
        this.plugin = this.api.getPluginMetadata().getVersion();
    }

    @Override
    public HashMap<String, Object> groupList() {
        HashMap<String, Object> response = new HashMap<String, Object>();
        this.api.getGroupManager().loadAllGroups().join();
        Set<Group> groupSet = this.api.getGroupManager().getLoadedGroups();
        ArrayList<String> groupList = new ArrayList<String>();
        for (Group group: groupSet) {
            groupList.add(group.getFriendlyName());
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
        User user = this.api.getUserManager().getUser(uuid);
        if (user == null) {
            CompletableFuture<User> userFuture = this.api.getUserManager().loadUser(uuid);
            user = userFuture.join();
        }
        if (user == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        Collection<Group> groupCollection = user.getInheritedGroups(user.getQueryOptions());
        ArrayList<String> groupList = new ArrayList<String>();
        for (Group group : groupCollection) {
            groupList.add(group.getFriendlyName());
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
        Group group = this.api.getGroupManager().getGroup(requestBody.get("group"));
        if (group == null) {
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
        User user = this.api.getUserManager().getUser(uuid);
        if (user == null) {
            CompletableFuture<User> userFuture = this.api.getUserManager().loadUser(uuid);
            user = userFuture.join();
        }
        if (user == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        Collection<Group> groupCollection = user.getInheritedGroups(user.getQueryOptions());
        if (groupCollection.contains(group)) {
            response.put("status", "User already in group.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        user.data().add(Node.builder("group." + group.getName()).build());
        this.api.getUserManager().saveUser(user);
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
        Group group = this.api.getGroupManager().getGroup(requestBody.get("group"));
        if (group == null) {
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
        User user = this.api.getUserManager().getUser(uuid);
        if (user == null) {
            CompletableFuture<User> userFuture = this.api.getUserManager().loadUser(uuid);
            user = userFuture.join();
        }
        if (user == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        Collection<Group> groupCollection = user.getInheritedGroups(user.getQueryOptions());
        if (!groupCollection.contains(group)) {
            response.put("status", "User not in group.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        user.data().remove(Node.builder("group." + group.getName()).build());
        this.api.getUserManager().saveUser(user);
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        return response;
    }
}
