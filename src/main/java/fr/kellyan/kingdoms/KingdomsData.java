package fr.kellyan.kingdoms;

import fr.kellyan.kingdoms.configurations.Lang;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * This class store all the data about the kingdoms.
 */
public class KingdomsData {
    private final List<Kingdom> kingdoms = new ArrayList<>();

    /**
     * @return all the registered kingdoms as an array.
     */
    public List<Kingdom> getKingdoms() {
        return kingdoms;
    }

    /**
     * Remove/delete a kingdom.
     * @param kingdom the kingdom to delete.
     */
    public void deleteKingdom(Kingdom kingdom) {
        if (kingdoms.contains(kingdom)) kingdoms.remove(kingdom);
        kingdom.delete();
    }

    /**
     * Remove all the kingdoms of a claim.
     * @param claim the claim.
     */
    public void deleteKingdomInClaim(Claim claim) {
        kingdoms.forEach(kingdom -> {
            if (kingdom.getClaim() == claim) {
                deleteKingdom(kingdom);
            }
        });
    }

    /**
     * Get the kingdom in the specified claim.
     * @param claim the claim.
     * @return the kingdom in this claim.
     */
    public Kingdom getKingdomInClaim(Claim claim) {
        for (Kingdom kingdom : kingdoms) {
            if (kingdom.getClaim() == claim) {
                return kingdom;
            }
        }
        return null;
    }

    /**
     * This method try to create a claim and register it. It also checks if the name and the claim are already used
     * or not and send an information message if the creator wasn't null.
     * @param name the name of the kingdom.
     * @param claim the claim of the kingdom.
     * @param creator the entity which will receive the messages.
     * @return the new kingdom or null if an error was encountered.
     */
    public Kingdom createKingdom(String name, Claim claim, CommandSender creator) {
        //Check if the values are valid.
        if (name == null || claim == null) {
            return null;
        }
        boolean sendMessage = creator != null;
        if (name.length() < 3 || name.length() > 30) {
            if (sendMessage) Lang.sendMessage(creator, Lang.nameSizeError.toString());
            return null;
        }

        //Check if the values are already used.
        for (Kingdom kingdom : kingdoms) {
            if (kingdom.getClaim() == claim) {
                if (sendMessage) Lang.sendMessage(creator, Lang.kingdomAlreadyExist.toString());
                return null;
            } else if (kingdom.getName() == name) {
                if (sendMessage) Lang.sendMessage(creator, Lang.nameAlreadyUsed.toString());
                return null;
            }
        }

        //Finally, create and register the kingdom.
        Kingdom kingdom = new Kingdom(name, claim);
        kingdoms.add(kingdom);
        if (sendMessage) Lang.sendMessage(creator, Lang.kingdomCreate.toString());
        return kingdom;
    }
}
