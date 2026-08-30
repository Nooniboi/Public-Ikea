package org.vined.ikea.modules.utility;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.ItemListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import org.vined.ikea.IKEA;
import org.vined.ikea.utils.TimerUtils;
import java.util.List;

public class AutoItemMove extends Module {
    public SettingGroup sgGeneral = settings.getDefaultGroup();
    public TimerUtils timer = new TimerUtils();

    private final Setting<List<Item>> items = sgGeneral.add(new ItemListSetting.Builder()
        .name("items")
        .description("Which items to put in the container.")
        .build()
    );

    private final Setting<Double> itemTimer = sgGeneral.add(new DoubleSetting.Builder()
        .name("item-timer")
        .description("Delay between putting items in the chest.")
        .defaultValue(0.05)
        .min(0)
        .build()
    );

    public AutoItemMove() {
        super(IKEA.UTILITY, "auto-item-move", "Automatically puts items in a container.");
    }

    @Override
    public void onActivate() {
        timer.reset();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        assert mc.player != null;
        AbstractContainerMenu handler = mc.player.containerMenu;
        if (handler instanceof ChestMenu || handler instanceof ShulkerBoxMenu) {
            moveItems(handler);
        }
    }

    private int getRows(AbstractContainerMenu handler) {
        return (handler instanceof ChestMenu ? ((ChestMenu) handler).getRowCount() : 3);
    }

    public void moveItems(AbstractContainerMenu handler) {
        int playerInvOffset = getRows(handler) * 9;
        MeteorExecutor.execute(() -> moveSlots(handler, playerInvOffset, playerInvOffset + 4 * 9));
    }

    private void moveSlots(AbstractContainerMenu handler, int start, int end) {
        for (int i = start; i < end; i++) {
            Slot slot = handler.getSlot(i);
            if (!slot.hasItem()) continue;
            if (items.get().contains(slot.getItem().getItem())) {
                if (timer.hasReached((long) (itemTimer.get() * 1000))) {
                    InvUtils.shiftClick().slotId(i);
                    timer.reset();
                }
            }
        }
    }
}
