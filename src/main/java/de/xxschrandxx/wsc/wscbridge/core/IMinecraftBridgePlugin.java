package de.xxschrandxx.wsc.wscbridge.core;

import java.util.logging.Logger;

import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeCoreAPI;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.core.api.configuration.IConfiguration;

public interface IMinecraftBridgePlugin<T extends MinecraftBridgeCoreAPI> {
    public static IMinecraftBridgePlugin<?> getInstance() {
        return null;
    }
    public void loadAPI(ISender<?> sender);
    public T getAPI();
    public boolean reloadConfiguration(ISender<?> sender);
    public boolean saveConfiguration();
    public IConfiguration<?> getConfiguration();
    public Logger getLogger();
}
