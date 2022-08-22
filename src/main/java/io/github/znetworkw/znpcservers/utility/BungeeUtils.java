package io.github.znetworkw.znpcservers.utility;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeUtils {
    private final Plugin plugin;

    public BungeeUtils(Plugin plugin) {
        this.plugin = plugin;
    }

    public void sendPlayerToServer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        player.sendPluginMessage(this.plugin, "BungeeCord", b.toByteArray());
    }
}
