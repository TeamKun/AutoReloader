package net.kunmc.lab.autoreloader;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public class BroadcastSender implements CommandSender {
    @Override
    public void sendMessage(@NotNull String message) {
        Bukkit.broadcastMessage(message);
    }

    @Override
    public void sendMessage(@NotNull String[] messages) {
        for (String message : messages) {
            Bukkit.broadcastMessage(message);
        }
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String message) {
        Bukkit.broadcastMessage(message);
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String[] messages) {
        for (String message : messages) {
            Bukkit.broadcastMessage(message);
        }
    }

    @Override
    public @NotNull Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    public @NotNull String getName() {
        return "broadcast";
    }

    @Override
    public @NotNull Spigot spigot() {
        return Bukkit.getConsoleSender()
                     .spigot();
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return Bukkit.getConsoleSender()
                     .isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return Bukkit.getConsoleSender()
                     .isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(@NotNull String name) {
        return Bukkit.getConsoleSender()
                     .hasPermission(name);
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return Bukkit.getConsoleSender()
                     .hasPermission(perm);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        return Bukkit.getConsoleSender()
                     .addAttachment(plugin, name, value);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return Bukkit.getConsoleSender()
                     .addAttachment(plugin);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin,
                                                        @NotNull String name,
                                                        boolean value,
                                                        int ticks) {
        return Bukkit.getConsoleSender()
                     .addAttachment(plugin, name, value, ticks);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        return Bukkit.getConsoleSender()
                     .addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment) {
        Bukkit.getConsoleSender()
              .removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        Bukkit.getConsoleSender()
              .recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return Bukkit.getConsoleSender()
                     .getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return Bukkit.getConsoleSender()
                     .isOp();
    }

    @Override
    public void setOp(boolean value) {
        Bukkit.getConsoleSender()
              .setOp(value);
    }
}
