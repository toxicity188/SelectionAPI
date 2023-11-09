package kor.toxicity.selection.api.menu;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public interface CreationStrategy {
    @NotNull CreationStrategy DEFAULT = CreationStrategies.DEFAULT.builder().build();
    @NotNull Vector getOrigin();
    @NotNull MenuLocation createLocation(int index, int size);
}
