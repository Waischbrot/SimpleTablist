package de.waischbrot.simpletablist.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Objects;

public final class Permissions {

    public static boolean hasPermission(Player player, String permission) {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            User user = api.getUserManager().getUser(player.getUniqueId());
            return Objects.requireNonNull(user).getCachedData().getPermissionData().checkPermission(permission).asBoolean();
        }
        else{
            return false;
        }
    }

    public static boolean hasPermissionEnabled(Player player, String permission){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            User user = api.getUserManager().getUser(player.getUniqueId());
            if(Objects.requireNonNull(user).getCachedData().getPermissionData().checkPermission(permission).asBoolean()){
                return Node.builder(permission).build().getValue();
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public static void addPermission(Player player, String permission) {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            User user = api.getUserManager().getUser(player.getUniqueId());
            Objects.requireNonNull(user).data().add(Node.builder(permission).build());
            api.getUserManager().saveUser(user);
        }
    }

    public static void removePermission(Player player, String permission){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPerms api = provider.getProvider();
            User user = api.getUserManager().getUser(player.getUniqueId());
            Objects.requireNonNull(user).data().remove(Node.builder(permission).build());
            api.getUserManager().saveUser(user);
        }
    }
}
