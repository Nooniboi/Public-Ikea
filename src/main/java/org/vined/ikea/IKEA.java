package org.vined.ikea;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.vined.ikea.modules.misc.DiscordNotifier;
import org.vined.ikea.modules.utility.AutoItemMove;
import org.vined.ikea.modules.utility.DubCounter;
import org.vined.ikea.modules.utility.NoBlockEntities;


public class IKEA extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category UTILITY = new Category("IUtils");
    public static final Category MISC = new Category("IMisc");


    @Override
    public void onInitialize() {
        LOG.info("Initializing IKEA Addon");

        // Modules
        Modules.get().add(new NoBlockEntities());
        Modules.get().add(new DubCounter());
        Modules.get().add(new AutoItemMove());
        Modules.get().add(new DiscordNotifier());

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
