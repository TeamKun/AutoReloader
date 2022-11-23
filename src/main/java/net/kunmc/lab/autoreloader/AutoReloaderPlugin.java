package net.kunmc.lab.autoreloader;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.Optional;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public final class AutoReloaderPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        try {
            SimplePluginManager pluginManager = ((SimplePluginManager) Bukkit.getPluginManager());
            PluginLoader pluginLoader = getPluginLoader();
            Path pluginsDirectory = Optional.ofNullable(pluginManager.pluginsDirectory())
                                            .orElse(new File("plugins"))
                                            .toPath();

            WatchService watcher = FileSystems.getDefault()
                                              .newWatchService();
            WatchKey watchKey = pluginsDirectory.register(watcher, ENTRY_MODIFY);

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (WatchEvent<?> e : watchKey.pollEvents()) {
                        Path filePath = pluginsDirectory.resolve((Path) e.context());
                        if (!FilenameUtils.getExtension(filePath.toString())
                                          .equalsIgnoreCase("jar")) {
                            return;
                        }

                        PluginDescriptionFile descriptionFile;
                        try {
                            descriptionFile = pluginLoader.getPluginDescription(filePath.toFile());
                        } catch (Exception ex) {
                            return;
                        }

                        if (pluginManager.getPlugin(descriptionFile.getName()) == null) {
                            String fileName = FilenameUtils.getBaseName(filePath.toString());
                            Bukkit.broadcastMessage("start loading " + fileName);
                            Bukkit.dispatchCommand(new BroadcastSender(), "plugman load " + fileName);
                        } else {
                            String name = descriptionFile.getName();
                            Bukkit.broadcastMessage("start reloading " + name);
                            Bukkit.dispatchCommand(new BroadcastSender(), "plugman reload " + name);
                        }
                    }

                    watchKey.reset();
                }
            }.runTaskTimer(this, 0, 20);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void onDisable() {
    }
}
