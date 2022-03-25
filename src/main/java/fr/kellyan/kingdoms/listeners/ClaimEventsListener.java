package fr.kellyan.kingdoms.listeners;

import fr.kellyan.kingdoms.Kingdoms;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * This class contains all the listened events about the claims of GriefPrevention.
 */
public class ClaimEventsListener implements Listener {
    /**
     * Delete the kingdom in a claim when it was deleted.
     * @param e the claim event.
     */
    @EventHandler
    public void onClaimDelete(ClaimDeletedEvent e) {
        Kingdoms.getInstance().getData().deleteKingdomInClaim(e.getClaim());
    }
}
