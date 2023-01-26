package org.vined.ikea.modules.utility;

import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import org.vined.ikea.IKEA;
import org.vined.ikea.utils.TimerUtils;

import java.util.ArrayList;
import java.util.List;

public class DubCounter extends Module {
    public List<BlockPos> coords = new ArrayList<>();
    public TimerUtils timer = new TimerUtils();

    public SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Integer> loadTime = sgGeneral.add(new IntSetting.Builder()
        .name("load-time")
        .description("How much time it's going to take to load all the dubs. (useful if there are dubs out of render distance so you can load them or if you have a performance mod)")
        .defaultValue(1)
        .min(1)
        .sliderMax(60)
        .build()
    );


    public DubCounter() {
        super(IKEA.UTILITY, "dub-counter", "Counts how many dubs are in render distance.");
    }

    @Override
    public void onActivate() {
        timer.reset();
        info("Please wait " + Formatting.WHITE + loadTime.get() + Formatting.GRAY + " second(s)...");
    }

    @Override
    public void onDeactivate() {
        coords.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (timer.hasReached(loadTime.get() * 1000L)) {
            int length = coords.size();
            if (length % 2 == 0) {
                int dubs = length / 2;
                info("There are roughly " + Formatting.WHITE + dubs + Formatting.GRAY + " (" + length + " normal chests) rendered double chests.");
                toggle();
            } else {
                int fixed = length - 1;
                int dubs = fixed / 2;
                info("There are roughly " + Formatting.WHITE + dubs + Formatting.GRAY + " (" + fixed + " normal chests) rendered double chests.");
                toggle();
            }
            timer.reset();
        }
    }

    @EventHandler
    private void onRenderBlockEntity(RenderBlockEntityEvent event) {
        BlockEntity block = event.blockEntity;
        if (block instanceof ChestBlockEntity chest) {
            if (!coords.contains(chest.getPos())) {
                coords.add(chest.getPos());
            }
        }
    }
}
