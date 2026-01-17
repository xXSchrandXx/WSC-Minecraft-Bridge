package de.xxschrandxx.wsc.wscbridge.core;

import de.xxschrandxx.wsc.wscbridge.core.api.BridgeCoreAPI;
import de.xxschrandxx.wsc.wscbridge.core.api.IBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.core.api.configuration.IConfiguration;

public interface IBridgePlugin<T extends BridgeCoreAPI> {
    public static IBridgePlugin<?> getInstance() {
        return null;
    }
    public void loadAPI(ISender<?> sender);
    public T getAPI();
    public boolean reloadConfiguration(ISender<?> sender);
    public boolean saveConfiguration();
    public IConfiguration<?> getConfiguration();
    public IBridgeLogger getBridgeLogger();
    public String getInfo();
}
