package de.xxschrandxx.wsc.wscbridge.core.commands;

import de.xxschrandxx.wsc.wscbridge.core.IMinecraftBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.MinecraftBridgeVars;
import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeCoreAPI;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public class WSCBridge {
    private IMinecraftBridgePlugin<? extends MinecraftBridgeCoreAPI> instance;
    public WSCBridge(IMinecraftBridgePlugin<? extends MinecraftBridgeCoreAPI> instance) {
        this.instance = instance;
    }
    public void execute(ISender<?> sender, String[] args) {
        if (!sender.checkPermission(MinecraftBridgeVars.Configuration.PermCmdWSCBridge)) {
            sender.send(MinecraftBridgeVars.Configuration.LangCmdNoPerm);
            return;
        }
        if (args.length == 0) {
            sender.send(MinecraftBridgeVars.Configuration.LangCmdReloadUsage);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "reload":
                reload(sender);
                break;
            default:
                sender.send(MinecraftBridgeVars.Configuration.LangCmdReloadUsage);
                break;
        }
    }

    public void reload(ISender<?> sender) {
        sender.send(MinecraftBridgeVars.Configuration.LangCmdReloadConfigStart);
        if (!this.instance.reloadConfiguration(sender)) {
            sender.send(MinecraftBridgeVars.Configuration.LangCmdReloadConfigError);
            return;
        }
        sender.send(MinecraftBridgeVars.Configuration.LangCmdReloadConfigSuccess);

        sender.send(MinecraftBridgeVars.Configuration.LangCmdReloadAPIStart);
        this.instance.loadAPI(sender);
        sender.send(MinecraftBridgeVars.Configuration.LangCmdReloadAPISuccess);
    }
}
