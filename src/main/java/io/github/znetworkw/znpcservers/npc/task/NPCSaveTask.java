package io.github.znetworkw.znpcservers.npc.task;

import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.configuration.Configuration;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCSaveTask extends BukkitRunnable {
    public NPCSaveTask(ServersNPC serversNPC, int seconds) {
        this.runTaskTimer(serversNPC, 200L, (long)seconds);
    }

    public void run() {
        Configuration.SAVE_CONFIGURATIONS.forEach(Configuration::save);
    }
}
