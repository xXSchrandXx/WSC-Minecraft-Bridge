package de.xxschrandxx.wsc.wscbridge.bungee.api.event;

import java.util.ArrayList;

import net.md_5.bungee.api.plugin.Event;

public class WSCBridgeModuleEventBungee extends Event {
    protected ArrayList<String> modules = new ArrayList<String>();

    public ArrayList<String> getModules() {
        return this.modules;
    }

    public boolean addModule(String name) {
        return this.modules.add(name);
    }
}
