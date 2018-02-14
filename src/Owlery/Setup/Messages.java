package Owlery.Setup;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Messages {

    PREFIX("&7[&dOwlery&7]&8 "),

    CMD_INVALID_EXECUTOR(PREFIX.getMessage() + "&cYou must be a player to execute this command."),
    CMD_INVALID_USAGE(PREFIX.getMessage() + "&cInvalid Arguments! Usage: /owl spawn/remove"),

    DESPAWN_OWL(PREFIX.getMessage() + "Click an owl npc to remove it."),
    DESPAWNED_OWL(PREFIX.getMessage() + "Successfully removed owl."),

    PLAYER_NOT_FOUND(PREFIX.getMessage() + "&cERROR 404: Player Not Found"),

    ENTER_DESTENATION(PREFIX.getMessage() + "Enter the recipient's name, type \"&7#CANCEL&8\" to cancel the process: "),
    ENTER_TITLE(PREFIX.getMessage() + "Enter the letter's title: "),
    ENTER_MESSAGE(PREFIX.getMessage() + "Enter the message: "),
    TITLE_TOO_SHORT(PREFIX.getMessage() + "&cThe title is too short/long, a title must be between 3 to 15 characters."),
    MESSAGE_TOO_SHORT(PREFIX.getMessage() + "&cThe message is too short."),

    SENT_MAIL(PREFIX.getMessage() + "Successfully sent mail."),

    NEW_LETTER(PREFIX.getMessage() + "You have a new letter in your mailbox."),

    CANCELED_PROCESS(PREFIX.getMessage() + "Cancelled the process successfully."),

    SPAWNED_OWL(PREFIX.getMessage() + "Successfully spawned an owl."),

    CLEARED_MAILBOX(PREFIX.getMessage() + "Cleared your mailbox successfully.");

    private String message;

    Messages(String message) {
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    public String getMessage() {
        return message;
    }

    public void sendMessage(Player player) {
        player.sendMessage(message);
    }

    public void sendMessage(CommandSender sender) {
        sender.sendMessage(message);
    }
}
