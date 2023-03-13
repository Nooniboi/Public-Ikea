package org.vined.ikea.modules.misc;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Formatting;
import org.vined.ikea.IKEA;
import java.lang.reflect.Field;

public class PacketLogger extends Module {
    public SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> c2s = sgGeneral.add(new BoolSetting.Builder()
        .name("C2S-packets")
        .description("If to log C2S (outgoing) packets.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> s2c = sgGeneral.add(new BoolSetting.Builder()
        .name("S2C-packets")
        .description("If to log S2C (ingoing) packets.")
        .defaultValue(false)
        .build()
    );
    public PacketLogger() {
        super(IKEA.MISC, "packet-logger", "Logs C2S and S2C packets.");
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (!s2c.get()) return;

        try {
            info(Formatting.WHITE + event.packet.getClass().getSimpleName() + Formatting.GRAY + " {");
            for (Field field : event.packet.getClass().getFields()) {
                if (field != null) {
                    if (!field.canAccess(event.packet)) {
                        field.setAccessible(true);
                    }
                    info("    " + Formatting.WHITE + field.getName() + Formatting.GRAY + ": " + field.get(event.packet));
                }
            }
            info("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (!c2s.get()) return;

        try {
            info(Formatting.WHITE + event.packet.getClass().getSimpleName() + Formatting.GRAY + " {");
            for (Field field : event.packet.getClass().getFields()) {
                if (field != null) {
                    if (!field.canAccess(event.packet)) {
                        field.setAccessible(true);
                    }
                    info("    " + Formatting.WHITE + field.getName() + Formatting.GRAY + ": " + field.get(event.packet));
                }
            }
            info("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
