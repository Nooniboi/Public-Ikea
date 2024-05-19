package org.vined.ikea.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vined.ikea.mixininterface.ITitleScreen;

@Mixin(TitleScreen.class)
public class TitleScreenMixin implements ITitleScreen {
    @Mutable
    @Final
    @Shadow
    private static Identifier PANORAMA_OVERLAY;

    @Override
    public void ikea$setOverlay(Identifier identifier) {
        PANORAMA_OVERLAY = identifier;
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ikea$setOverlay(new Identifier("textures/background.png"));
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
    }
}
