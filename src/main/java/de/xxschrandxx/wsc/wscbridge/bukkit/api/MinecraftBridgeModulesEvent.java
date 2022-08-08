package de.xxschrandxx.wsc.wscbridge.bukkit.api;

import java.util.ArrayList;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MinecraftBridgeModulesEvent extends Event {

    private final ArrayList<String> modules = new ArrayList<String>();

    public boolean addModule(String module) {
        return this.modules.add(module);
    }

    public ArrayList<String> getModules() {
        return this.modules;
    }

    private static final HandlerList HANDLERS = new HandlerList();
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
