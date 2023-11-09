package kor.toxicity.selection.api.nms;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface VirtualTextDisplay extends VirtualDisplay {
    void text(@Nullable Component component);
}
