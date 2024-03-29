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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import net.aoba.Aoba;
import net.aoba.AobaClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
	
	protected TitleScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/GameVersion;getName()Ljava/lang/String;", ordinal = 0) }, method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V")
	public void onRender(DrawContext context, int mouseX, int mouseY,float delta, CallbackInfo ci) {
		Aoba.getInstance();
		context.drawTextWithShadow(this.textRenderer, "Aoba " + AobaClient.AOBA_VERSION, 2, this.height - 20, 0xFF00FF);
	}
}
