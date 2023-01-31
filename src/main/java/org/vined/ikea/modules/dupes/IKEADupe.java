package org.vined.ikea.modules.dupes;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;

import meteordevelopment.orbit.EventPriority;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.Hand;

import org.vined.ikea.IKEA;
import org.vined.ikea.utils.TimerUtils;

import static org.vined.ikea.utils.PacketUtils.dismountPackets;

@SuppressWarnings("ALL")
public class IKEADupe extends Module {

    public IKEADupe(){
        super(IKEA.DUPES,"IKEA-dupe-2.0","Does the updated IKEA dupe.");
    }

    private final TimerUtils boatTimer = new TimerUtils();
    private final TimerUtils throwTimer = new TimerUtils();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<IKEADupe.Modes> modes = sgGeneral.add(new EnumSetting.Builder<IKEADupe.Modes>()
        .name("modes")
        .defaultValue(IKEADupe.Modes.Boat)
        .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Faces the boat.")
        .defaultValue(true)
        .visible(() -> modes.get() == Modes.Boat)
        .build()
    );

    private final Setting<Double> boatTime = sgGeneral.add(new DoubleSetting.Builder()
        .name("boat-timer")
        .description("Delay between entering and exiting the boat.")
        .defaultValue(0.5)
        .min(0.1)
        .visible(() -> modes.get() == Modes.Boat)
        .build()
    );

    @Override
    public void onDeactivate() {
        boatTimer.reset();
        throwTimer.reset();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onTickBoat(TickEvent.Post event) {
        if (modes.get() != Modes.Boat) return;
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof ChestBoatEntity boat) {
                if (PlayerUtils.distanceTo(boat.getPos()) > 5.5) continue;
                if (!boat.hasPassengers()) {
                    if (boatTimer.hasReached((long) (boatTime.get() * 1000))) {
                        interact(boat);
                        boatTimer.reset();
                    }
                } else {
                    if (boatTimer.hasReached((long) (boatTime.get() * 1000))) {
                        dismountPackets(boat);
                        boatTimer.reset();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onTickThrow(TickEvent.Pre event) {
        if (modes.get() != Modes.Throw) return;
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof ChestBoatEntity boat) {
                if (PlayerUtils.distanceTo(boat.getPos()) > 5.5) continue;
                if (!boat.hasPassengers()) {
                    if (mc.currentScreen instanceof HandledScreen screen) {
                        if (screen instanceof GenericContainerScreen container) {
                            Inventory inv = container.getScreenHandler().getInventory();
                            if (throwTimer.hasReached(100)) {
                                for (int i = 0; i < inv.size(); i++) {
                                    InvUtils.drop().slotId(i);
                                }
                            }
                            throwTimer.reset();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) { toggle(); }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        if (event.screen instanceof DisconnectedScreen) {
            toggle();
        }
    }

    private void interact(Entity entity) {
        if (rotate.get()) {
            Rotations.rotate(Rotations.getYaw(entity), Rotations.getPitch(entity), -100, () -> mc.interactionManager.interactEntity(mc.player, entity, Hand.MAIN_HAND));
        } else {
            mc.interactionManager.interactEntity(mc.player, entity, Hand.MAIN_HAND);
        }
    }

    public enum Modes {
        Boat,
        Throw
    }
}
