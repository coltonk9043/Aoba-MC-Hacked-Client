package net.aoba.mixin;

import net.aoba.Aoba;
import net.aoba.AobaClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    // TODO: Hacky but it works for now... like to implement a way to not use a dummy setting.
    private static SimpleOption<Double> fullbrightOption = new SimpleOption<Double>("fullbright_gamma", null, null, null, null, 99999.9, null);

    @Inject(at = {@At("HEAD")}, method = {"getGamma()Lnet/minecraft/client/option/SimpleOption;"}, cancellable = true)
    private void onGetGamma(CallbackInfoReturnable<SimpleOption<Double>> cir) {
        MinecraftClient MC = MinecraftClient.getInstance();
        if (MC.currentScreen instanceof GameOptionsScreen)
            return;

        AobaClient aoba = Aoba.getInstance();
        if (aoba.moduleManager.fullbright.getState() ||
                aoba.moduleManager.xray.getState()) {
            cir.setReturnValue(fullbrightOption);
        }
    }
}
