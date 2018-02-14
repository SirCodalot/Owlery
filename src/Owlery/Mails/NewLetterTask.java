package Owlery.Mails;

import Owlery.Setup.ChatState;
import Owlery.Setup.MenuItem;
import Owlery.Setup.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class NewLetterTask {

    public static HashMap<Player, ChatState> playerChatState = new HashMap<>();
    private static ArrayList<NewLetterTask> newLetterTasks = new ArrayList<>();

    public String title, message;
    public UUID dest;
    public ArrayList<ItemStack> items;
    private Player player;

    public NewLetterTask(Player player) {

        this.player = player;
        items = new ArrayList<>();

        player.closeInventory();

        playerChatState.put(player, ChatState.DEST);
        Messages.ENTER_DESTENATION.sendMessage(player);

        newLetterTasks.add(this);
    }

    public void openItemSelector() {
        Inventory inventory = Bukkit.getServer().createInventory(null, 54, ChatColor.BLUE + "Send Items");

        for (int i = 36; i <= 44; i++) {
            inventory.setItem(i, MenuItem.MENU_BARRIERS.getItem());
            inventory.setItem(i+9, MenuItem.MENU_FILLERS.getItem());
        }

        inventory.setItem(53, MenuItem.EXIT.getItem());
        inventory.setItem(49, MenuItem.SEND_MAIL.getItem());

        player.openInventory(inventory);
    }

    public void finish() {
        Letter letter = new Letter(player.getUniqueId(), dest, title, message, items);

        Messages.SENT_MAIL.sendMessage(player);

        end();
    }

    public void end() {
        playerChatState.remove(player);
        newLetterTasks.remove(this);
    }

    public static NewLetterTask get(Player player) {
        for (NewLetterTask newLetterTask : newLetterTasks) {
            if (newLetterTask.player == player) return newLetterTask;
        }
        return null;
    }

}
