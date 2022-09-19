package fr.blockincraft.kingdoms.listeners;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.Registry;
import fr.blockincraft.kingdoms.core.dto.CurrentCommissionDTO;
import fr.blockincraft.kingdoms.core.dto.KingdomFullDTO;
import fr.blockincraft.kingdoms.core.enums.Commissions;
import fr.blockincraft.kingdoms.util.Lang;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CommissionsListener implements Listener {
    private static final Registry registry = Kingdoms.getInstance().getRegistry();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerKillEntity(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        if (killer != null) {
            KingdomFullDTO playerKingdom = registry.getPlayerKingdom(killer.getUniqueId());
            if (playerKingdom != null) {
                CurrentCommissionDTO cc = playerKingdom.getCurrentCommissions().get(killer.getUniqueId());
                if (cc != null && cc.getCommission().type == Commissions.CommissionTypes.KILL) {
                    if (cc.getCommission().countForThis(entity.getType())) {
                        cc.setProgression(cc.getProgression() + 1);

                        if (cc.getProgression() >= cc.getObjective()) {
                            killer.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.COMPLETE_COMMISSION.get()
                                            .replace("<commission_name>", Lang.getFrom(cc.getCommission())))
                                    .split("\\|"));

                            int completed = playerKingdom.getCompletedCommissions().get(killer.getUniqueId()) + 1;

                            cc.setProgression(0);
                            cc.setCommission(Commissions.getRandom());
                            cc.setObjective(cc.getCommission().objective.calculation(completed));

                            playerKingdom.getCompletedCommissions().put(killer.getUniqueId(), completed);
                            registry.updateKingdom(playerKingdom);
                        }

                        registry.updateKingdom(playerKingdom);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        ItemStack tool = player.getInventory().getItem(EquipmentSlot.HAND);

        if (tool == null || !tool.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
            KingdomFullDTO playerKingdom = registry.getPlayerKingdom(player.getUniqueId());
            if (playerKingdom != null) {
                CurrentCommissionDTO cc = playerKingdom.getCurrentCommissions().get(player.getUniqueId());
                if (cc != null && cc.getCommission().type == Commissions.CommissionTypes.MINE) {
                    if (cc.getCommission().countForThis(block.getType())) {
                        cc.setProgression(cc.getProgression() + 1);

                        if (cc.getProgression() >= cc.getObjective()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Lang.COMPLETE_COMMISSION.get()
                                            .replace("<commission_name>", Lang.getFrom(cc.getCommission())))
                                    .split("\\|"));

                            int completed = playerKingdom.getCompletedCommissions().get(player.getUniqueId()) + 1;

                            cc.setProgression(0);
                            cc.setCommission(Commissions.getRandom());
                            cc.setObjective(cc.getCommission().objective.calculation(completed));

                            playerKingdom.getCompletedCommissions().put(player.getUniqueId(), completed);
                            registry.updateKingdom(playerKingdom);
                        }

                        registry.updateKingdom(playerKingdom);
                    }
                }
            }
        }
    }
}
