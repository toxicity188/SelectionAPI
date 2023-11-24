package kor.toxicity.selection.api.registry;

import kor.toxicity.selection.api.data.SkinData;
import kor.toxicity.selection.api.exception.ConnectionFailException;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.RenderedImage;
import java.io.InputStream;
import java.net.URL;

public interface SkinRegistry {
    @Nullable SkinData getByName(@NotNull String name);

    default @NotNull SkinData create(@NotNull InputStream stream) throws ConnectionFailException {
        return create(null, stream);
    }
    default @NotNull SkinData create(@NotNull RenderedImage image) throws ConnectionFailException {
        return create(null, image);
    }
    default @NotNull SkinData create(@Nullable String name, @NotNull InputStream stream) throws ConnectionFailException {
        return create(name, new Vector(1,1,1), stream);
    }
    default @NotNull SkinData create(@Nullable String name, @NotNull RenderedImage stream) throws ConnectionFailException {
        return create(name, new Vector(1,1,1), stream);
    }
    default @NotNull SkinData create(@NotNull URL url) {
        return create(null, url);
    }
    default @NotNull SkinData create(@Nullable String name, @NotNull URL url) {
        return create(name, new Vector(1,1,1) ,url);
    }

    @NotNull SkinData create(@Nullable String name, @NotNull Vector scale, @NotNull InputStream stream) throws ConnectionFailException;
    @NotNull SkinData create(@Nullable String name, @NotNull Vector scale, @NotNull RenderedImage stream) throws ConnectionFailException;
    @NotNull SkinData create(@Nullable String name, @NotNull Vector scale, @NotNull URL url);
}
