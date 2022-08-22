package io.github.znetworkw.znpcservers.npc.packet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.npc.ItemSlot;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map.Entry;

public class PacketV9 extends PacketV8 {
    public PacketV9() {
    }

    public int version() {
        return 9;
    }

    public Object convertItemStack(int entityId, ItemSlot itemSlot, ItemStack itemStack) throws ReflectiveOperationException {
        return ((Method)CacheRegistry.AS_NMS_COPY_METHOD.load()).invoke(CacheRegistry.CRAFT_ITEM_STACK_CLASS, itemStack);
    }

    public ImmutableList<Object> getEquipPackets(NPC npc) throws ReflectiveOperationException {
        Builder<Object> builder = ImmutableList.builder();
        Iterator var3 = npc.getNpcPojo().getNpcEquip().entrySet().iterator();

        while(var3.hasNext()) {
            Entry<ItemSlot, ItemStack> stackEntry = (Entry)var3.next();
            builder.add(((Constructor)CacheRegistry.PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_NEWEST_OLD.load()).newInstance(npc.getEntityID(), this.getItemSlot(((ItemSlot)stackEntry.getKey()).getSlot()), this.convertItemStack(npc.getEntityID(), (ItemSlot)stackEntry.getKey(), (ItemStack)stackEntry.getValue())));
        }

        return builder.build();
    }

    public void updateGlowPacket(NPC npc, Object packet) throws ReflectiveOperationException {
        Object enumChatString = ((Method)CacheRegistry.ENUM_CHAT_TO_STRING_METHOD.load()).invoke(npc.getGlowColor());
        if (Utils.BUKKIT_VERSION > 12) {
            Utils.setValue(packet, "g", npc.getGlowColor());
            Utils.setValue(packet, "c", ((Constructor)CacheRegistry.I_CHAT_BASE_COMPONENT_A_CONSTRUCTOR.load()).newInstance(enumChatString));
        } else {
            Utils.setValue(packet, "g", ((Method)CacheRegistry.GET_ENUM_CHAT_ID_METHOD.load()).invoke(npc.getGlowColor()));
            Utils.setValue(packet, "c", enumChatString);
        }

    }

    public boolean allowGlowColor() {
        return true;
    }
}
