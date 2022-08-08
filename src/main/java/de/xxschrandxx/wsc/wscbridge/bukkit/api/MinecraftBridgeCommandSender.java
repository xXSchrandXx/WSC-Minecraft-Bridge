package de.xxschrandxx.wsc.wscbridge.bukkit.api;

import java.net.InetAddress;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.bukkit.Server;
import org.bukkit.command.CommandException;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import de.xxschrandxx.wsc.wscbridge.bukkit.MinecraftBridgeBukkit;

public class MinecraftBridgeCommandSender implements RemoteConsoleCommandSender {

    private InetAddress ip;

    public InetAddress getIP() {
        return this.ip;
    }

    public void setIP(InetAddress ip) {
        this.ip = ip;
    }

    @Override
    public String getName() {
        return "MinecraftBridge";
    }

    protected final StringBuffer buffer = new StringBuffer();

    public String flush() {
        String result = buffer.toString();
        buffer.setLength(0);
        return result;
    }

    @Override
    public void sendMessage(String message) {
        buffer.append(message).append("\n");
    }


    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public void sendMessage(UUID sender, String message) {
        sendMessage(sender.toString() + ": " + message);
    }

    @Override
    public void sendMessage(UUID sender, String[] messages) {
        for (String message : messages) {
            sendMessage(sender, message);
        }
    }

    @Override
    public Server getServer() {
        return MinecraftBridgeBukkit.getInstance().getServer();
    }

    public boolean dispatchCommand(String commandLine) {
        CompletableFuture<Boolean> future = new CompletableFuture<Boolean>();
        MinecraftBridgeCommandSender instance = this;
        getServer().getScheduler().runTask(MinecraftBridgeBukkit.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    getServer().dispatchCommand(instance, commandLine);
                    future.complete(true);
                }
                catch (CommandException e) {
                    future.complete(false);
                }
            }
            
        });
        try {
            return future.get();
        }
        catch (CancellationException | ExecutionException | InterruptedException e) {
            return false;
        }
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void recalculatePermissions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("OP status cannot be changed for Rcon.");
    }

    @Override
    public boolean isPermissionSet(String name) {
        return true;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return true;
    }

    @Override
    public boolean hasPermission(String perm) {
        return true;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return true;
    }

    @Override
    public Spigot spigot() {
        return new MinecraftBridgeCommandSenderSpigot(this);
    }
}
