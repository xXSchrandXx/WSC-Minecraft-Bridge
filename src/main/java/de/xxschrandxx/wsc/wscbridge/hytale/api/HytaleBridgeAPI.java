package de.xxschrandxx.wsc.wscbridge.hytale.api;

import java.util.ArrayList;
import java.util.UUID;

import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.BridgeCoreAPI;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.hytale.api.command.SenderHytale;

public class HytaleBridgeAPI extends BridgeCoreAPI {

    public HytaleBridgeAPI(String auth, HytaleBridgeLogger logger, Boolean debug) {
        super(auth, logger, debug);
    }

    public HytaleBridgeAPI(String user, String password, HytaleBridgeLogger logger, Boolean debug) {
        super(user, password, logger, debug);
    }

    public HytaleBridgeAPI(HytaleBridgeAPI api, HytaleBridgeLogger logger) {
        super(api, logger);
    }

    @Override
    public ISender<?> getSender(UUID uuid, IBridgePlugin<?> instance) {
        PlayerRef player = Universe.get().getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return new SenderHytale(player, instance);
    }

    @Override
    public ISender<?> getSender(String name, IBridgePlugin<?> instance) {
        PlayerRef player = Universe.get().getPlayer(name, NameMatching.EXACT);
        if (player == null) {
            return null;
        }
        return new SenderHytale(player, instance);
    }

    @Override
    public ArrayList<ISender<?>> getOnlineSender(IBridgePlugin<?> instance) {
        ArrayList<ISender<?>> sender = new ArrayList<ISender<?>>();
        for (PlayerRef player : Universe.get().getPlayers()) {
            sender.add(new SenderHytale(player, instance));
        }
        return sender;
    }
}
