package kor.toxicity.selection.menu;

import kor.toxicity.selection.api.data.ButtonData;
import kor.toxicity.selection.api.exception.EmptyButtonException;
import kor.toxicity.selection.api.menu.CreationStrategy;
import kor.toxicity.selection.api.menu.SelectionMenu;
import kor.toxicity.selection.api.menu.SelectionMenuBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SelectionMenuBuilderImpl implements SelectionMenuBuilder {
    private final List<ButtonData> dataList = new ArrayList<>();
    private long teleportTime = 5L;
    private CreationStrategy strategy = CreationStrategy.DEFAULT;
    @Override
    public @NotNull SelectionMenuBuilder addButton(@NotNull ButtonData... data) {
        Collections.addAll(dataList, Objects.requireNonNull(data));
        return this;
    }

    @Override
    public @NotNull SelectionMenuBuilder setCreationStrategy(@NotNull CreationStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
        return this;
    }

    @Override
    public @NotNull SelectionMenu build() {
        if (dataList.isEmpty()) throw new EmptyButtonException();
        return new SelectionMenuImpl(dataList, strategy, teleportTime);
    }

    @Override
    public @NotNull SelectionMenuBuilder setTeleportTime(long teleportTime) {
        this.teleportTime = teleportTime;
        return this;
    }
}
