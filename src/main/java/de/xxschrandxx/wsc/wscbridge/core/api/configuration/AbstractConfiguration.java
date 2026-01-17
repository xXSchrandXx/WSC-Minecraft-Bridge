package de.xxschrandxx.wsc.wscbridge.core.api.configuration;

import java.lang.reflect.Field;

import de.xxschrandxx.wsc.wscbridge.core.api.IBridgeLogger;

public abstract class AbstractConfiguration {
    public static boolean startConfig(IConfiguration<?> configuration, Class<?> configClass, Class<?> defaultClass, IBridgeLogger logger) {
        boolean error = false;
        for (Field constant : configClass.getDeclaredFields()) {
            try {
                if (error) {
                    checkConfiguration(
                        configuration,
                        (String) constant.get(configClass),
                        defaultClass.getDeclaredField(constant.getName()).get(defaultClass),
                        logger
                        );
                }
                else {
                    error = checkConfiguration(
                        configuration,
                        (String) constant.get(configClass),
                        defaultClass.getDeclaredField(constant.getName()).get(defaultClass),
                        logger
                        );
                }
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            }
        }
        return error;
    }

    public static boolean checkConfiguration(IConfiguration<?> configuration, String path, Object def, IBridgeLogger logger) {
        if (configuration.get(path) == null) {
            logger.warn(path + " is not set. Resetting it.");
            configuration.set(path, def);
            return true;
        }
        else {
            return false;
        }
    }
}
