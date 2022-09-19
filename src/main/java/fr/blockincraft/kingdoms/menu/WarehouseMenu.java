package fr.blockincraft.kingdoms.menu;

import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.enums.Constructions;
import fr.blockincraft.kingdoms.util.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class WarehouseMenu extends ChestMenu {
    private final KingdomFullDTO kingdom;

    public WarehouseMenu(KingdomFullDTO kingdom) {
        super("&6" + Lang.getFrom(Constructions.WAREHOUSE), 3);

        this.kingdom = kingdom;

        setEmptySlotsClickable(false);
        setPlayerInventoryClickable(false);

        refreshMenu();
    }

    @Override
    public void refreshMenu() {
        for (int i = 0; i < 27; i++) {
            addMenuClickHandler(i, (p, slot, item, click) -> false);
        }

        ItemStack firstItemStack = new ItemStack(Material.OAK_PLANKS);
        ItemMeta fMeta = firstItemStack.getItemMeta();

        fMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lCollections Basiques"));
        fMeta.setLore(Arrays.asList(
                "",
                ChatColor.translateAlternateColorCodes('&', kingdom.getWarehouseLevel() >= 1 ? "&bClique pour les affichées" : "&8Pas encore débloquées")
        ));

        firstItemStack.setItemMeta(fMeta);
        this.replaceExistingItem(10, firstItemStack);
        this.addMenuClickHandler(10, (p, slot, item, click) -> {
           if (kingdom.getWarehouseLevel() >= 1) {
                new CollectionsMenu(this, kingdom, 1, "Basiques").open(p);
           }

           return false;
        });

        ItemStack secondItemStack = new ItemStack(Material.IRON_ORE);
        ItemMeta sMeta = secondItemStack.getItemMeta();

        sMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lCollections Avancées"));
        sMeta.setLore(Arrays.asList(
                "",
                ChatColor.translateAlternateColorCodes('&', kingdom.getWarehouseLevel() >= 2 ? "&bClique pour les affichées" : "&8Pas encore débloquées")
        ));

        secondItemStack.setItemMeta(sMeta);
        this.replaceExistingItem(13, secondItemStack);
        this.addMenuClickHandler(13, (p, slot, item, click) -> {
            if (kingdom.getWarehouseLevel() >= 2) {
                new CollectionsMenu(this, kingdom, 2, "Avancées").open(p);
            }

            return false;
        });

        ItemStack thirdItemStack = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta tMeta = thirdItemStack.getItemMeta();

        tMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lCollections Expertes"));
        tMeta.setLore(Arrays.asList(
                "",
                ChatColor.translateAlternateColorCodes('&', kingdom.getWarehouseLevel() >= 3 ? "&bClique pour les affichées" : "&8Pas encore débloquées")
        ));

        thirdItemStack.setItemMeta(tMeta);
        this.replaceExistingItem(16, thirdItemStack);
        this.addMenuClickHandler(16, (p, slot, item, click) -> {
            if (kingdom.getWarehouseLevel() >= 3) {
                new CollectionsMenu(this, kingdom, 3, "Expertes").open(p);
            }

            return false;
        });
    }
}
