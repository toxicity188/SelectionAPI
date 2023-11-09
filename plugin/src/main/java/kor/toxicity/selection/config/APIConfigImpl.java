package kor.toxicity.selection.config;

import kor.toxicity.selection.api.config.APIConfig;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

@Getter
public class APIConfigImpl implements APIConfig {
    private final String agent;
    public APIConfigImpl(ConfigurationSection section) {
        agent = section.getString("agent");
    }
}
