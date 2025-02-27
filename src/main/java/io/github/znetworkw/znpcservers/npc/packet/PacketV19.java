package io.github.znetworkw.znpcservers.npc.packet;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;

public class PacketV19 extends PacketV17 {
    public PacketV19() {
    }

    public int version() {
        return 19;
    }

    public Object getPlayerPacket(Object nmsWorld, GameProfile gameProfile) throws ReflectiveOperationException {
        return CacheRegistry.PLAYER_CONSTRUCTOR_NEW_1.load().newInstance(((Method)CacheRegistry.GET_SERVER_METHOD.load()).invoke(Bukkit.getServer()), nmsWorld, gameProfile, null);
    }
}
