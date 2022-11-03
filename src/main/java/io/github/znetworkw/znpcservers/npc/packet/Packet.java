package io.github.znetworkw.znpcservers.npc.packet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
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

    @PacketValue(
            keyName = "playerPacket"
    )
    Object getPlayerPacket(Object var1, GameProfile var2) throws ReflectiveOperationException;

    @PacketValue(
            keyName = "spawnPacket"
    )
    Object getSpawnPacket(Object var1, boolean var2) throws ReflectiveOperationException;

    Object convertItemStack(int var1, ItemSlot var2, ItemStack var3) throws ReflectiveOperationException;

    Object getClickType(Object var1) throws ReflectiveOperationException;

    Object getMetadataPacket(int var1, Object var2) throws ReflectiveOperationException;

    @PacketValue(
            keyName = "hologramSpawnPacket",
            valueType = ValueType.ARGUMENTS
    )
    Object getHologramSpawnPacket(Object var1) throws ReflectiveOperationException;

    @PacketValue(
            keyName = "destroyPacket",
            valueType = ValueType.ARGUMENTS
    )
    default Object getDestroyPacket(int entityId) throws ReflectiveOperationException {
        return ((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR.load()).newInstance(((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR.load()).getParameterTypes()[0].isArray() ? new int[]{entityId} : entityId);
    }

    @PacketValue(
            keyName = "enumSlot",
            valueType = ValueType.ARGUMENTS
    )
    default Object getItemSlot(int slot) {
        return CacheRegistry.ENUM_ITEM_SLOT.getEnumConstants()[slot];
    }

    @PacketValue(
            keyName = "removeTab"
    )
    default Object getTabRemovePacket(Object nmsEntity) throws ReflectiveOperationException {
        return ((Constructor)CacheRegistry.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.load()).newInstance(CacheRegistry.REMOVE_PLAYER_FIELD.load(), Collections.singletonList(nmsEntity));
    }

    @PacketValue(
            keyName = "equipPackets"
    )
    ImmutableList<Object> getEquipPackets(NPC var1) throws ReflectiveOperationException;

    @PacketValue(
            keyName = "scoreboardPackets"
    )
    default ImmutableList<Object> updateScoreboard(NPC npc) throws ReflectiveOperationException {
        Builder<Object> builder = ImmutableList.builder();
        boolean isVersion17 = Utils.BUKKIT_VERSION > 16;
        boolean isVersion9 = Utils.BUKKIT_VERSION > 8;
        Object scoreboardTeamPacket = isVersion17 ? ((Constructor)CacheRegistry.SCOREBOARD_TEAM_CONSTRUCTOR.load()).newInstance(null, npc.getGameProfile().getName()) : ((Constructor)CacheRegistry.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD.load()).newInstance();
        if (!isVersion17) {
            Utils.setValue(scoreboardTeamPacket, "a", npc.getGameProfile().getName());
            Utils.setValue(scoreboardTeamPacket, isVersion9 ? "i" : "h", 1);
        }

        builder.add(isVersion17 ? ((Method)CacheRegistry.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE_V1.load()).invoke((Object)null, scoreboardTeamPacket) : scoreboardTeamPacket);
        if (isVersion17) {
            scoreboardTeamPacket = ((Constructor)CacheRegistry.SCOREBOARD_TEAM_CONSTRUCTOR.load()).newInstance(null, npc.getGameProfile().getName());
            if (Utils.BUKKIT_VERSION > 17) {
                Utils.setValue(scoreboardTeamPacket, "d", npc.getGameProfile().getName());
                ReflectionUtils.findFieldForClassAndSet(scoreboardTeamPacket, CacheRegistry.ENUM_TAG_VISIBILITY, CacheRegistry.ENUM_TAG_VISIBILITY_NEVER_FIELD.load());
            } else {
                Utils.setValue(scoreboardTeamPacket, "e", npc.getGameProfile().getName());
                Utils.setValue(scoreboardTeamPacket, "l", CacheRegistry.ENUM_TAG_VISIBILITY_NEVER_FIELD.load());
            }
        } else {
            scoreboardTeamPacket = ((Constructor)CacheRegistry.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD.load()).newInstance();
            Utils.setValue(scoreboardTeamPacket, "a", npc.getGameProfile().getName());
            Utils.setValue(scoreboardTeamPacket, "e", "never");
            Utils.setValue(scoreboardTeamPacket, isVersion9 ? "i" : "h", 0);
        }

        Collection<String> collection = (Collection)(isVersion17 ? ((Method)CacheRegistry.SCOREBOARD_PLAYER_LIST.load()).invoke(scoreboardTeamPacket) : Utils.getValue(scoreboardTeamPacket, isVersion9 ? "h" : "g"));
        if (npc.getNpcPojo().getNpcType() == NPCType.PLAYER) {
            collection.add(npc.getGameProfile().getName());
        } else {
            collection.add(npc.getUUID().toString());
        }

        if (this.allowGlowColor() && FunctionFactory.isTrue(npc, "glow")) {
            this.updateGlowPacket(npc, scoreboardTeamPacket);
        }

        builder.add(isVersion17 ? ((Method)CacheRegistry.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE.load()).invoke((Object)null, scoreboardTeamPacket, true) : scoreboardTeamPacket);
        return builder.build();
    }

    void updateGlowPacket(NPC var1, Object var2) throws ReflectiveOperationException;

    boolean allowGlowColor();

    default void update(PacketCache packetCache) throws ReflectiveOperationException {
        packetCache.flushCache(new String[]{"scoreboardPackets"});
    }
}
