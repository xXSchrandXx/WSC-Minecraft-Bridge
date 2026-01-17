package de.xxschrandxx.wsc.wscbridge.bungee.api;

import java.util.ArrayList;
import java.util.UUID;

import de.xxschrandxx.wsc.wscbridge.bungee.api.command.SenderBungee;
import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.BridgeCoreAPI;
import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MinecraftBridgeBungeeAPI extends BridgeCoreAPI {

    public MinecraftBridgeBungeeAPI(String auth, MinecraftBridgeLogger logger, Boolean debug) {
        super(auth, logger, debug);
    }

    public MinecraftBridgeBungeeAPI(String user, String password, MinecraftBridgeLogger logger, Boolean debug) {
        super(user, password, logger, debug);
    }

    public MinecraftBridgeBungeeAPI(MinecraftBridgeBungeeAPI api, MinecraftBridgeLogger logger) {
        super(api, logger);
    }

    @Override
    public ISender<?> getSender(UUID uuid, IBridgePlugin<?> instance) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return new SenderBungee(player, instance);
    }

    @Override
    public ISender<?> getSender(String name, IBridgePlugin<?> instance) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
        if (player == null) {
            return null;
        }
        return new SenderBungee(player, instance);
    }

    @Override
    public ArrayList<ISender<?>> getOnlineSender(IBridgePlugin<?> instance) {
        ArrayList<ISender<?>> sender = new ArrayList<ISender<?>>();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            sender.add(new SenderBungee(player, instance));
        }
        return sender;
    }
}
