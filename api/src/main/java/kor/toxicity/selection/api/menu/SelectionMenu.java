package kor.toxicity.selection.api.menu;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface SelectionMenu {
    @NotNull SelectionMenuTask open(@NotNull Player player);
}
