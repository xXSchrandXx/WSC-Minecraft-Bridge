package de.xxschrandxx.wsc.core;

public final class MinecraftLinkerVars {
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
            /** maximum of tries until blacklisted */
            public final static String maxTries = "server.maxTries";
            /** time until maxTries reset for address */
            public final static String resetTime = "server.resetTime";
            public final static class defaults {
                public final static String hostname = "localhost";
                public final static Integer port = 8080;
                public final static String user = "user";
                public final static String password = "MySuperSecretPassword";
                public final static String whitelistPath = "whitelist.txt";
                public final static String blacklistPath = "blacklist.txt";
                public final static Integer maxTries = 600;
                public final static Long resetTime = 600000L;
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
                /** max attempts until an ip gets bocked */
                public final static String maxAttempts = "server.floodgate.maxAttempts";
                /** time in milliseconds after the floodgate resets */
                public final static String resetTime = "server.floodgate.resetTime";
                public final static class defaults {
                    public final static Integer maxAttempts = 600;
                    public final static Integer resetTime = 600000; 
                }
            }
        }
    }

    public class HttpStatusCodes {

    }
}
