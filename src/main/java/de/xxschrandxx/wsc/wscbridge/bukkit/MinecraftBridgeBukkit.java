package de.xxschrandxx.wsc.wscbridge.bukkit;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import de.xxschrandxx.wsc.wscbridge.bukkit.api.ConfigurationBukkit;
import de.xxschrandxx.wsc.wscbridge.bukkit.api.MinecraftBridgeBukkitAPI;
import de.xxschrandxx.wsc.wscbridge.bukkit.api.command.SenderBukkit;
import de.xxschrandxx.wsc.wscbridge.bukkit.api.event.*;
import de.xxschrandxx.wsc.wscbridge.bukkit.commands.WSCBridgeBukkit;
import de.xxschrandxx.wsc.wscbridge.core.IMinecraftBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.MinecraftBridgeVars;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public class MinecraftBridgeBukkit extends JavaPlugin implements IMinecraftBridgePlugin<MinecraftBridgeBukkitAPI> {

    // start of api part
    private static MinecraftBridgeBukkit instance;

    public static MinecraftBridgeBukkit getInstance() {
        return instance;
    }

    private MinecraftBridgeBukkitAPI api;

    public void loadAPI(ISender<?> sender) {
        this.api = new MinecraftBridgeBukkitAPI(
            getConfiguration().getInt(MinecraftBridgeVars.Configuration.ID),
            getConfiguration().getString(MinecraftBridgeVars.Configuration.User),
            getConfiguration().getString(MinecraftBridgeVars.Configuration.Password),
            getLogger(),
            getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.Debug)
        );
        getServer().getPluginManager().callEvent(new WSCBridgePluginReloadEventBukkit(sender));
    }
    public MinecraftBridgeBukkitAPI getAPI() {
        return this.api;
    }
    // end of api part

    // start of plugin part
    @Override
    public void onEnable() {
        instance = this;

        // Load configuration
        getLogger().log(Level.INFO, "Loading Configuration.");
        SenderBukkit sender = new SenderBukkit(getServer().getConsoleSender(), getInstance());
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
        getCommand("wscbridge").setExecutor(new WSCBridgeBukkit());
    }

    @Override
    public void onDisable() {
    }
    // end of plugin part

    // start config part
    public ConfigurationBukkit getConfiguration() {
        return new ConfigurationBukkit(getInstance().getConfig());
    }

    public boolean reloadConfiguration(ISender<?> sender) {
        reloadConfig();

        if (MinecraftBridgeVars.startConfig(getConfiguration(), getLogger())) {
            if (!saveConfiguration()) {
                return false;
            }
            return reloadConfiguration(sender);
        }
        getServer().getPluginManager().callEvent(new WSCBridgeConfigReloadEventBukkit(sender));
        return true;
    }

    public boolean saveConfiguration() {
        saveConfig();
        return true;
    }
    // end config part
}
