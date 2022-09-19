package de.xxschrandxx.wsc.wscbridge.bungee.api.command;

import java.util.UUID;

import de.xxschrandxx.wsc.wscbridge.core.IMinecraftBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SenderBungee implements ISender<CommandSender> {

    protected final IMinecraftBridgePlugin<?> instance;
    protected final CommandSender sender;

    public SenderBungee(CommandSender sender, IMinecraftBridgePlugin<?> instance) {
        this.sender = sender;
        this.instance = instance;
    }

    public CommandSender getParent() {
        return this.sender;
    }

    public boolean isPlayer() {
        return getParent() instanceof ProxiedPlayer;
    }

    public UUID getUniqueId() {
        if (isPlayer()) {
            ProxiedPlayer player = (ProxiedPlayer) getParent();
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
        TextComponent text = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        if (hoverMessage != null) {
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverMessage)));
        }
        if (copyText != null) {
            if (copyText.startsWith("http") || copyText.startsWith("https")) {
                text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, copyText));
            }
            else {
                text.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyText));
            }
        }
        this.sender.sendMessage(text);
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
