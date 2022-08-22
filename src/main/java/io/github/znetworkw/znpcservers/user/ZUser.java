package io.github.znetworkw.znpcservers.user;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCAction;
import io.github.znetworkw.znpcservers.npc.event.ClickType;
import io.github.znetworkw.znpcservers.npc.event.NPCInteractEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ZUser {
    private static final String CHANNEL_NAME = "npc_interact";
    private static final int DEFAULT_DELAY = 1;
    private static final Map<UUID, ZUser> USER_MAP = new HashMap<>();
    private final Map<Integer, Long> lastClicked;
    private final List<EventService<?>> eventServices;
    private final UUID uuid;
    private final GameProfile gameProfile;
    private final Object playerConnection;
    private boolean hasPath = false;
    private long lastInteract = 0L;

    public ZUser(UUID uuid) {
        this.uuid = uuid;
        this.lastClicked = new HashMap<>();
        this.eventServices = new ArrayList<>();

        try {
            Object playerHandle = CacheRegistry.GET_HANDLE_PLAYER_METHOD.load().invoke(this.toPlayer());
            this.gameProfile = (GameProfile) CacheRegistry.GET_PROFILE_METHOD.load().invoke(playerHandle);
            Channel channel = (Channel) CacheRegistry.CHANNEL_FIELD.load().get(CacheRegistry.NETWORK_MANAGER_FIELD.load().get(this.playerConnection = CacheRegistry.PLAYER_CONNECTION_FIELD.load().get(playerHandle)));
            if (channel.pipeline().names().contains("npc_interact")) {
                channel.pipeline().remove("npc_interact");
            }

            channel.pipeline().addAfter("decoder", "npc_interact", new ZNPCSocketDecoder());
        } catch (InvocationTargetException | IllegalAccessException var4) {
            throw new IllegalStateException("can't create player " + uuid.toString(), var4.getCause());
        }
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public Object getPlayerConnection() {
        return this.playerConnection;
    }

    public boolean isHasPath() {
        return this.hasPath;
    }

    public List<EventService<?>> getEventServices() {
        return this.eventServices;
    }

    public void setHasPath(boolean hasPath) {
        this.hasPath = hasPath;
    }

    public Player toPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public static ZUser find(UUID uuid) {
        return USER_MAP.computeIfAbsent(uuid, ZUser::new);
    }

    public static ZUser find(Player player) {
        return find(player.getUniqueId());
    }

    public static void unregister(Player player) {
        ZUser zUser = USER_MAP.get(player.getUniqueId());
        if (zUser == null) {
            throw new IllegalStateException("can't find user " + player.getUniqueId());
        } else {
            USER_MAP.remove(player.getUniqueId());
            NPC.all().stream().filter((npc) -> npc.getViewers().contains(zUser)).forEach((npc) -> npc.delete(zUser));
        }
    }

    class ZNPCSocketDecoder extends MessageToMessageDecoder<Object> {
        ZNPCSocketDecoder() {
        }

        protected void decode(ChannelHandlerContext channelHandlerContext, Object packet, List<Object> out) throws Exception {
            out.add(packet);
            if (packet.getClass() == CacheRegistry.PACKET_PLAY_IN_USE_ENTITY_CLASS) {
                long lastInteractNanos = System.nanoTime() - ZUser.this.lastInteract;
                if (ZUser.this.lastInteract != 0L && lastInteractNanos < 1000000000L) {
                    return;
                }

                int entityId = CacheRegistry.PACKET_IN_USE_ENTITY_ID_FIELD.load().getInt(packet);
                NPC npc = NPC.all().stream().filter((npc1) -> npc1.getEntityID() == entityId).findFirst().orElse(null);
                if (npc == null) {
                    return;
                }

                ClickType clickName = ClickType.forName(npc.getPackets().getProxyInstance().getClickType(packet).toString());
                ZUser.this.lastInteract = System.nanoTime();
                ServersNPC.SCHEDULER.scheduleSyncDelayedTask(() -> {
                    Bukkit.getServer().getPluginManager().callEvent(new NPCInteractEvent(ZUser.this.toPlayer(), clickName, npc));
                    List<NPCAction> actions = npc.getNpcPojo().getClickActions();
                    if (actions != null && !actions.isEmpty()) {
                        Iterator<NPCAction> var4 = actions.iterator();

                        while(true) {
                            NPCAction npcAction;
                            while(true) {
                                do {
                                    if (!var4.hasNext()) {
                                        return;
                                    }

                                    npcAction = var4.next();
                                } while(npcAction.getClickType() != ClickType.DEFAULT && clickName != npcAction.getClickType());

                                if (npcAction.getDelay() <= 0) {
                                    break;
                                }

                                int actionId = npc.getNpcPojo().getClickActions().indexOf(npcAction);
                                if (ZUser.this.lastClicked.containsKey(actionId)) {
                                    long lastClickNanos = System.nanoTime() - ZUser.this.lastClicked.get(actionId);
                                    if (lastClickNanos < npcAction.getFixedDelay()) {
                                        continue;
                                    }
                                }

                                ZUser.this.lastClicked.put(actionId, System.nanoTime());
                                break;
                            }

                            npcAction.run(ZUser.this, npcAction.getAction());
                        }
                    }
                }, 1);
            }

        }
    }
}
