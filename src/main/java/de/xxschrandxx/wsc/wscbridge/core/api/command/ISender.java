package de.xxschrandxx.wsc.wscbridge.core.api.command;

import java.util.UUID;

public interface ISender<T> {
    public void sendMessage(String message);
    public void sendMessage(String message, String hoverMessage);
    public void sendMessage(String message, String hoverMessage, String copyText);
    public boolean hasPermission(String permission);
    public void send(String path);
    public boolean checkPermission(String path);
    public T getParent();
    public boolean isPlayer();
    public UUID getUniqueId();
    public String getName();
}
