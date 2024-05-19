package org.vined.ikea.modules.utility;

import meteordevelopment.meteorclient.events.entity.DropItemsEvent;
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

public class AntiDrop extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> checkShulkers = sgGeneral.add(new BoolSetting.Builder()
        .name("check-shulkers")
        .description("If to check the items inside of a shulker you are dropping.")
        .defaultValue(false)
        .build());

    private final Setting<Boolean> allItems = sgGeneral.add(new BoolSetting.Builder()
        .name("all-items")
        .description("If to disable dropping completely.")
        .defaultValue(false)
        .build());

    private final Setting<List<Item>> items = sgGeneral.add(new ItemListSetting.Builder()
        .name("items")
        .description("The items to stop dropping.")
        .visible(() -> !allItems.get())
        .build()
    );

    public AntiDrop() {
        super(IKEA.UTILITY, "anti-drop", "Stops you from dropping certain items.");
    }

    @EventHandler
    private void onDrop(DropItemsEvent event) {
        if (allItems.get()) {
            event.cancel();
            return;
        }
        if (items.get().contains(event.itemStack.getItem())) {
            event.cancel();
            return;
        }
        if (Utils.isShulker(event.itemStack.getItem()) && checkShulkers.get()) {
            ItemStack[] itemStacks = new ItemStack[27];
            Utils.getItemsInContainerItem(event.itemStack, itemStacks);
            for (ItemStack item : itemStacks) {
                if (items.get().contains(item.getItem())) {
                    event.cancel();
                    return;
                }
            }
        }
    }
}
