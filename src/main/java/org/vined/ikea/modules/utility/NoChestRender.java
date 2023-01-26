package org.vined.ikea.modules.utility;

import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;

import org.vined.ikea.IKEA;

import java.util.Objects;


public class NoChestRender extends Module {
    public SettingGroup sgGeneral = settings.getDefaultGroup();
    public SettingGroup sgParty = settings.createGroup("Party Mode");

    private final Setting<Integer> radius = sgGeneral.add(new IntSetting.Builder()
        .name("render-radius")
        .description("The radius in which the storage blocks will render.")
        .defaultValue(0)
        .min(0)
        .sliderMax(128)
        .build()
    );

    private final Setting<Integer> partyModeMin = sgParty.add(new IntSetting.Builder()
        .name("party-min-radius")
        .description("The minimum radius party mode will change to.")
        .defaultValue(0)
        .min(0)
        .max(128)
        .sliderMax(128)
        .build()
    );

    private final Setting<Integer> partyModeMax = sgParty.add(new IntSetting.Builder()
        .name("party-max-radius")
        .description("The maximum radius party mode will change to.")
        .defaultValue(64)
        .min(1)
        .max(128)
        .sliderMax(128)
        .build()
    );

    private final Setting<Boolean> partyMode = sgParty.add(new BoolSetting.Builder()
        .name("party-mode")
        .description("Constantly changes the radius")
        .defaultValue(false)
        .build()
    );

    public boolean retreat = false;

    public NoChestRender() {
        super(IKEA.UTILITY, "no-chest-render", "Doesn't render chests.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (partyMode.get()) {
            if (partyModeMin.get() > partyModeMax.get()) {
                info("The party mode minimum can't be bigger than the party mode maximum, setting the minimum to 0...");
                partyModeMin.set(0);
                return;
            }
            if (retreat) {
                if (Objects.equals(radius.get(), partyModeMin.get())) {
                    retreat = false;
                    return;
                }
                radius.set(radius.get() - 1);
            } else {
                if (radius.get() >= partyModeMax.get()) {
                    retreat = true;
                } else {
                    radius.set(radius.get() + 1);
                }
            }
        }
    }

    @EventHandler
    private void onRenderBlockEntity(RenderBlockEntityEvent event) {
        BlockEntity block = event.blockEntity;
        if (block instanceof ChestBlockEntity) {
            if (PlayerUtils.distanceTo(block.getPos()) > radius.get()) {
                event.cancel();
            }
        }
    }
}
