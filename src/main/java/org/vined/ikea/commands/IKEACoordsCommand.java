package org.vined.ikea.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import org.vined.ikea.utils.LogUtils;

@SuppressWarnings("unused")
public class IKEACoordsCommand extends Command {

    public IKEACoordsCommand() {
        super("ikea-coords", "Copies your coordinates to your clipboard.", "c");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes(context -> {
            if (mc.player != null) {
                String text = mc.player.getBlockX()
                    + " "
                    + mc.player.getBlockY()
                    + " "
                    + mc.player.getBlockZ();

                LogUtils.info(text + " | Copied");
                mc.keyboardHandler.setClipboard(text);
            }
            return SINGLE_SUCCESS;
        });
    }
}
