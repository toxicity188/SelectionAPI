package kor.toxicity.selection;

import kor.toxicity.selection.api.SelectionAPI;
import kor.toxicity.selection.api.manager.SelectionAPIManager;
import kor.toxicity.selection.api.menu.SelectionMenuBuilder;
import kor.toxicity.selection.api.nms.NMS;
import kor.toxicity.selection.manager.ConfigManagerImpl;
import kor.toxicity.selection.manager.SkinManagerImpl;
import kor.toxicity.selection.menu.SelectionMenuBuilderImpl;
import kor.toxicity.selection.menu.SelectionMenuImpl;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public final class SelectionAPIImpl extends SelectionAPI {
    private List<SelectionAPIManager> managerList;
    @Override
    public void onEnable() {
        var pluginManager = Bukkit.getPluginManager();
        setAudiences(BukkitAudiences.create(this));
        try {
            setNms((NMS) Class.forName("kor.toxicity.selection.nms." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + ".NMSImpl").getConstructor().newInstance());
        } catch (Exception e) {
            getLogger().warning("Unsupported version found.");
            getLogger().warning("Plugin will automatically disabled.");
            pluginManager.disablePlugin(this);
            return;
        }
        var command = getCommand("selection");
        if (command != null) command.setExecutor((sender, command1, label, args) -> {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You are not OP!");
            } else {
                reloadAllAsync().thenAccept(v -> sender.sendMessage(ChatColor.GREEN + "Reload Completed!"));
            }
            return true;
        });
        pluginManager.registerEvents(new Listener() {
            @EventHandler
            public void click(PlayerInteractEvent event) {
                var player = event.getPlayer();
                var task = SelectionMenuImpl.getTask(player);
                if (task == null) return;
                task.select();
            }
        }, this);

        var configManager = new ConfigManagerImpl();
        var skinManager = new SkinManagerImpl();

        setConfigManager(configManager);
        setSkinManager(skinManager);

        managerList = List.of(
                configManager,
                skinManager
        );
        reloadAll();
        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    @Override
    public void reloadAll() {
        managerList.forEach(SelectionAPIManager::reload);
    }

    @Override
    public @NotNull SelectionMenuBuilder builder() {
        return new SelectionMenuBuilderImpl();
    }
}
