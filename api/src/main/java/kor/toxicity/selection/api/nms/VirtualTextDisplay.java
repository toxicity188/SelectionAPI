package kor.toxicity.selection.api.nms;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface VirtualTextDisplay extends VirtualDisplay {
    void text(@NotNull Component component);
}
