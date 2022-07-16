# Setting up WSC-Minecraft-Bridge
[German](#German) | [English](#English)

## English
1. Download [WSC-Minecraft-Bridge](https://www.spigotmc.org/resources/wsc-minecraft-bridge.100716/) and put it in your `plugins` folder.
2. Restart your server.
3. Open `plugins/wscbridge-{bukkit|bungee}/config.yml` and set your own `user` and `password`. (This data is required for the entry in [Minecraft-API](https://github.com/xXSchrandXx/de.xxschrandxx.wsc.minecraft-api/blob/main/Setup.md). If necessary, update `hostname ` and `port`.
4. [Optional] Create a `PKCS12` (.p12) file. ([Guide by Plan](https://github.com/plan-player-analytics/Plan/wiki/SSL-Certificate-%28HTTPS%29-Set-Up)) and set `ssl.enabled` to `true` , the path to the `PKCS12` (.p12) file in `ssl.keyPath`, `ssl.keyStorePassword`, `keyAlias` and `keyPassword`.
5. [Optional] Set `whitelistPath` and/or `blacklistPath` to an existing file. Addresses can appear line by line in the files.
If `whitelistPath` points to an existing file, only connections with addresses in the file will be accepted.
If `blacklistPath` points to an existing file, connections to addresses from the file will be blocked.
6. Restart your server.

## German
1. Lade [WSC-Minecraft-Bridge](https://www.spigotmc.org/resources/wsc-minecraft-bridge.100716/) herunter und lege es in deinem `plugins` Ordner.
2. Starte deinen Server neu.
3. Öffne die `plugins/wscbridge-{bukkit|bungee}/config.yml` und setze ein eigenen `user` und `password`. (Diese Daten werden für den Eintrag in [Minecraft-API](https://github.com/xXSchrandXx/de.xxschrandxx.wsc.minecraft-api/blob/main/Setup.md) benötigt. Aktuallisiere wenn nötig auch `hostname` und `port`.
4. [Optional] Erstelle eine `PKCS12` (.p12) Datei. ([Guide von Plan](https://github.com/plan-player-analytics/Plan/wiki/SSL-Certificate-%28HTTPS%29-Set-Up)) und setze `ssl.enabled` auf `true`, den Pfad zur `PKCS12` (.p12) Datei in `ssl.keyPath`, `ssl.keyStorePassword`, `keyAlias` und `keyPassword`.
5. [Optional] Setze `whitelistPath` und / oder `blacklistPath` zu einer exisiterenden Datei. Zeilenweise können in den Dateien Addressen stehen.
Wenn `whitelistPath` zu einer existierenden Datei verweist, werden nur Verbindungen mit Addressen aus der Datei akzeptiert.
Wenn `blacklistPath` zu einer exisitierenden Datei verweist, werden Verbindungen mit Addressen aus der Datei blockiert.
6. Starte deinen Server neu.

# Other projects
* [xXSchrandXx/WSC-Minecraft-Bridge](https://github.com/xXSchrandXx/WSC-Minecraft-Bridge/blob/main/Setup.md)
* [xXSchrandXx/de.xxschrandxx.wsc.minecraft-api](https://github.com/xXSchrandXx/de.xxschrandxx.wsc.minecraft-api/blob/main/Setup.md)
* [xXSchrandXx/de.xxschrandxx.wsc.minecraft-linker](https://github.com/xXSchrandXx/de.xxschrandxx.wsc.minecraft-linker/blob/main/Setup.md)
* [xXSchrandXx/de.xxschrandxx.wsc.minecraft-sync](https://github.com/xXSchrandXx/de.xxschrandxx.wsc.minecraft-sync/blob/main/Setup.md)
* [xXSchrandXx/de.xxschrandxx.wsc.minecraft-profile](https://github.com/xXSchrandXx/de.xxschrandxx.wsc.minecraft-profile/blob/main/Setup.md)
* [xXSchrandXx/WSC-Minecraft-Authenticator](https://github.com/xXSchrandXx/WSC-Minecraft-Authenticator/blob/main/Setup.md)