package de.xxschrandxx.wsc.wscbridge.bungee.api;

import java.util.List;

import de.xxschrandxx.wsc.wscbridge.core.api.configuration.IConfiguration;
import net.md_5.bungee.config.Configuration;

public class ConfigurationBungee implements IConfiguration<Configuration> {
    protected final Configuration configuration;
    public ConfigurationBungee() {
        this.configuration = new Configuration();
    }
    public ConfigurationBungee(Configuration configuration) {
        this.configuration = configuration;
    }
    public Configuration getConfiguration() {
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
    public double getDouble(String path) {
        return this.configuration.getDouble(path);
    }
    public float getFloat(String path) {
        return this.configuration.getFloat(path);
    }
    public long getLong(String path) {
        return this.configuration.getLong(path);
    }
    public List<String> getStringList(String path) {
        return this.configuration.getStringList(path);
    }
}
