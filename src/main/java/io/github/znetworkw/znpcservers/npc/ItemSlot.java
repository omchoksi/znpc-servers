package io.github.znetworkw.znpcservers.npc;

public enum ItemSlot {
    HELMET(5, "Helmet"),
    CHESTPLATE(4, "Chestplate"),
    LEGGINGS(3, "Leggings"),
    BOOTS(2, "Boots"),
    OFFHAND(1, "Offhand"),
    HAND(0, "Hand");

    private final int slot;
    private final int slotOld;
    private final String name;

    private ItemSlot(int slot, String name) {
        this.slot = slot;
        this.slotOld = slot == 0 ? 0 : slot - 1;
        this.name = name;
    }

    public int getSlot() {
        return this.slot;
    }

    public int getSlotOld() {
        return this.slotOld;
    }

    public String getName() {
        return this.name;
    }
}
