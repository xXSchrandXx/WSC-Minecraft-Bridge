package de.xxschrandxx.wsc.wscbridge.bukkit.api.event;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public final class WSCBridgeConfigReloadEventBukkit extends AbstractWSCConfigReloadEventBukkit {
    public WSCBridgeConfigReloadEventBukkit(ISender<?> sender) {
        super(sender);
    }
}
