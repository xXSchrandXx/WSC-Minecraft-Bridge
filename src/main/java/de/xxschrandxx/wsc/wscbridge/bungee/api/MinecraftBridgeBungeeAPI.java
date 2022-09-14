package de.xxschrandxx.wsc.wscbridge.bungee.api;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import de.xxschrandxx.wsc.wscbridge.bungee.api.command.SenderBungee;
import de.xxschrandxx.wsc.wscbridge.core.IMinecraftBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeCoreAPI;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MinecraftBridgeBungeeAPI extends MinecraftBridgeCoreAPI {

    public MinecraftBridgeBungeeAPI(Integer id, String auth, Logger logger, Boolean debug) {
        super(id, auth, logger, debug);
    }

    public MinecraftBridgeBungeeAPI(Integer id, String user, String password, Logger logger, Boolean debug) {
        super(id, user, password, logger, debug);
    }

    @Override
    public ISender<?> getSender(UUID uuid, IMinecraftBridgePlugin<?> instance) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return new SenderBungee(player, instance);
    }

    @Override
    public ISender<?> getSender(String name, IMinecraftBridgePlugin<?> instance) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);
        if (player == null) {
            return null;
        }
        return new SenderBungee(player, instance);
    }

    @Override
    public ArrayList<ISender<?>> getOnlineSender(IMinecraftBridgePlugin<?> instance) {
        ArrayList<ISender<?>> sender = new ArrayList<ISender<?>>();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            sender.add(new SenderBungee(player, instance));
        }
        return sender;
    }
}
