package fr.blockincraft.kingdoms.menu;

import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.enums.Collections;
import fr.blockincraft.kingdoms.util.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionMenu extends ChestMenu {
    private final ChestMenu from;
    private final KingdomFullDTO kingdom;
    private final Collections collection;

    public CollectionMenu(ChestMenu from, KingdomFullDTO kingdom, Collections collection) {
        super(collection.name, 3);

        this.from = from;
        this.kingdom = kingdom;
        this.collection = collection;

        setEmptySlotsClickable(false);
        setPlayerInventoryClickable(false);

        refreshMenu();
    }

    @Override
    public void refreshMenu() {
        for (int i = 0; i < 27; i++) {
            addMenuClickHandler(i, (p, slot, item, click) -> false);
        }

        for (int i = 0; i < 7; i++) {
            if (collection.rewards.length <= i) break;

            Integer value = kingdom.getCollections().get(collection);
            int collected = value == null ? 0 : value;

            Collections.Reward reward = collection.rewards[i];
            boolean complete = reward.amount() <= collected;
            int state = complete ? 0 : (i == 0 || collection.rewards[i - 1].amount() <= collected) ? 1 : 2;

            ItemStack itemStack = new ItemStack(state == 0 ? Material.LIME_STAINED_GLASS_PANE : state == 1 ? Material.ORANGE_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = itemStack.getItemMeta();

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&" + switch (state) {
                case 0 -> "a";
                case 1 -> "6";
                default -> "c";
            } + "Palier " + (i + 1)));

            List<String> lore = new ArrayList<>(List.of(""));

            if (reward.points() > 0) {
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7Récompenses:"));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7 - " + reward.points() + " points de classements"));
                lore.add("");
            }

            lore.add(ChatColor.translateAlternateColorCodes('&', "&7" + TextUtils.valueWithCommas(collected) + "/" + TextUtils.valueWithCommas(reward.amount())));

            meta.setLore(lore);

            itemStack.setItemMeta(meta);
            this.replaceExistingItem(10 + i, itemStack);
            this.addMenuClickHandler(10 + i, (p, slot, item, click) -> false);
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
