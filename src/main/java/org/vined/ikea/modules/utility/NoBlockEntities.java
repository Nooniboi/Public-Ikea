package org.vined.ikea.modules.utility;

import meteordevelopment.meteorclient.events.render.RenderBlockEntityEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;

import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import org.vined.ikea.IKEA;

import java.util.List;
import java.util.Objects;


public class NoBlockEntities extends Module {
    public SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> radius = sgGeneral.add(new IntSetting.Builder()
        .name("render-radius")
        .description("The radius in which the blocks will render.")
        .defaultValue(0)
        .min(0)
        .sliderMax(128)
        .build()
    );

    private final Setting<List<Block>> blocks = sgGeneral.add(new BlockListSetting.Builder()
        .name("blocks")
        .description("The blocks not to render.")
        .defaultValue(
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            Blocks.SHULKER_BOX,
            Blocks.RED_SHULKER_BOX,
            Blocks.BLUE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX,
            Blocks.GRAY_SHULKER_BOX,
            Blocks.LIGHT_GRAY_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX,
            Blocks.BROWN_SHULKER_BOX,
            Blocks.BLACK_SHULKER_BOX,
            Blocks.GREEN_SHULKER_BOX,
            Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.LIME_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX,
            Blocks.MAGENTA_SHULKER_BOX,
            Blocks.PURPLE_SHULKER_BOX,
            Blocks.WHITE_SHULKER_BOX,
            Blocks.ORANGE_SHULKER_BOX
        )
        .filter(NoBlockEntities::isBlockEntity)
        .build()
    );

    public NoBlockEntities() {
        super(IKEA.UTILITY, "no-block-entities", "Disables rendering for specified block entities.");
    }

    @EventHandler
    private void onRenderBlockEntity(RenderBlockEntityEvent event) {
        assert mc.world != null;
        BlockEntityRenderState block = event.blockEntityState;

        if (blocks.get().contains(mc.world.getBlockState(block.pos).getBlock())) {
            if (PlayerUtils.distanceTo(block.pos) > radius.get()) event.cancel();
        }
    }

    private static boolean isBlockEntity(Block block) {
        return
                block == Blocks.CHEST ||
                block == Blocks.TRAPPED_CHEST ||
                block == Blocks.ENDER_CHEST ||
                block == Blocks.BELL ||
                block == Blocks.ENCHANTING_TABLE ||
                isBed(block) ||
                isShulker(block) ||
                isSkull(block);
    }

    private static boolean isBed(Block block) {
        return
            block == Blocks.BLACK_BED ||
                block == Blocks.RED_BED ||
                block == Blocks.BLUE_BED ||
                block == Blocks.BROWN_BED ||
                block == Blocks.CYAN_BED ||
                block == Blocks.GRAY_BED ||
                block == Blocks.GREEN_BED ||
                block == Blocks.LIGHT_BLUE_BED ||
                block == Blocks.LIGHT_GRAY_BED ||
                block == Blocks.LIME_BED ||
                block == Blocks.MAGENTA_BED ||
                block == Blocks.ORANGE_BED ||
                block == Blocks.PINK_BED ||
                block == Blocks.PURPLE_BED ||
                block == Blocks.WHITE_BED ||
                block == Blocks.YELLOW_BED;
    }

    private static boolean isShulker(Block block) {
        return
            block == Blocks.SHULKER_BOX ||
                block == Blocks.RED_SHULKER_BOX ||
                block == Blocks.BLUE_SHULKER_BOX ||
                block == Blocks.YELLOW_SHULKER_BOX ||
                block == Blocks.GRAY_SHULKER_BOX ||
                block == Blocks.LIGHT_GRAY_SHULKER_BOX ||
                block == Blocks.CYAN_SHULKER_BOX ||
                block == Blocks.BROWN_SHULKER_BOX ||
                block == Blocks.BLACK_SHULKER_BOX ||
                block == Blocks.GREEN_SHULKER_BOX ||
                block == Blocks.LIGHT_BLUE_SHULKER_BOX ||
                block == Blocks.LIME_SHULKER_BOX ||
                block == Blocks.PINK_SHULKER_BOX ||
                block == Blocks.MAGENTA_SHULKER_BOX ||
                block == Blocks.PURPLE_SHULKER_BOX ||
                block == Blocks.WHITE_SHULKER_BOX ||
                block == Blocks.ORANGE_SHULKER_BOX;
    }

    private static boolean isSkull(Block block) {
        return
            block == Blocks.SKELETON_SKULL ||
                block == Blocks.WITHER_SKELETON_SKULL ||
                block == Blocks.CREEPER_HEAD ||
                block == Blocks.DRAGON_HEAD ||
                block == Blocks.PISTON_HEAD ||
                block == Blocks.PLAYER_HEAD ||
                block == Blocks.ZOMBIE_HEAD ||
                block == Blocks.DRAGON_WALL_HEAD ||
                block == Blocks.CREEPER_WALL_HEAD ||
                block == Blocks.PLAYER_WALL_HEAD ||
                block == Blocks.ZOMBIE_WALL_HEAD ||
                block == Blocks.SKELETON_WALL_SKULL ||
                block == Blocks.WITHER_SKELETON_WALL_SKULL;
    }
}
