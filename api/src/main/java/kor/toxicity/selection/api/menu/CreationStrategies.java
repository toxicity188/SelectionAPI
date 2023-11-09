package kor.toxicity.selection.api.menu;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public enum StandardCreationStrategy implements CreationStrategy {
    DEFAULT {
        @Override
        public @NotNull Vector getOrigin() {
            return new Vector(1, 0, 0);
        }

        @Override
        public @NotNull MenuLocation createLocation(int index, int size) {
            double degree = 2 * Math.PI * index / size;

            double cos = Math.cos(degree);
            double sin = Math.sin(degree);

            return null;
        }
    }
}
