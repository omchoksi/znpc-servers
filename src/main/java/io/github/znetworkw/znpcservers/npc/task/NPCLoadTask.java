package io.github.znetworkw.znpcservers.npc.task;

import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCLoadTask extends BukkitRunnable {
    private static final int DELAY = 40;
    private static final int MAX_TRIES = 10;
    private final NPC npc;
    private int tries = 0;

    public NPCLoadTask(NPC npc) {
        this.npc = npc;
        ServersNPC.SCHEDULER.runTaskTimer(this, 40);
    }

    public void run() {
        if (this.tries++ > 10) {
            this.cancel();
        } else {
            World world = Bukkit.getWorld(this.npc.getNpcPojo().getLocation().getWorldName());
            if (world != null) {
                this.cancel();
                this.npc.onLoad();
            }
        }
    }
}
