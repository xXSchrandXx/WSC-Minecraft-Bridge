package de.xxschrandxx.wsc.wscbridge.hytale.api.event;

import com.hypixel.hytale.event.IEvent;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public abstract class AbstractWSCPluginReloadEventHytale implements IEvent<Void> {
    protected final ISender<?> sender;
    public AbstractWSCPluginReloadEventHytale(ISender<?> sender) {
        this.sender = sender;
    }
    public ISender<?> getSender() {
        return this.sender;
    }
}
