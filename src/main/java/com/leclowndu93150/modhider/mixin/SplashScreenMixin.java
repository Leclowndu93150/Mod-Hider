package com.leclowndu93150.modhider.mixin;

import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class SplashScreenMixin {
    @Inject(method = "initGui", at = @At("HEAD"))
    private void onInitGui(CallbackInfo info) {
        System.out.println("TestMixin: initGui called in GuiMainMenu");
    }
}
