package org.vined.ikea.modules.misc;

import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.vined.ikea.IKEA;
import org.vined.ikea.utils.BlockUtils;
import java.util.List;

public class NoRender extends Module {
    public SettingGroup sgBlocks = settings.createGroup("Blocks");
    public SettingGroup sgBlockEntities = settings.createGroup("Block Entities");

    public final Setting<Integer> radius = sgBlockEntities.add(new IntSetting.Builder()
        .name("render-radius")
        .description("The radius in which the blocks will render.")
        .defaultValue(0)
        .min(0)
        .sliderMax(128)
        .build()
    );

    public final Setting<List<Block>> blocks = sgBlocks.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("The blocks not to render. (fully)")
        .filter(block -> !BlockUtils.isBlockEntity(block))
        .onChanged(value -> refreshChunks())
        .build()
    );

    public final Setting<List<Block>> blockEntities = sgBlockEntities.add(new BlockListSetting.Builder()
        .name("block-entities")
        .description("Block entities hidden outside the render radius.")
        .defaultValue(Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.SHULKER_BOX,
            Blocks.SHULKER_BOX,
            Blocks.SHULKER_BOX,
            Blocks.WHITE_SHULKER_BOX,
            Blocks.ORANGE_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX,
            Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX,
            Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX,
            Blocks.LIGHT_GRAY_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX,
            Blocks.PURPLE_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX,
            Blocks.BROWN_SHULKER_BOX,
            Blocks.GREEN_SHULKER_BOX,
            Blocks.RED_SHULKER_BOX,
            Blocks.BLACK_SHULKER_BOX
        )
        .filter(block -> BlockUtils.isBlockEntity(block) && !isUnsupportedBlockEntity(block))
        .build()
    );

    public NoRender() {
        super(
            IKEA.MISC,
            "NoRender",
            "Disables rendering for selected blocks."
        );
    }

    public boolean shouldHide(BlockState state) {
        if (!isActive()) return false;
        return blocks.get().contains(state.getBlock());
    }

    @Override
    public void onActivate() {
        refreshChunks();
    }

    @Override
    public void onDeactivate() {
        refreshChunks();
    }

    private void refreshChunks() {
        mc.execute(mc.levelRenderer::allChanged);
    }

    @EventHandler
    private void RenderBlockEntity(RenderBlockEntityEvent event) {
        if (mc.level == null) return;

        BlockPos pos = event.blockEntityState.blockPos;
        Block block = mc.level.getBlockState(pos).getBlock();
        if (!blockEntities.get().contains(block)) return;

        if (PlayerUtils.distanceTo(pos) > radius.get()) {
            event.cancel();
        }
    }

    private boolean isUnsupportedBlockEntity(Block block) {
        //i genuinely do not care that these don't work under the RenderBlockEntityEvent this is just to filter
        return block == Blocks.SUSPICIOUS_SAND
            || block == Blocks.SUSPICIOUS_GRAVEL
            || block == Blocks.DISPENSER
            || block == Blocks.CHISELED_BOOKSHELF
            || block == Blocks.OAK_SHELF
            || block == Blocks.SPRUCE_SHELF
            || block == Blocks.BIRCH_SHELF
            || block == Blocks.JUNGLE_SHELF
            || block == Blocks.ACACIA_SHELF
            || block == Blocks.DARK_OAK_SHELF
            || block == Blocks.MANGROVE_SHELF
            || block == Blocks.CHERRY_SHELF
            || block == Blocks.BAMBOO_SHELF
            || block == Blocks.CRIMSON_SHELF
            || block == Blocks.WARPED_SHELF
            || block == Blocks.SPAWNER
            || block == Blocks.CREAKING_HEART
            || block == Blocks.FURNACE
            || block == Blocks.JUKEBOX
            || block == Blocks.ENCHANTING_TABLE
            || block == Blocks.BREWING_STAND
            || block == Blocks.COMMAND_BLOCK
            || block == Blocks.BEACON
            || block == Blocks.COMPARATOR
            || block == Blocks.HOPPER
            || block == Blocks.DAYLIGHT_DETECTOR
            || block == Blocks.BARREL
            || block == Blocks.REPEATING_COMMAND_BLOCK
            || block == Blocks.CHAIN_COMMAND_BLOCK
            || block == Blocks.STRUCTURE_BLOCK
            || block == Blocks.JIGSAW
            || block == Blocks.SMOKER
            || block == Blocks.BLAST_FURNACE
            || block == Blocks.LECTERN
            || block == Blocks.CAMPFIRE
            || block == Blocks.SOUL_CAMPFIRE
            || block == Blocks.BEE_NEST
            || block == Blocks.BEEHIVE
            || block == Blocks.CRAFTER
            || block == Blocks.TRIAL_SPAWNER;
    }
}
