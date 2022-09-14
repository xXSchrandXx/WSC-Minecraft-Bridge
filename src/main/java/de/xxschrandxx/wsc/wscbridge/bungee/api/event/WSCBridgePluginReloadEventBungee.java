package de.xxschrandxx.wsc.wscbridge.bungee.api.event;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import net.md_5.bungee.api.plugin.Event;

public class WSCBridgePluginReloadEventBungee extends Event {
    protected final ISender<?> sender;
    public WSCBridgePluginReloadEventBungee(ISender<?> sender) {
        this.sender = sender;
    }
    public ISender<?> getSender() {
        return this.sender;
    }
}
