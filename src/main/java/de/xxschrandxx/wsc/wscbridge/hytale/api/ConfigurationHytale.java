package de.xxschrandxx.wsc.wscbridge.hytale.api;

import java.util.HashMap;
import java.util.List;

import de.xxschrandxx.wsc.wscbridge.core.api.configuration.IConfiguration;

public class ConfigurationHytale implements IConfiguration<HashMap> {
    protected final HashMap configuration;
    public ConfigurationHytale() {
        this.configuration = new HashMap();
    }
    public ConfigurationHytale(HashMap configuration) {
        this.configuration = configuration;
    }
    public HashMap getConfiguration() {
        return this.configuration;
    }
    public void set(String path, Object value) {
        this.configuration.put(path, value);
    }
    public Object get(String path) {
        return this.configuration.get(path);
    }
    public boolean getBoolean(String path) {
        return ((Boolean) this.get(path)).booleanValue();
    }
    public String getString(String path) {
        return String.valueOf(this.get(path));
    }
    public int getInt(String path) {
        return ((Number) this.get(path)).intValue();
    }
    public double getDouble(String path) {
        return ((Number) this.get(path)).doubleValue();
    }
    public float getFloat(String path) {
        return ((Number) this.get(path)).floatValue();
    }
    public long getLong(String path) {
        return ((Number) this.get(path)).longValue();
    }
    public List<String> getStringList(String path) {
        return (List<String>) this.get(path);
    }
}
