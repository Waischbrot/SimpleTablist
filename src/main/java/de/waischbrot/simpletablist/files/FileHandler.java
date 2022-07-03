package de.waischbrot.simpletablist.files;

import de.leonhard.storage.Yaml;
import de.waischbrot.simpletablist.Main;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class FileHandler {

    private Yaml config;
    private final Main plugin;

    public FileHandler(Main plugin) {
        this.plugin = plugin;

        load();
    }

    private void load() {

        config = new Yaml("config", plugin.getDataFolder().getPath());

        if (config.singleLayerKeySet().size() == 0) {
            copy(plugin.getResource("config,yml"), config.getFile());
        }
    }

    private void copy(InputStream source, File dest) {
        try {
            try (InputStream input = source; OutputStream output = Files.newOutputStream(dest.toPath())) {
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Yaml getConfig() {
        return config;
    }
}
