package org.vined.ikea.modules.utility;

import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.block.entity.state.ChestBlockEntityRenderState;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import org.vined.ikea.IKEA;
import org.vined.ikea.utils.ChunkUtils;
import org.vined.ikea.utils.LogUtils;
import org.vined.ikea.utils.TimerUtils;

import java.util.ArrayList;
import java.util.List;

public class DubCounter extends Module {
    public List<BlockPos> coords = new ArrayList<>();
    public TimerUtils timer = new TimerUtils();

    public SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<CountMode> countMode = sgGeneral.add(new EnumSetting.Builder<CountMode>()
        .name("count-mode")
        .description("The way the chests are counted.")
        .defaultValue(CountMode.Loaded)
        .build()
    );

    private final Setting<Integer> loadTime = sgGeneral.add(new IntSetting.Builder()
        .name("loading-time")
        .description("How much time it's going to take to load all the dubs.")
        .defaultValue(1)
        .min(1)
        .sliderMax(60)
        .visible(() -> countMode.get() == CountMode.Rendered)
        .build()
    );

    public DubCounter() {
        super(IKEA.UTILITY, "dub-counter", "Counts how many double chests are in render distance.");
    }

    @Override
    public void onActivate() {
        timer.reset();
        if (countMode.get() == CountMode.Rendered) {
            LogUtils.info(Formatting.GRAY + "Please wait " + Formatting.WHITE + loadTime.get() + Formatting.GRAY + " second(s)...");
        } else {
            int length = ChunkUtils.getChestCount();
            int dubs = length % 2 == 0 ? length / 2 : (length - 1) / 2;
            LogUtils.info(Formatting.GRAY + "There are roughly " + Formatting.WHITE + dubs + Formatting.GRAY + " (" + length + " normal chests)" + Formatting.GRAY + " loaded double chests.");
            toggle();
        }
    }

    @Override
    public void onDeactivate() {
        coords.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (countMode.get() == CountMode.Rendered) {
            if (timer.hasReached(loadTime.get() * 1000L)) {
                int length = coords.size();
                int dubs = length % 2 == 0 ? length / 2 : (length - 1) / 2;
                LogUtils.info(Formatting.GRAY + "There are roughly " + Formatting.WHITE + dubs + Formatting.GRAY + " (" + length + " normal chests)" + Formatting.GRAY + " rendered double chests.");

                toggle();
                timer.reset();
            }
        }
    }

    @EventHandler
    private void onRenderBlockEntity(RenderBlockEntityEvent event) {
        if (countMode.get() == CountMode.Rendered) {
            BlockEntityRenderState block = event.blockEntityState;
            if (block instanceof ChestBlockEntityRenderState chest) {
                if (coords.contains(block.pos)) return;
                coords.add(block.pos);
            }
        }
    }

    public enum CountMode {
        Rendered,
        Loaded
    }
}
