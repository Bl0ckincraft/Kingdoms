package fr.blockincraft.kingdoms.menu;

import fr.blockincraft.kingdoms.Kingdoms;
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

public class TeleportMenu extends ChestMenu {
    private List<KingdomFullDTO> kingdoms;
    private int page;
    private int pages;

    public TeleportMenu() {
        super(Lang.TELEPORT_MENU_TITLE.get(), 1);

        this.setEmptySlotsClickable(false);
        this.setPlayerInventoryClickable(false);

        kingdoms = Kingdoms.getInstance().getRegistry().getKingdoms();
        pages = (int) Math.floor(kingdoms.size() / 9.0 / 5.0) + 1;
        page = 1;

        refreshMenu();
    }

    public List<KingdomFullDTO> getPageKingdoms() {
        List<KingdomFullDTO> pageKingdoms = new ArrayList<>();

        for (int i = 45 * (page - 1); i < 45 * (page) && i < kingdoms.size(); i++) {
            pageKingdoms.add(kingdoms.get(i));
        }

        return pageKingdoms;
    }

    @Override
    public void refreshMenu() {
        if (kingdoms == null || page == 0 || pages == 0) {
            return;
        }

        List<KingdomFullDTO> pageKingdoms = getPageKingdoms();

        for (int i = 0; i < pageKingdoms.size(); i++) {
            KingdomFullDTO kingdom = pageKingdoms.get(i);

            if (Bukkit.getPlayer(kingdom.getOwner()) != null) {
                ItemStack item = getPlayerHead(Bukkit.getPlayer(kingdom.getOwner()));

                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Lang.TELEPORT_MENU_ITEM_NAME.get()
                        .replace("<rank>", String.valueOf(i + 1))
                        .replace("<kingdom_name>", kingdom.getName())
                        .replace("<members>", String.valueOf(kingdom.getMembers().size()))
                        .replace("<size>", String.valueOf(kingdom.getClaim().getArea().getSize2D()))
                        .replace("<bank>", String.valueOf(kingdom.getBank()))
                        .replace("<points>", String.valueOf(kingdom.getRankingPoints()))
                        .replace("<level>", Lang.getFrom(kingdom.getKingdomLevel()))
                        .replace("<owner>", Bukkit.getPlayer(kingdom.getOwner()).getDisplayName())));
                meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', Lang.TELEPORT_MENU_ITEM_LORE.get()
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

                this.addItem(i, item, (p, slot, item2, click) -> {
                    if (kingdom.getHomeAsBukkitLoc() != null) {
                        p.teleport(kingdom.getHomeAsBukkitLoc());
                    }
                    return false;
                });
            }
        }

        int kingdomsLines = (int) Math.floor(pageKingdoms.size() / 9) + 1;

        ItemStack previousPageItem = new ItemStack(Material.PAPER);

        ItemMeta ppMeta = previousPageItem.getItemMeta();
        ppMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Lang.PREVIOUS_PAGE_ITEM_NAME.get()
                .replace("<page>", String.valueOf(page))
                .replace("<pages", String.valueOf(pages))));
        ppMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', Lang.PREVIOUS_PAGE_ITEM_DESC.get()
                        .replace("<page>", String.valueOf(page))
                        .replace("<pages>", String.valueOf(pages)))
                .split("\\|")));
        previousPageItem.setItemMeta(ppMeta);

        this.addItem(kingdomsLines * 9 + 3, previousPageItem, (p, slot, item, click) -> {
            if (page > 1) {
                this.page--;
                this.refreshMenu();
            }
            return false;
        });

        ItemStack nextPageItem = new ItemStack(Material.PAPER);

        ItemMeta npMeta = nextPageItem.getItemMeta();
        npMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Lang.NEXT_PAGE_ITEM_NAME.get()
                .replace("<page>", String.valueOf(page))
                .replace("<pages", String.valueOf(pages))));
        npMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', Lang.NEXT_PAGE_ITEM_DESC.get()
                        .replace("<page>", String.valueOf(page))
                        .replace("<pages>", String.valueOf(pages)))
                .split("\\|")));
        nextPageItem.setItemMeta(npMeta);

        this.addItem(kingdomsLines * 9 + 5, nextPageItem, (p, slot, item, click) -> {
            if (page < pages) {
                this.page++;
                this.refreshMenu();
            }
            return false;
        });
    }

    private ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwningPlayer(player);
        stack.setItemMeta(meta);

        return stack;
    }
}
