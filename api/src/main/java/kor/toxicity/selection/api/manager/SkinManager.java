package kor.toxicity.selection.api.manager;

import kor.toxicity.selection.api.registry.SkinRegistry;
import org.jetbrains.annotations.NotNull;

public interface SkinManager extends SelectionAPIManager {
    @NotNull SkinRegistry getRegistry();
}
