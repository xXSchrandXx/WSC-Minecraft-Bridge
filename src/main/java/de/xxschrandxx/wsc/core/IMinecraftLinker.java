package de.xxschrandxx.wsc.core;

// TODO
public interface IMinecraftLinker<S> {
    public static IMinecraftLinker<?> getInstance() {
        return null;
    };

    public MinecraftLinkerHandler getHandler();

    public boolean setHandler(S sender);

    public void startHandler(S sender);

    public void stopHandler(S sender);

    public Object getConfiguration();

    public boolean reloadConfiguration();

    public boolean checkConfiguration(String path, Object def);

    public boolean saveConfiguration();
}
