package de.xxschrandxx.wsc.bukkit;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.xxschrandxx.wsc.core.IMinecraftLinker;
import de.xxschrandxx.wsc.core.MinecraftLinkerHandler;

public class MinecraftLinkerBukkit extends JavaPlugin implements IMinecraftLinker<CommandSender> {

    private static MinecraftLinkerBukkit instance;

    public static MinecraftLinkerBukkit getInstance() {
        return instance;
    }

    // start of handler part
    private MinecraftLinkerHandler handler;

    @Override
    public MinecraftLinkerHandler getHandler() {
        return handler;
    }

    @Override
    public boolean setHandler(CommandSender sender) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void startHandler(CommandSender sender) {
        // TODO Auto-generated method stub
    }

    @Override
    public void stopHandler(CommandSender sender) {
        // TODO Auto-generated method stub
    }
    // end of handler part

    // start of plugin part
    @Override
    public void onEnable() {
        // TODO
    }

    @Override
    public void onDisable() {
        // TODO
    }
    // end of plugin part

    // start of config part
    @Override
    public Object getConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean reloadConfiguration() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean saveConfiguration() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean checkConfiguration(String path, Object def) {
        // TODO Auto-generated method stub
        return false;
    }
    // end of config part
}
