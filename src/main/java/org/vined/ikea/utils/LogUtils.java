package org.vined.ikea.utils;

import meteordevelopment.meteorclient.mixininterface.IChatHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class LogUtils {
    protected static MinecraftClient mc = MinecraftClient.getInstance();

    public static void info(String txt) {
        assert mc.world != null;

        MutableText message = Text.literal("");
        message.append(Formatting.GRAY + "[" + Formatting.YELLOW + "IK" + Formatting.BLUE + "EA" + Formatting.GRAY + "] " + Formatting.GRAY);
        message.append(txt);

        IChatHud chatHud = (IChatHud) mc.inGameHud.getChatHud();
        chatHud.meteor$add(message,0);
    }
}
