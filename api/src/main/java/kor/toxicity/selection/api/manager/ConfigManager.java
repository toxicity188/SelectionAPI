package kor.toxicity.selection.api.manager;

import kor.toxicity.selection.api.config.APIConfig;
import org.jetbrains.annotations.NotNull;

public interface ConfigManager extends SelectionAPIManager {
    @NotNull APIConfig getConfig();
}
