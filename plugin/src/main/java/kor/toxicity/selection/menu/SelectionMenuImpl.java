package kor.toxicity.selection.menu;

import kor.toxicity.selection.api.SelectionAPI;
import kor.toxicity.selection.api.data.ButtonData;
import kor.toxicity.selection.api.menu.CreationStrategy;
import kor.toxicity.selection.api.menu.SelectionMenu;
import kor.toxicity.selection.api.menu.SelectionMenuTask;
import kor.toxicity.selection.api.nms.VirtualDisplay;
import kor.toxicity.selection.api.nms.VirtualEntity;
import kor.toxicity.selection.api.nms.VirtualItemDisplay;
import kor.toxicity.selection.api.nms.VirtualTextDisplay;
import kor.toxicity.selection.util.VectorUtil;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class SelectionMenuImpl implements SelectionMenu {

    private static final Map<UUID, SelectionMenuTask> TASK_MAP = new HashMap<>();

    private final List<ButtonData> dataList;
    private final CreationStrategy strategy;
    private final long teleportTime;

    @Override
    public @NotNull SelectionMenuTask open(@NotNull Player player) {
        var playerLocation = player.getLocation().add(0, 1.5, 0);
        var yaw = VectorUtil.yawToRadian(playerLocation.getYaw());
        var itemOrigin = playerLocation.clone().add(VectorUtil.rotateYaw(strategy.getOrigin().clone(), yaw));
        itemOrigin.setYaw(itemOrigin.getYaw() + 180);

        var entityList = new ArrayList<SelectionEntity>();
        var size = dataList.size();
        var index = 0;
        for (ButtonData buttonData : dataList) {
            var menuLocation = strategy.createLocation(index++, size);
            var entityLocation = itemOrigin.clone().add(VectorUtil.rotateYaw(new Vector(menuLocation.x(), menuLocation.y(), menuLocation.z()), yaw));
            entityLocation.setYaw(entityLocation.getYaw() + menuLocation.yaw() + 180);
            entityLocation.setPitch(entityLocation.getPitch() + menuLocation.pitch());
            entityList.add(new SelectionEntity(player, buttonData, itemOrigin, entityLocation));
        }
        
        SelectionMenuTask task = new SelectionMenuTask() {

            private SelectionEntity selectedEntity;
            private final BukkitTask cancelTask = Bukkit.getScheduler().runTaskLaterAsynchronously(SelectionAPI.getInstance(), () -> {
                cancel();
                TASK_MAP.remove(player.getUniqueId());
            }, 200);
            private final BukkitTask moveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(SelectionAPI.getInstance(), () -> {
                var loc = player.getLocation();
                if (!Objects.equals(loc.getWorld(), playerLocation.getWorld())) {
                    cancel();
                    return;
                }
                if (loc.distance(playerLocation) > 5) {
                    cancel();
                    TASK_MAP.remove(player.getUniqueId());
                    return;
                }
                update();
            }, 1, 1);

            @Override
            public @NotNull Location getOrigin() {
                return playerLocation;
            }

            @Override
            public void update() {
                var newLoc = player.getLocation().add(0, 1.5, 0);
                var pitch = VectorUtil.pitchToRadian(newLoc.getPitch());
                var yaw = VectorUtil.yawToRadian(newLoc.getYaw());
                var vec = new Vector(1, 0, 0);
                for (int i = 0; i < 15; ++i) {
                    var append = newLoc.clone().add(VectorUtil.rotateYaw(VectorUtil.rotatePitch(vec.clone().multiply(i), pitch), yaw));
                    var first = entityList.stream().filter(e -> e.ready && e.location.distance(append) <= 1).findFirst().orElse(null);
                    if (first != null) {
                        if (selectedEntity == first) return;
                        else if (selectedEntity != null) selectedEntity.deselect();
                        first.select();
                        selectedEntity = first;
                        return;
                    }
                }
                if (selectedEntity != null) selectedEntity.deselect();
                selectedEntity = null;
            }

            @Override
            public void select() {
                if (selectedEntity != null) {
                    selectedEntity.action.accept(player);
                    cancel();
                    TASK_MAP.remove(player.getUniqueId());
                }
            }

            @Override
            public void cancel() {
                moveTask.cancel();
                entityList.forEach(SelectionEntity::cancel);
                cancelTask.cancel();
            }
        };
        var oldTask = TASK_MAP.put(player.getUniqueId(), task);
        if (oldTask != null) oldTask.cancel();
        return task;
    }

    public static SelectionMenuTask getTask(Player player) {
        return TASK_MAP.get(player.getUniqueId());
    }

    private class SelectionEntity {
        private final VirtualItemDisplay display;
        private final List<VirtualTextDisplay> textDisplays;
        private final Location origin, location;
        private final List<Component> components;
        private final Consumer<Player> action;

        private BukkitTask task;
        private IndexedTask teleportTask;
        private float yawMultiplier = 0;
        private boolean ready;

        private SelectionEntity(Player player, ButtonData data, Location origin, Location teleport) {
            var nms = SelectionAPI.getInstance().getNms();
            display = nms.createItemDisplay(player, origin);

            var itemDisplay = data.skinData().get();

            display.item(itemDisplay.itemStack());
            display.setScale(itemDisplay.scale());
            components = data.text();
            textDisplays = new ArrayList<>();
            var height = components.size() * 0.2 + 0.5;
            teleportTask = teleport(display, origin, teleport,  () -> {
                ready = true;
                var i = 0;
                for (Component component : components) {
                    var textDisplay = nms.createTextDisplay(player, teleport.clone().add(0, height - 0.2 * (i++), 0));
                    textDisplay.text(component);
                    textDisplays.add(textDisplay);
                }
            });
            this.origin = origin;
            location = teleport;
            action = data.playerConsumer();
        }

        private void cancel() {
            if (task != null) task.cancel();
            if (teleportTask != null) teleportTask.cancel();
            textDisplays.forEach(VirtualDisplay::remove);
            teleportTask = teleport(display, location, origin, display::remove);
        }

        private void select() {
            var index = 0;
            for (VirtualTextDisplay textDisplay : textDisplays) {
                textDisplay.text(components.get(index++).style(Style.style(NamedTextColor.GREEN, TextDecoration.BOLD, TextDecoration.UNDERLINED)));
            }
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(SelectionAPI.getInstance(), () -> {
                var loc = location.clone().add(0, 0.25, 0);
                loc.setYaw(loc.getYaw() + (yawMultiplier += 18));
                display.teleport(loc);
            }, 1, 1);
        }

        private void deselect() {
            if (task != null) task.cancel();
            yawMultiplier = 0;
            var index = 0;
            for (VirtualTextDisplay textDisplay : textDisplays) {
                textDisplay.text(components.get(index++));
            }
            display.teleport(location);
        }
    }

    private IndexedTask teleport(VirtualEntity entity, Location from, Location to, Runnable runnable) {
        var minus = to.toVector().subtract(from.toVector());
        var divide = 1D / teleportTime;
        return new IndexedTask(i -> entity.teleport(from.clone().add(minus.clone().multiply(i * divide))), runnable);
    }

    private class IndexedTask {
        private final BukkitTask task;
        private int index = 0;
        private IndexedTask(Consumer<Integer> consumer, Runnable end) {
            task = Bukkit.getScheduler().runTaskTimerAsynchronously(SelectionAPI.getInstance(), () -> {
                if (index == teleportTime) {
                    end.run();
                    cancel();
                } else {
                    consumer.accept(index);
                }
                index++;
            },1, 1);
        }

        private void cancel() {
            task.cancel();
        }
    }
}
