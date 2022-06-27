package de.xxschrandxx.wsc.core.permission;

public enum PermissionPlugin {
    /**
     * LuckPerms
     * @see <a href="https://luckperms.net/">LuckPerms</a>
     */
    LuckPerms("LuckPerms"),
    /**
     * CloudNet
     * @see <a href="https://cloudnetservice.eu/">CloudNetService</a>
     */
    CloudNet("CloudNet-CloudPerms")
    ;

    PermissionPlugin(String name) {
        this.pluginName = name;
    }
    private final String pluginName;
    public String getName() {
        return this.pluginName;
    }
}
