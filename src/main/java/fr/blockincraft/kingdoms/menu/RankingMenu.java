package fr.blockincraft.kingdoms.menu;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class RankingMenu extends ChestMenu {
    private static final Registry registry = Kingdoms.getInstance().getRegistry();

    public RankingMenu() {
        super(Lang.RANKING_MENU_TITLE.get(), 3);

        this.setEmptySlotsClickable(false);
        this.setPlayerInventoryClickable(false);

        refreshMenu();
    }

    @Override
    public void refreshMenu() {
        List<KingdomFullDTO> ranking = registry.getRanking();
        Map<Integer, ItemStack> rankingItems = new HashMap<>();

        for (int i = 0; i < ranking.size() && i < 10; i++) {
            KingdomFullDTO kingdom = ranking.get(i);
            if (Bukkit.getPlayer(kingdom.getOwner()) != null) {
                ItemStack item = getPlayerHead(Bukkit.getPlayer(kingdom.getOwner()));

                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Lang.RANKING_MENU_ITEM_NAME.get()
                        .replace("<rank>", String.valueOf(i + 1))
                        .replace("<kingdom_name>", kingdom.getName())
                        .replace("<members>", String.valueOf(kingdom.getMembers().size()))
                        .replace("<size>", String.valueOf(kingdom.getClaim().getArea().getSize2D()))
                        .replace("<bank>", String.valueOf(kingdom.getBank()))
                        .replace("<points>", String.valueOf(kingdom.getRankingPoints()))
                        .replace("<level>", Lang.getFrom(kingdom.getKingdomLevel()))
                        .replace("<owner>", Bukkit.getPlayer(kingdom.getOwner()).getDisplayName())));
                meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', Lang.RANKING_MENU_ITEM_LORE.get()
                        .replace("<rank>", String.valueOf(i + 1))
                        .replace("<kingdom_name>", kingdom.getName())
                        .replace("<members>", String.valueOf(kingdom.getMembers().size()))
                        .replace("<size>", String.valueOf(kingdom.getClaim().getArea().getSize2D()))
                        .replace("<bank>", String.valueOf(kingdom.getBank()))
                        .replace("<points>", String.valueOf(kingdom.getRankingPoints()))
                        .replace("<level>", Lang.getFrom(kingdom.getKingdomLevel()))
                        .replace("<owner>", Bukkit.getPlayer(kingdom.getOwner()).getDisplayName()))
                        .split("\\|")));
                item.setItemMeta(meta);

                rankingItems.put(i + 1, item);
            }
        }

        if (rankingItems.containsKey(1)) {
            this.addItem(4, rankingItems.get(1), (p, slot, item, click) -> {
                return false;
            });
        }
        if (rankingItems.containsKey(2)) {
            this.addItem(12, rankingItems.get(2), (p, slot, item, click) -> {
                return false;
            });
        }
        if (rankingItems.containsKey(3)) {
            this.addItem(14, rankingItems.get(3), (p, slot, item, click) -> {
                return false;
            });
        }
        if (rankingItems.containsKey(4)) {
            this.addItem(19, rankingItems.get(4), (p, slot, item, click) -> {
                return false;
            });
        }
        if (rankingItems.containsKey(5)) {
            this.addItem(20, rankingItems.get(5), (p, slot, item, click) -> {
                return false;
            });
        }
        if (rankingItems.containsKey(6)) {
            this.addItem(21, rankingItems.get(6), (p, slot, item, click) -> {
                return false;
            });
        }
        if (rankingItems.containsKey(7)) {
            this.addItem(22, rankingItems.get(7), (p, slot, item, click) -> {
                return false;
            });
        }
        if (rankingItems.containsKey(8)) {
            this.addItem(23, rankingItems.get(8), (p, slot, item, click) -> {
                return false;
            });
        }
        if (rankingItems.containsKey(9)) {
            this.addItem(24, rankingItems.get(9), (p, slot, item, click) -> {
                return false;
            });
        }
        if (rankingItems.containsKey(10)) {
            this.addItem(25, rankingItems.get(10), (p, slot, item, click) -> {
                return false;
            });
        }
    }

    private ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwningPlayer(player);
        stack.setItemMeta(meta);

        return stack;
    }
}
