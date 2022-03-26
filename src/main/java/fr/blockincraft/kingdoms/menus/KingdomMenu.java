package fr.blockincraft.kingdoms.menus;

import fr.blockincraft.kingdoms.Kingdom;

/**
 * A simple menu with contain a kingdom.
 */
public abstract class KingdomMenu extends BaseMenu {
    protected final Kingdom kingdom;

    /**
     * The constructor of the class.
     * @param title the title of the menu.
     * @param kingdom the kingdom associated to menu.
     */
    public KingdomMenu(String title, Kingdom kingdom) {
        super(title);
        this.kingdom = kingdom;
    }

    /**
     * @return the kingdom.
     */
    public Kingdom getKingdom() {
        return kingdom;
    }

    /**
     * This method has for aim to actualize items in GUI.
     */
    public abstract void actualize();
}
