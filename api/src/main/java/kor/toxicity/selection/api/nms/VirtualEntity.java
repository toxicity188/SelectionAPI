package kor.toxicity.selection.api.nms;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface VirtualEntity {
    void teleport(@NotNull Location location);
    void remove();
}
