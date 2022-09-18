package de.xxschrandxx.wsc.wscbridge.core.api.configuration;

import java.util.List;

public interface IConfiguration<T> {
    public T getConfiguration();
    public Object get(String path);
    public void set(String path, Object value);
    public boolean getBoolean(String path);
    public String getString(String path);
    public int getInt(String path);
    public double getDouble(String path);
    public long getLong(String path);
    public float getFloat(String path);
    public List<String> getStringList(String path);
}
