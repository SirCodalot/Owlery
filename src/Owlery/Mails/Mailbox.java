package Owlery.Mails;

import Owlery.Setup.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Mailbox {

    public void openMailbox(Player player) {
        Inventory inventory = Bukkit.getServer().createInventory(null, 54, ChatColor.BLUE + "Mailbox");

        for (int i = 36; i <= 44; i++) {
            inventory.setItem(i, MenuItem.MENU_BARRIERS.getItem());
            inventory.setItem(i+9, MenuItem.MENU_FILLERS.getItem());
        }

        inventory.setItem(48, MenuItem.CREATE_NEW_MAIL.getItem());
        inventory.setItem(50, MenuItem.CLEAR_MAILBOX.getItem());
        inventory.setItem(53, MenuItem.EXIT.getItem());

        for (Letter l : Letter.letters(player.getUniqueId())) {
            inventory.addItem(l.getDisplayItem());
        }

        player.openInventory(inventory);
    }

}
