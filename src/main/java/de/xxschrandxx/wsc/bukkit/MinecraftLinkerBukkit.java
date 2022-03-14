package de.xxschrandxx.wsc.bukkit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import de.xxschrandxx.wsc.bukkit.api.MinecraftLinkerEvent;
import de.xxschrandxx.wsc.bukkit.command.WSCLinker;
import de.xxschrandxx.wsc.bukkit.listener.HandlerListener;
import de.xxschrandxx.wsc.core.IMinecraftLinker;
import de.xxschrandxx.wsc.core.MinecraftLinkerHandler;
import de.xxschrandxx.wsc.core.MinecraftLinkerVars;

public class MinecraftLinkerBukkit extends JavaPlugin implements IMinecraftLinker<CommandSender> {

    private static MinecraftLinkerBukkit instance;

    public static MinecraftLinkerBukkit getInstance() {
        return instance;
    }

    // start of handler part
    private MinecraftLinkerHandler handler;

    @Override
    public MinecraftLinkerHandler getHandler() {
        return this.handler;
    }

    @Override
    public boolean setHandler(CommandSender sender) {
        InetSocketAddress addr;
        try {
            addr = new InetSocketAddress(getConfiguration().getString(MinecraftLinkerVars.Configuration.server.hostname), getConfiguration().getInt(MinecraftLinkerVars.Configuration.server.port));
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
            this.handler = new MinecraftLinkerHandler(
                getLogger(),
                addr,
                getConfiguration().getBoolean(MinecraftLinkerVars.Configuration.server.ssl.enabled),
                getConfiguration().getString(MinecraftLinkerVars.Configuration.server.user),
                getConfiguration().getString(MinecraftLinkerVars.Configuration.server.password)
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
        instance.getServer().getPluginManager().callEvent(new MinecraftLinkerEvent());
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
        if (getConfiguration().getBoolean(MinecraftLinkerVars.Configuration.server.ssl.enabled)) {
            if (
                getInstance().handler.start(
                    getConfiguration().getString(MinecraftLinkerVars.Configuration.server.whitelistPath),
                    getConfiguration().getString(MinecraftLinkerVars.Configuration.server.blacklistPath),
                    getConfiguration().getInt(MinecraftLinkerVars.Configuration.server.floodgate.maxTries),
                    getConfiguration().getLong(MinecraftLinkerVars.Configuration.server.floodgate.resetTime),
                    getConfiguration().getString(MinecraftLinkerVars.Configuration.server.ssl.keyStorePath),
                    getConfiguration().getString(MinecraftLinkerVars.Configuration.server.ssl.keyStorePassword),
                    getDataFolder(),
                    getConfiguration().getString(MinecraftLinkerVars.Configuration.server.ssl.keyAlias),
                    getConfiguration().getString(MinecraftLinkerVars.Configuration.server.ssl.keyPassword)
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
            getConfiguration().getString(MinecraftLinkerVars.Configuration.server.whitelistPath),
            getConfiguration().getString(MinecraftLinkerVars.Configuration.server.blacklistPath),
            getConfiguration().getInt(MinecraftLinkerVars.Configuration.server.floodgate.maxTries),
            getConfiguration().getLong(MinecraftLinkerVars.Configuration.server.floodgate.resetTime),
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
        if (getHandler() != null) {
            getHandler().stop();
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

        if (!setHandler(null)) {
            return;
        }

        getServer().getPluginManager().registerEvents(new HandlerListener(), getInstance());
        getCommand("wsclinker").setExecutor(new WSCLinker());

        getServer().getPluginManager().callEvent(new MinecraftLinkerEvent());

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
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.hostname, MinecraftLinkerVars.Configuration.server.defaults.hostname))
            error = true;
        // port
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.port, MinecraftLinkerVars.Configuration.server.defaults.port))
            error = true;
        // user
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.user, MinecraftLinkerVars.Configuration.server.defaults.user))
            error = true;
        // password
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.password, MinecraftLinkerVars.Configuration.server.defaults.password))
            error = true;
        // whitelistPath
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.whitelistPath, MinecraftLinkerVars.Configuration.server.defaults.whitelistPath))
            error = true;
        // blacklistPath
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.blacklistPath, MinecraftLinkerVars.Configuration.server.defaults.blacklistPath))
            error = true;
        // maxTries
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.floodgate.maxTries, MinecraftLinkerVars.Configuration.server.floodgate.defaults.maxTries))
            error = true;
        // resetTime
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.floodgate.resetTime, MinecraftLinkerVars.Configuration.server.floodgate.defaults.resetTime))
            error = true;
        // enabled
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.ssl.enabled, MinecraftLinkerVars.Configuration.server.ssl.defaults.enabled))
            error = true;
        // keyStorePath
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.ssl.keyStorePath, MinecraftLinkerVars.Configuration.server.ssl.defaults.keyStorePath))
            error = true;
        // keyStorePassword
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.ssl.keyStorePassword, MinecraftLinkerVars.Configuration.server.ssl.defaults.keyStorePassword))
            error = true;
        // keyAlias
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.ssl.keyAlias, MinecraftLinkerVars.Configuration.server.ssl.defaults.keyAlias))
            error = true;
        // keyPassword
        if (checkConfiguration(MinecraftLinkerVars.Configuration.server.ssl.keyPassword, MinecraftLinkerVars.Configuration.server.ssl.defaults.keyPassword))
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
        if (getConfiguration().get(path) == null) {
            getLogger().log(Level.WARNING, path + " is not set. Resetting it.");
            getConfiguration().set(path, def);
            return true;
        }
        else {
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
