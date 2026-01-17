package de.xxschrandxx.wsc.wscbridge.core.api;

public interface IBridgeLogger {
    void info(String s);

    void warn(String s);

    void warn(String s, Throwable t);

    void severe(String s);

    void severe(String s, Throwable t);
}
