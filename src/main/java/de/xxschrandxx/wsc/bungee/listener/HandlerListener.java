package de.xxschrandxx.wsc.bungee.listener;

import java.util.logging.Level;

import de.xxschrandxx.wsc.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.bungee.api.MinecraftBridgeEvent;

import de.xxschrandxx.wsc.bungee.handler.*;
import de.xxschrandxx.wsc.bungee.handler.permission.*;
import de.xxschrandxx.wsc.core.MinecraftBridgeHandler;
import de.xxschrandxx.wsc.core.MinecraftBridgeVars;
import de.xxschrandxx.wsc.core.permission.PermissionMethodEnum;
import de.xxschrandxx.wsc.core.permission.PermissionPlugin;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class HandlerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnable(MinecraftBridgeEvent event) {
        MinecraftBridgeBungee instance = MinecraftBridgeBungee.getInstance();
        MinecraftBridgeHandler handler = instance.getHandler();
        handler.addPasswordHandler("/", new StatusHandler());
        handler.addPasswordHandler("/list", new UserListHandler());
        handler.addPasswordHandler("/command", new CommandHandler());
        handler.addPasswordHandler("/sendCode", new SendCodeHandler());
        if (instance.getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.modules.permission.enabled)) {
            PermissionPlugin plugin = PermissionPlugin.valueOf(instance.getConfiguration().getString(MinecraftBridgeVars.Configuration.modules.permission.plugin));
            switch (plugin) {
                case LuckPerms:
                    if (instance.getProxy().getPluginManager().getPlugin(plugin.toString()) != null) {
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
                    return;
            }
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
