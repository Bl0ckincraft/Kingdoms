package fr.blockincraft.kingdoms.util;

import fr.blockincraft.kingdoms.Kingdoms;
import fr.blockincraft.kingdoms.core.enums.Commissions;
import fr.blockincraft.kingdoms.core.enums.Constructions;
import fr.blockincraft.kingdoms.core.enums.KingdomLevels;
import fr.blockincraft.kingdoms.core.enums.KingdomPermissionLevels;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Locale;

public enum Lang {
    /**<b><font color="red" size=6>ALL - ERROR</font></b>*/
    ALL_ERROR("", ""),
    ONLY_PLAYERS_CAN_EXECUTE_THIS_COMMAND("commands.only_players_can_execute_this_command", "&9Kingdoms >> &cOnly players can execute this command"),
    BAD_COMMAND_USAGE("commands.bad_command_usage", "&9Kingdoms >> &cInvalid command usage. Type '/<command> help' to know how to use it."),
    A_CLAIM_IS_ALREADY_IN_AREA("commands.a_claim_is_in_the_area", "&9Kingdoms >> &cThis area already contain at least a claim."),
    AREA_CONTAIN_A_PROTECTED_REGION("commands.area_contain_a_protected_region", "&9Kingdoms >> &cThis area contain at least a protected region."),
    /**
     * Params: <br/>
     * size = size of the selected area <br/>
     * max_size = maximum size required
     **/
    AREA_IS_TOO_BIG("commands.area_is_too_big", "&9Kingdoms >> &cThis area is to big. <size> blocks instead of <max_size> blocks (max)."),
    /**
     * Params: <br/>
     * size = size of the selected area <br/>
     * min_size = minimum size required
     */
    AREA_IS_TOO_SMALL("commands.area_is_too_small", "&9Kingdoms >> &cThis area is to small. <size> blocks instead of <min_size> blocks (min)."),
    INVALID_NUMBER("commands.invalid_number", "&9Kingdoms >> &cYou send an invalid number."),
    /**
     * Params: <br/>
     * player_name = name of the unknown player
     */
    UNKNOWN_PLAYER("commands.unknown_player", "&9Kingdoms >> &cUnknown player '<player_name>&c'."),
    YOU_DO_NOT_HAVE_PERMISSION_TO_DO_THAT("commands.no_permission_to_do_that", "&9Kingdoms >> &cYou don't have permission to do that."),
    UNEXPECTED_ERROR_OCCURRED("commands.unexpected_error_occurred", "&9Kingdoms >> &cAn unexpected error occurred!"),

    /**<b><font color="aqua" size=6>ALL - INFO</font></b>*/
    ALL_INFO("", ""),
    /**
     * Params: <br/>
     * command = command name to send (ex: /<command> delete confirm : /kingdoms delete confirm)
     */
    SEND_DELETE_CONFIRM("commands.send_delete_confirm", "&9Kingdoms >> &bSend '/<command> delete confirm' to confirm."),

    /**<b><font color="aqua" size=6>AREAS COMMAND MESSAGES - INFO</font></b>*/
    AREAS_COMMAND_MESSAGES_INFO("", ""),
    AREA_HELP_MESSAGE("commands.areas.help_message", "&9Kingdoms >> &bAll informations are on our wiki: &n&ohttp://minexplore.fr/wiki/areas"),
    /**
     * Params: <br/>
     * size_2d = size of the area in two dimensions (x * z) <br/>
     * size_3d = size of the area in three dimensions (x * y * z) <br/>
     * x1 = x coordinate of the first position <br/>
     * y1 = y coordinate of the first position <br/>
     * z1 = z coordinate of the first position <br/>
     * x2 = x coordinate of the second position <br/>
     * y2 = y coordinate of the second position <br/>
     * z2 = z coordinate of the second position
     */
    AREA_INFO_MESSAGE("commands.areas.info_message",
            "&9&l&m---------------------------------------------|" +
                    "&9&lYour Selection|" +
                    "&bSize: <size_2d> blocks (2D) <size_3d> blocks (3D)|" +
                    "&bFirst pos: <x1>, <y1>, <z1>|" +
                    "&bSecond pos: <x2>, <y2>, <z2>|" +
                    "&9&l&m---------------------------------------------"),

