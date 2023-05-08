package org.vined.ikea.modules.misc;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.utils.player.SlotUtils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import org.vined.ikea.IKEA;
import org.vined.ikea.utils.TimerUtils;

import java.util.List;

public class IKEADupe extends Module {

    public IKEADupe() {
        super(IKEA.MISC, "IKEA-dupe-5.0", "Does the updated IKEA dupe.");
    }

    private final TimerUtils timer = new TimerUtils();

    public SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> delay = sgGeneral.add(new DoubleSetting.Builder()
        .name("delay")
        .description("Amount of time to wait between the steal and dump actions.")
        .defaultValue(0.5)
        .min(0.3)
        .build());

    private final Setting<List<Item>> shulkers = sgGeneral.add(new ItemListSetting.Builder()
        .name("shulkers")
        .description("Shulkers to dupe.")
        .filter(IKEADupe::isShulker)
        .build()
    );

    @Override
    public void onActivate() {
        timer.reset();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        assert mc.player != null;
        ScreenHandler handler = mc.player.currentScreenHandler;

        if (handler instanceof GenericContainerScreenHandler container) {
            MeteorExecutor.execute(() -> chestDupe(container));
        }
    }

    private void chestDupe(GenericContainerScreenHandler handler) {
        int offset = SlotUtils.indexToId(SlotUtils.MAIN_START);
        if (timer.hasReached((long) (delay.get() * 1000))) {
            for (int i = 0; i < SlotUtils.indexToId(SlotUtils.MAIN_START); i++) {
                Slot slot = handler.getSlot(i);
                if (mc.currentScreen == null) break;
                if (!slot.hasStack()) continue;
                if (shulkers.get().contains(slot.getStack().getItem())) {
                    InvUtils.quickMove().slotId(i);
                }
            }
        }

        if (timer.hasReached((long) (delay.get() * 1000))) {
            for (int i = offset; i < offset + 4 * 9; i++) {
                Slot slot = handler.getSlot(i);
                if (mc.currentScreen == null) break;
                if (!slot.hasStack()) continue;
                if (shulkers.get().contains(slot.getStack().getItem())) {
                    InvUtils.quickMove().slotId(i);
                }
            }
            timer.reset();
        }
    }

    public static boolean isShulker(Item item) {
        return
            item == Items.SHULKER_BOX ||
            item == Items.RED_SHULKER_BOX ||
            item == Items.BLUE_SHULKER_BOX ||
            item == Items.YELLOW_SHULKER_BOX ||
            item == Items.GRAY_SHULKER_BOX ||
            item == Items.LIGHT_GRAY_SHULKER_BOX ||
            item == Items.CYAN_SHULKER_BOX ||
            item == Items.BROWN_SHULKER_BOX ||
            item == Items.BLACK_SHULKER_BOX ||
            item == Items.GREEN_SHULKER_BOX ||
            item == Items.LIGHT_BLUE_SHULKER_BOX ||
            item == Items.LIME_SHULKER_BOX ||
            item == Items.PINK_SHULKER_BOX ||
            item == Items.MAGENTA_SHULKER_BOX ||
            item == Items.PURPLE_SHULKER_BOX ||
            item == Items.WHITE_SHULKER_BOX ||
            item == Items.ORANGE_SHULKER_BOX;
    }
}

