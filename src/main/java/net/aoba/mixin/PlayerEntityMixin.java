/*
 * Aoba Hacked Client
 * Copyright (C) 2019-2024 coltonk9043
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.aoba.mixin;

import net.aoba.Aoba;
import net.aoba.AobaClient;
import net.aoba.module.modules.misc.FastBreak;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {

    @Shadow
    private PlayerInventory inventory;

    @Shadow
    public abstract boolean isSpectator();

    @Inject(method = "getBlockBreakingSpeed", at = @At("HEAD"), cancellable = true)
    public void onGetBlockBreakingSpeed(BlockState blockState, CallbackInfoReturnable<Float> ci) {
        AobaClient aoba = Aoba.getInstance();
        FastBreak fastBreak = (FastBreak) aoba.moduleManager.fastbreak;

        // If fast break is enabled.
        if (fastBreak.getState()) {
            // Multiply the break speed and override the return value.
            float speed = inventory.getBlockBreakingSpeed(blockState);
            speed *= fastBreak.multiplier.getValue();

            if (!fastBreak.ignoreWater.getValue()) {
                if (isSubmergedIn(FluidTags.WATER) || isSubmergedIn(FluidTags.LAVA) || !isOnGround()) {
                    speed /= 5.0F;
                }
            }

            ci.setReturnValue(speed);
        }
    }

    @Inject(at = {@At("HEAD")}, method = "getOffGroundSpeed()F", cancellable = true)
    protected void onGetOffGroundSpeed(CallbackInfoReturnable<Float> cir) {
        return;
    }
}
