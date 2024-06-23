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
 * Spider Module
 */
package net.aoba.module.modules.movement;

import org.lwjgl.glfw.GLFW;
import net.aoba.Aoba;
import net.aoba.event.events.TickEvent;
import net.aoba.event.listeners.TickListener;
import net.aoba.module.Module;
import net.aoba.settings.types.FloatSetting;
import net.aoba.settings.types.KeybindSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;

public class Spider extends Module implements TickListener {

	private FloatSetting speed;
	
	public Spider() {
		super(new KeybindSetting("key.spider", "Spider Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

		this.setName("Spider");
		this.setCategory(Category.Movement);
		this.setDescription("Allows players to climb up blocks.");
		
		speed = new FloatSetting("spider.speed", "Speed", "Speed that player goes up blocks.", 0.1f, 0.05f, 1.0f, 0.05f);
		
		this.addSetting(speed);
	}

	@Override
	public void onDisable() {
		Aoba.getInstance().eventManager.RemoveListener(TickListener.class, this);
	}

	@Override
	public void onEnable() {
		Aoba.getInstance().eventManager.AddListener(TickListener.class, this);
	}

	@Override
	public void onToggle() {

	}

	@Override
	public void OnUpdate(TickEvent event) {
		ClientPlayerEntity player = MC.player;
		
		if(player.horizontalCollision) {
			Vec3d playerVelocity = player.getVelocity();
			MC.player.setVelocity(new Vec3d(playerVelocity.getX(), speed.getValue(), playerVelocity.getZ()));
		}
	}
}
