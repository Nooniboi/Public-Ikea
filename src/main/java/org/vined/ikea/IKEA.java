package org.vined.ikea;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.vined.ikea.commands.IKEACoordsCommand;
import org.vined.ikea.modules.misc.LogAt;
import org.vined.ikea.modules.misc.NoRender;
import org.vined.ikea.modules.utility.*;


public class IKEA extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category UTILITY = new Category("IUtils");
    public static final Category MISC = new Category("IMisc");


    @Override
    public void onInitialize() {
        LOG.info("Initializing IKEA Addon");

        // Modules
        Modules.get().add(new DubCounter());
        Modules.get().add(new AutoItemMove());
        Modules.get().add(new AntiBreak());
        Modules.get().add(new AntiDrop());
        Modules.get().add(new AntiInteract());
        Modules.get().add(new AutoShulkerDrop());
        Modules.get().add(new LogAt());
        Modules.get().add(new NoRender());

        //Commands
        Commands.add(new IKEACoordsCommand());
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
