package de.xxschrandxx.wsc.wscbridge.bukkit.api.command;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.xxschrandxx.wsc.wscbridge.core.IMinecraftBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public class SenderBukkit implements ISender<CommandSender> {

    protected final IMinecraftBridgePlugin<?> instance;
    protected final CommandSender sender;
    public SenderBukkit(CommandSender sender, IMinecraftBridgePlugin<?> instance) {
        this.sender = sender;
        this.instance = instance;
    }

    public CommandSender getParent() {
        return this.sender;
    }

    public boolean isPlayer() {
        return getParent() instanceof Player;
    }

    public UUID getUniqueId() {
        if (isPlayer()) {
            Player player = (Player) getParent();
            return player.getUniqueId();
        }
        else {
            return null;
        }
    }

    public String getName() {
        return this.sender.getName();
    }

    public void sendMessage(String message) {
        this.sendMessage(message, null, null);
    }

    public void sendMessage(String message, String hoverMessage) {
        this.sendMessage(message, hoverMessage, null);
    }

    public void sendMessage(String message, String hoverMessage, String copyText) {
        try {
            new SenderSpigot(this.sender, this.instance).sendMessage(message, hoverMessage, copyText);
        }
        catch (Exception e) {
            this.sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
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
