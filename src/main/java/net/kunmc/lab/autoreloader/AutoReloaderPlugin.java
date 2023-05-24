package net.kunmc.lab.autoreloader;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public final class AutoReloaderPlugin extends JavaPlugin {
    private final Map<Path, Long> pathToLastModifiedMap = new HashMap<>();

    @Override
    public void onEnable() {
        try {
            Path pluginsDirectory = new File("plugins").toPath();
            PluginLoader pluginLoader = getPluginLoader();
            PluginManager pluginManager = Bukkit.getPluginManager();

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
                        if (pathToLastModifiedMap.getOrDefault(filePath, 0L) == filePath.toFile()
                                .lastModified()) {
                            return;
                        }
                        pathToLastModifiedMap.put(filePath,
                                filePath.toFile()
                                        .lastModified());

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