    /**<b><font color="lime" size=6>AREAS COMMAND MESSAGES - SUCCESS</font></b>*/
    AREAS_COMMAND_MESSAGES_SUCCESS("", ""),
    SUCCESS_SET_POS_ONE("commands.areas.success_set_pos_one", "&9Kingdoms >> &aYou have been set the first pos."),
    SUCCESS_SET_POS_TWO("commands.areas.success_set_pos_two", "&9Kingdoms >> &aYou have been set the second pos."),
    SUCCESS_TOGGLE_VIEW_TO_TRUE("commands.areas.success_toggle_view_to_true", "&9Kingdoms >> &aYou have been enable the view."),
    SUCCESS_TOGGLE_VIEW_TO_FALSE("commands.areas.success_toggle_view_to_false", "&9Kingdoms >> &aYou have been disable the view."),

    /**<b><font color="red" size=6>PERSONAL CLAIMS COMMAND MESSAGES - ERROR</font></b>*/
    PERSONAL_CLAIMS_COMMAND_MESSAGES_ERROR("", ""),
    ALREADY_HAVE_A_CLAIM("commands.claims.already_have_a_claim", "&9Kingdoms >> &cYou have already a claim."),
    NEED_TO_SELECT_A_AREA_TO_CLAIM("commands.claims.need_to_select_area_to_claim", "&9Kingdoms >> &cIn order to create a claim, you need to select an area with the command '/areas'."),
    DO_NOT_HAVE_A_CLAIM("commands.claims.do_not_have_a_claim", "&9Kingdoms >> &cYou don't have a claim."),
    INVALID_CLAIM_DIRECTION("commands.claims.invalid_direction", "&9Kingdoms >> &cThis direction isn't valid use 'north', 'south', 'east' or 'west'."),
    CANNOT_TRUST_YOURSELF("commands.claims.cannot_trust_yourself", "&9Kingdoms >> &cYou can't trust yourself."),
    /**
     * Params: <br/>
     * player_name = name of the player which we try to trust
     */
    PLAYER_ALREADY_TRUST("commands.claims.player_already_trust", "&9Kingdoms >> &c<player_name>&c is already trust."),
    CANNOT_UNTRUST_YOURSELF("commands.claims.cannot_untrust_yourself", "&9Kingdoms >> &cYou can't untrust yourself."),
    /**
     * Params: <br/>
     * player_name = name of the player which we try to untrust
     */
    PLAYER_WAS_NOT_TRUST("commands.claims.player_was_not_trust", "&9Kingdoms >> &c<player_name&c isn't trust."),
    TRUST_IN_NO_CLAIM("commands.claims.trust_in_no_claim", "&9Kingdoms >> &cYou are trust in no claim."),

    /**<b><font color="aqua" size=6>PERSONAL CLAIMS COMMAND MESSAGES - INFO</font></b>*/
    PERSONAL_CLAIMS_COMMAND_MESSAGES_INFO("", ""),
    CLAIM_HELP_MESSAGE("commands.claims.help_message", "&9Kingdoms >> &bAll informations are on our wiki: &n&ohttp://minexplore.fr/wiki/claim."),
    /**
     * Params: <br/>
     * player_name = name of the player which trusted the receiver
     */
    PLAYER_TRUST_YOU("commands.claims.player_trust_you", "&9Kingdoms >> &b<player_name>&b trust you in her claim."),
    /**
     * Params: <br/>
     * player_name = name of the player which untrusted the receiver
     */
    PLAYER_UNTRUST_YOU("commands.claims.player_untrust_you", "&9Kingdoms >> &b<player_name>&b untrust you in her claim."),
    /**
     * Params: <br/>
     * size = size of the claim in two dimensions (x * z) <br/>
     * max_size = maximum size of a claim in two dimensions (x * z) <br/>
     * smallest_x = x coordinate of the smallest corner (corner with smallest x and z) <br/>
     * smallest_z = z coordinate of the smallest corner (corner with smallest x and z) <br/>
     * biggest_x = x coordinate of the biggest corner (corner with biggest x and z) <br/>
     * biggest_z = z coordinate of the biggest corner (corner with biggest x and z) <br/>
     * owner = name of the owner of the claim <br/>
     * trusted_players = list of trusted player's name separated by commas (ex: 'BlOckIncraft, xWzino, Filspartan')
     */
    PLAYER_CLAIM_INFO("commands.claims.player_claim_info",
            "&9&l&m---------------------------------------------|" +
                    "&9&lYour Claim|" +
                    "&bSize: <size> blocks / <max_size>|" +
                    "&bLocation: from <smallest_x>, <smallest_z> to <biggest_x>, <biggest_z>|" +
                    "&bOwner: <owner>&b|" +
                    "&bTrusted: <trusted_players>|" +
                    "&9&l&m---------------------------------------------"),
    /**
     * Params: <br/>
     * size = size of the claim in two dimensions (x * z) <br/>
     * max_size = maximum size of a claim in two dimensions (x * z) <br/>
     * smallest_x = x coordinate of the smallest corner (corner with smallest x and z) <br/>
     * smallest_z = z coordinate of the smallest corner (corner with smallest x and z) <br/>
     * biggest_x = x coordinate of the biggest corner (corner with biggest x and z) <br/>
     * biggest_z = z coordinate of the biggest corner (corner with biggest x and z) <br/>
     * owner = name of the owner of the claim <br/>
     * trusted_players = list of trusted player's name separated by commas (ex: 'BlOckIncraft, xWzino, Filspartan')
     */
    TRUSTED_CLAIM_INFO("commands.claims.trusted_claim_info",
            "&9&l&m---------------------------------------------|" +
                    "&9&lTrusted Claim|" +
                    "&bSize: <size> blocks / <max_size>|" +
                    "&bLocation: from <smallest_x>, <smallest_z> to <biggest_x>, <biggest_z>|" +
                    "&bOwner: <owner>&b|" +
                    "&bTrusted: <trusted_players>&b|" +
                    "&9&l&m---------------------------------------------"),

