package de.xxschrandxx.wsc.wscbridge.bukkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.xxschrandxx.wsc.wscbridge.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.wscbridge.bukkit.api.command.SenderBukkit;
import de.xxschrandxx.wsc.wscbridge.core.commands.WSCBridge;

public class WSCBridgeBukkit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MinecraftBridgeBukkit instance = MinecraftBridgeBukkit.getInstance();
        SenderBukkit sb = new SenderBukkit(sender, instance);
        new WSCBridge(instance).execute(sb, args);
        return true;
    }
}
