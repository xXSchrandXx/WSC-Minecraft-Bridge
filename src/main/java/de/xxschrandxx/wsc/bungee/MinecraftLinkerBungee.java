package de.xxschrandxx.wsc.bungee;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;

import de.xxschrandxx.wsc.bungee.handler.*;
import de.xxschrandxx.wsc.core.MinecraftLinkerHandler;
import de.xxschrandxx.wsc.core.MinecraftLinkerVars;

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


    public void onEnable() {
        // setting instance
        instance = this;

        if (!reloadConfig()) {
            getLogger().log(Level.SEVERE, "Could not load config.yml, disabeling plugin!");
            onDisable();
            return;
        }
       
        InetSocketAddress addr;
        try {
            addr = new InetSocketAddress(getConfig().getString(MinecraftLinkerVars.Configuration.server.hostname), getConfig().getInt(MinecraftLinkerVars.Configuration.server.port));
        }
        catch (IllegalArgumentException | SecurityException e) {
            getLogger().log(Level.SEVERE, "Could not set address.", e);
            onDisable();
            return;
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
            getLogger().log(Level.SEVERE, "Could not create Webserver!");
            onDisable();
            return;
        }

        // TODO add modules
        getHandler().addHandler("/version/", new VersionHandler());
        getHandler().addPasswordHandler("/command/", new CommandHandler());

        // TODO wait until every plugin has started.
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
                getLogger().log(Level.INFO, "WebServer started with ssl.");
                return;
            }
            getLogger().log(Level.WARNING, "WebServer could not start with ssl. Starting without it.");
        }
        getInstance().handler.startHttp();
        getLogger().log(Level.INFO, "WebServer started.");
    }

    public void onDisable() {
        if (getHandler() != null) {
            getHandler().stop();
            getLogger().log(Level.INFO, "WebServer stopped.");
        }
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
