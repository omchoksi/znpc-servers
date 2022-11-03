package io.github.znetworkw.znpcservers.npc.task;

import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.npc.FunctionFactory;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel.ConversationType;
import io.github.znetworkw.znpcservers.user.ZUser;

import java.util.Collection;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCManagerTask extends BukkitRunnable {
    public NPCManagerTask(ServersNPC serversNPC) {
        this.runTaskTimerAsynchronously(serversNPC, 60L, 1L);
    }

    public void run() {
        for(NPC npc : NPC.all()) {
            final boolean hasPath = npc.getNpcPath() != null;
            if (hasPath) {
                npc.getNpcPath().handle();
            }
            for(Player player : Bukkit.getOnlinePlayers()) {
                final ZUser zUser = ZUser.find(player);
                final boolean canSeeNPC = player.getWorld() == npc.getLocation().getWorld() && player.getLocation().distance(npc.getLocation()) <= (double)ConfigurationConstants.VIEW_DISTANCE;
                if (npc.getViewers().contains(zUser) && !canSeeNPC) {
                    npc.delete(zUser);
                } else if (canSeeNPC) {
                    if (!npc.getViewers().contains(zUser)) {
                        npc.spawn(zUser);
                    }
                    if (FunctionFactory.isTrue(npc, "look") && !hasPath) {
                        npc.lookAt(zUser, player.getLocation(), false);
                    }
                    npc.getHologram().updateNames(zUser);
                    ConversationModel conversationStorage = npc.getNpcPojo().getConversation();
                    if (conversationStorage != null && conversationStorage.getConversationType() == ConversationType.RADIUS) {
                        npc.tryStartConversation(player);
                    }
                }
            }
        }
    }

}
