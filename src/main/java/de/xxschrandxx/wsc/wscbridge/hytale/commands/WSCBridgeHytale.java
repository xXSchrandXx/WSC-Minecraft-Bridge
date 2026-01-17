package de.xxschrandxx.wsc.wscbridge.hytale.commands;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;

import de.xxschrandxx.wsc.wscbridge.core.commands.WSCBridge;
import de.xxschrandxx.wsc.wscbridge.hytale.HytaleBridge;
import de.xxschrandxx.wsc.wscbridge.hytale.api.command.SenderHytale;

public class WSCBridgeHytale extends AbstractCommand {

    public WSCBridgeHytale(List<String> aliases) {
        super("wscbridge", "WSCBridge command");
        if (aliases != null) {
            for (String alias : aliases) {
                addAliases(alias);
            }
        }
        setAllowsExtraArguments(true);
    }

    @Override
    @Nullable
    protected CompletableFuture<Void> execute(@Nonnull CommandContext ctx) {
        HytaleBridge instance = HytaleBridge.getInstance();
        SenderHytale s = new SenderHytale(ctx.sender(), instance);
        String[] args = ctx.getInputString().split(" ");
        String[] argsWithoutFirst = Arrays.copyOfRange(args, 1, args.length);
        new WSCBridge(instance).execute(s, argsWithoutFirst);
        return CompletableFuture.completedFuture(null);
    }
}
