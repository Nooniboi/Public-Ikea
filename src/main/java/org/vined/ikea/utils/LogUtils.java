package org.vined.ikea.utils;

import meteordevelopment.meteorclient.mixininterface.IChatHud;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class LogUtils {
    protected static Minecraft mc = Minecraft.getInstance();

    public static void info(String txt) {
        if (mc.level == null) return;
        MutableComponent message = Component.empty();
        message.append(
            Component.literal("[")
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal("IK").withStyle(ChatFormatting.YELLOW))
                .append(Component.literal("EA").withStyle(ChatFormatting.BLUE))
                .append(Component.literal("] ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(txt).withStyle(ChatFormatting.GRAY))
        );

        IChatHud chatHud = (IChatHud) mc.gui.hud.getChat();
        chatHud.meteor$add(message, 0);
    }
}
