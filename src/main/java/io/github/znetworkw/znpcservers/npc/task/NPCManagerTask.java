package io.github.znetworkw.znpcservers.npc.task;

import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.npc.FunctionFactory;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel.ConversationType;
import io.github.znetworkw.znpcservers.user.ZUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class NPCManagerTask extends BukkitRunnable {
    public NPCManagerTask(ServersNPC serversNPC) {
        this.runTaskTimerAsynchronously(serversNPC, 60L, 1L);
    }

    public void run() {
        Iterator var1 = NPC.all().iterator();

        label62:
        while(var1.hasNext()) {
            NPC npc = (NPC)var1.next();
            boolean hasPath = npc.getNpcPath() != null;
            if (hasPath) {
                npc.getNpcPath().handle();
            }

            Iterator var4 = Bukkit.getOnlinePlayers().iterator();

            while(true) {
                while(true) {
                    if (!var4.hasNext()) {
                        continue label62;
                    }

                    Player player = (Player)var4.next();
                    ZUser zUser = ZUser.find(player);
                    boolean canSeeNPC = player.getWorld() == npc.getLocation().getWorld() && player.getLocation().distance(npc.getLocation()) <= (double)ConfigurationConstants.VIEW_DISTANCE;
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
}
