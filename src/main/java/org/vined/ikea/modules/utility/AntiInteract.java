package org.vined.ikea.modules.utility;

import meteordevelopment.meteorclient.events.entity.player.InteractBlockEvent;
import meteordevelopment.meteorclient.events.entity.player.InteractEntityEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ItemListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.vined.ikea.IKEA;

import java.util.List;

public class AntiInteract extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> entities = sgGeneral.add(new BoolSetting.Builder()
        .name("entities")
        .description("If to disable interacting with entities.")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> blocks = sgGeneral.add(new BoolSetting.Builder()
        .name("blocks")
        .description("If to disable interacting with blocks.")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> checkShulkers = sgGeneral.add(new BoolSetting.Builder()
        .name("check-shulkers")
        .description("If to check the items inside of a shulker you are holding.")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> allItems = sgGeneral.add(new BoolSetting.Builder()
        .name("all-items")
        .description("If to disable interacting completely.")
        .defaultValue(false)
        .build());

    private final Setting<List<Item>> items = sgGeneral.add(new ItemListSetting.Builder()
        .name("items")
        .description("The items.")
        .visible(() -> !allItems.get())
        .build()
    );


    public AntiInteract() {
        super(IKEA.UTILITY, "anti-interact", "Stops you from interacting (right click) with entities/blocks if you're holding certain items.");
    }

    @EventHandler
    private void onInteractEntity(InteractEntityEvent event) {
        if (!entities.get()) return;
        if (allItems.get()) {
            event.cancel();
            return;
        }
        ItemStack item = mc.player.getInventory().getMainHandStack();
        if (items.get().contains(item.getItem())) {
            event.cancel();
            return;
        }
        if (Utils.isShulker(item.getItem()) && checkShulkers.get()) {
            ItemStack[] itemStacks = new ItemStack[27];
            Utils.getItemsInContainerItem(item, itemStacks);
            for (ItemStack itemStack : itemStacks) {
                if (items.get().contains(itemStack.getItem())) {
                    event.cancel();
                    return;
                }
            }
        }
    }

    @EventHandler
    private void onInteractBlock(InteractBlockEvent event) {
        if (!blocks.get()) return;
        if (allItems.get()) {
            event.cancel();
            return;
        }
        Item item = mc.player.getInventory().getMainHandStack().getItem();
        if (items.get().contains(item)) {
            event.cancel();
            return;
        }
        if (Utils.isShulker(item) && checkShulkers.get()) {
            ItemStack[] itemStacks = new ItemStack[27];
            Utils.getItemsInContainerItem(item.getDefaultStack(), itemStacks);
            for (ItemStack itemStack : itemStacks) {
                if (items.get().contains(itemStack.getItem())) {
                    event.cancel();
                    return;
                }
            }
        }
    }
}
