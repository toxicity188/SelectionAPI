package kor.toxicity.selection.api;

import kor.toxicity.selection.api.manager.ConfigManager;
import kor.toxicity.selection.api.manager.SkinManager;
import kor.toxicity.selection.api.menu.SelectionMenuBuilder;
import kor.toxicity.selection.api.nms.NMS;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
@SuppressWarnings("unused")
public abstract class SelectionAPI extends JavaPlugin {
    private static SelectionAPI api;

    private @NotNull BukkitAudiences audiences;
    private @NotNull NMS nms;
    private @NotNull ConfigManager configManager;
    private @NotNull SkinManager skinManager;

    @Override
    public final void onLoad() {
        if (api != null) throw new SecurityException();
        api = this;
    }

    public static @NotNull SelectionAPI getInstance() {
        return Objects.requireNonNull(api);
    }

    public abstract void reloadAll();
    public abstract @NotNull SelectionMenuBuilder builder();

    public final CompletableFuture<Void> reloadAllAsync() {
        return CompletableFuture.runAsync(this::reloadAll);
    }
}