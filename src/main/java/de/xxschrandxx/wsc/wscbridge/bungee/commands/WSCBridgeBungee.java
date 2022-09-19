package de.xxschrandxx.wsc.wscbridge.bungee.commands;

import de.xxschrandxx.wsc.wscbridge.bungee.MinecraftBridgeBungee;
import de.xxschrandxx.wsc.wscbridge.bungee.api.command.SenderBungee;
import de.xxschrandxx.wsc.wscbridge.core.commands.WSCBridge;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class WSCBridgeBungee extends Command {

    public WSCBridgeBungee(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MinecraftBridgeBungee instance = MinecraftBridgeBungee.getInstance();
        SenderBungee sb = new SenderBungee(sender, instance);
        new WSCBridge(instance).execute(sb, args);
    }
    
}
