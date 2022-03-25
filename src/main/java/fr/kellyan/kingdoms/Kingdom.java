package fr.kellyan.kingdoms;

import me.ryanhamshire.GriefPrevention.Claim;

public class Kingdom {
    private String name;
    private final Claim claim;

    /**
     * The constructor of the class.
     * @param name the name of the kingdom.
     * @param claim the claim of the kingdom.
     */
    public Kingdom(String name, Claim claim) {
        this.name = name;
        this.claim = claim;
    }

    /**
     * La le but c'est que quand on supprime le royaume, l'argent qui sera dans la banque du royaume soit donnée au propriétaire mais j'ai pas encore fais la banque.
     */
    public void delete() {

    }

    /**
     * @return the claim of the kingdom.
     */
    public Claim getClaim() {
        return claim;
    }

    /**
     * @return the name of the kingdom.
     */
    public String getName() {
        return name;
    }

    /**
     * This method allow you to rename the kingdom.
     * @param name the new name.
     */
    public void setName(String name) {
        this.name = name;
    }
}
