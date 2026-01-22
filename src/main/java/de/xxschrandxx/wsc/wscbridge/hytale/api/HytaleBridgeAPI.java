package de.xxschrandxx.wsc.wscbridge.hytale.api;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

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
        PlayerRef playerRef = Universe.get().getPlayer(uuid);
        if (playerRef == null) {
            return null;
        }
        return new SenderHytale(getPlayer(playerRef), instance);
    }

    @Override
    public ISender<?> getSender(String name, IBridgePlugin<?> instance) {
        PlayerRef playerRef = Universe.get().getPlayer(name, NameMatching.EXACT);
        if (playerRef == null) {
            return null;
        }
        return new SenderHytale(getPlayer(playerRef), instance);
    }

    public Player getPlayer(PlayerRef playerRef) {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null || !ref.isValid()) {
            throw new IllegalArgumentException();
        };

        Store<EntityStore> store = ref.getStore();

        CompletableFuture<Player> f = new CompletableFuture<Player>();

        store.getExternalData().getWorld().execute(() -> {
            Player playerComponent = (Player) store.getComponent(ref, Player.getComponentType());
            f.complete(playerComponent);
        });

        try {
            return f.get();
        } catch (CancellationException | ExecutionException | InterruptedException e) {
        }
        return null;
    }

    @Override
    public ArrayList<ISender<?>> getOnlineSender(IBridgePlugin<?> instance) {
        ArrayList<ISender<?>> sender = new ArrayList<ISender<?>>();
        for (PlayerRef playerRef : Universe.get().getPlayers()) {
            sender.add(new SenderHytale(getPlayer(playerRef), instance));
        }
        return sender;
    }
}
