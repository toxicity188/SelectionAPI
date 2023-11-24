package kor.toxicity.selection.api.data;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.awt.image.RenderedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record ButtonData(@NotNull Supplier<@NotNull ItemDisplayData> skinData, @NotNull Consumer<Player> playerConsumer, @NotNull List<Component> text) {
    public static @NotNull ButtonData of(@NotNull InputStream stream, @NotNull Consumer<Player> playerConsumer, @NotNull List<Component> components) {
        return new ButtonData(
                SkinData.of(stream),
                playerConsumer,
                components
        );
    }
    public static @NotNull ButtonData of(@NotNull RenderedImage image, @NotNull Consumer<Player> playerConsumer, @NotNull List<Component> components) {
        return new ButtonData(
                SkinData.of(image),
                playerConsumer,
                components
        );
    }
    public static @NotNull ButtonData of(@NotNull ItemStack itemStack, @NotNull Consumer<Player> playerConsumer, @NotNull List<Component> components) {
        return ButtonData.of(
                itemStack,
                new Vector(1, 1, 1),
                playerConsumer,
                components
        );
    }
    public static @NotNull ButtonData of(@NotNull ItemStack itemStack, @NotNull Vector scale, @NotNull Consumer<Player> playerConsumer, @NotNull List<Component> components) {
        var display = new ItemDisplayData(itemStack, scale);
        return new ButtonData(
                () -> display,
                playerConsumer,
                components
        );
    }
    public static @NotNull ButtonData of(@NotNull URL url, @NotNull Consumer<Player> playerConsumer, @NotNull List<Component> components) {
        return new ButtonData(
                SkinData.of(url),
                playerConsumer,
                components
        );
    }
}
