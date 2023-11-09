package kor.toxicity.selection.api.menu;

import kor.toxicity.selection.api.data.ButtonData;
import kor.toxicity.selection.api.exception.EmptyButtonException;
import org.jetbrains.annotations.NotNull;

public interface SelectionMenuBuilder {
    @NotNull SelectionMenuBuilder addButton(@NotNull ButtonData... data);
    @NotNull SelectionMenuBuilder setCreationStrategy(@NotNull CreationStrategy strategy);
    @NotNull SelectionMenuBuilder setTeleportTime(long time);
    @NotNull SelectionMenu build() throws EmptyButtonException;
}
