package de.xxschrandxx.wsc.core.permission;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import de.xxschrandxx.wsc.core.AbstractHttpHandler;

public abstract class AbstractPermsMethods extends AbstractHttpHandler {
    protected final HashMap<String, Object> notFound = new HashMap<String, Object>() {{
        put("status", "Not found.");
        put("statusCode", HttpURLConnection.HTTP_NOT_FOUND);
    }};

    /**
     * Name of the plugin.
     * Override with used plugin.
     */
    protected String plugin = "none";

    /**
     * Version of the plugin.
     * Override with used version.
     */
    protected String version = "none";

    /**
     * Returns the status of the permission handler.
     * @return Hashmap containing status information.
     */
    public HashMap<String, Object> status() {
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        response.put("plugin", this.plugin);
        response.put("version", this.version);
        return response;
    }

    /**
     * Returns the groups of the server.
     * @return Hashamp containing a list of groups with key `groups`, status and statusCode.
     */
    public HashMap<String, Object> groupList() {
        return this.notFound;
    }

    /**
     * Returns the groups of an user.
     * @param requestBody Hashmap that should contain uuid under key `uuid`.
     * @return Hashmap containing a list of groups of an user with key `groups`, status and statusCode.
     */
    public HashMap<String, Object> getUserGroups(HashMap<String, String> requestBody) {
        return this.notFound;
    }

    /**
     * TODO
     * @param requestBody
     * @return
     */
    public HashMap<String, Object> getUsersGroups(List<String> requestBody) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (requestBody.isEmpty()) {
            response.put("status", "Empty request");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        HashMap<String, Object> userGroups = new HashMap<String, Object>();
        for (String uuidString : requestBody) {
            UUID uuid = null;
            try {
                uuid = UUID.fromString(uuidString);
            }
            catch (IllegalArgumentException e) {
                HashMap<String, Object> tmpResponse = new HashMap<String, Object>();
                tmpResponse.put("status", "UUID could not be parsed.");
                tmpResponse.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
                userGroups.put(uuidString, tmpResponse);
                continue;
            }
            HashMap<String, String> request = new HashMap<String, String>();
            request.put("uuid", uuid.toString());
            HashMap<String, Object> tmpResponse = getUserGroups(request);
            userGroups.put(uuid.toString(), tmpResponse);
        }
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        response.put("users", userGroups);
        return response;
    }

    /**
     * Add an user to group.
     * @param requestBody Hashmap that should contain uuid under key `uuid` and groupname under key `group`.
     * @return Hashmap containing status and statusCode.
     */
    public HashMap<String, Object> addUserToGroup(HashMap<String, String> requestBody) {
        return this.notFound;
    }

    /**
     * TODO
     * @param requestBody
     * @return
     */
    public HashMap<String, Object> addUsersToGroups(HashMap<String, List<String>> requestBody) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (requestBody.isEmpty()) {
            response.put("status", "Empty request");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        HashMap<String, Object> results = new HashMap<String, Object>();
        for (Entry<String, List<String>> entry : requestBody.entrySet()) {
            HashMap<String, Object> tmpResponse = new HashMap<String, Object>();
            String uuidString = entry.getKey();
            UUID uuid = null;
            try {
                uuid = UUID.fromString(uuidString);
            }
            catch (IllegalArgumentException e) {
                tmpResponse.put("status", "UUID could not be parsed.");
                tmpResponse.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
                results.put(uuidString, tmpResponse);
                continue;
            }
            HashMap<String, Object> groupResonse = new HashMap<String, Object>();
            for (String groupName : entry.getValue()) {
                HashMap<String, String> request = new HashMap<String, String>();
                request.put("uuid", uuid.toString());
                request.put("group", groupName);
                groupResonse.put(groupName, addUserToGroup(request));
            }
            results.put(uuid.toString(), groupResonse);
        }
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        response.put("users", results);
        return response;
    }

    /**
     * Removes an user from group.
     * @param requestBody Hashmap that should contain uuid under key `uuid` and groupname under key `group`.
     * @return Hashmap containing status and statusCode.
     */
    public HashMap<String, Object> removeUserFromGroup(HashMap<String, String> requestBody) {
        return this.notFound;
    }
    /**
     * TODO
     * @param requestBody
     * @return
     */
    public HashMap<String, Object> removeUsersFromGroups(HashMap<String, List<String>> requestBody) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        if (requestBody.isEmpty()) {
            response.put("status", "Empty request");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
        HashMap<String, Object> results = new HashMap<String, Object>();
        for (Entry<String, List<String>> entry : requestBody.entrySet()) {
            HashMap<String, Object> tmpResponse = new HashMap<String, Object>();
            String uuidString = entry.getKey();
            UUID uuid = null;
            try {
                uuid = UUID.fromString(uuidString);
            }
            catch (IllegalArgumentException e) {
                tmpResponse.put("status", "UUID could not be parsed.");
                tmpResponse.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
                results.put(uuidString, tmpResponse);
                continue;
            }
            HashMap<String, Object> groupResonse = new HashMap<String, Object>();
            for (String groupName : entry.getValue()) {
                HashMap<String, String> request = new HashMap<String, String>();
                request.put("uuid", uuid.toString());
                request.put("group", groupName);
                groupResonse.put(groupName, removeUserFromGroup(request));
            }
            results.put(uuid.toString(), groupResonse);
        }
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);
        response.put("users", results);
        return response;
    }

}
