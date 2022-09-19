package fr.blockincraft.kingdoms.menu;

import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.enums.Constructions;
import fr.blockincraft.kingdoms.util.Lang;
import fr.blockincraft.kingdoms.util.Parameters;
import fr.blockincraft.kingdoms.util.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KingdomMenu extends ChestMenu {
    private final KingdomFullDTO kingdom;

    public KingdomMenu(KingdomFullDTO kingdom) {
        super(ChatColor.translateAlternateColorCodes('&', kingdom.getName()), 3);

        this.kingdom = kingdom;

        setEmptySlotsClickable(false);
        setPlayerInventoryClickable(false);

        refreshMenu();
    }

    @Override
    public void refreshMenu() {
        replaceExistingItem(10, getConstructionsItem());
        addMenuClickHandler(10, (p, slot, item, click) -> false);
        replaceExistingItem(11, getFeaturesItem());
        addMenuClickHandler(11, (p, slot, item, click) -> false);

        replaceExistingItem(13, getInfoItem());
        addMenuClickHandler(13, (p, slot, item, click) -> false);

        replaceExistingItem(15, getPenaltyItem());
        addMenuClickHandler(15, (p, slot, item, click) -> false);
        replaceExistingItem(16, getCommissionsItem());
        addMenuClickHandler(16, (p, slot, item, click) -> false);
    }

    public ItemStack getConstructionsItem() {
        ItemStack itemStack = new ItemStack(Material.STONE_BRICKS);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lConstructions"));

        List<String> lore = new ArrayList<>(List.of(""));

        lore.addAll(Arrays.stream(Constructions.values()).map(construction -> {
            return ChatColor.translateAlternateColorCodes('&', "&b" + kingdom.getConstructionAmount(construction) + " " + Lang.getFrom(construction));
        }).toList());

        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public ItemStack getFeaturesItem() {
        ItemStack itemStack = new ItemStack(Material.CHEST);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lContenue"));

        int bankLevel = kingdom.getBankLevel();
        int warehouseLevel = kingdom.getWarehouseLevel();

        String collectionsText = switch (warehouseLevel) {
            case 1 -> "Basiques";
            case 2 -> "Avancées";
            case 3 -> "Expertes";
            default -> "Bloquées";
        };

        meta.setLore(Arrays.asList(
                "",
                ChatColor.translateAlternateColorCodes('&', "&bArgent: " + TextUtils.valueWithCommas(kingdom.getBank()) + "/" + TextUtils.valueWithCommas(Parameters.maxMoneyPerBankLevel[bankLevel])) + "$",
                ChatColor.translateAlternateColorCodes('&', "&bCollections: " + collectionsText)
        ));

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public ItemStack getInfoItem() {
        ItemStack itemStack = new ItemStack(Material.ARMOR_STAND);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lInformations"));

        meta.setLore(Arrays.asList(
                "",
                ChatColor.translateAlternateColorCodes('&', "&bNiveau: " + Lang.getFrom(kingdom.getKingdomLevel())),
                ChatColor.translateAlternateColorCodes('&', "&bPoints: " + kingdom.getRankingPoints()),
                ChatColor.translateAlternateColorCodes('&', "&bMembres: " + kingdom.getMembers().size()),
                ChatColor.translateAlternateColorCodes('&', "&bTaille: " + TextUtils.valueWithCommas(kingdom.getClaim().getArea().getSize2D()))
        ));

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public ItemStack getPenaltyItem() {
        ItemStack itemStack = new ItemStack(Material.IRON_BARS);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lPénalité"));

        LocalDate last = kingdom.getLastPenalty();

        meta.setLore(Arrays.asList(
                "",
                ChatColor.translateAlternateColorCodes('&', "&bPenalité: " + kingdom.getPenalty()),
                ChatColor.translateAlternateColorCodes('&', "&bRaison: " + kingdom.getPenaltyReason()),
                ChatColor.translateAlternateColorCodes('&', "&bDate: " + last.getDayOfMonth() + "/" + last.getMonthValue() + "/" + last.getYear())
        ));

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public ItemStack getCommissionsItem() {
        ItemStack itemStack = new ItemStack(Material.BOOK);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lCommissions"));

        int completed = 0;

        for (Integer c : kingdom.getCompletedCommissions().values()) {
            completed += c;
        }

        meta.setLore(Arrays.asList(
                "",
                ChatColor.translateAlternateColorCodes('&', "&bAccomplies: " + TextUtils.valueWithCommas(completed))
        ));

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
