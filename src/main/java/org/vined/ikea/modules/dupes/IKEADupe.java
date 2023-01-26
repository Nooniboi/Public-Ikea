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

@SuppressWarnings("ALL")
public class IKEADupe extends Module {
    public IKEADupe() {
        super(IKEA.DUPES, "ikea-dupe", "Does the boat dupe. (Make sure an alt or your friend is in render distance for it to work)");
    }

    public ClientPlayNetworkHandler handler;
    private final TimerUtils timer = new TimerUtils();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Faces the boat.")
        .defaultValue(true)
        .build()
    );

    @Override
    public void onActivate() {
        assert mc.getNetworkHandler() != null;
        handler = mc.getNetworkHandler();
    }

    @Override
    public void onDeactivate() {
        timer.reset();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onTickBoat(TickEvent.Post event) {
        assert mc.world != null;
        assert mc.player != null;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof ChestBoatEntity nearestBoat) {
                if (PlayerUtils.distanceTo(nearestBoat.getPos()) > 5.5) continue;
                if (!nearestBoat.hasPassenger(mc.player)) {
                    if (timer.hasReached(100)) {
                        sit(nearestBoat);
                        timer.reset();
                    }
                } else {
                    boatInventory();

                    if (timer.hasReached(100)) {
                        sendDismountPackets(nearestBoat);
                        if (mc.currentScreen instanceof HandledScreen screen) {
                            if (screen instanceof GenericContainerScreen) {
                                if (nearestBoat.hasPassenger(mc.player)) {
                                    sendDismountPackets(nearestBoat);
                                }
                            }
                        }
                        timer.reset();
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onTickThrow(TickEvent.Post event) {
        assert mc.world != null;
        assert mc.interactionManager != null;
        assert mc.player != null;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof ChestBoatEntity nearestBoat) {
                if (PlayerUtils.distanceTo(nearestBoat.getPos()) > 5.5) continue;
                if (!nearestBoat.hasPassenger(mc.player)) {
                    if(mc.currentScreen instanceof HandledScreen screen) {
                        if (screen instanceof GenericContainerScreen container) {
                            Inventory inv = container.getScreenHandler().getInventory();
                            if (!inv.isEmpty()) {
                                for (int i = 0; i < inv.size(); i++) {
                                    InvUtils.drop().slotId(i);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        toggle();
    }

    @EventHandler
    private void onScreenOpen(OpenScreenEvent event) {
        if (event.screen instanceof DisconnectedScreen) {
            toggle();
        }
    }

    public void sit(ChestBoatEntity boat) {
        interact(boat);
    }
    private void interact(Entity entity) {
        assert mc.interactionManager != null;
        if (rotate.get()) Rotations.rotate(Rotations.getYaw(entity), Rotations.getPitch(entity), -100, () -> mc.interactionManager.interactEntity(mc.player, entity, Hand.MAIN_HAND));
        else mc.interactionManager.interactEntity(mc.player, entity, Hand.MAIN_HAND);
    }

    private void boatInventory() {
        assert mc.player != null;
        if (mc.currentScreen instanceof HandledScreen screen) {
            if (screen instanceof GenericContainerScreen) return;
        }
        mc.player.openRidingInventory();
    }

    private void sendDismountPackets(ChestBoatEntity boat) {
        assert mc.player != null;
        assert handler != null;
        handler.sendPacket(new PlayerInputC2SPacket(1, 1, false, true));
        handler.sendPacket(new VehicleMoveC2SPacket(boat));
        handler.sendPacket(new BoatPaddleStateC2SPacket(false, false));
        handler.sendPacket(new TeleportConfirmC2SPacket(1));
        handler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
    }
}
