package org.vined.ikea.utils;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChunkUtils {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    public static List<Chunk> getLoadedChunks() {
        assert mc.world != null;
        assert mc.player != null;

        List<Chunk> loadedChunks = new ArrayList<>();
        BlockPos bPos = mc.player.getBlockPos();
        int playerX = bPos.getX();
        int playerZ = bPos.getZ();
        int renderDistance = mc.options.getViewDistance().getValue() * 16;

        for (int x = playerX - renderDistance; x <= playerX + renderDistance; x += 16) {
            for (int z = playerZ - renderDistance; z <= playerZ + renderDistance; z += 16) {
                ChunkPos chunkPos = new ChunkPos(x >> 4, z >> 4);
                if (mc.world.isChunkLoaded(chunkPos.x, chunkPos.z)) {
                    loadedChunks.add(mc.world.getChunk(chunkPos.getStartPos()));
                }
            }
        }

        return loadedChunks;
    }

    public static int getChestCount(WorldChunk chunk) {
        int count = 0;

        Map<BlockPos, BlockEntity> map = chunk.getBlockEntities();
        for (Map.Entry<BlockPos, BlockEntity> entry : map.entrySet()) {
            if (entry.getValue() instanceof ChestBlockEntity) count++;
        }

        return count;
    }


    public static int getShulkerCount(Chunk chunk) {
        int count = 0;

        for (BlockPos pos : chunk.getBlockEntityPositions()) {
            BlockEntity block = chunk.getBlockEntity(pos);
            if (block instanceof ShulkerBoxBlockEntity) count++;
        }

        return count;
    }

    public static int getChestCount() {
        List<Chunk> chunks = getLoadedChunks();
        int count = 0;

        for (Chunk chunk : chunks) {
            for (BlockPos pos : chunk.getBlockEntityPositions()) {
                BlockEntity block = chunk.getBlockEntity(pos);
                if (block instanceof ChestBlockEntity) count++;
            }
        }

        return count;
    }



    public static int getShulkerCount() {
        List<Chunk> chunks = getLoadedChunks();
        int count = 0;

        for (Chunk chunk : chunks) {
            for (BlockPos pos : chunk.getBlockEntityPositions()) {
                BlockEntity block = chunk.getBlockEntity(pos);
                if (block instanceof ShulkerBoxBlockEntity) count++;
            }
        }

        return count;
    }

    public static int getBlockCount(Block block) {
        int count = 0;
        List<Chunk> loadedChunks = getLoadedChunks();

        for (Chunk chunk : loadedChunks) {
            ChunkPos chunkPos = chunk.getPos();
            for (int x = 0; x < 16; x++) {
                for (int y = -64; y < 320; y++) {
                    for (int z = 0; z < 16; z++) {
                        BlockPos pos = new BlockPos(chunkPos.x * 16 + x, y, chunkPos.z * 16 + z);
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
