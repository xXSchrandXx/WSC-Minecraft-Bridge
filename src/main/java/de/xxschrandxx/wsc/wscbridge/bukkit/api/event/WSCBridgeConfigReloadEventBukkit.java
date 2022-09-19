package de.xxschrandxx.wsc.wscbridge.bukkit.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public class WSCBridgeConfigReloadEventBukkit extends Event {
    protected final ISender<?> sender;
    public WSCBridgeConfigReloadEventBukkit(ISender<?> sender) {
        this.sender = sender;
    }
    public ISender<?> getSender() {
        return this.sender;
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
