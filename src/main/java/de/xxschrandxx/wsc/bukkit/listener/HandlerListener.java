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
import de.xxschrandxx.wsc.core.permission.PermissionPlugin;

public class HandlerListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEnable(MinecraftBridgeEvent event) {
        MinecraftBridgeBukkit instance = MinecraftBridgeBukkit.getInstance();
        MinecraftBridgeHandler handler = instance.getHandler();
        handler.addPasswordHandler("/", new StatusHandler());
        handler.addPasswordHandler("/list", new UserListHandler());
        handler.addPasswordHandler("/command", new CommandHandler());
        handler.addPasswordHandler("/broadcast", new BroadcastHandler());
        handler.addPasswordHandler("/message", new MessageHandler());
        handler.addPasswordHandler("/sendCode", new SendCodeHandler());
        if (instance.getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.modules.groupsync.enabled)) {
            PermissionPlugin plugin = PermissionPlugin.valueOf(instance.getConfiguration().getString(MinecraftBridgeVars.Configuration.modules.groupsync.plugin));
            switch (plugin) {
                case LuckPerms:
                    if (instance.getServer().getPluginManager().getPlugin(plugin.getName()) != null) {
                        instance.getLogger().log(Level.INFO, "WebServer: Permissionplugin " + plugin.getName() + " found. Using it.");
                        handler.addPasswordHandler("/permission", new LuckPermsPermissionHandler(PermissionMethodEnum.status));
                        handler.addPasswordHandler("/permission/groupList", new LuckPermsPermissionHandler(PermissionMethodEnum.groupList));
                        handler.addPasswordHandler("/permission/getUserGroups", new LuckPermsPermissionHandler(PermissionMethodEnum.getUserGroups));
                        handler.addPasswordHandler("/permission/getUsersGroups", new LuckPermsPermissionHandler(PermissionMethodEnum.getUsersGroups));
                        handler.addPasswordHandler("/permission/addUserToGroup", new LuckPermsPermissionHandler(PermissionMethodEnum.addUserToGroup));
                        handler.addPasswordHandler("/permission/addUsersToGroups", new LuckPermsPermissionHandler(PermissionMethodEnum.addUsersToGroups));
                        handler.addPasswordHandler("/permission/removeUserFromGroup", new LuckPermsPermissionHandler(PermissionMethodEnum.removeUserFromGroup));
                        handler.addPasswordHandler("/permission/removeUsersFromGroups", new LuckPermsPermissionHandler(PermissionMethodEnum.removeUsersFromGroups));
                        return;
                    }
                case CloudNet:
                    if (instance.getServer().getPluginManager().getPlugin(plugin.getName()) != null) {
                        instance.getLogger().log(Level.INFO, "WebServer: Permissionplugin " + plugin.getName() + " found. Using it.");
                        handler.addPasswordHandler("/permission", new CloudNetPermissionHandler(PermissionMethodEnum.status));
                        handler.addPasswordHandler("/permission/groupList", new CloudNetPermissionHandler(PermissionMethodEnum.groupList));
                        handler.addPasswordHandler("/permission/getUserGroups", new CloudNetPermissionHandler(PermissionMethodEnum.getUserGroups));
                        handler.addPasswordHandler("/permission/getUsersGroups", new CloudNetPermissionHandler(PermissionMethodEnum.getUsersGroups));
                        handler.addPasswordHandler("/permission/addUserToGroup", new CloudNetPermissionHandler(PermissionMethodEnum.addUserToGroup));
                        handler.addPasswordHandler("/permission/addUsersToGroups", new CloudNetPermissionHandler(PermissionMethodEnum.addUsersToGroups));
                        handler.addPasswordHandler("/permission/removeUserFromGroup", new CloudNetPermissionHandler(PermissionMethodEnum.removeUserFromGroup));
                        handler.addPasswordHandler("/permission/removeUsersFromGroups", new CloudNetPermissionHandler(PermissionMethodEnum.removeUsersFromGroups));
                        return;
                    }
                case Vault:
                    if (instance.getServer().getPluginManager().getPlugin(plugin.getName()) != null) {
                        instance.getLogger().log(Level.INFO, "WebServer: Permissionplugin " + plugin.getName() + " found. Using it.");
                        handler.addPasswordHandler("/permission", new VaultPermissionHandler(PermissionMethodEnum.status));
                        handler.addPasswordHandler("/permission/groupList", new VaultPermissionHandler(PermissionMethodEnum.groupList));
                        handler.addPasswordHandler("/permission/getUserGroups", new VaultPermissionHandler(PermissionMethodEnum.getUserGroups));
                        handler.addPasswordHandler("/permission/getUsersGroups", new VaultPermissionHandler(PermissionMethodEnum.getUsersGroups));
                        handler.addPasswordHandler("/permission/addUserToGroup", new VaultPermissionHandler(PermissionMethodEnum.addUserToGroup));
                        handler.addPasswordHandler("/permission/addUsersToGroups", new VaultPermissionHandler(PermissionMethodEnum.addUsersToGroups));
                        handler.addPasswordHandler("/permission/removeUserFromGroup", new VaultPermissionHandler(PermissionMethodEnum.removeUserFromGroup));
                        handler.addPasswordHandler("/permission/removeUsersFromGroups", new VaultPermissionHandler(PermissionMethodEnum.removeUsersFromGroups));
                        return;
                    }
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
