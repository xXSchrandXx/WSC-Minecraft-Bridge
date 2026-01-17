package de.xxschrandxx.wsc.wscbridge.core.commands;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.BridgeVars;
import de.xxschrandxx.wsc.wscbridge.core.api.BridgeCoreAPI;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public class WSCBridge {
    private IBridgePlugin<? extends BridgeCoreAPI> instance;
    public WSCBridge(IBridgePlugin<? extends BridgeCoreAPI> instance) {
        this.instance = instance;
    }
    public void execute(ISender<?> sender, String[] args) {
        if (!sender.checkPermission(BridgeVars.Configuration.PermCmdWSCBridge)) {
            sender.send(BridgeVars.Configuration.LangCmdNoPerm);
            return;
        }
        if (args.length == 0) {
            sender.send(BridgeVars.Configuration.LangCmdReloadUsage);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "reload":
                reload(sender);
                break;
            case "info":
                String info = instance.getInfo();
                sender.sendMessage(info, instance.getConfiguration().getString(BridgeVars.Configuration.LangCmdInfoCopy), info);
                break;
            default:
                sender.send(BridgeVars.Configuration.LangCmdReloadUsage);
                break;
        }
    }

    public void reload(ISender<?> sender) {
        sender.send(BridgeVars.Configuration.LangCmdReloadConfigStart);
        if (!this.instance.reloadConfiguration(sender)) {
            sender.send(BridgeVars.Configuration.LangCmdReloadConfigError);
            return;
        }
        sender.send(BridgeVars.Configuration.LangCmdReloadConfigSuccess);

        sender.send(BridgeVars.Configuration.LangCmdReloadAPIStart);
        this.instance.loadAPI(sender);
        sender.send(BridgeVars.Configuration.LangCmdReloadAPISuccess);
    }
}
