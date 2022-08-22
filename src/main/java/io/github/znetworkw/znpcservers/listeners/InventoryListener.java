package io.github.znetworkw.znpcservers.listeners;

import io.github.znetworkw.znpcservers.ServersNPC;
import io.github.znetworkw.znpcservers.utility.inventory.ZInventory;
import io.github.znetworkw.znpcservers.utility.inventory.ZInventoryHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
    public InventoryListener(ServersNPC serversNPC) {
        serversNPC.getServer().getPluginManager().registerEvents(this, serversNPC);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            if (event.getCurrentItem() != null) {
                if (event.getInventory().getHolder() instanceof ZInventoryHolder) {
                    event.setCancelled(true);
                    ZInventory zInventory = ((ZInventoryHolder)event.getInventory().getHolder()).getzInventory();
                    if (zInventory.getPage().containsItem(event.getRawSlot())) {
                        zInventory.getPage().findItem(event.getRawSlot()).getInventoryCallback().onClick(event);
                        ((Player)event.getWhoClicked()).updateInventory();
                    }
                }
            }
        }
    }
}
