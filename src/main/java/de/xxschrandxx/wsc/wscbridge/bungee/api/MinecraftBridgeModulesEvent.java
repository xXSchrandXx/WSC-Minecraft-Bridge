package de.xxschrandxx.wsc.wscbridge.bungee.api;

import java.util.ArrayList;

import net.md_5.bungee.api.plugin.Event;

public class MinecraftBridgeModulesEvent extends Event {
    private ArrayList<String> modules = new ArrayList<String>();

    public boolean addModule(String module) {
        return this.modules.add(module);
    }

    public ArrayList<String> getModules() {
        return this.modules;
    }
}
