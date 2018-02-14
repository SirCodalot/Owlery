package Owlery.Commands;

import Owlery.Setup.Messages;
import Owlery.Setup.Owl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdOwl implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            Messages.CMD_INVALID_EXECUTOR.sendMessage(sender);
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            Messages.CMD_INVALID_USAGE.sendMessage(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("spawn")) {
            new Owl(player.getLocation(), null);

            Messages.SPAWNED_OWL.sendMessage(player);

            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            Owl.removers.add(player);

            Messages.DESPAWN_OWL.sendMessage(player);

            return true;
        }

        Messages.CMD_INVALID_USAGE.sendMessage(player);

        return true;
    }
}
