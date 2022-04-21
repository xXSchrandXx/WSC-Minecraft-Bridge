Languages: [General](#general) | [API](#api) | [Links](#links)

"Minecraft"™ is a trademark of Mojang Synergies AB. This Resource ist not affiliate with Mojang.

# General
## Description
This plugin serves as the basis for the bridge between [xXSchrandXx/de.xxschrarndxx.wsc.minecraft-api](https://github.com/xXSchrandXx/de.xxschrarndxx.wsc.minecraft-api) and a bukkit / spigot / BungeeCord plugin. You can add your own extensions as described in [API](#api).
## Installation
1. Download latest version from [Spigot/Resources/WSC-Minecraft-Bridge](https://www.spigotmc.org/resources/wsc-minecraft-bridge.100716/).
2. Put it into your `plugins` folder.
3. Start your server.
4. Update the configuration in `plugins/wscbridge-{bukkit|spigot}`.
5. Restart your server or use the `wscbridge{bukkit|spigot} reload` command.
6. (Configure [WoltLab](#woltlab) plugins)
## Configration
```YAML
# Server configuration
server:
  hostname: localhost
  port: 8080
  # Username for login
  user: user
  # Password for login
  password: MySuperSecretPassword
  # Path to whitelist file
  whitelistPath: whitelist.txt
  # Path to blacklist file
  blacklistPath: blacklist.txt
  # Floodgate configuration
  floodgate:
    # Max attempts until an ip gets blocked temporality
    # Disabled with number below 1
    maxTries: 600
    # Time in milliseconds after the floodgate resets
    resetTime: 600000
    # Max overruns until ip gets blocked permanent
    # Disabled with number below 1
    maxOverruns: 0
  # SSL configuration
  ssl:
    # Weather ssl should be enabled
    enabled: false
    # Path to keystore
    keyPath: keys
    # Password for keystore
    keyStorePassword: MySuperSecretPassword
    # Alias for key
    keyAlias: alias
    # Password for key
    keyPassword: MySuperSecretPassword
# Module configuration
modules:
  # Weahter the permission module should be enabled
  permission: false

```
# API
## Creating a Handler
```JAVA
// You should use AbstractBungeeHttpHandler and AbstractBukkitHttpHandler
public class MyHandler extends AbstractHttpHandler {
    @Override
    public HashMap<String, Object> run(HttpExchange exchange) {
        HashMap<String, Object> response = new HashMap<String, Object>();

        // Do stuff.

        // A response has to have `status` and `statusCode` set!
        response.put("status", "OK");
        response.put("statusCode", HttpURLConnection.HTTP_OK);

        return response;
    }
```
Example: [Bukkit/Spigot](https://github.com/xXSchrandXx/WSC-Minecraft-Bridge/blob/main/src/main/java/de/xxschrandxx/wsc/bukkit/handler/StatusHandler.java) | [BungeeCord](https://github.com/xXSchrandXx/WSC-Minecraft-Bridge/blob/main/src/main/java/de/xxschrandxx/wsc/bungee/handler/StatusHandler.java)
## Adding a Handler
```JAVA
@EventHandler
public void addHandlerListener(MinecraftBridgeEvent event) {
    MinecraftBridgeBukkit instance = MinecraftBridgeBukkit.getInstance();
    MinecraftBridgeHandler handler = instance.getHandler();
    handler.addPasswordHandler("path", new MyHandler());
}
```
Example: [Bukkit/Spigot](https://github.com/xXSchrandXx/WSC-Minecraft-Bridge/blob/main/src/main/java/de/xxschrandxx/wsc/bukkit/listener/HandlerListener.java) | [BungeeCord](https://github.com/xXSchrandXx/WSC-Minecraft-Bridge/blob/main/src/main/java/de/xxschrandxx/wsc/bungee/listener/HandlerListener.java)

# Links
## GitHub
* [xXSchrandXx/de.xxschrandxx.wsc.minecraft-api](https://github.com/xXSchrandXx/de.xxschrandxx.wsc.minecraft-api)
* [xXSchrandXx/de.xxschrandxx.wsc.minecraft-linker](https://github.com/xXSchrandXx/de.xxschrandxx.wsc.minecraft-linker)
* [xXSchrandXx/de.xxschrandxx.wsc.minecraft-sync](https://github.com/xXSchrandXx/de.xxschrandxx.wsc.minecraft-sync)
* [xXSchrandXx/de.xxschrandxx.wsc.minecraft-profile](https://github.com/xXSchrandXx/de.xxschrandxx.wsc.minecraft-profile)
* [xXSchrandXx/WSC-Minecraft-Bridge](https://github.com/xXSchrandXx/WSC-Minecraft-Bridge)
* [xXSchrandXx/WSC-Minecraft-Authenticator](https://github.com/xXSchrandXx/WSC-Minecraft-Authenticator)

## WoltLab
* [Plugin-Store/Minecraft-API](https://www.woltlab.com/pluginstore/file/7077-minecraft-api/)
* [Plugin-Store/Minecraft-Linker](https://www.woltlab.com/pluginstore/file/7093-minecraft-linker/)
## Spigot
* [Resources/WSC-Minecraft-Bridge](https://www.spigotmc.org/resources/wsc-minecraft-bridge.100716/)
* [Resources/WSC-Minecraft-Authenticator](https://www.spigotmc.org/resources/wsc-minecraft-authenticator.101169/)
## JavaDocs
* [Docs/wscbridge](https://maven.gamestrike.de/docs/wscbridge/)
* [Docs/wscauthenticator](https://maven.gamestrike.de/docs/wscauthenticator/)
## Maven
```XML
<repository>
	<id>schrand-repo</id>
	<url>https://maven.gamestrike.de/mvn/</url>
</repository>
```