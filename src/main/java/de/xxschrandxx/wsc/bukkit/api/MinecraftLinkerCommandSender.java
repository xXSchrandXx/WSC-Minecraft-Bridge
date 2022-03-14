package de.xxschrandxx.wsc.bukkit.api;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class MinecraftLinkerCommandSender implements CommandSender {

    private InetAddress ip;

    public InetAddress getIP() {
        return this.ip;
    }

    public void setIP(InetAddress ip) {
        this.ip = ip;
    }

    public String getName() {
        return "MinecraftLinker";
    }

    public boolean hasPermission(String permission) {
        return true;
    }

    protected final StringBuffer buffer = new StringBuffer();

    public String flush() {
        String result = buffer.toString();
        buffer.setLength(0);
        return result;
    }

    public void sendMessage(String message) {
        buffer.append(message).append("\n");
    }

    public boolean dispatchCommand(String commandline) {
        return getServer().getPluginManager().dispatchCommand(this, commandline);
    }

    @Override
    public boolean sendMessages(String... messages) {
        for (String line : messages) {
            sendMessage(line);
        }
    }

    @Override
    public void sendMessage(BaseComponent... message) {
        for (BaseComponent line : message) {
            sendMessage(line);
        }
    }

    @Override
    public void sendMessage(BaseComponent message) {
        sendMessage(message.toLegacyText());
    }

    @Override
    public Collection<String> getGroups() {
        return new ArrayList<>();
    }

    @Override
    public void addGroups(String... groups) {
    }

    @Override
    public void removeGroups(String... groups) {
    }

    @Override
    public void setPermission(String permission, boolean value) {
    }

    @Override
    public Collection<String> getPermissions() {
        return new ArrayList<>();
    }

    @Override
    public PermissionAttachment addAttachment(Plugin arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin arg0, String arg1, boolean arg2, int arg3) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasPermission(Permission arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPermissionSet(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void recalculatePermissions() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeAttachment(PermissionAttachment arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isOp() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setOp(boolean arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Server getServer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendMessage(String[] arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendMessage(UUID arg0, String arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendMessage(UUID arg0, String[] arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Spigot spigot() {
        // TODO Auto-generated method stub
        return null;
    }    
}
