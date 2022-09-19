package fr.blockincraft.kingdoms.listeners;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.core.dto.AreaDTO;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.enums.Constructions;
import fr.blockincraft.kingdoms.core.enums.KingdomPermissionLevels;
import fr.blockincraft.kingdoms.data.MenuData;
import fr.blockincraft.kingdoms.menu.BankMenu;
import fr.blockincraft.kingdoms.menu.ChestMenu;
import fr.blockincraft.kingdoms.menu.KingdomMenu;
import fr.blockincraft.kingdoms.menu.WarehouseMenu;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class MenuListener implements Listener {
    private static final Registry registry = Kingdoms.getInstance().getRegistry();

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        ChestMenu menu = MenuData.menus.remove(e.getPlayer().getUniqueId());

        if (menu != null) {
            menu.getMenuCloseHandler().onClose((Player) e.getPlayer());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ChestMenu menu = MenuData.menus.get(e.getWhoClicked().getUniqueId());

        if (menu != null) {
            if (e.getRawSlot() < e.getInventory().getSize()) {
                ChestMenu.MenuClickHandler handler = menu.getMenuClickHandler(e.getSlot());

                if (handler == null) {
                    e.setCancelled(!menu.isEmptySlotsClickable() && (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR));
                } else {
                    e.setCancelled(!handler.onClick((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getClick()));
                }
            } else {
                e.setCancelled(!menu.getPlayerInventoryClickHandler().onClick((Player) e.getWhoClicked(), e.getSlot(), e.getCurrentItem(), e.getClick()));
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block != null && block.getType() == Material.LECTERN) {
            Location loc = block.getLocation();

            KingdomFullDTO kingdom = registry.getKingdomAt(loc);
            if (kingdom == null) return;

            if (!registry.canDoAt(KingdomPermissionLevels.Permissions.BUILD, e.getPlayer().getUniqueId(), loc)) return;

            if (kingdom.getTownHallArea().isIn(loc)) {
                new KingdomMenu(kingdom).open(e.getPlayer());
                return;
            }

            AreaDTO warehouseArea = null;
            AreaDTO bankArea = null;

            for (Map.Entry<AreaDTO, Constructions> entry : kingdom.getConstructions().entrySet()) {
                if (entry.getValue() == Constructions.BANK) {
                    bankArea = entry.getKey();
                } else if (entry.getValue() == Constructions.WAREHOUSE) {
                    warehouseArea = entry.getKey();
                }
            }

            if (warehouseArea != null && warehouseArea.isIn(loc)) {
                new WarehouseMenu(kingdom).open(e.getPlayer());
            } else if (bankArea != null && bankArea.isIn(loc)) {
                new BankMenu(kingdom).open(e.getPlayer());
            }
        }
    }
}
