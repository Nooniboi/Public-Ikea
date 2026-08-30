package org.vined.ikea.modules.utility;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.vined.ikea.IKEA;
import org.vined.ikea.utils.TimerUtils;
import java.util.List;

@SuppressWarnings("ALL")
public class AutoShulkerDrop extends Module {
    public TimerUtils timer = new TimerUtils();
    public SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> throwTimer = sgGeneral.add(new DoubleSetting.Builder()
        .name("throw-delay")
        .description("The delay between throwing items.")
        .defaultValue(0.1)
        .min(0)
        .sliderMax(10)
        .build()
    );

    private final Setting<List<Item>> shulkers = sgGeneral.add(new ItemListSetting.Builder()
        .name("shulkers")
        .description("Shulkers to drop.")
        .filter(Utils::isShulker)
        .build()
    );

    public AutoShulkerDrop() {
        super(IKEA.UTILITY, "auto-shulker-drop", "Inventory tweaks auto drop but with a delay and shulkers.");
    }

    @Override
    public void onDeactivate() {
        timer.reset();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.screen != null || shulkers.get().isEmpty()) return;
        for (int i = 0; i < mc.player.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = mc.player.getInventory().getItem(i);
            if (shulkers.get().contains(itemStack.getItem())) {
                if (!timer.hasReached((long) (throwTimer.get() * 1000))) continue;
                InvUtils.drop().slot(i);
                timer.reset();
            }
        }
    }
}
