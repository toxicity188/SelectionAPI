package kor.toxicity.selection.api.data;

import kor.toxicity.selection.api.SelectionAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.RenderedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import java.util.function.Supplier;


public record SkinData(@Nullable String name, @NotNull URL url, @NotNull Vector scale) implements Supplier<@NotNull ItemDisplayData> {
    private static final PlayerProfile PROFILE = Bukkit.createPlayerProfile(UUID.randomUUID(), null);

    public static @NotNull SkinData of(@NotNull InputStream stream) {
        return SelectionAPI.getInstance().getSkinManager().getRegistry().create(stream);
    }
    public static @NotNull SkinData of(@NotNull RenderedImage image) {
        return SelectionAPI.getInstance().getSkinManager().getRegistry().create(image);
    }

    @Override
    public @NotNull ItemDisplayData get() {
        var skull = new ItemStack(Material.PLAYER_HEAD);
        var meta = (SkullMeta) skull.getItemMeta();
        assert meta != null;
        var profile = PROFILE.clone();
        var textures = profile.getTextures();
        textures.setSkin(url);
        profile.setTextures(textures);
        meta.setOwnerProfile(profile);
        skull.setItemMeta(meta);
        return new ItemDisplayData(skull, scale);
    }
}
