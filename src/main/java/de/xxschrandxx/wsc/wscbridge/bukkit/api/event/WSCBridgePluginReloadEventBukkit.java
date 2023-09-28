package de.xxschrandxx.wsc.wscbridge.bukkit.api.event;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public final class WSCBridgePluginReloadEventBukkit extends AbstractWSCPluginReloadEventBukkit {
    public WSCBridgePluginReloadEventBukkit(ISender<?> sender) {
        super(sender);
    }
}
