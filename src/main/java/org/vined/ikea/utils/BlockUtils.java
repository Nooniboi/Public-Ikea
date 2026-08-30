package org.vined.ikea.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public final class BlockUtils {

    private BlockUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isBlockEntity(Block block) {
        return block instanceof EntityBlock;
    }

    public static boolean isShulker(Block block) {
        return block instanceof ShulkerBoxBlock;
    }

    public static boolean isChest(Block block) {
        return block instanceof ChestBlock;
    }

    public static boolean isHead(Block block) {
        return block instanceof net.minecraft.world.level.block.AbstractSkullBlock;
    }

    public static boolean isBed(Block block) {
        return block instanceof net.minecraft.world.level.block.BedBlock;
    }
}
