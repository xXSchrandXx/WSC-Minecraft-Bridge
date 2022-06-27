package de.xxschrandxx.wsc.core.permission;

public enum PermissionPlugin {
    /**
     * LuckPerms
     * @see <a href="https://luckperms.net/">LuckPerms</a>
     */
    LuckPerms("LuckPerms"),

    PermissionPlugin(String name) {
        this.pluginName = name;
    }
    private final String pluginName;
    public String getName() {
        return this.pluginName;
    }
}
