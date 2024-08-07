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
 * ItemESP Module
 */
package net.aoba.module.modules.render;

import net.aoba.Aoba;
import net.aoba.event.events.Render3DEvent;
import net.aoba.event.listeners.Render3DListener;
import net.aoba.gui.colors.Color;
import net.aoba.utils.render.Render3D;
import net.aoba.module.Category;
import net.aoba.module.Module;
import net.aoba.settings.types.BooleanSetting;
import net.aoba.settings.types.ColorSetting;
import net.aoba.settings.types.FloatSetting;
import net.aoba.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class ItemESP extends Module implements Render3DListener {

    private ColorSetting color = new ColorSetting("itemesp_color", "Color", "Color", new Color(0, 1f, 1f));
    private BooleanSetting visibilityToggle = new BooleanSetting("itemesp_visibility", "Visibility", true);
    private FloatSetting range = new FloatSetting("itemesp_range", "Range", 100f, 10f, 500f, 5f);
    private ColorSetting rareItemColor = new ColorSetting("itemesp_rare_color", "Rare Item Color", new Color(1f, 0.5f, 0f));
    private BooleanSetting colorRarity = new BooleanSetting("itemesp_color_rarity", "Color Rarity", true);
    private FloatSetting lineThickness = new FloatSetting("itemesp_linethickness", "Line Thickness", "Adjust the thickness of the ESP box lines", 2f, 0f, 5f, 0.1f);
    
    public ItemESP() {
        super(new KeybindSetting("key.itemesp", "ItemESP Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

        this.setName("ItemESP");
        this.setCategory(Category.of("Render"));
        this.setDescription("Allows the player to see items with an ESP.");

        this.addSetting(color);
        this.addSetting(lineThickness);
    }

    @Override
    public void onDisable() {
        Aoba.getInstance().eventManager.RemoveListener(Render3DListener.class, this);
    }

    @Override
    public void onEnable() {
        Aoba.getInstance().eventManager.AddListener(Render3DListener.class, this);
    }

    @Override
    public void onToggle() {

    }

    @Override
    public void OnRender(Render3DEvent event) {
        if (!visibilityToggle.getValue()) return;

        Vec3d playerPos = MC.player.getPos();
        for (Entity entity : MC.world.getEntities()) {
            if (entity instanceof ItemEntity) {
                Vec3d itemPos = entity.getPos();
                if (playerPos.distanceTo(itemPos) <= range.getValue()) {
                    Color finalColor = colorRarity.getValue() ? getColorBasedOnItemRarity(entity) : color.getValue();
                    Render3D.draw3DBox(event.GetMatrix(), entity.getBoundingBox(), finalColor, lineThickness.getValue().floatValue());
                }
            }
        }
    }

    private Color getColorBasedOnItemRarity(Entity entity) {
        boolean isRare = false;

        if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entity;
            isRare = itemEntity.getStack().getRarity() == Rarity.RARE;
        }

        return isRare ? rareItemColor.getValue() : color.getValue();
    }
}
