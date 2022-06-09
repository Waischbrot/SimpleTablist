package de.waischbrot.simpletablist.tablist;

import java.util.Collection;

import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CustomTablist {

    public static void addAllPlayers(Player p) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            addPlayers(p, ((CraftPlayer) player).getHandle());
        }
    }

    public static void removePlayers(Player receiver) {
        Collection<? extends Player> playersBukkit = Bukkit.getOnlinePlayers();
        EntityPlayer[] playersNMS = new EntityPlayer[playersBukkit.size()];
        int current = 0;
        for (Player player : playersBukkit) {
            playersNMS[current] = ((CraftPlayer) player).getHandle();
            current++;
        }
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, playersNMS);
        ((CraftPlayer) receiver).getHandle().b.sendPacket(packet);
    }


    public static void addPlayers(Player receiver, EntityPlayer... createdPlayers) {
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, createdPlayers);
        ((CraftPlayer) receiver).getHandle().b.sendPacket(packet);
    }

}