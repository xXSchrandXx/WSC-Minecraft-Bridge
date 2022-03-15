package de.xxschrandxx.wsc.core;

// TODO
public interface IMinecraftBridge<S> {
    public static IMinecraftBridge<?> getInstance() {
        return null;
    };

    public MinecraftBridgeHandler getHandler();

    public boolean setHandler(S sender);

    public void startHandler(S sender);

    public void stopHandler(S sender);

    public Object getConfiguration();

    public boolean reloadConfiguration();

    public boolean checkConfiguration(String path, Object def);

    public boolean saveConfiguration();
}
