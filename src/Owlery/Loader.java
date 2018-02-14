package Owlery;

import Owlery.Commands.CmdMailbox;
import Owlery.Commands.CmdOwl;
import Owlery.Events.InventoryEvents;
import Owlery.Events.PlayerEvents;
import Owlery.Mails.Letter;
import Owlery.Mails.Mailbox;
import Owlery.Setup.Messages;
import Owlery.Setup.Owl;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/*
    Created by SirCodalot as a trial
    Twitter: @Sir_Codalot
*/
public class Loader extends JavaPlugin {

    private static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        registerEvents();

        registerCommands();

        Letter.load();

        packetListener();

        Owl.load();

    }

    @Override
    public void onDisable() {
        if (Owl.owls == null) return;
        ArrayList<Owl> remove = new ArrayList<>(Owl.owls);
        for (Owl owl : remove) owl.despawn();
        remove.clear();

    }

    private void packetListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                //super.onPacketReceiving(event);

                if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
                    PacketContainer packet = event.getPacket();
                    packet.getIntegers().read(0);
                    if (!event.getPlayer().getOpenInventory().getTitle().contains("Mailbox")) {
                        Owl owl = null;
                        for (Owl o : Owl.owls) {
                            if (packet.getIntegers().read(0) == o.getNpc().getBukkitEntity().getEntityId())
                                owl = o;
                        }
                        if (owl == null) return;
                        if (Owl.removers.contains(event.getPlayer())) {
                                owl.remove();
                                Owl.removers.remove(event.getPlayer());
                            Messages.DESPAWNED_OWL.sendMessage(event.getPlayer());
                        } else new Mailbox().openMailbox(event.getPlayer());

                    }
                }
            }
        });
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new InventoryEvents(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);
    }

    private void registerCommands() {
        getCommand("mailbox").setExecutor(new CmdMailbox());
        getCommand("owl").setExecutor(new CmdOwl());
    }

    public static Plugin getInstance() {
        return instance;
    }
}
