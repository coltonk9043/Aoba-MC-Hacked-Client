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

/**
 * ChestESP Module
 */
package net.aoba.module.modules.render;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.lwjgl.glfw.GLFW;
import net.aoba.Aoba;
import net.aoba.event.events.RenderEvent;
import net.aoba.event.listeners.RenderListener;
import net.aoba.gui.colors.Color;
import net.aoba.misc.ModuleUtils;
import net.aoba.misc.RenderUtils;
import net.aoba.module.Module;
import net.aoba.settings.types.BooleanSetting;
import net.aoba.settings.types.ColorSetting;
import net.aoba.settings.types.FloatSetting;
import net.aoba.settings.types.KeybindSetting;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Box;

public class ChestESP extends Module implements RenderListener {
	private ColorSetting color = new ColorSetting("chestesp_color", "Color", "Color", new Color(0, 1f, 1f));
	
	public BooleanSetting rainbow = new BooleanSetting("chestesp_rainbow", "Rainbow", "Rainbow", false);
	public FloatSetting effectSpeed = new FloatSetting("chestesp_effectspeed", "Effect Speed", "Effect Speed", 4f, 1f, 20f, 0.1f);
	
	
	public ChestESP() {
		super(new KeybindSetting("key.chestesp", "ChestESP Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("ChestESP");
		this.setCategory(Category.Render);
		this.setDescription("Allows the player to see Chests with an ESP.");
		
		this.addSetting(color);
		this.addSetting(rainbow);
		this.addSetting(effectSpeed);
	}

	@Override
	public void onDisable() {
		Aoba.getInstance().eventManager.RemoveListener(RenderListener.class, this);
	}

	@Override
	public void onEnable() {
		Aoba.getInstance().eventManager.AddListener(RenderListener.class, this);
	}

	@Override
	public void onToggle() {

	}
	
	@Override
	public void OnRender(RenderEvent event) {
		ArrayList<BlockEntity> blockEntities = ModuleUtils.getTileEntities().collect(Collectors.toCollection(ArrayList::new));
		for(BlockEntity blockEntity : blockEntities) {
			if(blockEntity instanceof ChestBlockEntity || blockEntity instanceof TrappedChestBlockEntity || blockEntity instanceof BarrelBlockEntity) {
				Box box = new Box(blockEntity.getPos());
				RenderUtils.draw3DBox(event.GetMatrix().peek().getPositionMatrix(), box, color.getValue());
			}
		}
	}
}
