package de.xxschrandxx.wsc.wscbridge.bungee.api.event;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public final class WSCBridgeConfigReloadEventBungee extends AbstractWSCConfigReloadEventBungee {
    public WSCBridgeConfigReloadEventBungee(ISender<?> sender) {
        super(sender);
    }
}
