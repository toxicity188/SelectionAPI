package kor.toxicity.selection.manager;

import kor.toxicity.selection.api.SelectionAPI;
import kor.toxicity.selection.api.manager.SkinManager;
import kor.toxicity.selection.api.registry.SkinRegistry;
import kor.toxicity.selection.registry.SkinRegistryImpl;
import kor.toxicity.selection.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.mineskin.MineskinClient;

import javax.imageio.ImageIO;

public class SkinManagerImpl implements SkinManager {
    private SkinRegistry registry;
    @Override
    public @NotNull SkinRegistry getRegistry() {
        return registry;
    }

    @Override
    public void reload() {
        var plugin = SelectionAPI.getInstance();
        var agent = plugin.getConfigManager().getConfig().getAgent();
        registry = new SkinRegistryImpl(agent != null ? new MineskinClient(agent) : new MineskinClient("MineSkin-JavaClient"));
        FileUtil.loadFolder(plugin, "skins", f -> {
            try {
                var fileName = FileUtil.getFileName(f);
                if (fileName.extension().equals("png")) {
                    var image = ImageIO.read(f);
                    if (image.getWidth() == 64 && (image.getHeight() == 32 || image.getHeight() == 64)) {
                        registry.create(fileName.name(), image);
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("This is not a valid skin file: " + f.getName());
            }
        });
    }
}
