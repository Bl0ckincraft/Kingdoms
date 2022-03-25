package fr.kellyan.kingdoms.menus;

import fr.kellyan.kingdoms.listeners.MenuListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The core of a custom menu.
 */
public class BaseMenu {
    private final List<ItemStack> items;
    private final Map<Integer, MenuClickHandler> handlers;
    private MenuOpeningHandler open;
    private MenuCloseHandler close;
    private MenuClickHandler playerClick;
    private final String title;
    private Inventory inv;
    private boolean clickable;
    private boolean emptyClickable;

    /**
     * The constructor of the class.
     * @param title the title of the menu.
     */
    public BaseMenu(String title) {
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.clickable = false;
        this.emptyClickable = true;
        this.items = new ArrayList();
        this.handlers = new HashMap();
        this.open = (p) -> {
        };
        this.close = (p) -> {
        };
        this.playerClick = (p, slot, item, action) -> {
            return this.isPlayerInventoryClickable();
        };
    }

    /**
     * Set if the player inventory is clickable.
     * @param clickable new value.
     * @return this menu after change.
     */
    public BaseMenu setPlayerInventoryClickable(boolean clickable) {
        this.clickable = clickable;
        return this;
    }

    /**
     * @return if the player inventory is clickable.
     */
    public boolean isPlayerInventoryClickable() {
        return this.clickable;
    }

    /**
     * Set if the empty slots are clickable.
     * @param emptyClickable the new value.
     * @return this menu after change.
     */
    public BaseMenu setEmptySlotsClickable(boolean emptyClickable) {
        this.emptyClickable = emptyClickable;
        return this;
    }

    /**
     * @return if empty slots are clickable.
     */
    public boolean isEmptySlotsClickable() {
        return this.emptyClickable;
    }

    /**
     * Set the handler executed when the player inventory clicked.
     * @param handler the new handler.
     * @return this menu after change.
     */
    public BaseMenu setOnPlayerInventoryClicked(MenuClickHandler handler) {
        this.playerClick = handler;
        return this;
    }

    /**
     * This method set an item stack in a slot.
     * @param slot the slot.
     * @param item the item.
     * @return this menu after change.
     */
    public BaseMenu setItem(int slot, ItemStack item) {
        int size = this.items.size();
        if (size > slot) {
            this.items.set(slot, item);
        } else {
            for(int i = 0; i < slot - size; ++i) {
                this.items.add(null);
            }

            this.items.add(item);
        }

        return this;
    }

    /**
     * This method set an item stack in a slot and actualize the inventory.
     * @param slot the slot.
     * @param item the new item.
     * @return this menu after change.
     */
    public BaseMenu replaceExistingItem(int slot, ItemStack item) {
        this.setup();
        this.setItem(slot, item);
        this.inv.setItem(slot, item);
        return this;
    }

    /**
     * This method set an item stack in a slot and bind a menu click handler to the slot.
     * @param slot the slot.
     * @param item the item.
     * @param clickHandler the handler.
     * @return this menu after change.
     */
    public BaseMenu setItem(int slot, ItemStack item, MenuClickHandler clickHandler) {
        this.setItem(slot, item);
        this.setMenuClickHandler(slot, clickHandler);
        return this;
    }

    /**
     * @param slot the slot.
     * @return the requested item.
     */
    public ItemStack getItemInSlot(int slot) {
        this.setup();
        return this.inv.getItem(slot);
    }

    /**
     * This method bind a menu click handler to a slot.
     * @param slot the slot.
     * @param handler the handler.
     * @return this menu after change.
     */
    public BaseMenu setMenuClickHandler(int slot, MenuClickHandler handler) {
        this.handlers.put(slot, handler);
        return this;
    }

    /**
     * This method apply a new menu opening handler to the menu.
     * @param handler the new handler.
     * @return this menu after change.
     */
    public BaseMenu setMenuOpeningHandler(MenuOpeningHandler handler) {
        this.open = handler;
        return this;
    }

    /**
     * This method apply a new menu close handler to the menu.
     * @param handler the new handler.
     * @return this menu after change.
     */
    public BaseMenu setMenuCloseHandler(MenuCloseHandler handler) {
        this.close = handler;
        return this;
    }

    /**
     * @return all items of the menu.
     */
    public ItemStack[] getContents() {
        this.setup();
        return this.inv.getContents();
    }

    /**
     * Initialize the menu.
     */
    private void setup() {
        if (this.inv == null) {
            this.inv = Bukkit.createInventory((InventoryHolder)null, (int)Math.ceil((double)((float)this.items.size() / 9.0F)) * 9, this.title);

            for(int i = 0; i < this.items.size(); ++i) {
                this.inv.setItem(i, (ItemStack)this.items.get(i));
            }
        }
    }

    /**
     * Display the menu to players.
     * @param players targeted players.
     */
    public void display(Player... players) {
        this.setup();

        for(int i = 0; i < players.length; i++) {
            Player player = players[i];
            player.openInventory(this.inv);
            MenuListeners.menus.put(player.getUniqueId(), this);
            if (this.open != null) {
                this.open.onOpen(player);
            }
        }

    }

    /**
     * @param slot the slot.
     * @return the requested menu click handler.
     */
    public MenuClickHandler getMenuClickHandler(int slot) {
        return this.handlers.get(slot);
    }

    /**
     * @return the menu close handler.
     */
    public MenuCloseHandler getMenuCloseHandler() {
        return this.close;
    }

    /**
     * @return the menu opening handler.
     */
    public MenuOpeningHandler getMenuOpeningHandler() {
        return this.open;
    }

    /**
     * @return the player inventory click handler.
     */
    public MenuClickHandler getPlayerInventoryClickHandler() {
        return this.playerClick;
    }

    /**
     * @return this menu as an inventory.
     */
    public Inventory toInventory() {
        return this.inv;
    }

    /**
     * The 'on close' handler.
     */
    @FunctionalInterface
    public interface MenuCloseHandler {
        /**
         * Code to execute when the menu was closed by a player.
         * @param player the player who closed the menu.
         */
        void onClose(Player player);
    }

    /**
     * The 'on open' handler.
     */
    @FunctionalInterface
    public interface MenuOpeningHandler {
        /**
         * Code to execute when the menu displayed to player.
         * @param player the player who see the menu.
         */
        void onOpen(Player player);
    }

    /**
     * The 'on click' handler.
     */
    @FunctionalInterface
    public interface MenuClickHandler {
        /**
         * Code to execute when a slot clicked.
         * @param player the player who clicked.
         * @param slot the clicked slot.
         * @param item the item in slot.
         * @param clickType the type of click.
         * @return if the event must be cancelled.
         */
        boolean onClick(Player player, int slot, ItemStack item, ClickType clickType);
    }
}

