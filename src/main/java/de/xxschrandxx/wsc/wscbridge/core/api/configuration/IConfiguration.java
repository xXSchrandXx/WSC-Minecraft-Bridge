package de.xxschrandxx.wsc.wscbridge.core.api.configuration;

public interface IConfiguration<T> {
    public T getConfiguration();
    public Object get(String path);
    public void set(String path, Object value);
    public boolean getBoolean(String path);
    public String getString(String path);
    public int getInt(String path);
    public long getLong(String path);
}
