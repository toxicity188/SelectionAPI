package kor.toxicity.selection.api.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface NMS {
    @NotNull VirtualItemDisplay createItemDisplay(@NotNull Player player, @NotNull Location location);
    @NotNull VirtualTextDisplay createTextDisplay(@NotNull Player player, @NotNull Location location);
}
