package de.xxschrandxx.wsc.core.permission;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionManagement;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;

public abstract class CloudNetMethods extends AbstractPermsMethods {

    protected IPermissionManagement api;

    public CloudNetMethods(IPermissionManagement api, String version) {
        this.api = api;
        this.plugin = "CloudNet-CloudPerms";
        this.version = version;
    }

    @Override
    public HashMap<String, Object> groupList() {
        HashMap<String, Object> response = new HashMap<String, Object>();
        Collection<IPermissionGroup> groupSet = this.api.getGroups();
        ArrayList<String> groupList = new ArrayList<String>();
        for (IPermissionGroup group: groupSet) {
            groupList.add(group.getName());
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
        IPermissionUser user = this.api.getUser(uuid);
        if (user == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        response.put("groups", new ArrayList<String>(user.getGroupNames()));
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
        IPermissionGroup group = this.api.getGroup(requestBody.get("group"));
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
        IPermissionUser user = this.api.getUser(uuid);
        if (user == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        Collection<String> groupCollection = user.getGroupNames();
        if (groupCollection.contains(group.getName())) {
            response.put("status", "User already in group.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        user.addGroup(group.getName());
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
        IPermissionGroup group = this.api.getGroup(requestBody.get("group"));
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
        IPermissionUser user = this.api.getUser(uuid);
        if (user == null) {
            response.put("status", "User not found.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        Collection<String> groupCollection = user.getGroupNames();
        if (!groupCollection.contains(group.getName())) {
            response.put("status", "User not in group.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        user.removeGroup(group.getName());
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        return response;
    }
}
