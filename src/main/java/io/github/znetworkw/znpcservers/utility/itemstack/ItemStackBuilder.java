package io.github.znetworkw.znpcservers.utility.itemstack;

import io.github.znetworkw.znpcservers.utility.Utils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    protected ItemStackBuilder(ItemStack stack) {
        this.itemStack = stack;
        this.itemMeta = stack.getItemMeta();
    }

    public static ItemStackBuilder forMaterial(Material material) {
        if (material != null && material != Material.AIR) {
            return new ItemStackBuilder(new ItemStack(material, 1));
        } else {
            throw new IllegalStateException("can't create builder for a NULL material.");
        }
    }

    public ItemStackBuilder setName(String name) {
        this.itemMeta.setDisplayName(Utils.toColor(name));
        return this;
    }

    public ItemStackBuilder setLore(Iterable<String> lore) {
        this.itemMeta.setLore((List)StreamSupport.stream(lore.spliterator(), false).map(Utils::toColor).collect(Collectors.toList()));
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemStackBuilder setLore(String... lore) {
        return this.setLore((Iterable)Arrays.asList(lore));
    }

    public ItemStackBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}
