package fr.blockincraft.kingdoms.menu;

import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.util.Parameters;
import fr.blockincraft.kingdoms.util.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BankMenu extends ChestMenu {
    private final KingdomFullDTO kingdom;

    public BankMenu(KingdomFullDTO kingdom) {
        super("&e&lBanque", 3);

        this.kingdom = kingdom;

        setEmptySlotsClickable(false);
        setPlayerInventoryClickable(false);

        refreshMenu();
    }

    @Override
    public void refreshMenu() {
        ItemStack itemStack = new ItemStack(Material.GOLD_BLOCK);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lContenue"));

        meta.setLore(Arrays.asList(
                "",
                ChatColor.translateAlternateColorCodes('&', "&b" + TextUtils.valueWithCommas(kingdom.getBank()) + "/" + TextUtils.valueWithCommas(Parameters.maxMoneyPerBankLevel[kingdom.getBankLevel()]) + "$")
        ));

        itemStack.setItemMeta(meta);

        replaceExistingItem(13, itemStack);
        addMenuClickHandler(13, (p, slot, item, click) -> false);
    }
}
