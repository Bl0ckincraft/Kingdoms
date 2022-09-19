package fr.blockincraft.kingdoms.data;

import fr.blockincraft.kingdoms.menu.ChestMenu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuData {
    public static final Map<UUID, ChestMenu> menus = new HashMap<>();

    public static <C extends ChestMenu> Map<UUID, C> getAllMenuByType(Class<C> menuClass) {
        Map<UUID, C> menus = new HashMap<>();

        for (Map.Entry<UUID, ChestMenu> entry : MenuData.menus.entrySet()) {
            if (entry.getValue().getClass() == menuClass) {
                menus.put(entry.getKey(), (C) entry.getValue());
            }
        }

        return menus;
    }

    public static <C extends ChestMenu> void refreshAll(Class<C> menuClass) {
        Map<UUID, C> menus = getAllMenuByType(menuClass);

        for (C menu : menus.values()) {
            menu.refreshMenu();
        }
    }
}
