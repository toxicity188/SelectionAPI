package kor.toxicity.selection.manager;

import kor.toxicity.selection.api.SelectionAPI;
import kor.toxicity.selection.api.config.APIConfig;
import kor.toxicity.selection.api.manager.ConfigManager;
import kor.toxicity.selection.config.APIConfigImpl;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ConfigManagerImpl implements ConfigManager {
    private APIConfig config;
    @Override
    public @NotNull APIConfig getConfig() {
        return config;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void reload() {
        var api = SelectionAPI.getInstance();
        var dataFolder = api.getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdir();
        var configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) api.saveResource("config.yml", false);
        var yaml = new YamlConfiguration();
        try {
            yaml.load(configFile);
        } catch (Exception e) {
            api.getLogger().warning("Unable to load config.yml");
        }
        config = new APIConfigImpl(yaml);
    }
}