    /**<b><font color="lime" size=6>PERSONAL CLAIMS COMMAND MESSAGES - SUCCESS</font></b>*/
    PERSONAL_CLAIMS_COMMAND_MESSAGES_SUCCESS("", ""),
    SUCCESS_CLAIM_CREATION("commands.claims.success_creation", "&9Kingdoms >> &aThe claim was been created."),
    SUCCESS_CLAIM_EXTENSION("commands.claims.success_extension", "&9Kingdoms >> &aThe claim was been resized."),
    SUCCESS_CLAIM_DELETION("commands.claims.success_deletion", "&9Kingdoms >> &aYour claim was been deleted."),
    /**
     * Params: <br/>
     * player_name = name of the player which we trusted
     */
    SUCCESS_PLAYER_TRUST("commands.claims.success_player_trust", "&9Kingdoms >> &a<player_name>&a has been trusted."),
    /**
     * Params: <br/>
     * player_name = name of the player which we untrusted
     */
    SUCCESS_PLAYER_UNTRUST("commands.claims.success_player_untrust", "&9Kingdoms >> &a<player_name>&a has been untrusted."),

    /**<b><font color="red" size=6>KINGDOMS COMMAND MESSAGES - ERROR</font></b>*/
    KINGDOMS_COMMAND_MESSAGES_ERROR("", ""),
    NAME_CANNOT_BE_EMPTY("commands.kingdoms.name_cannot_be_empty", "&9Kingdoms >> &cName cannot be empty."),
    /**
     * Params: <br/>
     * min_length = minimum name length (space exclude)
     */
    NAME_TOO_SHORT("commands.kingdoms.name_too_short", "&9Kingdoms >> &cName is too short, it need to contains at least <min_length> characters (space exclude)."),
    /**
     * Params: <br/>
     * max_length = maximum name length (space include)
     */
    NAME_TOO_LONG("commands.kingdoms.name_too_long", "&9Kingdoms >> &cName is too long, it can't contains more than <max_length> characters (space include)."),
    NAME_ALREADY_TAKEN("commands.kingdoms.name_already_taken", "&9Kingdoms >> &cThis name is already used, choose another one."),
    DO_NOT_HAVE_A_KINGDOM("commands.kingdoms.do_not_have_a_kingdom", "&9Kingdoms >> &cYou don't have a kingdom."),
    DO_NOT_HAVE_KINGDOM_PERMISSION_TO_DO_THAT("commands.kingdoms.do_not_have_kingdom_permission_to_do_that", "&9Kingdoms >> &cYou don't have permission to do that in your kingdom, you need to be promote."),
    INVALID_KINGDOM_DIRECTION("commands.kingdoms.invalid_direction", "&9Kingdoms >> &cThis direction isn't valid use 'north', 'south', 'east' or 'west'."),
    KINGDOM_HOME_MUST_BE_IN_KINGDOM("commands.kingdoms.kingdom_home_must_be_in_kingdom", "&9Kingdoms >> &cHome must be inside the kingdom."),
    ONLY_OWNER_CAN_DO_THAT("commands.kingdoms.only_owner_can_do_that", "&9Kingdoms >> &cOnly owner of the kingdom can do that."),
    CANNOT_INVITE_YOURSELF("commands.kingdoms.cannot_invite_yourself", "&9Kingdoms >> &cYou can't invite yourself."),
    /**
     * Params: <br/>
     * player_name = name of the player which we try to invite
     */
    PLAYER_ALREADY_MEMBER("commands.kingdoms.player_already_member", "&9Kingdoms >> &c<player_name>&c is already a member of the kingdom."),
    /**
     * Params: <br/>
     * player_name = name of the player which we try to invite
     */
    PLAYER_ALREADY_INVITED("commands.kingdoms.player_already_invited", "&9Kingdoms >> &c<player_name>&c was already invited to join the kingdom."),
    CANNOT_KICK_YOURSELF("commands.kingdoms.cannot_kick_yourself", "&9Kingdoms >> &cYou can't kick yourself."),
    /**
     * Params: <br/>
     * player_name = name of the player which we try to kick/edit
     */
    PLAYER_WAS_NOT_MEMBER("commands.kingdoms.player_was_not_member", "&9Kingdoms >> &c<player_name> wasn't a member of the kingdom."),
    /**
     * Params: <br/>
     * player_name = name of the player which we try to kick
     */
    CANNOT_KICK_THIS_PLAYER("commands.kingdoms.cannot_kick_this_player", "&9Kingdoms >> &cYou can't kick <player_name>&c, you can only kick people with lower permissions than you."),
    CANNOT_PROMOTE_YOURSELF("commands.kingdoms.cannot_promote_yourself", "&9Kingdoms >> &cYou can't promote yourself."),
    /**
     * Params: <br/>
     * player_name = name of the player which we try to promote
     */
    CANNOT_PROMOTE_THIS_PLAYER("commands.kingdoms.cannot_promote_this_player", "&9Kingdoms >> &cYou can't promote <player_name>&c, you can only promote people to a lower rank than you."),
    CANNOT_DEMOTE_YOURSELF("commands.kingdoms.cannot_demote_yourself", "&9Kingdoms >> &cYou can't demote yourself."),
    /**
     * Params: <br/>
     * player_name = name of the player which we try to demote
     */
    CANNOT_DEMOTE_THIS_PLAYER("commands.kingdoms.cannot_demote_this_player", "&9Kingdoms >> &cYou can't demote <player_name>&c, you can only demode people with lower permissions than you."),
    NO_CONSTRUCTIONS_IN_KINGDOM("commands.kingdoms.no_constructions_in_kingdom", "&9Kingdoms >> &cThere are no constructions in the kingdom."),
    NEED_TO_BE_IN_A_CONSTRUCTION("commands.kingdoms.need_to_be_in_a_construction", "&9Kingdoms >> &cYou need to stay on a construction to do that."),
    INVALID_CONSTRUCTION_NAME("commands.kingdoms.invalid_construction_name", "&9Kingdoms >> &cInvalid construction name."),
    /**
     * Params: <br/>
     * max = max amount of the construction <br/>
     * construction = construction name
     */
    YOU_REACHED_THE_LIMIT_OF_THIS_CONSTRUCTION("commands.kingdoms.you_reached_the_limit_of_this_construction", "&9Kingdoms >> &cYou reached the limit of <construction>&c in the kingdom, <max>/<max>."),
    NEED_TO_SELECT_A_AREA_TO_CREATE_A_KINGDOM("commands.kingdoms.need_to_select_a_area_to_create_a_kingdom", "&9Kingdoms >> &cIn order to create a kingdom, you need to select an area with the command '/areas'."),
    NEED_TO_SELECT_A_AREA_TO_CREATE_A_CONSTRUCTION("commands.kingdoms.need_to_select_a_area_to_create_a_construction", "&9Kingdoms >> &cIn order to create a construction, you need to select an area inside the kingdom with the command '/areas'."),
    AREA_NEED_TO_BE_IN_KINGDOM("commands.kingdoms.area_need_to_be_in_kingdom", "&9Kingdoms >> &cSelected area must be in the kingdom."),
    AREA_TOUCH_OTHER_CONSTRUCTION("commands.kingdoms.area_touch_other_construction", "&9Kingdoms >> &cSelected area touch at least one other construction."),
    /**
     * Params: <br/>
     * min_x = minimum construction size x <br/>
     * min_y = minimum construction size y <br/>
     * min_z = minimum construction size z
     */
    CONSTRUCTION_MINIMUM_SIZE("commands.kingdoms.construction_minimum_size", "&9Kingdoms >> &cThe construction must be at least <min_x> * <min_z> * <min_y> blocks (x;z;y)."),
    INVALID_CONSTRUCTION_DIRECTION("commands.kingdoms.invalid_contruction_direction", "&9Kingdoms >> &cThis direction isn't valid use 'up', 'down', 'north', 'south', 'east' or 'west'."),
    ALREADY_HAVE_A_KINGDOM("commands.kingdoms.already_have_a_kingdom", "&9Kingdoms >> &cYou have already a kingdom, you need to leave it to do that."),
    YOU_ARE_NOT_INVITED_IN_THIS_KINGDOM("commands.kingdoms.you_are_not_invited_in_this_kingdom", "&9Kingdoms >> &cYou aren't invited in this kingdom."),
    THIS_KINGDOM_NO_LONGER_EXIST("commands.kingdoms.this_kingom_no_longer_exist", "&9Kingdoms >> &cThis kingdom no longer exist."),
    YOU_CANNOT_LEAVE_YOUR_KINGDOM("commands.kingdoms.you_cannot_leave_your_kingdom", "&9Kingdoms >> &cYou cannot leave this kingdom, you need to delete it."),
    /**
     * Params: <br/>
     * max_amount = max amount that the bank can store <br/>
     * current_amount = amount already stored in the bank
     */
    BANK_CAN_ONLY_CONTAIN("commands.kingdoms.bank_can_only_contain", "&9Kingdoms >> &cThe bank can only contains <max_amount> and it already contains <current_amount>."),
    DO_NOT_HAVE_ENOUGH_MONEY("commands.kingdoms.do_not_have_enough_money", "&9Kingdoms >> &cYou haven't enough money to do that."),
    DO_NOT_HAVE_ENOUGH_MONEY_IN_BANK("commands.kingdoms.do_not_have_enough_money_in_bank", "&9Kingdoms >> &cYou haven't enough money in the bank to do that."),
    ERROR_OCCURRED_DURING_TRANSACTION("commands.kingdoms.error_occurred_during_transaction", "&9Kingdoms >> &cAn error has occurred during transaction."),
    NEED_ITEM_IN_HAND("commands.kingdoms.need_item_in_hand", "&9Kingdoms >> &cYou need to hold an item in the main hand to do that."),
    NO_COLLECTION_OF_THIS_MATERIAL("commands.kingdoms.no_collection_of_this_material", "&9Kingdoms >> &cThis item isn't collectionable."),
    COLLECTION_ALREADY_MAXED("commands.kingdoms.collection_already_maxed", "&9Kingdoms >> &cThe collection is already maxed."),

