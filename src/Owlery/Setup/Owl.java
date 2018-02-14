package Owlery.Setup;

import Owlery.Loader;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class Owl {

    public static ArrayList<Owl> owls = new ArrayList<>();
    public static ArrayList<Player> removers = new ArrayList<>();
    private static FileConfiguration cf = Loader.getInstance().getConfig();

    private UUID uuid;
    private Location location;
    private EntityPlayer npc;

    public Owl(Location location, UUID uuid) {
        this.uuid = (uuid == null) ? UUID.randomUUID() : uuid;
        this.location = location;

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();

        GameProfile skin10032 = new GameProfile(UUID.fromString("813cdb0a-3d5c-3b7a-868f-6c7d9564a021"), ChatColor.LIGHT_PURPLE + "Mail Owl");
        skin10032.getProperties().put("textures", new Property(
                "textures",
                "eyJ0aW1lc3RhbXAiOjE0OTE5MzMxODMwMjYsInByb2ZpbGVJZCI6ImRhNzQ2NWVkMjljYjRkZTA5MzRkOTIwMTc0NDkxMzU1IiwicHJvZmlsZU5hbWUiOiJJc2F5bGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzcxNGE0NWViM2Q1NGNkMzZlZjUyMTJlNzEwZTA1ZDk1N2Y4OGJkMWMwNzhmMTdhM2VmMDY5MTA3ODZmNDU0In19fQ==",
                "dhEdStZ5RnXSkqm7h6Ufoi0ssBEFu06bIcW4AD8VvDAuimJwrJ9bOhIVUhuEH95DlqUaevL03dUsBttMSQwugKSkTetduCUouyBu4yi+RqApR5DfTgKqeV/LioviqiUuDTuHtkAkNY1et04NVYrJtRGid8Vy1E+Z6kD8/7UPrBjhtFCyPUnbydwpUqetV3gEYrFiVt7UaeuCt/5EPpq4wBevVB9YWDWBDKwKxUAO2of82Sy1n6uE9TZMc9SazycIF6blsr3JFHdR2ruN7n94puoVLkhOno624KUE52BUtKdRTC/m6ebz3jZy8UToJsGuRLgnx0wQCxv3Mxhz6AvGWXQj1lMAgs229v1nmqEP1FloHwc0DTGF/31lb6HWpXgycSBIwhR6OUMETI+jj0c60gEk0t2uRBbNCCBrluWWTBNbocUuyW3h4k6PV2iwP8LN2l04kHfjwhOAsL7lf7bewtBhm9j58VgInvmBmGHpwleho1YqOvqvEzYUi6HUwkmRV2i+w0g+AXCeZdEwUNckP+VmC/S/zT73yUhTNK/TkmcWghWjUtAzkm3ae53Gnp/cmL/jtcgY6VRuMFPMvhSUhZwHbvc87BEzTKWTxIJjqztQvPdhnqOSbRVgQZ3odw86c+kEQ00xMjOD9LIBSUemnBiMabvMbyOnfPnRJHPKsZw="));

        npc = new EntityPlayer(server, world, skin10032, new PlayerInteractManager(world));
        Player player = npc.getBukkitEntity().getPlayer();
        player.setPlayerListName("");

        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        npc.getBukkitEntity().setRemoveWhenFarAway(false);
        npc.getBukkitEntity().setCollidable(false);
        npc.setNoGravity(true);
        npc.pitch = location.getPitch();
        npc.lastPitch = location.getPitch();

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (location.getYaw() * 256 / 360)));
        }
        new BukkitRunnable() {
            public void run() {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                }
            }
        }.runTaskLater(Loader.getInstance(), 10);

        save();

        owls.add(this);

    }

    public void save() {
        String path = "NPCS." + uuid.toString() + ".";
        cf.set(path + "w", location.getWorld().getName());
        cf.set(path + "x", location.getX());
        cf.set(path + "y", location.getY());
        cf.set(path + "z", location.getZ());
        cf.set(path + "p", location.getPitch());
        cf.set(path + "yw", location.getYaw());

        Loader.getInstance().saveConfig();
    }

    public static void load() {
        if (cf.getConfigurationSection("NPCS") == null) return;

        for (String key : cf.getConfigurationSection("NPCS").getKeys(false)) {
            World world = Bukkit.getServer().getWorld(cf.getString("NPCS." + key + ".w"));
            double x = cf.getDouble("NPCS." + key + ".x"),
                    y = cf.getDouble("NPCS." + key + ".y"),
                    z = cf.getDouble("NPCS." + key + ".z");
            float p = (float) cf.getDouble("NPCS." + key + ".p"),
                    yw = (float) cf.getDouble("NPCS." + key + ".yw");
            Location loc = new Location(world, x, y, z, p, yw);
            new Owl(loc, UUID.fromString(key));
        }
    }

    public void despawn() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getBukkitEntity().getEntityId()));
        }

        npc.getBukkitEntity().remove();
        owls.remove(this);
    }

    public void remove() {

        despawn();

        cf.set("NPCS." + uuid.toString(), null);
        Loader.getInstance().saveConfig();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

    public EntityPlayer getNpc() {
        return npc;
    }
}
