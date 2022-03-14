package de.xxschrandxx.wsc.bukkit.api;

import java.util.UUID;

import org.bukkit.command.CommandSender.Spigot;

import net.md_5.bungee.api.chat.BaseComponent;

public class MinecraftLinkerCommandSenderSpigot extends Spigot {

    private final MinecraftLinkerCommandSender parent;

    public MinecraftLinkerCommandSenderSpigot(MinecraftLinkerCommandSender parent) {
        this.parent = parent;
    }

    @Override
    public void sendMessage(BaseComponent component) {
        this.parent.sendMessage(component.toLegacyText());
    }

    @Override
    public void sendMessage(BaseComponent... components) {
        for (BaseComponent line : components) {
            sendMessage(line);
        }
    }

    @Override
    public void sendMessage(UUID sender, BaseComponent component) {
    }

    @Override
    public void sendMessage(UUID sender, BaseComponent... components) {
    }
}
