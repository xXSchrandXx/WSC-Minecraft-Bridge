package de.xxschrandxx.wsc.wscbridge.hytale.api.command;

import java.util.UUID;

import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;

public class SenderHytale implements ISender<CommandSender> {

    protected final IBridgePlugin<?> instance;
    protected final CommandSender sender;

    public SenderHytale(CommandSender sender, IBridgePlugin<?> instance) {
        this.sender = sender;
        this.instance = instance;
    }

    public SenderHytale(PlayerRef playerRef, IBridgePlugin<?> instance) {
        Ref<EntityStore> ref = playerRef.getReference();
        if (ref == null || !ref.isValid()) {
            throw new IllegalArgumentException();
        };

        Store<EntityStore> store = ref.getStore();
        Player player = (Player) store.getComponent(ref, Player.getComponentType());
        if (player == null) {
            throw new IllegalArgumentException();
        }

        this.sender = (CommandSender) player;
        this.instance = instance;
    }

    public CommandSender getParent() {
        return this.sender;
    }

    public boolean isPlayer() {
        return getParent() instanceof Player || getParent() instanceof PlayerRef;
    }

    public UUID getUniqueId() {
        CommandSender sender = getParent();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            return player.getUuid();
        } else if (sender instanceof PlayerRef) {
            PlayerRef player = (PlayerRef) sender;
            return player.getUuid();
        }
        return null;
    }

    public String getName() {
        return this.sender.getDisplayName();
    }

    public void sendMessage(String message) {
        this.sendMessage(message, null, null);
    }

    public void sendMessage(String message, String hoverMessage) {
        this.sendMessage(message, hoverMessage, null);
    }

    public void sendMessage(String text, String hoverMessage, String copyText) {
        Message message = Message.raw(text);
        this.sender.sendMessage(message);
    }

    public void send(String path) {
        this.sendMessage(instance.getConfiguration().getString(path));
    }

    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    public boolean checkPermission(String path) {
        return this.hasPermission(instance.getConfiguration().getString(path));
    }
}
