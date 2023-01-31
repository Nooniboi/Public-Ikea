package org.vined.ikea.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

@SuppressWarnings("ALL")
public class PacketUtils {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    public static void sendPlayerPacket(Packet packet) {
        mc.player.networkHandler.sendPacket(packet);
    }

    public static void sendPacket(Packet packet) {
        mc.getNetworkHandler().sendPacket(packet);
    }

    public static void dismountPackets(Entity vehicle) {
        sendPlayerPacket(new VehicleMoveC2SPacket(vehicle));
        sendPlayerPacket(new PlayerInputC2SPacket(0, 0, true, true));
        sendPlayerPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
    }
}
