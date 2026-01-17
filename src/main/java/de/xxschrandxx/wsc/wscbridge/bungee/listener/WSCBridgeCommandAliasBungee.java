package de.xxschrandxx.wsc.wscbridge.bungee.listener;

import java.util.Arrays;


import de.xxschrandxx.wsc.wscbridge.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.api.command.SenderBungee;
import de.xxschrandxx.wsc.wscbridge.core.BridgeVars;
import de.xxschrandxx.wsc.wscbridge.core.commands.WSCBridge;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class WSCBridgeCommandAliasBungee implements Listener {
    @EventHandler
    public void onCommand(ChatEvent event) {
        MinecraftBridgeBungee instance = MinecraftBridgeBungee.getInstance();
        if (!instance.getConfiguration().getBoolean(BridgeVars.Configuration.cmdAliasEnabled)) {
            return;
        }

        if (!event.isCommand()) {
            return;
        }

        String[] split = event.getMessage().split(" ");
        String command = split[0].replaceFirst("/", "");
        if (!instance.getConfiguration().getStringList(BridgeVars.Configuration.cmdAliases).contains(command.toLowerCase())) {
            return;
        }
        String[] args = {};
        if (split.length > 0) {
            args = Arrays.copyOfRange(split, 1, split.length);
        }
        if (!(event.getSender() instanceof CommandSender)) {
            return;
        }
        SenderBungee sb = new SenderBungee((CommandSender) event.getSender(), instance);
        new WSCBridge(instance).execute(sb, args);
    }
}
