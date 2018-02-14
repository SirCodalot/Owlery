package Owlery.Commands;

import Owlery.Mails.Mailbox;
import Owlery.Setup.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdMailbox implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            Messages.CMD_INVALID_EXECUTOR.sendMessage(sender);
            return true;
        }

        new Mailbox().openMailbox((Player) sender);

        return true;
    }
}
