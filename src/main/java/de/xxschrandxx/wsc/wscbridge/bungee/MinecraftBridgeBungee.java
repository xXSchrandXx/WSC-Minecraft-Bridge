package de.xxschrandxx.wsc.wscbridge.bungee;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import de.xxschrandxx.wsc.wscbridge.bungee.api.ConfigurationBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.api.MinecraftBridgeBungeeAPI;
import de.xxschrandxx.wsc.wscbridge.bungee.api.command.SenderBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.api.event.*;
import de.xxschrandxx.wsc.wscbridge.bungee.commands.WSCBridgeBungee;
import de.xxschrandxx.wsc.wscbridge.core.IMinecraftBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.MinecraftBridgeVars;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MinecraftBridgeBungee extends Plugin implements IMinecraftBridgePlugin<MinecraftBridgeBungeeAPI> {

    // start of api part
    private static MinecraftBridgeBungee instance;

    public static MinecraftBridgeBungee getInstance() {
        return instance;
    }

    private MinecraftBridgeBungeeAPI api;

    public void loadAPI(ISender<?> sender) {
        api = new MinecraftBridgeBungeeAPI(
            getConfiguration().getInt(MinecraftBridgeVars.Configuration.ID),
            getConfiguration().getString(MinecraftBridgeVars.Configuration.User),
            getConfiguration().getString(MinecraftBridgeVars.Configuration.Password),
            getLogger(),
            getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.Debug)
        );
        getProxy().getPluginManager().callEvent(new WSCBridgePluginReloadEventBungee(sender));
    }

    public MinecraftBridgeBungeeAPI getAPI() {
        return this.api;
    }
    // end of api part

    // start of plugin part
    @Override
    public void onEnable() {
        instance = this;

        // Load configuration
        getLogger().log(Level.INFO, "Loading Configuration.");
        SenderBungee sender = new SenderBungee(getProxy().getConsole(), getInstance());
        if (!reloadConfiguration(sender)) {
            getLogger().log(Level.WARNING, "Could not load config.yml, disabeling plugin!");
            onDisable();
            return;
        }

        // Load API
        getLogger().log(Level.INFO, "Loading API.");
        loadAPI(sender);

        // Load commands
        getLogger().log(Level.INFO, "Loading Commands.");
        getProxy().getPluginManager().registerCommand(getInstance(), new WSCBridgeBungee("wscbridge"));
    }

    @Override
    public void onDisable() {
    }
    // end of plugin part

    // start config part
    private File configFile = new File(getDataFolder(), "config.yml");
    private ConfigurationBungee config;

    public ConfigurationBungee getConfiguration() {
        return getInstance().config;
    }

    public boolean reloadConfiguration(ISender<?> sender) {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if (configFile.exists()) {
            try {
                config = new ConfigurationBungee(ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile));
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
            config = new ConfigurationBungee();
        }

        if (MinecraftBridgeVars.startConfig(getConfiguration(), getLogger())) {
            if (!saveConfiguration()) {
                return false;
            }
            return reloadConfiguration(sender);
        }
        getProxy().getPluginManager().callEvent(new WSCBridgeConfigReloadEventBungee(sender));
        return true;
    }

    public boolean saveConfiguration() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config.getConfiguration(), configFile);
        }
        catch (IOException e) {
            getLogger().log(Level.WARNING, "Could not save config.yml.", e);
            return false;
        }
        return true;
    }
    // end config part
}
