package de.xxschrandxx.wsc.bungee.command;

import de.xxschrandxx.wsc.bungee.MinecraftLinkerBungee;
import de.xxschrandxx.wsc.bungee.api.MinecraftLinkerCommandSender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginDescription;

public class WSCLinker extends Command {

    public WSCLinker() {
        super("wsclinkerbungee");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                reload(sender, args);
                return;
            }
        }
        PluginDescription d = MinecraftLinkerBungee.getInstance().getDescription();
        sender.sendMessage(new TextComponent(d.getName() + " " + d.getVersion() + " by " + d.getAuthor()));
    }

    public void reload(CommandSender sender, String[] args) {
        if (sender instanceof MinecraftLinkerCommandSender) {
            sender.sendMessage(new TextComponent("Not supported."));
        }
        else if (sender.hasPermission("rcon.command.reload")) {
            MinecraftLinkerBungee instance = MinecraftLinkerBungee.getInstance();
            instance.stopHandler(sender);
            if (!instance.reloadConfig()) {
                sender.sendMessage(new TextComponent("Could not reload Plugin. Error while loading 'config.xml'. Read logs for more intormation."));
                return;
            }
            sender.sendMessage(new TextComponent("Reloaded config."));
            if (!instance.setHandler(sender)) {
                sender.sendMessage(new TextComponent("Could not initialate handler. Read logs for more information."));
                return;
            }
            instance.startHandler(sender);
        }
        else {
            sender.sendMessage(new TextComponent("You don't have permission to do that."));
        }
    }
    
}
