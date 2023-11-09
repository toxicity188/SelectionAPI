package kor.toxicity.selection.nms.v1_20_R2;

import com.mojang.math.Transformation;
import kor.toxicity.selection.api.nms.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class NMSImpl implements NMS {

    @Override
    public @NotNull VirtualItemDisplay createItemDisplay(@NotNull Player player, @NotNull Location location) {
        return new VirtualItemDisplayImpl(player, location);
    }

    @Override
    public @NotNull VirtualTextDisplay createTextDisplay(@NotNull Player player, @NotNull Location location) {
        return new VirtualTextDisplayImpl(player, location);
    }

    private static abstract class VirtualEntityImpl<T extends Entity> implements VirtualEntity {
        protected final T entity;
        protected final ServerGamePacketListenerImpl conn;
        private VirtualEntityImpl(T entity, Player player, Location location) {
            this.entity = entity;
            entity.moveTo(
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    location.getYaw(),
                    0
            );
            conn = ((CraftPlayer) player).getHandle().connection;
            conn.send(new ClientboundAddEntityPacket(entity));
            var watcher = entity.getEntityData().getNonDefaultValues();
            if (watcher != null) conn.send(new ClientboundSetEntityDataPacket(entity.getId(), watcher));
        }

        @Override
        public void teleport(@NotNull Location location) {
            entity.moveTo(
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    location.getYaw(),
                    0
            );
            conn.send(new ClientboundTeleportEntityPacket(entity));
        }

        @Override
        public void remove() {
            conn.send(new ClientboundRemoveEntitiesPacket(entity.getId()));
        }
    }
    private static abstract class VirtualDisplayImpl<T extends Display> extends VirtualEntityImpl<T> implements VirtualDisplay {

        public VirtualDisplayImpl(T entity, Player player, Location location) {
            super(apply(entity, d -> d.setBrightnessOverride(new Brightness(15, 15))), player, location);
        }

        @Override
        public void setScale(@NotNull Vector vector) {
            entity.setTransformation(new Transformation(null, null, new Vector3f((float) vector.getX(), (float) vector.getY(), (float) vector.getZ()), null));
            var watcher = entity.getEntityData().getNonDefaultValues();
            if (watcher != null) conn.send(new ClientboundSetEntityDataPacket(entity.getId(), watcher));
        }
    }
    private static class VirtualTextDisplayImpl extends VirtualDisplayImpl<Display.TextDisplay> implements VirtualTextDisplay {
        private VirtualTextDisplayImpl(Player player, Location location) {
            super(apply(new Display.TextDisplay(EntityType.TEXT_DISPLAY, ((CraftWorld) location.getWorld()).getHandle()), d -> d.setBillboardConstraints(Display.BillboardConstraints.CENTER)), player, location);
        }

        @Override
        public void text(@NotNull Component component) {
            entity.setText(CraftChatMessage.fromJSON(GsonComponentSerializer.gson().serialize(component)));
            var watcher = entity.getEntityData().getNonDefaultValues();
            if (watcher != null) conn.send(new ClientboundSetEntityDataPacket(entity.getId(), watcher));
        }
    }
    private static class VirtualItemDisplayImpl extends VirtualDisplayImpl<Display.ItemDisplay> implements VirtualItemDisplay {
        private VirtualItemDisplayImpl(Player player, Location location) {
            super(new Display.ItemDisplay(EntityType.ITEM_DISPLAY, ((CraftWorld) location.getWorld()).getHandle()), player, location);
        }

        @Override
        public void item(@NotNull ItemStack itemStack) {
            entity.setItemStack(CraftItemStack.asNMSCopy(itemStack));
            var watcher = entity.getEntityData().getNonDefaultValues();
            if (watcher != null) conn.send(new ClientboundSetEntityDataPacket(entity.getId(), watcher));
        }
    }

    private static <T> T apply(T t, Consumer<T> consumer) {
        consumer.accept(t);
        return t;
    }
}