    /**<b><font color="aqua" size=6>KINGDOMS COMMAND MESSAGES - INFO</font></b>*/
    KINGDOMS_COMMAND_MESSAGES_INFO("", ""),
    /**
     * Params: <br/>
     * player_name = invited player name <br/>
     * inviter_name = name of the member who invite the player
     */
    MEMBER_INVITE_PLAYER("commands.kingdoms.member_invite_player",
            "&9&l&m---------------------------------------------|" +
                    "&b<inviter_name>&b invite <player_name>&b to join the kingdom. They have &c60 &bseconds to accept.|" +
                    "&9&l&m---------------------------------------------"),
    /**
     * Params: <br/>
     * kingdom_name = name of the kingdom which invite the player <br/>
     * kingdom_id = id of the kingdom which invite the player <br/>
     * inviter_name = name of the member who invite the player
     */
    PLAYER_INVITE_YOU("commands.kingdoms.player_invite_you",
            "&9&l&m---------------------------------------------|" +
                    "&b<inviter_name>&b invite you to join her kingdom '<kingdom_name>&b', to join it send '/kingdom join <kingdom_id>'. You have &c60&b seconds to accept.|" +
                    "&9&l&m---------------------------------------------"),
    /**
     * Params: <br/>
     * invited_name = invited player name
     */
    INVITE_EXPIRE_MESSAGE_FOR_KINGDOM("commands.kingdoms.invite_expire_message_for_kingdom",
            "&9&l&m---------------------------------------------|" +
                    "&bThe join invite to <invited_name>&b has expired.|" +
                    "&9&l&m---------------------------------------------"),
    /**
     * Params: <br/>
     * inviter_name = inviter player name
     */
    INVITE_EXPIRE_MESSAGE_FOR_INVITED("commands.kingdoms.invite_expire_message_for_invited",
            "&9&l&m---------------------------------------------|" +
                    "&bThe join invite from <inviter_name>&b has expired.|" +
                    "&9&l&m---------------------------------------------"),
    /**
     * Params: <br/>
     * player_name = name of the player which join
     */
    PLAYER_JOIN_KINGDOM("commands.kingdoms.player_join_kingdom",
            "&9&l&m---------------------------------------------|" +
                    "&b<player_name>&b join the kingdom.|" +
                    "&9&l&m---------------------------------------------"),
    /**
     * Params: <br/>
     * player_name = player which promote the player <br/>
     * rank = new rank of the player
     */
    PLAYER_PROMOTED_YOU("commands.kingdoms.player_promoted_you", "&9Kingdoms >> &b<player_name>&b promoted you, you are now <rank>&b."),
    /**
     * Params: <br/>
     * player_name = player which demote the player <br/>
     * rank = new rank of the player
     */
    PLAYER_DEMOTED_YOU("commands.kingdoms.player_demoted_you", "&9Kingdoms >> &b<player_name>&b demoted you, you are now <rank>&b."),
    /**
     * Params: <br/>
     * construction_name : name of the construction <br/>
     * level : current level of the construction <br/>
     * max_level : maximum level of the construction <br/>
     * points : points earned from the construction <br/>
     * size : size of the construction <br/>
     * smallest_x = x coordinate of the smallest corner (corner with smallest x and z) <br/>
     * smallest_z = z coordinate of the smallest corner (corner with smallest x and z) <br/>
     * biggest_x = x coordinate of the biggest corner (corner with biggest x and z) <br/>
     * biggest_z = z coordinate of the biggest corner (corner with biggest x and z)
     */
    CONSTRUCTION_INFO_MESSAGE("commands.kingdoms.construction_info_message",
            "&9&l&m---------------------------------------------|" +
            "&9&l<construction_name>|" +
            "&bLevel: <level> / <max_level>|" +
            "&bPoints: <points>|" +
            "&bSize: <size> blocks|" +
            "&bLocation: from <smallest_x>, <smallest_z> to <biggest_x>, <biggest_z>|" +
            "&9&l&m---------------------------------------------"),
    /**
     * Params: <br/>
     * player_name = name of the player which leave the kingdom
     */
    MEMBER_LEAVE_KINGDOM("commands.kingdoms.member_leave_kingdom",
            "&9&l&m---------------------------------------------|" +
                    "&b<player_name>&b leave the kingdom.|" +
                    "&9&l&m---------------------------------------------"),
    KINGDOM_HELP_MESSAGE("commands.kingdoms.help_message", "&9Kingdoms >> &bAll informations are on our wiki: &n&ohttp://minexplore.fr/wiki/kingdoms"),
    KINGDOM_COMMISSION_INFO_MESSAGE("commands.kingdoms.commission_info_message",
            "&9&l&m---------------------------------------------|" +
                    "&9&l<commission_name>: &b<commission_desc>|" +
                    "&9Progression: &b<progression>&9/&b<objective> &9[&b<progress_bar>&9]|" +
                    "&9Completed commissions: &b<completed_commissions>|" +
                    "&9&l&m---------------------------------------------"),
    /**
     * No split for this element, so you can use '|' as text.
     */
    KINGDOM_COMMISSION_PROGRESS_BAR("commands.kingdoms.commission_progress_bar", "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||"),

