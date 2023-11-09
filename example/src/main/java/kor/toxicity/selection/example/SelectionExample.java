package kor.toxicity.selection.example;

import kor.toxicity.selection.api.SelectionAPI;
import kor.toxicity.selection.api.data.ButtonData;
import kor.toxicity.selection.api.menu.CreationStrategies;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class SelectionExample extends JavaPlugin {
    @Override
    public void onEnable() {
        var barrier = Objects.requireNonNull(getResource("barrier.png"));

        var menu = SelectionAPI.getInstance().builder()
                .setCreationStrategy(CreationStrategies.DEFAULT.builder().setScale(1.25).build())
                .addButton(
                        ButtonData.of(
                                barrier,
                                player -> player.sendMessage("Hello world!"),
                                List.of(
                                        Component.text("Hello world!").color(NamedTextColor.GREEN)
                                )
                        ),
                        ButtonData.of(
                                new ItemStack(Material.APPLE),
                                player -> player.sendMessage("This is an apple!"),
                                List.of(
                                        Component.text("Apple").color(NamedTextColor.RED)
                                )
                        ),
                        ButtonData.of(
                                new ItemStack(Material.NETHERITE_SWORD),
                                player -> player.sendMessage("You want that?"),
                                List.of(
                                        Component.text("Netherite Sword").color(NamedTextColor.GOLD)
                                )
                        )
                )
                .build();
        try {
            barrier.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Objects.requireNonNull(getCommand("menu")).setExecutor((sender, command, label, args) -> {
            if (!sender.isOp()) {
                sender.sendMessage("You are not OP!");
                return true;
            }
            if (sender instanceof Player player) {
                menu.open(player);
            } else {
                sender.sendMessage("Player only can execute this command!");
            }
            return true;
        });
    }
}
