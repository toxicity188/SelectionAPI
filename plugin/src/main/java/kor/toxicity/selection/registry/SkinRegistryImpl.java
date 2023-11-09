package kor.toxicity.selection.registry;

import kor.toxicity.selection.api.data.SkinData;
import kor.toxicity.selection.api.exception.ConnectionFailException;
import kor.toxicity.selection.api.registry.SkinRegistry;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineskin.MineskinClient;
import org.mineskin.SkinOptions;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SkinRegistryImpl implements SkinRegistry {
    private final MineskinClient client;
    private final Map<String, SkinData> dataMap = new HashMap<>();
    @Override
    public @Nullable SkinData getByName(@NotNull String name) {
        return dataMap.get(name);
    }

    @Override
    public @NotNull SkinData create(@Nullable String name, @NotNull Vector scale, @NotNull InputStream stream) {
        try {
            var skin = client.generateUpload(stream, SkinOptions.none(), name + ".png").join();
            var data = new SkinData(name, new URL(skin.data.texture.url), scale);
            if (name != null) dataMap.put(data.name(), data);
            return data;
        } catch (Exception e) {
            throw new ConnectionFailException(e);
        }
    }
    @Override
    public @NotNull SkinData create(@Nullable String name, @NotNull Vector scale, @NotNull RenderedImage image) {
        try (var stream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", stream);
            try (var input = new ByteArrayInputStream(stream.toByteArray())) {
                return create(name, scale, input);
            } catch (Exception e) {
                throw new ConnectionFailException(e);
            }
        } catch (Exception e) {
            throw new ConnectionFailException(e);
        }
    }
}
