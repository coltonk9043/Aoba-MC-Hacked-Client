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
package net.aoba.gui.navigation.huds;

import net.aoba.Aoba;
import net.aoba.AobaClient;
import net.aoba.gui.GuiManager;
import net.aoba.gui.Rectangle;
import net.aoba.gui.navigation.HudWindow;
import net.aoba.module.Category;
import net.aoba.module.Module;
import net.aoba.utils.render.Render2D;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModuleSelectorHud extends HudWindow {
    private static final AobaClient AOBA = Aoba.getInstance();;
    private static final float ROW_HEIGHT = 30.0f;
    
    private KeyBinding keybindUp;
    private KeyBinding keybindDown;
    private KeyBinding keybindLeft;
    private KeyBinding keybindRight;

    private int index = 0;
    private int indexMods = 0;
    private boolean isCategoryMenuOpen = false;

    private List<Category> categories = new ArrayList<>();
    private ArrayList<Module> modules = new ArrayList<Module>();

    public ModuleSelectorHud() {
        super("ModuleSelectorHud", 0, 0, 225, 32);
        this.keybindUp = new KeyBinding("key.tabup", GLFW.GLFW_KEY_UP, "key.categories.aoba");
        this.keybindDown = new KeyBinding("key.tabdown", GLFW.GLFW_KEY_DOWN, "key.categories.aoba");
        this.keybindLeft = new KeyBinding("key.tableft", GLFW.GLFW_KEY_LEFT, "key.categories.aoba");
        this.keybindRight = new KeyBinding("key.tabright", GLFW.GLFW_KEY_RIGHT, "key.categories.aoba");

        categories.addAll(Category.getAllCategories().values());

        this.inheritHeightFromChildren = false;
        this.resizeable = false;
        this.setHeight(categories.size() * ROW_HEIGHT);
        this.minHeight = 32f;
        this.maxHeight = 32f;
    }

    @Override
    public void update() {
        if (this.keybindUp.isPressed()) {
            if (!isCategoryMenuOpen) {
                if (index == 0) {
                    index = categories.size() - 1;
                } else {
                    index -= 1;
                }
            } else {
                if (indexMods == 0) {
                    indexMods = modules.size() - 1;
                } else {
                    indexMods -= 1;
                }
            }
            this.keybindUp.setPressed(false);
        } else if (this.keybindDown.isPressed()) {
            if (!isCategoryMenuOpen) {
                index = (index + 1) % categories.size();
            } else {
                indexMods = (indexMods + 1) % modules.size();
            }
            this.keybindDown.setPressed(false);
        } else if (this.keybindRight.isPressed()) {
            if (!isCategoryMenuOpen) {
                isCategoryMenuOpen = true;
                if (modules.isEmpty()) {
                    for (Module module : AOBA.moduleManager.modules) {
                        if (module.isCategory(this.categories.get(this.index))) {
                            modules.add(module);
                        }
                    }
                }
            } else {
                modules.get(indexMods).toggle();
            }
            this.keybindRight.setPressed(false);
        } else if (this.keybindLeft.isPressed()) {
            if (this.isCategoryMenuOpen) {
                this.indexMods = 0;
                this.modules.clear();
                this.isCategoryMenuOpen = false;
            }
            this.keybindLeft.setPressed(false);
        }
    }

    @Override
    public void draw(DrawContext drawContext, float partialTicks) {
        // Gets the client and window.
        MatrixStack matrixStack = drawContext.getMatrices();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

        Rectangle pos = position.getValue();

        if (pos.isDrawable()) {
            float x = pos.getX().floatValue();
            float y = pos.getY().floatValue();
            float width = pos.getWidth().floatValue();
            float height = pos.getHeight().floatValue();

            // Draws the table including all of the categories.
            Render2D.drawRoundedBox(matrix4f, x, y, width, height, 6f,
                GuiManager.backgroundColor.getValue());
            Render2D.drawRoundedBoxOutline(matrix4f, x, y, width, height, 6f,
                GuiManager.borderColor.getValue());

            // For every category, draw a cell for it.
            for (int i = 0; i < this.categories.size(); i++) {
                Render2D.drawString(drawContext, ">>", x + width - 24, y + (ROW_HEIGHT * i) + 8, GuiManager.foregroundColor.getValue());
                
                // Draws the name of the category dependent on whether it is selected.
                if (this.index == i)
                    Render2D.drawString(drawContext, "> " + this.categories.get(i).getName(), x + 8, y + (ROW_HEIGHT * i) + 8, GuiManager.foregroundColor.getValue());
                 else 
                    Render2D.drawString(drawContext, this.categories.get(i).getName(), x + 8, y + (ROW_HEIGHT * i) + 8, 0xFFFFFF);
            }

            // If any particular category menu is open.
            if (isCategoryMenuOpen) {
                // Draw the table underneath
                Render2D.drawRoundedBox(matrix4f, x + width, y + (ROW_HEIGHT * index), 165,
                		ROW_HEIGHT * modules.size(), GuiManager.roundingRadius.getValue(), GuiManager.backgroundColor.getValue());
                Render2D.drawRoundedBoxOutline(matrix4f, x + width, y + (ROW_HEIGHT * index), 165,
                		ROW_HEIGHT * modules.size(), GuiManager.roundingRadius.getValue(), GuiManager.borderColor.getValue());

                // For every mod, draw a cell for it.
                for (int i = 0; i < modules.size(); i++) {
                    if (this.indexMods == i) {
                        Render2D.drawString(drawContext, "> " + modules.get(i).getName(), x + width + 5,
                            y + (i * ROW_HEIGHT) + (index * ROW_HEIGHT) + 8, modules.get(i).getState() ? 0x00FF00
                                : GuiManager.foregroundColor.getValue().getColorAsInt());
                    } else {
                        Render2D.drawString(drawContext, modules.get(i).getName(), x + width + 5,
                            y + (i * ROW_HEIGHT) + (index * ROW_HEIGHT) + 8,
                            modules.get(i).getState() ? 0x00FF00 : 0xFFFFFF);
                    }
                }
            }
        }
        super.draw(drawContext, partialTicks);
    }
}