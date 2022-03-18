package de.xxschrandxx.wsc.bungee.handler.permission;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import de.xxschrandxx.wsc.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.core.permission.LuckPermsMethods;
import de.xxschrandxx.wsc.core.permission.PermissionMethodEnum;
import net.luckperms.api.LuckPermsProvider;

public class LuckPermsPermissionHandler extends LuckPermsMethods {

    private final PermissionMethodEnum method;

    public LuckPermsPermissionHandler(PermissionMethodEnum method) {
        super(LuckPermsProvider.get());
        this.method = method;
    }

    @Override
    protected Logger getLogger() {
        return MinecraftBridgeBungee.getInstance().getLogger();
    }

    @Override
    protected HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        try {
            if (method == PermissionMethodEnum.status) {
                return status();
            }
            else if (method == PermissionMethodEnum.groupList) {
                return groupList();
            }
            else if (method == PermissionMethodEnum.getUserGroups) {
                return getUserGroups(readRequestBody(exchange));
            }
            else if (method == PermissionMethodEnum.addUserToGroup) {
                return addUserToGroup(readRequestBody(exchange));
            }
            else if (method == PermissionMethodEnum.removeUserFromGroup) {
                return removeUserFromGroup(readRequestBody(exchange));
            }
            else {
                response.put("status", "Not found.");
                response.put("statusCode", HttpURLConnection.HTTP_NOT_FOUND);
                return response;
            }
        }
        catch (IOException e) {
            response.put("status", "Could not read request body.");
            response.put("statusCode", HttpURLConnection.HTTP_INTERNAL_ERROR);
            return response;
        }
        catch (JsonParseException e) {
            response.put("status", "Could parse JSON.");
            response.put("statusCode", HttpURLConnection.HTTP_BAD_REQUEST);
            return response;
        }
    }
}