    /**<b><font color="lime" size=6>KINGDOMS COMMAND MESSAGES - SUCCESS</font></b>*/
    KINGDOMS_COMMAND_MESSAGES_SUCCESS("", ""),
    SUCCESS_KINGDOM_CREATION("commands.kingdoms.success_creation", "&9Kingdoms >> &aYou have been create a kingdom."),
    SUCCESS_KINGDOM_EXTENSION("commands.kingdoms.success_extension", "&9Kingdoms >> &aYou have been extend the kingdom."),
    SUCCESS_KINGDOM_RENAME("commands.kingdoms.success_rename", "&9Kingdoms >> &aYou have been rename the kingdom."),
    SUCCESS_KINGDOM_DELETION("commands.kingdoms.success_deletion", "&9Kingdoms >> &aYou have been delete the kingdom."),
    SUCCESS_KINGDOM_SETHOME("commands.kingdoms.success_sethome", "&9Kingdoms >> &aYou have been set the kingdom home."),
    SUCCESS_TELEPORT_TO_KINGDOM_HOME("commands.kingdoms.success_teleport_to_home", "&9Kingdoms >> &aYou was been teleport to the home of your kingdom."),
    /**
     * Params: <br/>
     * player_name = name of the player which was kick
     */
    SUCCESS_KICK_PLAYER("commands.kingdoms.success_kick_player", "&9Kingdoms >> &aYou have been kick <player_name>&a from the kingdom."),
    /**
     * Params: <br/>
     * player_name = name of the player which was promoted <br/>
     * rank = new rank of the player which was promoted
     */
    SUCCESS_PROMOTE_PLAYER("commands.kingdoms.success_promote_player", "&9Kingdoms >> &aYou have been promote <player_name>&a, he is now <rank>&a."),
    /**
     * Params: <br/>
     * player_name = name of the player which was demoted <br/>
     * rank = new rank of the player which was demoted
     */
    SUCCESS_DEMOTE_PLAYER("commands.kingdoms.success_demote_player", "&9Kingdoms >> &aYou have been demote <player_name>&a, he is now <rank>&a."),
    SUCCESS_CONSTRUCTION_DELETION("commands.kingdoms.success_construction_deletion", "&9Kingdoms >> &aYou have been delete this construction."),
    /**
     * Params: <br/>
     * construction_name = name of the construction which was created.
     */
    SUCCESS_CONSTRUCTION_CREATION("commands.kingdoms.success_construction_creation", "&9Kingdoms >> &aYou have been create a new <construction_name>&a."),
    SUCCESS_CONSTRUCTION_EXTENSION("commands.kingdoms.success_construction_extension", "&9Kingdoms >> &aYou have been extend this construction."),
    SUCCESS_KINGDOM_LEAVE("commands.kingdoms.success_kingdom_leave", "&9Kingdoms >> &aYou have been leave your kingdom."),
    /**
     * Params: <br/>
     * amount = amount of money
     */
    SUCCESS_DEPOSIT("commands.kingdoms.success_deposit", "&9Kingdoms >> &aYou have been deposit <amount>."),
    /**
     * Params: <br/>
     * amount = amount of money
     */
    SUCCESS_WITHDRAW("commands.kingdoms.success_withdraw", "&9Kingdoms >> &aYou have been withdraw <amount>."),
    /**
     * Params: <br/>
     * amount = amount of item consumed <br/>
     * material = material of item (ex: DIAMOND)
     */
    SUCCESS_CONSUME("commands.kingdoms.success_consume", "&9Kingdoms >> &aYou have been give <amount> <material> to the kingdom collection."),
    /**
     * Params: <br/>
     * amount = amount of item consumed <br/>
     * material = material of item (ex: DIAMOND)
     */
    SUCCESS_CONSUME_AND_FINISH("commands.kingdoms.success_consume_and_finish", "&9Kingdoms >> &aYou have been give <amount> <material> to the kingdom collection and finish it!"),

