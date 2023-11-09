package kor.toxicity.selection.api.menu;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public interface CreationStrategyBuilder {
    @NotNull CreationStrategyBuilder setScale(double scale);
    @NotNull CreationStrategyBuilder setOrigin(@NotNull Vector vector);
    @NotNull CreationStrategy build();
}
