package de.xxschrandxx.wsc.wscbridge.bukkit.api;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.xxschrandxx.wsc.wscbridge.bukkit.api.command.SenderBukkit;
import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.BridgeCoreAPI;
import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public class MinecraftBridgeBukkitAPI extends BridgeCoreAPI {

    public MinecraftBridgeBukkitAPI(String auth, MinecraftBridgeLogger logger, Boolean debug) {
        super(auth, logger, debug);
    }

    public MinecraftBridgeBukkitAPI(String user, String password, MinecraftBridgeLogger logger, Boolean debug) {
        super(user, password, logger, debug);
    }

    public MinecraftBridgeBukkitAPI(MinecraftBridgeBukkitAPI api, MinecraftBridgeLogger logger) {
        super(api, logger);
    }

    @Override
    public ISender<?> getSender(UUID uuid, IBridgePlugin<?> instance) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return new SenderBukkit(player, instance);
    }

    @Override
    public ISender<?> getSender(String name, IBridgePlugin<?> instance) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return null;
        }
        return new SenderBukkit(player, instance);
    }

    @Override
    public ArrayList<ISender<?>> getOnlineSender(IBridgePlugin<?> instance) {
        ArrayList<ISender<?>> sender = new ArrayList<ISender<?>>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            sender.add(new SenderBukkit(player, instance));
        }
        return sender;
    }
}
