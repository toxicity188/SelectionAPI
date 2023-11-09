package kor.toxicity.selection.api.menu;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface SelectionMenuTask {
    @NotNull Location getOrigin();
    void update();
    void select();
    void cancel();
}
