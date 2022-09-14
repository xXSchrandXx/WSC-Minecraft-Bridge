package de.xxschrandxx.wsc.wscbridge.bukkit.api;

import org.bukkit.configuration.file.FileConfiguration;

import de.xxschrandxx.wsc.wscbridge.core.api.configuration.IConfiguration;

public class ConfigurationBukkit implements IConfiguration<FileConfiguration> {
    protected final FileConfiguration configuration;
    public ConfigurationBukkit(FileConfiguration configuration) {
        this.configuration = configuration;
    }
    public FileConfiguration getConfiguration() {
        return this.configuration;
    }
    public Object get(String path) {
        return this.configuration.get(path);
    }
    public void set(String path, Object value) {
        this.configuration.set(path, value);
    }
    public boolean getBoolean(String path) {
        return this.configuration.getBoolean(path);
    }
    public String getString(String path) {
        return this.configuration.getString(path);
    }
    public int getInt(String path) {
        return this.configuration.getInt(path);
    }
    public long getLong(String path) {
        return this.configuration.getLong(path);
    }
}
