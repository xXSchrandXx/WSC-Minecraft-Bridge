package de.xxschrandxx.wsc.wscbridge.hytale.api.event;

import java.util.ArrayList;

import com.hypixel.hytale.event.IEvent;

public class WSCBridgeModuleEventHytale implements IEvent<Void> {
    protected ArrayList<String> modules = new ArrayList<String>();

    public ArrayList<String> getModules() {
        return this.modules;
    }

    public boolean addModule(String name) {
        return this.modules.add(name);
    }
}
