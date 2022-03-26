package fr.blockincraft.kingdoms.listeners;

import fr.blockincraft.kingdoms.menus.BaseMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.*;

public class MenuListeners implements Listener {
    public static final Map<UUID, BaseMenu> menus = new HashMap<>();

    /**
     * Execute the menu click handlers of the clicked menu.
     * @param e the click event.
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        BaseMenu menu = menus.get(e.getWhoClicked().getUniqueId());
        if (menu != null) {
            if (e.getRawSlot() < e.getInventory().getSize()) {
                BaseMenu.MenuClickHandler handler = menu.getMenuClickHandler(e.getSlot());
                if (handler == null) {
                    e.setCancelled(!menu.isEmptySlotsClickable() && (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR));
                } else {
                    e.setCancelled(!handler.onClick((Player)e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getClick()));
                }
            } else {
                e.setCancelled(!menu.getPlayerInventoryClickHandler().onClick((Player)e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getClick()));
            }
        }
    }

    /**
     * Execute the menu close handler of the clicked menu.
     * @param e the close event.
     */
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        menus.forEach((uuid, baseMenu) -> {
            if (e.getPlayer().getUniqueId() == uuid) {
                menus.remove(uuid);
                baseMenu.getMenuCloseHandler().onClose((Player) e.getPlayer());
            }
        });
    }
}
