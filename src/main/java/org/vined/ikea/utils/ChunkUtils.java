package org.vined.ikea.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ChunkUtils {

    protected static final Minecraft mc = Minecraft.getInstance();

    public static List<LevelChunk> getLoadedChunks() {
        if (mc.level == null || mc.player == null) {
            return new ArrayList<>();
        }
        List<LevelChunk> loadedChunks = new ArrayList<>();
        BlockPos playerPos = mc.player.blockPosition();
        int playerChunkX = playerPos.getX() >> 4;
        int playerChunkZ = playerPos.getZ() >> 4;
        int renderDistance = mc.options.getEffectiveRenderDistance();

        for (int chunkX = playerChunkX - renderDistance;
             chunkX <= playerChunkX + renderDistance;
             chunkX++) {

            for (int chunkZ = playerChunkZ - renderDistance;
                 chunkZ <= playerChunkZ + renderDistance;
                 chunkZ++) {

                if (mc.level.hasChunk(chunkX, chunkZ)) {
                    loadedChunks.add(mc.level.getChunk(chunkX, chunkZ));
                }
            }
        }

        return loadedChunks;
    }

    public static int getChestCount(LevelChunk chunk) {
        int count = 0;
        Map<BlockPos, BlockEntity> blockEntities = chunk.getBlockEntities();
        for (BlockEntity blockEntity : blockEntities.values()) {
            if (blockEntity instanceof ChestBlockEntity) {
                count++;
            }
        }
        return count;
    }

    public static int getShulkerCount(LevelChunk chunk) {
        int count = 0;
        Map<BlockPos, BlockEntity> blockEntities = chunk.getBlockEntities();

        for (BlockEntity blockEntity : blockEntities.values()) {
            if (blockEntity instanceof ShulkerBoxBlockEntity) {
                count++;
            }
        }
        return count;
    }

    public static int getChestCount() {
        int count = 0;
        for (LevelChunk chunk : getLoadedChunks()) {
            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (blockEntity instanceof ChestBlockEntity) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int getShulkerCount() {
        int count = 0;
        for (LevelChunk chunk : getLoadedChunks()) {
            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (blockEntity instanceof ShulkerBoxBlockEntity) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int getBlockCount(Block block) {
        int count = 0;
        for (LevelChunk chunk : getLoadedChunks()) {
            ChunkPos chunkPos = chunk.getPos();

            for (int x = 0; x < 16; x++) {
                for (int y = -64; y < 320; y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockPos pos = new BlockPos(
                            chunkPos.x() * 16 + x,
                            y,
                            chunkPos.z() * 16 + z
                        );
                        if (chunk.getBlockState(pos).getBlock() == block) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }
}
