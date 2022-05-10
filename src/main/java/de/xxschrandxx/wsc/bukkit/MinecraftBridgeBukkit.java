package de.xxschrandxx.wsc.bukkit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.xxschrandxx.wsc.bukkit.api.MinecraftBridgeEvent;
import de.xxschrandxx.wsc.bukkit.command.*;
import de.xxschrandxx.wsc.bukkit.listener.*;
import de.xxschrandxx.wsc.core.IMinecraftBridge;
import de.xxschrandxx.wsc.core.MinecraftBridgeHandler;
import de.xxschrandxx.wsc.core.MinecraftBridgeVars;
import de.xxschrandxx.wsc.core.permission.PermissionPlugin;

public class MinecraftBridgeBukkit extends JavaPlugin implements IMinecraftBridge<CommandSender> {

    private static MinecraftBridgeBukkit instance;

    public static MinecraftBridgeBukkit getInstance() {
        return instance;
    }

    // start of handler part
    private MinecraftBridgeHandler handler;

    @Override
    public MinecraftBridgeHandler getHandler() {
        return this.handler;
    }

    @Override
    public boolean setHandler(CommandSender sender) {
        InetSocketAddress addr;
        try {
            addr = new InetSocketAddress(getConfiguration().getString(MinecraftBridgeVars.Configuration.server.hostname), getConfiguration().getInt(MinecraftBridgeVars.Configuration.server.port));
        }
        catch (IllegalArgumentException | SecurityException e) {
            if (sender == null) {
                getLogger().log(Level.SEVERE, "Could not set address.", e);
            }
            else {
                sender.sendMessage("Could not set address. \n" + e.getMessage());
            }
            return false;
        }

        try {
            this.handler = new MinecraftBridgeHandler(
                getLogger(),
                addr,
                getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.server.ssl.enabled),
                getConfiguration().getString(MinecraftBridgeVars.Configuration.server.user),
                getConfiguration().getString(MinecraftBridgeVars.Configuration.server.password)
            );
        }
        catch (IOException e) {
            if (sender == null) {
                getLogger().log(Level.SEVERE, "Could not create Webserver!");
            }
            else {
                sender.sendMessage("Could not create Webserver!");
            }
            return false;
        }
        if (sender == null) {
            getLogger().log(Level.INFO, "WebServer: Created instance.");
        }
        else {
            sender.sendMessage("WebServer: Created instance.");
        }
        instance.getServer().getPluginManager().callEvent(new MinecraftBridgeEvent());
        if (sender == null) {
            getLogger().log(Level.INFO, "Called registration event.");
        }
        else {
            sender.sendMessage("Called registration event.");
        }
    return true;
    }

    @Override
    public void startHandler(CommandSender sender) {
        if (getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.server.ssl.enabled)) {
            if (
                getInstance().handler.start(
                    getConfiguration().getString(MinecraftBridgeVars.Configuration.server.whitelistPath),
                    getConfiguration().getString(MinecraftBridgeVars.Configuration.server.blacklistPath),
                    getConfiguration().getInt(MinecraftBridgeVars.Configuration.server.floodgate.maxTries),
                    getConfiguration().getLong(MinecraftBridgeVars.Configuration.server.floodgate.resetTime),
                    getConfiguration().getInt(MinecraftBridgeVars.Configuration.server.floodgate.maxOverruns),
                    getConfiguration().getString(MinecraftBridgeVars.Configuration.server.ssl.keyStorePath),
                    getConfiguration().getString(MinecraftBridgeVars.Configuration.server.ssl.keyStorePassword),
                    getDataFolder(),
                    getConfiguration().getString(MinecraftBridgeVars.Configuration.server.ssl.keyAlias),
                    getConfiguration().getString(MinecraftBridgeVars.Configuration.server.ssl.keyPassword)
                )
            ) {
                if (sender == null) {
                    getLogger().log(Level.INFO, "WebServer started with ssl.");
                }
                else {
                    sender.sendMessage("WebServer started with ssl.");
                }
                return;
            }
            if (sender == null) {
                getLogger().log(Level.WARNING, "WebServer could not start with ssl. Starting without it.");
            }
            else {
                sender.sendMessage("WebServer could not start with ssl. Starting without it.");
            }
        }
        getInstance().handler.start(
            getConfiguration().getString(MinecraftBridgeVars.Configuration.server.whitelistPath),
            getConfiguration().getString(MinecraftBridgeVars.Configuration.server.blacklistPath),
            getConfiguration().getInt(MinecraftBridgeVars.Configuration.server.floodgate.maxTries),
            getConfiguration().getLong(MinecraftBridgeVars.Configuration.server.floodgate.resetTime),
            getConfiguration().getInt(MinecraftBridgeVars.Configuration.server.floodgate.maxOverruns),
            null,
            null,
            null,
            null,
            null
        );
        if (sender == null) {
            getLogger().log(Level.INFO, "WebServer started.");
        }
        else {
            sender.sendMessage("WebServer started.");
        }
    }

    @Override
    public void stopHandler(CommandSender sender) {
        this.stopHandler(sender, true);
    }

    @Override
    public void stopHandler(CommandSender sender, boolean saveLists) {
        if (getHandler() != null) {
            getHandler().stop(saveLists);
            if (sender == null) {
                getLogger().log(Level.INFO, "WebServer stopped.");
            }
            else {
                sender.sendMessage("Webserver stopped");
            }
        }
    }
    // end of handler part

    // start of plugin part
    @Override
    public void onEnable() {
        instance = this;

        if (!reloadConfiguration()) {
            getLogger().log(Level.SEVERE, "Could not load config.yml, disabeling plugin!");
            onDisable();
            return;
        }


        // start bstats
        new Metrics(this, 14658);

        if (!setHandler(null)) {
            return;
        }

        getServer().getPluginManager().registerEvents(new HandlerListener(), getInstance());
        getServer().getPluginManager().registerEvents(new ModulesListener(), getInstance());
        getCommand("wscbridge").setExecutor(new WSCBridge());

        getServer().getPluginManager().callEvent(new MinecraftBridgeEvent());

        // TODO wait until every plugin has started.
        startHandler(null);
    }

    @Override
    public void onDisable() {
        stopHandler(null);
    }
    // end of plugin part

    // start config part
    @Override
    public FileConfiguration getConfiguration() {
        return getInstance().getConfig();
    }

    @Override
    public boolean reloadConfiguration() {
        reloadConfig();

        boolean error = false;

        // start config data

        // hostname
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.hostname, MinecraftBridgeVars.Configuration.server.defaults.hostname))
            error = true;
        // port
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.port, MinecraftBridgeVars.Configuration.server.defaults.port))
            error = true;
        // user
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.user, MinecraftBridgeVars.Configuration.server.defaults.user))
            error = true;
        // password
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.password, MinecraftBridgeVars.Configuration.server.defaults.password))
            error = true;

        // Floodgate
        // whitelistPath
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.whitelistPath, MinecraftBridgeVars.Configuration.server.defaults.whitelistPath))
            error = true;
        // blacklistPath
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.blacklistPath, MinecraftBridgeVars.Configuration.server.defaults.blacklistPath))
            error = true;
        // maxTries
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.floodgate.maxTries, MinecraftBridgeVars.Configuration.server.floodgate.defaults.maxTries))
            error = true;
        // resetTime
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.floodgate.resetTime, MinecraftBridgeVars.Configuration.server.floodgate.defaults.resetTime))
            error = true;
        // maxOverruns
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.floodgate.maxOverruns, MinecraftBridgeVars.Configuration.server.floodgate.defaults.maxOverruns))
            error = true;

        // SSL
        // enabled
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.ssl.enabled, MinecraftBridgeVars.Configuration.server.ssl.defaults.enabled))
            error = true;
        // keyStorePath
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.ssl.keyStorePath, MinecraftBridgeVars.Configuration.server.ssl.defaults.keyStorePath))
            error = true;
        // keyStorePassword
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.ssl.keyStorePassword, MinecraftBridgeVars.Configuration.server.ssl.defaults.keyStorePassword))
            error = true;
        // keyAlias
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.ssl.keyAlias, MinecraftBridgeVars.Configuration.server.ssl.defaults.keyAlias))
            error = true;
        // keyPassword
        if (checkConfiguration(MinecraftBridgeVars.Configuration.server.ssl.keyPassword, MinecraftBridgeVars.Configuration.server.ssl.defaults.keyPassword))
            error = true;

        // Modules
        // Permission Module
        // enabled
        if (checkConfiguration(MinecraftBridgeVars.Configuration.modules.groupsync.enabled, MinecraftBridgeVars.Configuration.modules.groupsync.defaults.enabled))
            error = true;
        // plugin
        if (checkConfiguration(MinecraftBridgeVars.Configuration.modules.groupsync.plugin, MinecraftBridgeVars.Configuration.modules.groupsync.defaults.plugin))
            error = true;

        // end config data

        if (error) {
            if (!saveConfiguration()) {
                return false;
            }
            return reloadConfiguration();
        }
        return true;
    }

    @Override
    public boolean checkConfiguration(String path, Object def) {
        Object o = getConfiguration().get(path);
        if (o == null) {
            getLogger().log(Level.WARNING, path + " is not set. Resetting it.");
            getConfiguration().set(path, def);
            return true;
        }
        else {
            // Check valid enums
            if (def instanceof PermissionPlugin) {
                try {
                    PermissionPlugin.valueOf((String) o);
                }
                catch (IllegalArgumentException | NullPointerException e) {
                    getLogger().log(Level.WARNING, path + " is wrong. Resetting it.");
                    getConfiguration().set(path, def.toString());
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public boolean saveConfiguration() {
        saveConfig();
        return true;
    }
    // end config part
}
