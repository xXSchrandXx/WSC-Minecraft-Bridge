package de.xxschrandxx.wsc.wscbridge.bukkit.api.event;

import java.util.ArrayList;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WSCBridgeModuleEventBukkit extends Event {
    protected ArrayList<String> modules = new ArrayList<String>();

    public ArrayList<String> getModules() {
        return this.modules;
    }

    public boolean addModule(String name) {
        return this.modules.add(name);
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