    /**<b><font color="yellow" size=6>DYNMAP TITLES</font></b>*/
    DYNMAP_TITLES("", ""),
    DYNMAP_CLAIMS_TITLE("dynmap.claims.title", "Claims"),
    DYNMAP_KINGDOMS_TITLE("dynmap.claims.title", "Kingdoms"),
    DYNMAP_CONSTRUCTIONS_TITLE("dynmap.claims.title", "Constructions"),

    /**<b><font color="yellow" size=6>DYNMAP BUBBLE TEXTS</font></b>*/
    DYNMAP_BUBBLE_TEXT("", ""),
    DYNMAP_CLAIMS_BUBBLE_TEXT("dynmap.claims.info_bubble.description",
            "<h1><b><owner_name>'s claim</b></h1><br/>" +
                    "<b>Members: </b><member_amount><br/>" +
                    "<b>Size: </b><size> blocks"),
    DYNMAP_KINGDOMS_BUBBLE_TEXT("dynmap.kingdoms.info_bubble.description",
            "<h1><b><kingdom_name></b></h1>" +
                    "<b>Classement: </b>#<rank><br/>" +
                    "<b>Propriétaire: </b><owner_name><br/>" +
                    "<b>Members: </b><member_amount><br/>" +
                    "<b>Points: </b><ranking_points><br/>" +
                    "<b>Level: </b><level><br/>" +
                    "<b>Size: </b><size> blocks"),
    DYNMAP_CONSTRUCTIONS_BUBBLE_TEXT("dynmap.constructions.info_bubble.description",
            "<h1><b><construction_name></b></h1>" +
                    "<center><div>" +
                    "<b>Kingdom: </b><kingdom_name><br/>" +
                    "<b>Level: </b><level>/<max_level><br/>" +
                    "<b>Points: </b><points><br/>" +
                    "<b>Size: </b><size> blocks" +
                    "</div></center>"),

