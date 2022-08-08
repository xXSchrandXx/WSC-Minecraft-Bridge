package de.xxschrandxx.wsc.wscbridge.core;

// TODO finish interface
public interface IMinecraftBridge<S> {
    public static IMinecraftBridge<?> getInstance() {
        return null;
    };

    public MinecraftBridgeHandler getHandler();

    public boolean setHandler(S sender);

    public void startHandler(S sender);

    public void stopHandler(S sender);

    public void stopHandler(S sender, boolean saveLists);

    public Object getConfiguration();

    public boolean reloadConfiguration();

    public boolean checkConfiguration(String path, Object def);

    public boolean saveConfiguration();
}
