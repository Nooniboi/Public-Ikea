package org.vined.ikea.modules.utility;

import meteordevelopment.meteorclient.events.entity.player.BreakBlockEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import org.vined.ikea.IKEA;
import java.util.List;

public class AntiBreak extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> allBlocks = sgGeneral.add(
        new BoolSetting.Builder()
            .name("all-blocks")
            .description("If to disable breaking blocks completely.")
            .defaultValue(false)
            .build()
    );

    private final Setting<List<Block>> blocks = sgGeneral.add(
        new BlockListSetting.Builder()
            .name("blocks")
            .description("The blocks.")
            .visible(() -> !allBlocks.get())
            .build()
    );

    public AntiBreak() {
        super(IKEA.UTILITY, "anti-break", "Stops you from breaking certain blocks.");
    }

    @EventHandler
    private void onBreakBlock(BreakBlockEvent event) {
        if (allBlocks.get()) {
            event.cancel();
            return;
        }
        Block block = mc.level.getBlockState(event.blockPos).getBlock();
        if (blocks.get().contains(block)) {
            event.cancel();
        }
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (!(event.packet instanceof ServerboundPlayerActionPacket packet)) return;
        if (packet.getAction() != ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK) return;
        Block block = mc.level.getBlockState(packet.getPos()).getBlock();
        if (allBlocks.get()) {
            event.cancel();
            return;
        }
        if (blocks.get().contains(block)) {
            event.cancel();
        }
    }
}
