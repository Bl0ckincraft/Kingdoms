package fr.blockincraft.kingdoms.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Parameters {
    public static final int[] maxMoneyPerBankLevel = new int[]{0, 100000, 1000000, 5000000};
    public static final int personalClaimBlocks = 4096;
    public static final int minPersonalClaimSize = 25;
    public static final int kingdomClaimBlocks = 16384;
    public static final int minKingdomClaimSize = 100;
    public static final int maxNameLength = 40;
    public static final int minNameLength = 3;
    public static final ItemStack defaultMenuItem;

    static {
        defaultMenuItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta dmiMeta = defaultMenuItem.getItemMeta();
        dmiMeta.setDisplayName(" ");
        defaultMenuItem.setItemMeta(dmiMeta);
    }
}
