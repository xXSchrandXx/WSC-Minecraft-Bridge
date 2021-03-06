package de.xxschrandxx.wsc.core;

import de.xxschrandxx.wsc.core.permission.PermissionPlugin;

public final class MinecraftBridgeVars {
    public final static class Configuration {
        public final static class server {
            /** hostname for webserver */
            public final static String hostname = "server.hostname";
            /** port for webserver */
            public final static String port = "server.port";
            /** user for webserver */
            public final static String user = "server.user";
            /** password for webserver */
            public final static String password = "server.password";
            /** path to whitelist */
            public final static String whitelistPath = "server.whitelistPath";
            /** path for blacklist */
            public final static String blacklistPath = "server.blacklistPath";
            public final static class defaults {
                public final static String hostname = "localhost";
                public final static Integer port = 8080;
                public final static String user = "user";
                public final static String password = "MySuperSecretPassword";
                public final static String whitelistPath = "whitelist.txt";
                public final static String blacklistPath = "blacklist.txt";
            }
            public final static class ssl {
                /** weather ssl should be enabled */
                public final static String enabled = "server.ssl.enabled";
                /** path to keystore  */
                public final static String keyStorePath = "server.ssl.keyPath";
                /** keyStorePassword */
                public final static String keyStorePassword = "server.ssl.keyStorePassword";
                /** keyAlias */
                public final static String keyAlias = "server.ssl.keyAlias";
                /** keyPassword */
                public final static String keyPassword = "server.ssl.keyPassword";
                public final static class defaults {
                    public final static Boolean enabled = false;
                    public final static String keyStorePath = "keys";
                    public final static String keyStorePassword = "MySuperSecretPassword";
                    public final static String keyAlias = "alias";
                    public final static String keyPassword = "MySuperSecretPassword";
                }
            }
            public final static class floodgate {
                /** max attempts until an ip gets blocked temporality */
                public final static String maxTries = "server.floodgate.maxTries";
                /** time in milliseconds after the floodgate resets */
                public final static String resetTime = "server.floodgate.resetTime";
                /** max overruns until ip gets blocked permanent */
                public final static String maxOverruns = "server.floodgate.maxOverruns";
                public final static class defaults {
                    public final static Integer maxTries = 600;
                    public final static Long resetTime = 600000L; 
                    public final static Integer maxOverruns = 0;
                }
            }
        }
        public final static class modules {
            public final static class groupsync {
                /** weather groupsync module should be enabled */
                public final static String enabled = "modules.groupsync.enabled";
                /** permission plugin name */
                public final static String plugin = "modules.groupsync.plugin";
                public final static class defaults {
                    public final static Boolean enabled = false;
                    public final static PermissionPlugin plugin = PermissionPlugin.LuckPerms;
                }
            }
        }
    }
}
