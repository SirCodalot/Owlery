package Owlery.Events;

import Owlery.Loader;
import Owlery.Setup.ChatState;
import Owlery.Setup.Messages;
import Owlery.Setup.Owl;
import Owlery.Mails.NewLetterTask;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class PlayerEvents implements Listener{

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        String message = event.getMessage();

        if (!NewLetterTask.playerChatState.keySet().contains(player)) return;

        NewLetterTask newLetterTask = NewLetterTask.get(player);

        if (newLetterTask == null) return;

        event.setCancelled(true);

        if (message.equalsIgnoreCase("#CANCEL")) {
            newLetterTask.end();
            Messages.CANCELED_PROCESS.sendMessage(player);
            return;
        }

        switch (NewLetterTask.playerChatState.get(player)) {
            case DEST:
                OfflinePlayer dest = Bukkit.getServer().getOfflinePlayer(message);
                if (dest == null) {
                    Messages.PLAYER_NOT_FOUND.sendMessage(player);
                    break;
                }
                newLetterTask.dest = dest.getUniqueId();
                NewLetterTask.playerChatState.put(player, ChatState.TITLE);
                Messages.ENTER_TITLE.sendMessage(player);
                break;
            case TITLE:
                if (message.length() < 3 || message.length() > 15) {
                    Messages.TITLE_TOO_SHORT.sendMessage(player);
                    break;
                }
                newLetterTask.title = message;
                NewLetterTask.playerChatState.put(player, ChatState.MESSAGE);
                Messages.ENTER_MESSAGE.sendMessage(player);
                break;
            case MESSAGE:
                if (message.length() < 5) {
                    Messages.MESSAGE_TOO_SHORT.sendMessage(player);
                    break;
                }
                newLetterTask.message = message;
                newLetterTask.openItemSelector();
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        new BukkitRunnable() {
            public void run() {

                PlayerConnection connection = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection;
                for (Owl owl : Owl.owls) {
                    connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, owl.getNpc()));
                    connection.sendPacket(new PacketPlayOutNamedEntitySpawn(owl.getNpc()));
                    connection.sendPacket(new PacketPlayOutEntityHeadRotation(owl.getNpc(), (byte) (owl.getNpc().yaw * 256 / 360)));
                }
                new BukkitRunnable() {
                    public void run() {
                        for (Owl owl : Owl.owls) {
                            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, owl.getNpc()));
                        }
                    }
                }.runTaskLater(Loader.getInstance(), 10);
            }
        }.runTaskLater(Loader.getInstance(), 20);
    }

}
