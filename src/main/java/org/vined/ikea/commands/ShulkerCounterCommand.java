package org.vined.ikea.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import meteordevelopment.meteorclient.systems.commands.Command;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

@SuppressWarnings("ALL")
public class ShulkerCounterCommand extends Command {
    public ShulkerCounterCommand() {
        super("shulker-counter", "Counts every shulker on the ground.");
    }
    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            int count = 0;
            for (Entity entity : mc.world.getEntities()) {
                if (!(entity instanceof ItemEntity item)) continue;
                if (item.getStack().getItem().getName().toString().contains("shulker")) {
                    count += 1;
                }
            }
            info("There are " + Formatting.WHITE + count + " shulkers on the ground.");
            return SINGLE_SUCCESS;
        });
    }
}
