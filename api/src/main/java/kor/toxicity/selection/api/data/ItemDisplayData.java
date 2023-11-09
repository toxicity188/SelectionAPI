package kor.toxicity.selection.api.data;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public record ItemDisplayData(@NotNull ItemStack itemStack, @NotNull Vector scale) {
}
