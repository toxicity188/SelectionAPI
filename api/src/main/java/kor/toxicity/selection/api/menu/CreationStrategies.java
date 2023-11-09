package kor.toxicity.selection.api.menu;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public enum CreationStrategies {
    DEFAULT {
        @Override
        public @NotNull CreationStrategyBuilder builder() {
            return new CreationStrategyBuilder() {
                private double scale = 1;
                private Vector vector = new Vector(1.5, 0, 0);
                @Override
                public @NotNull CreationStrategyBuilder setScale(double scale) {
                    this.scale = scale;
                    return this;
                }

                @Override
                public @NotNull CreationStrategyBuilder setOrigin(@NotNull Vector vector) {
                    this.vector = Objects.requireNonNull(vector);
                    return this;
                }

                @Override
                public @NotNull CreationStrategy build() {
                    return new CreationStrategy() {
                        @Override
                        public @NotNull Vector getOrigin() {
                            return vector;
                        }

                        @Override
                        public @NotNull MenuLocation createLocation(int index, int size) {
                            double degree = 2 * Math.PI * index / size;

                            return new MenuLocation(
                                    0,
                                    Math.cos(degree) * scale,
                                    Math.sin(degree) * scale,
                                    0,
                                    0
                            );
                        }
                    };
                }
            };
        }
    }
    ;
    public abstract @NotNull CreationStrategyBuilder builder();
}
