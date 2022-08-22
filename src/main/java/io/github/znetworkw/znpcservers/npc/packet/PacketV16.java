package io.github.znetworkw.znpcservers.npc.packet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.npc.ItemSlot;
import io.github.znetworkw.znpcservers.npc.NPC;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.inventory.ItemStack;

public class PacketV16 extends PacketV9 {
    public PacketV16() {
    }

    public int version() {
        return 16;
    }

    public ImmutableList<Object> getEquipPackets(NPC npc) throws ReflectiveOperationException {
        List<Pair<?, ?>> pairs = Lists.newArrayListWithCapacity(ItemSlot.values().length);
        Iterator var3 = npc.getNpcPojo().getNpcEquip().entrySet().iterator();

        while(var3.hasNext()) {
            Entry<ItemSlot, ItemStack> entry = (Entry)var3.next();
            pairs.add(new Pair(this.getItemSlot(((ItemSlot)entry.getKey()).getSlot()), this.convertItemStack(npc.getEntityID(), (ItemSlot)entry.getKey(), (ItemStack)entry.getValue())));
        }

        return ImmutableList.of(((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_V1.load()).newInstance(npc.getEntityID(), pairs));
    }
}
