package fr.kellyan.kingdoms.configurations;

import fr.kellyan.kingdoms.FileHelper;
import fr.kellyan.kingdoms.commands.CmdExecutor;
import fr.kellyan.kingdoms.commands.KAction;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class contain all the translatable messages.
 */
public enum Lang {
    //Prefix :
    prefix("&b&9[Kingdoms] "),

    //Help message components :
    helpBar("&9<------- Kingdoms ------->"),
    commandPrefix("&9"),

    //Negative messages :
    nameSizeError("&cThe name must contain %min_name_length% to %max_name_length% characters!"),
    nameAlreadyUsed("&cThis name is already used!"),
    kingdomAlreadyExist("&cThis claim contains already a kingdom!"),
    youMustBeInAClaimToExecuteThisCommand("&cYou must be in a claim to execute this command!"),
    onlyPlayersCanExecuteThisCommand("&cOnly players can execute this command!"),
    haveNotClaimPermissionToDoThat("&cYou haven't the claim permission to do that, you must be trust!"),
    youMustBeInAKingdomToExecuteThisCommand("&cYou must be in a kingdom to execute this command!"),

    //Positive messages :
    kingdomCreate("&aYou been created a new Kingdom!"),
    kingdomDelete("&aYou been deleted the Kingdom '%name%'!"),

    //Words :
    name("name"),

    //Commands name :
    kCreate("create", Type.noSpace),
    kDelete("delete", Type.noSpace);

    private final Type type;
    private final String defaultValue;
    private String value;

    /**
     * The default constructor of the enum.
     * @param defaultValue the default translation of the message.
     * @param type the type of message.
     */
    Lang(String defaultValue, Type type) {
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.type = type;
    }

    /**
     * The little constructor of the enum.
     * @param defaultValue the default translation of the message.
     */
    Lang(String defaultValue) {
        this(defaultValue, Type.normal);
    }

    /**
     * Convert the name to the id. Example : myTest -> my_test.
     * @return the id of the message.
     */
    public String getId() {
        char[] nameChars = this.name().toCharArray();
        String id = "";
        for (char nameChar : nameChars) {
            if (Character.isUpperCase(nameChar)) {
                id += "_";
            }
            id += nameChar;
        }
        id.toLowerCase(Locale.ROOT);
        return id;
    }

    /**
     * @return the translation of the messages.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * This method send a message with the prefix and the color translation.
     * @param receiver the receiver of the message.
     * @param messages the messages to send.
     */
    public static void sendMessage(CommandSender receiver, String... messages) {
        for (String message : messages) {
            String editedMessage = ChatColor.translateAlternateColorCodes('&', prefix + Settings.replaceValues(message));
            receiver.sendMessage(receiver instanceof Player ? editedMessage : ChatColor.stripColor(editedMessage));
        }
    }

    /**
     * Send the help message to a user without the plugin prefix.
     * @param receiver the receiver of the message.
     */
    public static void sendHelpMessage(CommandSender receiver) {
        //Build the message parts.
        List<String> helpParts = new ArrayList<>();
        helpParts.add(helpBar.toString());
        for (KAction action : CmdExecutor.actions.values()) {
            helpParts.add(commandPrefix + action.getCommandHasHelp());
        }
        helpParts.add(helpBar.toString());

        //Send the message parts.
        for (String part : helpParts) {
            String editedPart = ChatColor.translateAlternateColorCodes('&', part);
            receiver.sendMessage(receiver instanceof Player ? editedPart : ChatColor.stripColor(editedPart));
        }
    }

    /**
     * This method load all the messages from the lang.yml file.
     */
    public static void loadMessages() {
        FileConfiguration config = FileHelper.getLangConfig();
        for (Lang message : Lang.values()) {
            String value = config.getString(message.getId(), message.defaultValue);
            if (message.type == Type.noSpace && value.contains(" ")) {

            } else {
                message.value = value;
            }
        }
    }

    /**
     * This is the types of the messages. It refers to requirements example : command -> no space and lower
     * case.
     */
    private static enum Type {
        normal,
        noSpace
    }
}
