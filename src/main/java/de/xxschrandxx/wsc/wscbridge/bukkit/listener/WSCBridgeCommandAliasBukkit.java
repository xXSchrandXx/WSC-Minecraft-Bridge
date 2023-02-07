package de.xxschrandxx.wsc.wscbridge.bukkit.listener;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import de.xxschrandxx.wsc.wscbridge.bukkit.MinecraftBridgeBukkit;
import de.xxschrandxx.wsc.wscbridge.bukkit.api.command.SenderBukkit;
import de.xxschrandxx.wsc.wscbridge.core.MinecraftBridgeVars;
import de.xxschrandxx.wsc.wscbridge.core.commands.WSCBridge;

public class WSCBridgeCommandAliasBukkit implements Listener {
    @EventHandler
    public void onCommand(ServerCommandEvent event) {
        if (event.isCancelled()) {
            return;
        }
        MinecraftBridgeBukkit instance = MinecraftBridgeBukkit.getInstance();
        if (!instance.getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.cmdAliasEnabled)) {
            return;
        }

        String[] split = event.getCommand().split(" ");
        String command = split[0];
        if (!instance.getConfiguration().getStringList(MinecraftBridgeVars.Configuration.cmdAliases).contains(command.toLowerCase())) {
            return;
        }
        String[] args = {};
        if (split.length > 0) {
            args = Arrays.copyOfRange(split, 1, split.length);
        }

        SenderBukkit sb = new SenderBukkit(event.getSender(), instance);
        new WSCBridge(instance).execute(sb, args);
        event.setCancelled(true);
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        MinecraftBridgeBukkit instance = MinecraftBridgeBukkit.getInstance();
        if (!instance.getConfiguration().getBoolean(MinecraftBridgeVars.Configuration.cmdAliasEnabled)) {
            return;
        }

        String[] split = event.getMessage().split(" ");
        String command = split[0].replaceFirst("/", "");
        if (!instance.getConfiguration().getStringList(MinecraftBridgeVars.Configuration.cmdAliases).contains(command.toLowerCase())) {
            return;
        }
        String[] args = {};
        if (split.length > 0) {
            args = Arrays.copyOfRange(split, 1, split.length);
        }

        SenderBukkit sb = new SenderBukkit(event.getPlayer(), instance);
        new WSCBridge(instance).execute(sb, args);
        event.setCancelled(true);
    }
}
