package Owlery.Mails;

import Owlery.Loader;
import Owlery.Setup.Messages;
import Owlery.Utils.ItemStackSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Letter {

    private static HashMap<Letter, UUID> mails = new HashMap<>();
    private static FileConfiguration cf = Loader.getInstance().getConfig();

    private UUID sender, dest, id;
    private ArrayList<ItemStack> items;
    private String title, message;
    private ItemStack displayItem;

    Letter(UUID sender, UUID dest, String title, String message, ArrayList<ItemStack> items) {
        this.sender = sender;
        this.dest = dest;
        this.title = title;
        this.message = message;
        this.items = items;
        id = UUID.randomUUID();

        Player player = Bukkit.getServer().getPlayer(dest);
        if (player != null) Messages.NEW_LETTER.sendMessage(player);

        setup();

    }

    private Letter(UUID sender, UUID dest, String title, String message, ArrayList<ItemStack> items, UUID id) {
        this.sender = sender;
        this.dest = dest;
        this.title = title;
        this.message = message;
        this.items = items;
        this.id = id;

        setup();

    }

    private void setup() {
        displayItem = new ItemStack(Material.BOOK);
        ItemMeta meta = displayItem.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + title);
        meta.setLore(new ArrayList<String>() {{
            add(ChatColor.BLUE + "From: " + ChatColor.GREEN + Bukkit.getOfflinePlayer(sender).getName());
            add("");
            add(ChatColor.RED + "Left-click to open.");
            add(ChatColor.RED + "Right-click to delete.");
            add(id.toString());
        }});

        displayItem.setItemMeta(meta);

        save();

        mails.put(this, dest);
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDestenation() {
        return dest;
    }

    public ArrayList<ItemStack> getItems() {

        return items;
    }

    public void setItems(ArrayList<ItemStack> items) {
        this.items = items;
    }

    public void addItem(ItemStack item) {
        items.add(item);
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void delete() {

        cf.set("Letters." + id, null);
        Loader.getInstance().saveConfig();

        mails.remove(this);
    }

    private void save() {
        String path = "Letters." + id.toString() + ".";
        cf.set(path + "From", sender.toString());
        cf.set(path + "To", dest.toString());
        cf.set(path + "Title", title);
        cf.set(path + "Message", message);

        ArrayList<String> items = new ArrayList<>();
        for (ItemStack i : this.items) items.add(ItemStackSerializer.serialize(i));
        cf.set(path + "Items", items);

        Loader.getInstance().saveConfig();

    }

    public static void load() {
        if (cf.getConfigurationSection("Letters") == null) return;

        for (String key : cf.getConfigurationSection("Letters").getKeys(false)) {
            UUID id = UUID.fromString(key), sender = UUID.fromString(cf.getString("Letters." + key + ".From")), to = UUID.fromString(cf.getString("Letters." + key + ".To"));
            String title = cf.getString("Letters." + key + ".Title"), message = cf.getString("Letters." + key + ".Message");
            ArrayList<ItemStack> items = new ArrayList<>();
            for (String s : cf.getStringList("Letters." + key + ".Items")) items.add(ItemStackSerializer.deserialize(s));
            new Letter(sender, to, title, message, items, id);
        }
    }

    public void open(Player player) {
        player.closeInventory();
        player.sendMessage(ChatColor.GOLD + title);
        player.sendMessage(ChatColor.BLUE + message);
        for (ItemStack item : items) player.getInventory().addItem(item);
        delete();
    }

    public static ArrayList<Letter> letters(UUID uuid) {
        return new ArrayList<Letter>() {{
            for (Letter l : mails.keySet()) {
                if (mails.get(l).equals(uuid)) add(l);
            }
        }};
    }

    public static void clearLetters(Player player) {
        if (!mails.containsValue(player.getUniqueId())) return;
        ArrayList<Letter> remove = new ArrayList<>();
        for (Letter l : mails.keySet()) {
            if (mails.get(l) == player.getUniqueId()) remove.add(l);
        }
        for (Letter l : remove) {
            mails.remove(l);
        }
        remove.clear();
        player.closeInventory();
        Messages.CLEARED_MAILBOX.sendMessage(player);
    }

    public static Letter getLetter(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;
        for (Letter letter : mails.keySet()) if (letter.getDisplayItem().isSimilar(item)) return letter;
        return null;
    }

}
