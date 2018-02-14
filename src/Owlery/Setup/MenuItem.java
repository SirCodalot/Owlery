package Owlery.Setup;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum MenuItem {

    EXIT(exit()),
    CLEAR_MAILBOX(clearMail()),
    CREATE_NEW_MAIL(createMail()),
    MENU_BARRIERS(barrier()),
    MENU_FILLERS(filler()),
    SEND_MAIL(send()),
    NULL(new ItemStack(Material.AIR));

    private ItemStack item;

    MenuItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public static MenuItem getMenuItem(ItemStack item) {
        for (MenuItem menuItem : MenuItem.values()) if (menuItem.getItem().isSimilar(item)) return menuItem;
        return NULL;
    }

    private static ItemStack exit() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Close Menu");

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack clearMail() {
        ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RED + "Clear Mailbox");

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack createMail() {
        ItemStack item = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.BLUE + "Write a Letter");

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack send() {
        ItemStack item = new ItemStack(Material.FEATHER);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "Send Letter");

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack barrier() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(" ");

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack filler() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(" ");

        item.setItemMeta(meta);
        return item;
    }
}
