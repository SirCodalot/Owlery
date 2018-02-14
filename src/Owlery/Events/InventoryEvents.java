package Owlery.Events;

import Owlery.Mails.Letter;
import Owlery.Mails.Mailbox;
import Owlery.Setup.MenuItem;
import Owlery.Setup.Messages;
import Owlery.Mails.NewLetterTask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getTitle().equalsIgnoreCase(ChatColor.BLUE + "Mailbox")) event.setCancelled(true);
        if (MenuItem.getMenuItem(event.getCurrentItem()) != MenuItem.NULL) event.setCancelled(true);

        switch (MenuItem.getMenuItem(event.getCurrentItem())) {
            case EXIT:
                player.closeInventory();
                if (inv.getTitle().equalsIgnoreCase(ChatColor.BLUE + "Send Items")) {
                    NewLetterTask task = NewLetterTask.get(player);
                    if (task == null) break;
                    task.end();
                    Messages.CANCELED_PROCESS.sendMessage(player);
                }
                break;
            case CLEAR_MAILBOX:
                Letter.clearLetters(player);
                break;
            case CREATE_NEW_MAIL:
                new NewLetterTask(player);
                break;
            case SEND_MAIL:
                NewLetterTask task = NewLetterTask.get(player);
                if (task == null) break;

                task.items = new ArrayList<ItemStack>() {
                    {
                        for (int i = 0; i <= 35; i++) {
                            if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR) add(inv.getItem(i));
                        }
                    }};
                task.finish();
                player.closeInventory();
        }

        if (!event.getInventory().getTitle().equalsIgnoreCase(ChatColor.BLUE + "Mailbox")) return;

        Letter letter = Letter.getLetter(event.getCurrentItem());
        if (letter == null) return;

        switch (event.getClick()) {
            case RIGHT:
            case SHIFT_RIGHT:
                letter.delete();
                new Mailbox().openMailbox(player);
                break;
            case LEFT:
            case SHIFT_LEFT:
                letter.open(player);
                break;
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getInventory().getTitle().equalsIgnoreCase(ChatColor.BLUE + "Send Items")) return;

        NewLetterTask task = NewLetterTask.get((Player) event.getPlayer());
        if (task == null) return;
        task.end();
        Messages.CANCELED_PROCESS.sendMessage(event.getPlayer());
    }

}
