package de.xxschrandxx.wsc.bukkit.listener;

import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.xxschrandxx.wsc.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.bukkit.api.MinecraftBridgeEvent;
import de.xxschrandxx.wsc.bukkit.handler.*;
import de.xxschrandxx.wsc.bukkit.handler.permission.*;
import de.xxschrandxx.wsc.core.MinecraftBridgeHandler;
import de.xxschrandxx.wsc.core.MinecraftBridgeVars;
import de.xxschrandxx.wsc.core.permission.PermissionMethodEnum;

public class HandlerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnable(MinecraftBridgeEvent event) {
        MinecraftBridgeBukkit instance = MinecraftBridgeBukkit.getInstance();
        MinecraftBridgeHandler handler = instance.getHandler();
        handler.addPasswordHandler("/", new StatusHandler());
        handler.addPasswordHandler("/list", new UserListHandler());
        handler.addPasswordHandler("/command", new CommandHandler());
        handler.addPasswordHandler("/sendCode", new SendCodeHandler());
        if (instance.getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.modules.permission)) {
            if (instance.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
                instance.getLogger().log(Level.INFO, "WebServer: Permissionplugin LuckPerms found. Using it.");
                handler.addPasswordHandler("/permission", new LuckPermsPermissionHandler(PermissionMethodEnum.status));
                handler.addPasswordHandler("/permission/groupList", new LuckPermsPermissionHandler(PermissionMethodEnum.groupList));
                handler.addPasswordHandler("/permission/getUserGroups", new LuckPermsPermissionHandler(PermissionMethodEnum.getUserGroups));
                handler.addPasswordHandler("/permission/getUsersGroups", new LuckPermsPermissionHandler(PermissionMethodEnum.getUsersGroups));
                handler.addPasswordHandler("/permission/addUserToGroup", new LuckPermsPermissionHandler(PermissionMethodEnum.addUserToGroup));
                handler.addPasswordHandler("/permission/addUsersToGroups", new LuckPermsPermissionHandler(PermissionMethodEnum.addUsersToGroups));
                handler.addPasswordHandler("/permission/removeUserFromGroup", new LuckPermsPermissionHandler(PermissionMethodEnum.removeUserFromGroup));
                handler.addPasswordHandler("/permission/removeUsersFromGroups", new LuckPermsPermissionHandler(PermissionMethodEnum.removeUsersFromGroups));
            }
            else {
                instance.getLogger().log(Level.WARNING, "WebServer: No supportet Permissionplugin found.");
                handler.addPasswordHandler("/permission", new DefaultPermissionHandler());
                handler.addPasswordHandler("/permission/groupList", new DefaultPermissionHandler());
                handler.addPasswordHandler("/permission/getUserGroups", new DefaultPermissionHandler());
                handler.addPasswordHandler("/permission/getUsersGroups", new DefaultPermissionHandler());
                handler.addPasswordHandler("/permission/addUsersToGroups", new DefaultPermissionHandler());
                handler.addPasswordHandler("/permission/addUserToGroup", new DefaultPermissionHandler());
                handler.addPasswordHandler("/permission/removeUsersFromGroups", new DefaultPermissionHandler());
                handler.addPasswordHandler("/permission/removeUserFromGroup", new DefaultPermissionHandler());
            }
        }
    }
}