    /**<b><font color="purple" size=6>MENU TEXTS</font></b>*/
    MENU_TEXTS("", ""),

    NEXT_PAGE_ITEM_NAME("menus.next_page_item_name", "&9Next page"),
    NEXT_PAGE_ITEM_DESC("menus.next_page_item_desc", "&9Page &b<page>&9/&b<pages>"),
    PREVIOUS_PAGE_ITEM_NAME("menus.previous_page_item_name", "&9Previous page"),
    PREVIOUS_PAGE_ITEM_DESC("menus.previous_page_item_desc", "&9Page &b<page>&9/&b<pages>"),

    RANKING_MENU_TITLE("menus.ranking.title", "&9&lRanking"),
    RANKING_MENU_ITEM_NAME("menus.ranking.item.name", "&9#<rank> <kingdom_name>"),
    RANKING_MENU_ITEM_LORE("menus.ranking.item.lore",
            "|" +
                    "&9Owner: &b<owner>|" +
                    "&9Points: &b<points>|" +
                    "&9Level: &b<level>|" +
                    "&9Members: &b<members>|" +
                    "&9Size: &b<size>|" +
                    "&9Bank: &b<bank>"),

    TELEPORT_MENU_TITLE("menus.teleport.title", "&9&lTeleportation"),
    TELEPORT_MENU_ITEM_NAME("menus.teleport.item.name", "&9#<rank> <kingdom_name>"),
    TELEPORT_MENU_ITEM_LORE("menus.teleport.item.lore",
            "|" +
                    "&9Owner: &b<owner>|" +
                    "&9Points: &b<points>|" +
                    "&9Level: &b<level>|" +
                    "&9Members: &b<members>|" +
                    "&9Size: &b<size>|" +
                    "&9Bank: &b<bank>|" +
                    "|" +
                    "&9Click to teleport"),

