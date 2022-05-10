package de.xxschrandxx.wsc.bungee;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;

import org.bstats.bungeecord.Metrics;

import de.xxschrandxx.wsc.bungee.api.MinecraftBridgeEvent;
import de.xxschrandxx.wsc.bungee.command.*;
import de.xxschrandxx.wsc.bungee.listener.*;
import de.xxschrandxx.wsc.core.IMinecraftBridge;
import de.xxschrandxx.wsc.core.MinecraftBridgeHandler;
import de.xxschrandxx.wsc.core.MinecraftBridgeVars;
import de.xxschrandxx.wsc.core.permission.PermissionPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MinecraftBridgeBungee extends Plugin implements IMinecraftBridge<CommandSender> {

    private static MinecraftBridgeBungee instance;

    public static MinecraftBridgeBungee getInstance() {
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
                sender.sendMessage(new TextComponent("Could not set address. \n" + e.getMessage()));
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
                sender.sendMessage(new TextComponent("Could not create Webserver!"));
            }
            return false;
        }
        if (sender == null) {
            getLogger().log(Level.INFO, "WebServer: Created instance.");
        }
        else {
            sender.sendMessage(new TextComponent("WebServer: Created instance."));
        }
        instance.getProxy().getPluginManager().callEvent(new MinecraftBridgeEvent());
        if (sender == null) {
            getLogger().log(Level.INFO, "Called registration event.");
        }
        else {
            sender.sendMessage(new TextComponent("Called registration event."));
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
                    sender.sendMessage(new TextComponent("WebServer started with ssl."));
                }
                return;
            }
            if (sender == null) {
                getLogger().log(Level.WARNING, "WebServer could not start with ssl. Starting without it.");
            }
            else {
                sender.sendMessage(new TextComponent("WebServer could not start with ssl. Starting without it."));
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
            sender.sendMessage(new TextComponent("WebServer started."));
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
                sender.sendMessage(new TextComponent("Webserver stopped"));
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
        new Metrics(this, 14659);

        if (!setHandler(null)) {
            return;
        }

        getProxy().getPluginManager().registerListener(getInstance(), new HandlerListener());
        getProxy().getPluginManager().registerListener(getInstance(), new ModulesListener());
        getProxy().getPluginManager().registerCommand(getInstance(), new WSCBridge());

        getProxy().getPluginManager().callEvent(new MinecraftBridgeEvent());

        // TODO wait until every plugin has started.
        startHandler(null);
    }

    @Override
    public void onDisable() {
        stopHandler(null);
    }
    // end of plugin part

    // start config part
    private File configFile = new File(getDataFolder(), "config.yml");
    private Configuration config;

    @Override
    public Configuration getConfiguration() {
        return getInstance().config;
    }

    @Override
    public boolean reloadConfiguration() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if (configFile.exists()) {
            try {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            }
            catch (IOException e) {
                getLogger().log(Level.WARNING, "Could not load config.yml.", e);
                return false;
            }
        }
        else {
            try {
                configFile.createNewFile();
            }
            catch (IOException e) {
                getLogger().log(Level.WARNING, "Could not create config.yml.", e);
                return false;
            }
            config = new Configuration();
        }

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
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
        }
        catch (IOException e) {
            getLogger().log(Level.WARNING, "Could not save config.yml.", e);
            return false;
        }
        return true;
    }
    // end config part
}
