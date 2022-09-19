package fr.blockincraft.kingdoms.menu;

import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.enums.Collections;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class CollectionsMenu extends ChestMenu {
    private final ChestMenu from;
    private final KingdomFullDTO kingdom;
    private final int level;

    public CollectionsMenu(ChestMenu from, KingdomFullDTO kingdom, int level, String name) {
        super("&6&lCollections " + name, 3);

        this.from = from;
        this.kingdom = kingdom;
        this.level = level;

        setEmptySlotsClickable(false);
        setPlayerInventoryClickable(false);

        refreshMenu();
    }

    @Override
    public void refreshMenu() {
        for (int i = 0; i < 27; i++) {
            addMenuClickHandler(i, (p, slot, item, click) -> false);
        }

        List<Collections> collections = Arrays.stream(Collections.values()).filter(collection -> collection.level == level).toList();

        for (int i = 0; i < 5; i++) {
            if (collections.size() <= i) break;

            Collections col = collections.get(i);
            Integer value = kingdom.getCollections().get(col);
            int collected = value == null ? 0 : value;

            this.replaceExistingItem(11 + i, col.getForDisplay(collected));
            this.addMenuClickHandler(11 + i, (p, slot, item, click) -> {
                new CollectionMenu(this, kingdom, col).open(p);
                return false;
            });
        }

        if (from != null) {
            ItemStack returnItem = new ItemStack(Material.BARRIER);
            ItemMeta meta = returnItem.getItemMeta();

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lRetour"));
            meta.setLore(Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&', "&8Clique ici pour retourner"),
                    ChatColor.translateAlternateColorCodes('&', "&8en arrière.")
            ));

            returnItem.setItemMeta(meta);
            this.replaceExistingItem(22, returnItem);
            this.addMenuClickHandler(22, (p, slot, item, click) -> {
                from.open(p);
                return false;
            });
        }
    }
}
