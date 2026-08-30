package org.vined.ikea.modules.misc;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.AutoReconnect;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.vined.ikea.IKEA;

@SuppressWarnings("ALL")
public class LogAt extends Module {

    public SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<BlockPos> position = sgGeneral.add(new BlockPosSetting.Builder()
        .name("position")
        .description("The position to log at.")
        .defaultValue(new BlockPos(0, 0, 0))
        .build()
    );

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .description("How close to the coordinates should you log out.")
        .defaultValue(1024)
        .min(0)
        .build()
    );

    public LogAt() {
        super(IKEA.MISC, "log-at", "Log at specific coordinates with range.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null) return;
        BlockPos targetPos = position.get();
        int maxRange = range.get();
        double dx = mc.player.getX() - targetPos.getX();
        double dy = mc.player.getY() - targetPos.getY();
        double dz = mc.player.getZ() - targetPos.getZ();
        double distance = dx * dx + dy * dy + dz * dz;
        if (distance <= maxRange * maxRange) {
            disconnect("Reached coordinates. AutoReconnect disabled");
        }
    }

    private void disconnect(String reason) {
        disconnect(Component.literal(reason));
    }

    private void disconnect(Component reason) {
        MutableComponent text = Component.literal("[LogAt] ");
        text.append(reason);
        AutoReconnect autoReconnect = Modules.get().get(AutoReconnect.class);
        if (autoReconnect.isActive()) {
            text.append(
                Component.literal("\n\nINFO - AutoReconnect was disabled")
                    .withColor(0x808080)
            );
            autoReconnect.toggle();
        }

        if (mc.getConnection() != null) {
            mc.getConnection().onDisconnect(
                new DisconnectionDetails(text)
            );
        }
    }
}
