package de.xxschrandxx.wsc.wscbridge.bukkit.api;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.xxschrandxx.wsc.wscbridge.bukkit.api.command.SenderBukkit;
import de.xxschrandxx.wsc.wscbridge.core.IMinecraftBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeCoreAPI;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public class MinecraftBridgeBukkitAPI extends MinecraftBridgeCoreAPI {

    public MinecraftBridgeBukkitAPI(String auth, Logger logger, Boolean debug) {
        super(auth, logger, debug);
    }

    public MinecraftBridgeBukkitAPI(String user, String password, Logger logger, Boolean debug) {
        super(user, password, logger, debug);
    }

    public MinecraftBridgeBukkitAPI(MinecraftBridgeBukkitAPI api, Logger logger) {
        super(api, logger);
    }

    @Override
    public ISender<?> getSender(UUID uuid, IMinecraftBridgePlugin<?> instance) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return new SenderBukkit(player, instance);
    }

    @Override
    public ISender<?> getSender(String name, IMinecraftBridgePlugin<?> instance) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return null;
        }
        return new SenderBukkit(player, instance);
    }

    @Override
    public ArrayList<ISender<?>> getOnlineSender(IMinecraftBridgePlugin<?> instance) {
        ArrayList<ISender<?>> sender = new ArrayList<ISender<?>>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            sender.add(new SenderBukkit(player, instance));
        }
        return sender;
    }
}
