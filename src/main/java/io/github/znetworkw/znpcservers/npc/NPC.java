package io.github.znetworkw.znpcservers.npc;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.UnexpectedCallException;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.npc.NPCPath.AbstractTypeWriter;
import io.github.znetworkw.znpcservers.npc.NPCPath.PathInitializer;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel;
import io.github.znetworkw.znpcservers.npc.hologram.Hologram;
import io.github.znetworkw.znpcservers.npc.packet.PacketCache;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;
import io.github.znetworkw.znpcservers.utility.location.ZLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NPC {
    private static final ConcurrentMap<Integer, NPC> NPC_MAP = new ConcurrentHashMap();
    private static final String PROFILE_TEXTURES = "textures";
    private static final String START_PREFIX = "[ZNPC] ";
    private final Set<ZUser> viewers;
    private final PacketCache packets;
    private final NPCModel npcPojo;
    private final Hologram hologram;
    private final String npcName;
    private final NPCSkin npcSkin;
    private long lastMove;
    private int entityID;
    private Object glowColor;
    private Object tabConstructor;
    private Object nmsEntity;
    private Object bukkitEntity;
    private UUID uuid;
    private GameProfile gameProfile;
    private PathInitializer npcPath;

    public NPC(NPCModel npcModel, boolean load) {
        this.viewers = new HashSet();
        this.packets = new PacketCache();
        this.lastMove = -1L;
        this.npcPojo = npcModel;
        this.hologram = new Hologram(this);
        this.npcName = NamingType.DEFAULT.resolve(this);
        this.npcSkin = NPCSkin.forValues(new String[]{npcModel.getSkin(), npcModel.getSignature()});
        if (load) {
            this.onLoad();
        }

    }

    public NPC(NPCModel npcModel) {
        this(npcModel, false);
    }

    public void onLoad() {
        if (NPC_MAP.containsKey(this.getNpcPojo().getId())) {
            throw new IllegalStateException("npc with id " + this.getNpcPojo().getId() + " already exists.");
        } else {
            this.gameProfile = new GameProfile(UUID.randomUUID(), "[ZNPC] " + this.npcName);
            this.gameProfile.getProperties().put("textures", new Property("textures", this.npcPojo.getSkin(), this.npcPojo.getSignature()));
            this.changeType(this.npcPojo.getNpcType());
            this.updateProfile(this.gameProfile.getProperties());
            this.setLocation(this.getNpcPojo().getLocation().bukkitLocation(), false);
            this.hologram.createHologram();
            if (this.npcPojo.getPathName() != null) {
                this.setPath(AbstractTypeWriter.find(this.npcPojo.getPathName()));
            }

            this.npcPojo.getCustomizationMap().forEach((key, value) -> {
                this.npcPojo.getNpcType().updateCustomization(this, key, value);
            });
            NPC_MAP.put(this.getNpcPojo().getId(), this);
        }
    }

    public NPCModel getNpcPojo() {
        return this.npcPojo;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public Object getBukkitEntity() {
        return this.bukkitEntity;
    }

    public Object getNmsEntity() {
        return this.nmsEntity;
    }

    public Object getGlowColor() {
        return this.glowColor;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public PathInitializer getNpcPath() {
        return this.npcPath;
    }

    public Hologram getHologram() {
        return this.hologram;
    }

    public Set<ZUser> getViewers() {
        return this.viewers;
    }

    public PacketCache getPackets() {
        return this.packets;
    }

    public void setGlowColor(Object glowColor) {
        this.glowColor = glowColor;
    }

    public void setLocation(Location location, boolean updateTime) {
        try {
            if (this.npcPath == null) {
                this.lookAt((ZUser)null, location, true);
                if (updateTime) {
                    this.lastMove = System.nanoTime();
                }

                this.npcPojo.setLocation(new ZLocation(location = new Location(location.getWorld(), (double)location.getBlockX() + 0.5D, location.getY(), (double)location.getBlockZ() + 0.5D, location.getYaw(), location.getPitch())));
            }

            ((Method)CacheRegistry.SET_LOCATION_METHOD.load()).invoke(this.nmsEntity, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            Object npcTeleportPacket = ((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR.load()).newInstance(this.nmsEntity);
            this.viewers.forEach((player) -> {
                Utils.sendPackets(player, new Object[]{npcTeleportPacket});
            });
            this.hologram.setLocation(location, this.npcPojo.getNpcType().getHoloHeight());
        } catch (ReflectiveOperationException var4) {
            throw new UnexpectedCallException(var4);
        }
    }

    public void changeSkin(NPCSkin skinFetch) {
        this.npcPojo.setSkin(skinFetch.getTexture());
        this.npcPojo.setSignature(skinFetch.getSignature());
        this.gameProfile.getProperties().clear();
        this.gameProfile.getProperties().put("textures", new Property("textures", this.npcPojo.getSkin(), this.npcPojo.getSignature()));
        this.updateProfile(this.gameProfile.getProperties());
        this.deleteViewers();
    }

    public void setSecondLayerSkin() {
        try {
            Object dataWatcherObject = ((Method)CacheRegistry.GET_DATA_WATCHER_METHOD.load()).invoke(this.nmsEntity);
            if (Utils.versionNewer(9)) {
                ((Method)CacheRegistry.SET_DATA_WATCHER_METHOD.load()).invoke(dataWatcherObject, ((Constructor)CacheRegistry.DATA_WATCHER_OBJECT_CONSTRUCTOR.load()).newInstance(this.npcSkin.getLayerIndex(), CacheRegistry.DATA_WATCHER_REGISTER_FIELD.load()), 127);
            } else {
                ((Method)CacheRegistry.WATCH_DATA_WATCHER_METHOD.load()).invoke(dataWatcherObject, 10, 127);
            }

        } catch (ReflectiveOperationException var2) {
            throw new UnexpectedCallException(var2);
        }
    }

    public void changeType(NPCType npcType) {
        try {
            Object nmsWorld = ((Method)CacheRegistry.GET_HANDLE_WORLD_METHOD.load()).invoke(this.getLocation().getWorld());
            boolean isPlayer = npcType == NPCType.PLAYER;
            this.nmsEntity = isPlayer ? this.packets.getProxyInstance().getPlayerPacket(nmsWorld, this.gameProfile) : (Utils.versionNewer(14) ? npcType.getConstructor().newInstance(npcType.getNmsEntityType(), nmsWorld) : npcType.getConstructor().newInstance(nmsWorld));
            this.bukkitEntity = ((Method)CacheRegistry.GET_BUKKIT_ENTITY_METHOD.load()).invoke(this.nmsEntity);
            this.uuid = (UUID)((Method)CacheRegistry.GET_UNIQUE_ID_METHOD.load()).invoke(this.nmsEntity);
            if (isPlayer) {
                this.tabConstructor = ((Constructor)CacheRegistry.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.load()).newInstance(CacheRegistry.ADD_PLAYER_FIELD.load(), Collections.singletonList(this.nmsEntity));
                this.setSecondLayerSkin();
            }

            this.npcPojo.setNpcType(npcType);
            this.setLocation(this.getLocation(), false);
            this.packets.flushCache(new String[]{"spawnPacket", "removeTab"});
            this.deleteViewers();
            this.entityID = (Integer)((Method)CacheRegistry.GET_ENTITY_ID.load()).invoke(this.nmsEntity);
            FunctionFactory.findFunctionsForNpc(this).forEach((function) -> {
                function.resolve(this);
            });
            this.getPackets().getProxyInstance().update(this.packets);
        } catch (ReflectiveOperationException var4) {
            var4.printStackTrace();
            throw new UnexpectedCallException(var4);
        }
    }

    public void spawn(ZUser user) {
        if (this.viewers.contains(user)) {
            throw new IllegalStateException(user.getUUID().toString() + " is already a viewer.");
        } else {
            try {
                boolean npcIsPlayer = this.npcPojo.getNpcType() == NPCType.PLAYER;
                if (FunctionFactory.isTrue(this, "glow") || npcIsPlayer) {
                    ImmutableList<Object> scoreboardPackets = this.packets.getProxyInstance().updateScoreboard(this);
                    scoreboardPackets.forEach((p) -> {
                        Utils.sendPackets(user, new Object[]{p});
                    });
                }

                if (npcIsPlayer) {
                    if (FunctionFactory.isTrue(this, "mirror")) {
                        this.updateProfile(user.getGameProfile().getProperties());
                    }

                    Utils.sendPackets(user, new Object[]{this.tabConstructor});
                }

                Utils.sendPackets(user, new Object[]{this.packets.getProxyInstance().getSpawnPacket(this.nmsEntity, npcIsPlayer)});
                if (FunctionFactory.isTrue(this, "holo")) {
                    this.hologram.spawn(user);
                }

                this.viewers.add(user);
                this.updateMetadata(Collections.singleton(user));
                this.sendEquipPackets(user);
                this.lookAt(user, this.getLocation(), true);
                if (npcIsPlayer) {
                    Object removeTabPacket = this.packets.getProxyInstance().getTabRemovePacket(this.nmsEntity);
                    ServersNPC.SCHEDULER.scheduleSyncDelayedTask(() -> {
                        Utils.sendPackets(user, new Object[]{removeTabPacket});
                    }, 60);
                }

            } catch (ReflectiveOperationException var4) {
                throw new UnexpectedCallException(var4);
            }
        }
    }

    public void delete(ZUser user) {
        if (!this.viewers.contains(user)) {
            throw new IllegalStateException(user.getUUID().toString() + " is not a viewer.");
        } else {
            this.handleDelete(user);
            this.viewers.remove(user);
        }
    }

    private void handleDelete(ZUser user) {
        if (!this.viewers.contains(user)) {
            throw new IllegalStateException(user.getUUID().toString() + " is not a viewer.");
        } else {
            try {
                if (this.npcPojo.getNpcType() == NPCType.PLAYER) {
                    this.packets.getProxyInstance().getTabRemovePacket(this.nmsEntity);
                }

                this.hologram.delete(user);
                Utils.sendPackets(user, new Object[]{this.packets.getProxyInstance().getDestroyPacket(this.entityID)});
            } catch (ReflectiveOperationException var3) {
                throw new UnexpectedCallException(var3);
            }
        }
    }

    public void lookAt(ZUser player, Location location, boolean rotation) {
        long lastMoveNanos = System.nanoTime() - this.lastMove;
        if (this.lastMove <= 1L || lastMoveNanos >= 1000000000L) {
            Location direction = rotation ? location : this.npcPojo.getLocation().bukkitLocation().clone().setDirection(location.clone().subtract(this.npcPojo.getLocation().bukkitLocation().clone()).toVector());

            try {
                Object lookPacket = ((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_LOOK_CONSTRUCTOR.load()).newInstance(this.entityID, (byte)((int)(direction.getYaw() * 256.0F / 360.0F)), (byte)((int)(direction.getPitch() * 256.0F / 360.0F)), true);
                Object headRotationPacket = ((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CONSTRUCTOR.load()).newInstance(this.nmsEntity, (byte)((int)(direction.getYaw() * 256.0F / 360.0F)));
                if (player != null) {
                    Utils.sendPackets(player, new Object[]{lookPacket, headRotationPacket});
                } else {
                    this.viewers.forEach((players) -> {
                        Utils.sendPackets(players, new Object[]{headRotationPacket});
                    });
                }

            } catch (ReflectiveOperationException var9) {
                throw new UnexpectedCallException(var9);
            }
        }
    }

    public void deleteViewers() {
        Iterator var1 = this.viewers.iterator();

        while(var1.hasNext()) {
            ZUser user = (ZUser)var1.next();
            this.handleDelete(user);
        }

        this.viewers.clear();
    }

    protected void updateMetadata(Iterable<ZUser> users) {
        try {
            Object metaData = this.packets.getProxyInstance().getMetadataPacket(this.entityID, this.nmsEntity);
            Iterator var3 = users.iterator();

            while(var3.hasNext()) {
                ZUser user = (ZUser)var3.next();
                Utils.sendPackets(user, new Object[]{metaData});
            }

        } catch (ReflectiveOperationException var5) {
            throw new UnexpectedCallException(var5);
        }
    }

    public void updateProfile(PropertyMap propertyMap) {
        if (this.npcPojo.getNpcType() == NPCType.PLAYER) {
            try {
                Object gameProfileObj = ((Method)CacheRegistry.GET_PROFILE_METHOD.load()).invoke(this.nmsEntity);
                Utils.setValue(gameProfileObj, "name", this.gameProfile.getName());
                Utils.setValue(gameProfileObj, "id", this.gameProfile.getId());
                Utils.setValue(gameProfileObj, "properties", propertyMap);
            } catch (ReflectiveOperationException var3) {
                throw new UnexpectedCallException(var3);
            }
        }
    }

    public void sendEquipPackets(ZUser zUser) {
        if (!this.npcPojo.getNpcEquip().isEmpty()) {
            try {
                ImmutableList<Object> equipPackets = this.packets.getProxyInstance().getEquipPackets(this);
                equipPackets.forEach((o) -> {
                    Utils.sendPackets(zUser, new Object[]{o});
                });
            } catch (ReflectiveOperationException var3) {
                throw new UnexpectedCallException(var3.getCause());
            }
        }
    }

    public void setPath(AbstractTypeWriter typeWriter) {
        if (typeWriter == null) {
            this.npcPath = null;
            this.npcPojo.setPathName("none");
        } else {
            this.npcPath = typeWriter.getPath(this);
            this.npcPojo.setPathName(typeWriter.getName());
        }

    }

    public void tryStartConversation(Player player) {
        ConversationModel conversation = this.npcPojo.getConversation();
        if (conversation == null) {
            throw new IllegalStateException("can't find conversation");
        } else {
            conversation.startConversation(this, player);
        }
    }

    public Location getLocation() {
        return this.npcPath != null ? this.npcPath.getLocation().bukkitLocation() : this.npcPojo.getLocation().bukkitLocation();
    }

    public static NPC find(int id) {
        return (NPC)NPC_MAP.get(id);
    }

    public static void unregister(int id) {
        NPC npc = find(id);
        if (npc == null) {
            throw new IllegalStateException("can't find npc with id " + id);
        } else {
            NPC_MAP.remove(id);
            npc.deleteViewers();
        }
    }

    public static Collection<NPC> all() {
        return NPC_MAP.values();
    }
}
