package io.github.znetworkw.znpcservers.utility.inventory;

import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ZInventory {
    private static final int MAX_ROWS = 6;
    private final Player player;
    private ZInventoryPage page;
    private Inventory inventory;

    public ZInventory(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ZInventoryPage getPage() {
        return this.page;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setCurrentPage(ZInventoryPage page) {
        this.page = page;
    }

    public Inventory build(ZInventoryPage page) {
        if (page == null) {
            throw new IllegalStateException("page is null");
        } else if (page.getRows() / 9 > 6) {
            throw new IllegalArgumentException(String.format("Unexpected rows size. Has %d, max %d", page.getRows(), 6));
        } else {
            this.setCurrentPage(page);
            page.getInventoryItems().removeIf((zInventoryItem) -> {
                return !zInventoryItem.isDefault();
            });
            page.update();
            this.inventory = Bukkit.createInventory(new ZInventoryHolder(this), page.getRows(), Utils.toColor(page.getPageName()));
            page.getInventoryItems().forEach((zInventoryItem) -> {
                this.inventory.setItem(zInventoryItem.getSlot(), zInventoryItem.getItemStack());
            });
            return this.inventory;
        }
    }

    public Inventory build() {
        return this.build(this.page);
    }
}
