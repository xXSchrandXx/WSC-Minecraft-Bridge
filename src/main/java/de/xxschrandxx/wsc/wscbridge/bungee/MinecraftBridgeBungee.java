package de.xxschrandxx.wsc.wscbridge.bungee;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;

import org.bstats.bungeecord.Metrics;
import org.bstats.charts.SimpleBarChart;

import de.xxschrandxx.wsc.wscbridge.bungee.api.ConfigurationBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.api.MinecraftBridgeBungeeAPI;
import de.xxschrandxx.wsc.wscbridge.bungee.api.command.SenderBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.api.event.*;
import de.xxschrandxx.wsc.wscbridge.bungee.commands.WSCBridgeBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.listener.WSCBridgeCommandAliasBungee;
import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.BridgeVars;
import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class MinecraftBridgeBungee extends Plugin implements IBridgePlugin<MinecraftBridgeBungeeAPI> {

    // start of api part
    public String getInfo() {
        String rawMessage = getConfiguration().getString(BridgeVars.Configuration.LangCmdInfoInfo);
        String message = rawMessage
            .replaceAll("%server%", instance.getProxy().getName())
            .replaceAll("%serverversion%", instance.getProxy().getVersion())
            .replaceAll("%pluginversion%", instance.getDescription().getVersion());
        return message;
    }

    private static MinecraftBridgeBungee instance;

    public static MinecraftBridgeBungee getInstance() {
        return instance;
    }

    private MinecraftBridgeBungeeAPI api;

    private MinecraftBridgeLogger bridgeLogger;

    @Override
    public MinecraftBridgeLogger getBridgeLogger() {
        return bridgeLogger;
    }

    public void loadAPI(ISender<?> sender) {
        api = new MinecraftBridgeBungeeAPI(
            getConfiguration().getString(BridgeVars.Configuration.User),
            getConfiguration().getString(BridgeVars.Configuration.Password),
            getBridgeLogger(),
            getConfiguration().getBoolean(BridgeVars.Configuration.Debug)
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
        bridgeLogger = new MinecraftBridgeLogger(getLogger());

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

        // Load listener
        getLogger().log(Level.INFO, "Loading Listener.");
        getProxy().getPluginManager().registerListener(getInstance(), new WSCBridgeCommandAliasBungee());

        // Load bStats
        getProxy().getScheduler().runAsync(getInstance(), new Runnable() {
            @Override
            public void run() {
                Metrics metrics = new Metrics(getInstance(), 14659);
                WSCBridgeModuleEventBungee event = new WSCBridgeModuleEventBungee();
                getProxy().getPluginManager().callEvent(event);
                metrics.addCustomChart(new SimpleBarChart("Module", new Callable<Map<String, Integer>>() {
                    @Override
                    public Map<String, Integer> call() throws Exception {
                        Map<String, Integer> map = new HashMap<String, Integer>();
                        for (String module : event.getModules()) {
                            map.put(module, 1);
                        }
                        return map;
                    }
                }));
            }
        });
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

        if (BridgeVars.startConfig(getConfiguration(), getBridgeLogger())) {
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