    /**<b><font color="white" size=6>OTHER MESSAGES</font></b>*/
    OTHER_MESSAGES("", ""),
    COMPLETE_COMMISSION("messages.complete_commission", "&9Kingdoms >> &bYou completed a commission : <commission_name>!");

    public final String path;
    public final String defaultValue;

    Lang(String path, String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public String get() {
        if (Kingdoms.getInstance() == null || Kingdoms.getInstance().getLangConfig() == null) return defaultValue;
        FileConfiguration langConfig = Kingdoms.getInstance().getLangConfig();

        return langConfig.getString(path, defaultValue);
    }

    public static String getFrom(Constructions construction) {
        String defaultValue = construction.name().toLowerCase(Locale.ROOT).replace("_", " ");
        if (Kingdoms.getInstance() == null || Kingdoms.getInstance().getLangConfig() == null) return defaultValue;
        FileConfiguration langConfig = Kingdoms.getInstance().getLangConfig();

        return langConfig.getString(construction.langName, defaultValue);
    }

    public static String getFrom(KingdomLevels kingdomLevel) {
        String defaultValue = kingdomLevel.name().toLowerCase(Locale.ROOT).replace("_", " ");
        if (Kingdoms.getInstance() == null || Kingdoms.getInstance().getLangConfig() == null) return defaultValue;
        FileConfiguration langConfig = Kingdoms.getInstance().getLangConfig();

        return langConfig.getString(kingdomLevel.langName, defaultValue);
    }

    public static String getFrom(KingdomPermissionLevels permissionLevel) {
        String defaultValue = permissionLevel.name().toLowerCase(Locale.ROOT).replace("_", " ");
        if (Kingdoms.getInstance() == null || Kingdoms.getInstance().getLangConfig() == null) return defaultValue;
        FileConfiguration langConfig = Kingdoms.getInstance().getLangConfig();

        return langConfig.getString(permissionLevel.getLangName(), defaultValue);
    }

    public static String getFrom(Commissions commission) {
        String defaultValue = commission.name().toLowerCase(Locale.ROOT).replace("_", " ");
        if (Kingdoms.getInstance() == null || Kingdoms.getInstance().getLangConfig() == null) return defaultValue;
        FileConfiguration langConfig = Kingdoms.getInstance().getLangConfig();

        return langConfig.getString(commission.langName, defaultValue);
    }

    public static String getCommissionDesc(Commissions commission) {
        String defaultValue = commission.name().toLowerCase(Locale.ROOT).replace("_", " ");
        if (Kingdoms.getInstance() == null || Kingdoms.getInstance().getLangConfig() == null) return defaultValue;
        FileConfiguration langConfig = Kingdoms.getInstance().getLangConfig();

        return langConfig.getString(commission.langDesc, defaultValue);
    }
}
