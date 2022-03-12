package de.xxschrandxx.wsc.bungee;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;

import de.xxschrandxx.wsc.bungee.api.MinecraftLinkerEvent;
import de.xxschrandxx.wsc.bungee.command.WSCLinker;
import de.xxschrandxx.wsc.bungee.listener.HandlerListener;
import de.xxschrandxx.wsc.core.MinecraftLinkerHandler;
import de.xxschrandxx.wsc.core.MinecraftLinkerVars;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MinecraftLinkerBungee extends Plugin {

    private static MinecraftLinkerBungee instance;

    public static MinecraftLinkerBungee getInstance() {
        return instance;
    }

    private MinecraftLinkerHandler handler;

    public MinecraftLinkerHandler getHandler() {
        return this.handler;
    }

    public boolean setHandler(CommandSender sender) {
        InetSocketAddress addr;
        try {
            addr = new InetSocketAddress(getConfig().getString(MinecraftLinkerVars.Configuration.server.hostname), getConfig().getInt(MinecraftLinkerVars.Configuration.server.port));
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
            this.handler = new MinecraftLinkerHandler(
                getLogger(),
                addr,
                getConfig().getBoolean(MinecraftLinkerVars.Configuration.server.ssl.enabled),
                getConfig().getString(MinecraftLinkerVars.Configuration.server.user),
                getConfig().getString(MinecraftLinkerVars.Configuration.server.password)
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
        instance.getProxy().getPluginManager().callEvent(new MinecraftLinkerEvent());
        if (sender == null) {
            getLogger().log(Level.INFO, "Called registration event.");
        }
        else {
            sender.sendMessage(new TextComponent("Called registration event."));
        }
    return true;
    }

    public void startHandler(CommandSender sender) {
        if (getConfig().getBoolean(MinecraftLinkerVars.Configuration.server.ssl.enabled)) {
            if (
                getInstance().handler.startHttps(
                    getConfig().getString(MinecraftLinkerVars.Configuration.server.ssl.keyStorePath),
                    getConfig().getString(MinecraftLinkerVars.Configuration.server.ssl.keyStorePassword),
                    getDataFolder(),
                    getConfig().getString(MinecraftLinkerVars.Configuration.server.ssl.keyAlias),
                    getConfig().getString(MinecraftLinkerVars.Configuration.server.ssl.keyPassword)
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
        getInstance().handler.startHttp();
        if (sender == null) {
            getLogger().log(Level.INFO, "WebServer started.");
        }
        else {
            sender.sendMessage(new TextComponent("WebServer started."));
        }
    }

    public void stopHandler(CommandSender sender) {
        if (getHandler() != null) {
            getHandler().stop();
            if (sender == null) {
                getLogger().log(Level.INFO, "WebServer stopped.");
            }
            else {
                sender.sendMessage(new TextComponent("Webserver stopped"));
            }
        }
    }

    public void onEnable() {
        // setting instance
        instance = this;

        if (!reloadConfig()) {
            getLogger().log(Level.SEVERE, "Could not load config.yml, disabeling plugin!");
            onDisable();
            return;
        }

        if (!setHandler(null)) {
            return;
        }

        getProxy().getPluginManager().registerListener(getInstance(), new HandlerListener());
        getProxy().getPluginManager().registerCommand(getInstance(), new WSCLinker());

        // TODO add Event
        getProxy().getPluginManager().callEvent(new MinecraftLinkerEvent());

        // TODO wait until every plugin has started.
        startHandler(null);
    }

    public void onDisable() {
        stopHandler(null);
    }

    // start config part
    private File configFile = new File(getDataFolder(), "config.yml");
    private Configuration config;
    public Configuration getConfig() {
        return getInstance().config;
    }

    public boolean reloadConfig() {
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
        if (checkConfig(MinecraftLinkerVars.Configuration.server.hostname, MinecraftLinkerVars.Configuration.server.defaults.hostname))
            error = true;
        // port
        if (checkConfig(MinecraftLinkerVars.Configuration.server.port, MinecraftLinkerVars.Configuration.server.defaults.port))
            error = true;
        // user
        if (checkConfig(MinecraftLinkerVars.Configuration.server.user, MinecraftLinkerVars.Configuration.server.defaults.user))
            error = true;
        // password
        if (checkConfig(MinecraftLinkerVars.Configuration.server.password, MinecraftLinkerVars.Configuration.server.defaults.password))
            error = true;
        // enabled
        if (checkConfig(MinecraftLinkerVars.Configuration.server.ssl.enabled, MinecraftLinkerVars.Configuration.server.ssl.defaults.enabled))
            error = true;
        // keyStorePath
        if (checkConfig(MinecraftLinkerVars.Configuration.server.ssl.keyStorePath, MinecraftLinkerVars.Configuration.server.ssl.defaults.keyStorePath))
            error = true;
        // keyStorePassword
        if (checkConfig(MinecraftLinkerVars.Configuration.server.ssl.keyStorePassword, MinecraftLinkerVars.Configuration.server.ssl.defaults.keyStorePassword))
            error = true;
        // keyAlias
        if (checkConfig(MinecraftLinkerVars.Configuration.server.ssl.keyAlias, MinecraftLinkerVars.Configuration.server.ssl.defaults.keyAlias))
            error = true;
        // keyPassword
        if (checkConfig(MinecraftLinkerVars.Configuration.server.ssl.keyPassword, MinecraftLinkerVars.Configuration.server.ssl.defaults.keyPassword))
            error = true;
        // maxAttempts
        if (checkConfig(MinecraftLinkerVars.Configuration.server.floodgate.maxAttempts, MinecraftLinkerVars.Configuration.server.floodgate.defaults.maxAttempts))
            error = true;
        // resetTime
        if (checkConfig(MinecraftLinkerVars.Configuration.server.floodgate.resetTime, MinecraftLinkerVars.Configuration.server.floodgate.defaults.resetTime))
            error = true;

        // end config data

        if (error) {
            if (!saveConfig()) {
                return false;
            }
            return reloadConfig();
        }
        return true;
    }

    public boolean checkConfig(String path, Object def) {
        if (getConfig().get(path) == null) {
            getLogger().log(Level.WARNING, path + " is not set. Resetting it.");
            getConfig().set(path, def);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean saveConfig() {
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
