package de.xxschrandxx.wsc.bungee.api;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;

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

    public ProxyServer getServer() {
        return ProxyServer.getInstance();
    }

    public boolean dispatchCommand(String commandline) {
        return getServer().getPluginManager().dispatchCommand(this, commandline);
    }

    @Override
    public void sendMessages(String... messages) {
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
}
