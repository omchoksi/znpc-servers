package io.github.znetworkw.znpcservers.npc.packet;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.npc.FunctionFactory;
import io.github.znetworkw.znpcservers.npc.ItemSlot;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCType;
import io.github.znetworkw.znpcservers.utility.ReflectionUtils;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

public interface Packet {
    int version();

    @PacketValue(keyName = "playerPacket")
    Object getPlayerPacket(Object paramObject, GameProfile paramGameProfile) throws ReflectiveOperationException;

    @PacketValue(keyName = "spawnPacket")
    Object getSpawnPacket(Object paramObject, boolean paramBoolean) throws ReflectiveOperationException;

    Object convertItemStack(int paramInt, ItemSlot paramItemSlot, ItemStack paramItemStack) throws ReflectiveOperationException;

    Object getClickType(Object paramObject) throws ReflectiveOperationException;

    Object getMetadataPacket(int paramInt, Object paramObject) throws ReflectiveOperationException;

    @PacketValue(keyName = "hologramSpawnPacket", valueType = ValueType.ARGUMENTS)
    Object getHologramSpawnPacket(Object paramObject) throws ReflectiveOperationException;

    @PacketValue(keyName = "destroyPacket", valueType = ValueType.ARGUMENTS)
    default Object getDestroyPacket(int entityId) throws ReflectiveOperationException {
        (new int[1])[0] = entityId;
        return ((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR.load()).newInstance(new Object[] { ((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR.load()).getParameterTypes()[0].isArray() ? new int[1] : Integer.valueOf(entityId) });
    }

    @PacketValue(keyName = "enumSlot", valueType = ValueType.ARGUMENTS)
    default Object getItemSlot(int slot) {
        return CacheRegistry.ENUM_ITEM_SLOT.getEnumConstants()[slot];
    }

    @PacketValue(keyName = "removeTab")
    default Object getTabRemovePacket(Object nmsEntity) throws ReflectiveOperationException {
        return CacheRegistry.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.load().newInstance(CacheRegistry.REMOVE_PLAYER_FIELD
                .load(),
                Collections.singletonList(nmsEntity));
    }

    @PacketValue(keyName = "equipPackets")
    ImmutableList<Object> getEquipPackets(NPC paramNPC) throws ReflectiveOperationException;

    @PacketValue(keyName = "scoreboardPackets")
    default ImmutableList<Object> updateScoreboard(NPC npc) throws ReflectiveOperationException {
        ImmutableList.Builder<Object> builder = ImmutableList.builder();
        boolean isVersion17 = (Utils.BUKKIT_VERSION > 16);
        boolean isVersion9 = (Utils.BUKKIT_VERSION > 8);
        Object scoreboardTeamPacket = isVersion17 ? CacheRegistry.SCOREBOARD_TEAM_CONSTRUCTOR.load().newInstance(null, npc.getGameProfile().getName()) : CacheRegistry.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD.load().newInstance();
        if (!isVersion17) {
            Utils.setValue(scoreboardTeamPacket, "a", npc.getGameProfile().getName());
            Utils.setValue(scoreboardTeamPacket, isVersion9 ? "i" : "h", 1);
        }
        builder.add(isVersion17 ? CacheRegistry.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE_V1.load().invoke(null, scoreboardTeamPacket) : scoreboardTeamPacket);
        if (isVersion17) {
            scoreboardTeamPacket = CacheRegistry.SCOREBOARD_TEAM_CONSTRUCTOR.load().newInstance(null, npc.getGameProfile().getName());
            if (Utils.BUKKIT_VERSION > 17) {
                Utils.setValue(scoreboardTeamPacket, "d", npc.getGameProfile().getName());
                ReflectionUtils.findFieldForClassAndSet(scoreboardTeamPacket, CacheRegistry.ENUM_TAG_VISIBILITY, CacheRegistry.ENUM_TAG_VISIBILITY_NEVER_FIELD.load());
            } else {
                Utils.setValue(scoreboardTeamPacket, "e", npc.getGameProfile().getName());
                Utils.setValue(scoreboardTeamPacket, "l", CacheRegistry.ENUM_TAG_VISIBILITY_NEVER_FIELD.load());
            }
        } else {
            scoreboardTeamPacket = CacheRegistry.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD.load().newInstance();
            Utils.setValue(scoreboardTeamPacket, "a", npc.getGameProfile().getName());
            Utils.setValue(scoreboardTeamPacket, "e", "never");
            Utils.setValue(scoreboardTeamPacket, isVersion9 ? "i" : "h", 0);
        }
        Collection<String> collection = isVersion17 ? (Collection<String>) CacheRegistry.SCOREBOARD_PLAYER_LIST.load().invoke(scoreboardTeamPacket, new Object[0]) : (Collection<String>)Utils.getValue(scoreboardTeamPacket, isVersion9 ? "h" : "g");
        if (npc.getNpcPojo().getNpcType() == NPCType.PLAYER) {
            collection.add(npc.getGameProfile().getName());
        } else {
            collection.add(npc.getUUID().toString());
        }
        if (allowGlowColor() &&
                FunctionFactory.isTrue(npc, "glow"))
            updateGlowPacket(npc, scoreboardTeamPacket);
        builder.add(isVersion17 ? ((Method)CacheRegistry.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE.load()).invoke(null, scoreboardTeamPacket, Boolean.TRUE) : scoreboardTeamPacket);
        return builder.build();
    }

    void updateGlowPacket(NPC paramNPC, Object paramObject) throws ReflectiveOperationException;

    boolean allowGlowColor();

    default void update(PacketCache packetCache) throws ReflectiveOperationException {
        packetCache.flushCache("scoreboardPackets");
    }
}
