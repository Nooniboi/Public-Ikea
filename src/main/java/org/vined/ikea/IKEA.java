package org.vined.ikea;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Modules;

import com.mojang.logging.LogUtils;

import org.slf4j.Logger;

import org.vined.ikea.modules.misc.IKEADupe;
import org.vined.ikea.modules.misc.PacketLogger;
import org.vined.ikea.modules.utility.AutoItemMove;
import org.vined.ikea.modules.utility.DubCounter;
import org.vined.ikea.modules.utility.NoChestRender;
import org.vined.ikea.commands.ShulkerCounterCommand;


public class IKEA extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category UTILITY = new Category("IUtils");
    public static final Category MISC = new Category("IMisc");


    @Override
    public void onInitialize() {
        LOG.info("Initializing IKEA Addon");

        // Modules
        Modules.get().add(new IKEADupe());
        Modules.get().add(new NoChestRender());
        Modules.get().add(new DubCounter());
        Modules.get().add(new AutoItemMove());
        Modules.get().add(new PacketLogger());

	// Commands
	Commands.get().add(new ShulkerCounterCommand());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(UTILITY);
        Modules.registerCategory(MISC);
    }

    @Override
    public String getPackage() {
        return "org.vined.ikea";
    }
}
