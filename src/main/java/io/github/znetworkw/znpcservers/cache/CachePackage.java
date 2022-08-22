package io.github.znetworkw.znpcservers.cache;

import io.github.znetworkw.znpcservers.utils.Utils;

public enum CachePackage {
    DEFAULT,
    CRAFT_BUKKIT("org.bukkit.craftbukkit." + Utils.getBukkitPackage()),
    MINECRAFT_SERVER("net.minecraft");

    private static final String EMPTY_STRING = "";
    private static final String DOT = ".";
    private final String fixedPackageName;

    private CachePackage(String packageName) {
        this.fixedPackageName = Utils.versionNewer(17) ? packageName : packageName + (packageName.contains("minecraft") ? ".server." + Utils.getBukkitPackage() : "");
    }

    private CachePackage() {
        this.fixedPackageName = "";
    }

    public String getForCategory(CacheCategory packetCategory, String extra) {
        return Utils.versionNewer(17) ? packetCategory.getPackageName() + (extra.length() > 0 ? "." + extra : "") : this.fixedPackageName;
    }

    public String getFixedPackageName() {
        return this.fixedPackageName;
    }
}
