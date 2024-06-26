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

package net.aoba.gui.tabs.components;

import org.joml.Matrix4f;
import net.aoba.Aoba;
import net.aoba.event.events.MouseClickEvent;
import net.aoba.event.events.MouseMoveEvent;
import net.aoba.event.listeners.MouseClickListener;
import net.aoba.event.listeners.MouseMoveListener;
import net.aoba.gui.GuiManager;
import net.aoba.gui.IGuiElement;
import net.aoba.gui.colors.Color;
import net.aoba.misc.RenderUtils;
import net.aoba.settings.types.ColorSetting;
import net.aoba.utils.types.MouseAction;
import net.aoba.utils.types.MouseButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;


public class ColorPickerComponent extends Component implements MouseClickListener, MouseMoveListener {

	private String text;
	private boolean isSliding = false;
	private boolean collapsed = true;
	private float hue = 0.0f;
	private float saturation = 0.0f;
	private float luminance = 0.0f;
	private float alpha = 0.0f;
	
	private ColorSetting color;

	public ColorPickerComponent(String text, IGuiElement parent) {
		super(parent);
		this.text = text;
		
		this.setHeight(145);
		this.setLeft(4);
		this.setRight(4);
	}
	
	public ColorPickerComponent(IGuiElement parent, ColorSetting color) {
		super(parent);
		
		this.text = color.displayName;
		this.color = color;
		this.color.setOnUpdate((Color newColor) -> ensureGuiUpdated(newColor));
	
		this.hue = color.getValue().hue;
		this.saturation = color.getValue().saturation;
		this.luminance = color.getValue().luminance;
		
		this.setHeight(30);
		this.setLeft(4);
		this.setRight(4);
	}
	
	public void ensureGuiUpdated(Color newColor) {
		this.hue = newColor.hue;
		this.saturation = newColor.saturation;
		this.luminance = newColor.luminance;
		this.alpha = newColor.alpha;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	@Override
	public void OnMouseClick(MouseClickEvent event) {
		if(event.button == MouseButton.LEFT) {
			if(event.action == MouseAction.DOWN) {
				double mouseY = event.mouseY;
				
				if(hovered && Aoba.getInstance().hudManager.isClickGuiOpen()) {
					if(mouseY < actualY + 29) {
						collapsed = !collapsed;
						if(collapsed) 
							this.setHeight(30);
						else
							this.setHeight(145);
					}else {
						if(!collapsed)
							isSliding = true;
					}
				}
			}else if(event.action == MouseAction.UP) {
				isSliding = false;
			}
		}
	}
	
	@Override
	public void OnMouseMove(MouseMoveEvent event) {
		super.OnMouseMove(event);
		
		double mouseX = event.GetHorizontal();
		double mouseY = event.GetVertical();
		if (Aoba.getInstance().hudManager.isClickGuiOpen() && this.isSliding) {

			float vertical = (float) Math.min(Math.max(1.0f - (((mouseY - (actualY + 29)) - 1) / (actualHeight - 33)), 0.0f), 1.0f);
			
			// If inside of saturation/lightness box.
			if(mouseX >= actualX + 4 && mouseX <= actualX + actualWidth - 68) {
				float horizontal = (float) Math.min(Math.max(((mouseX - (actualX + 4)) - 1) / (actualWidth - 68), 0.0f), 1.0f);
				
				this.luminance = vertical;
				this.saturation = horizontal;
			}else if(mouseX >= actualX + actualWidth - 72 && mouseX <= actualX + actualWidth - 38) {
				this.hue = (1.0f - vertical) * 360.0f;
			}else if(mouseX >= actualX + actualWidth - 34 && mouseX <= actualX + actualWidth - 4) {
				this.alpha = (vertical) * 255.0f;
			}
			
			this.color.getValue().setHSV(hue, saturation, luminance);
			this.color.getValue().setAlpha((int) alpha);
		}
	}


	@Override
	public void OnVisibilityChanged() {
		if(this.isVisible()) {
			Aoba.getInstance().eventManager.AddListener(MouseClickListener.class, this);
		}else {
			Aoba.getInstance().eventManager.RemoveListener(MouseClickListener.class, this);
		}
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		MatrixStack matrixStack = drawContext.getMatrices();
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		
		RenderUtils.drawString(drawContext, this.text, actualX + 6, actualY + 6, 0xFFFFFF);
		RenderUtils.drawString(drawContext, collapsed ?  ">>" :  "<<", (actualX + actualWidth - 24), actualY + 6, GuiManager.foregroundColor.getValue().getColorAsInt());
		
		if(!collapsed) {
			Color newColor = new Color(255, 0, 0);
			newColor.setHSV(this.hue, 1.0f, 1.0f);
			RenderUtils.drawHorizontalGradient(matrix4f, actualX + 4, actualY + 29, actualWidth - 76, actualHeight - 33, new Color(255, 255, 255), newColor);
			RenderUtils.drawVerticalGradient(matrix4f, actualX + 4, actualY+ 29, actualWidth - 76, actualHeight - 33, new Color(0, 0, 0, 0), new Color(0, 0, 0));
			
			// Draw Hue Rectangle
			float increment = ((this.actualHeight - 33) / 6.0f);
			RenderUtils.drawVerticalGradient(matrix4f, actualX + actualWidth - 68, actualY + 29, 30, increment, new Color(255, 0, 0), new Color(255, 255, 0));
			RenderUtils.drawVerticalGradient(matrix4f, actualX + actualWidth - 68, actualY + 29 + increment, 30,increment, new Color(255, 255, 0), new Color(0, 255, 0));
			RenderUtils.drawVerticalGradient(matrix4f, actualX + actualWidth - 68, actualY + 29 + (2 * increment), 30, increment, new Color(0, 255, 0), new Color(0, 255, 255));
			RenderUtils.drawVerticalGradient(matrix4f, actualX + actualWidth - 68, actualY + 29 + (3 * increment), 30, increment, new Color(0, 255, 255), new Color(0, 0, 255));
			RenderUtils.drawVerticalGradient(matrix4f, actualX + actualWidth - 68, actualY + 29 + (4 * increment), 30, increment, new Color(0, 0, 255), new Color(255, 0, 255));
			RenderUtils.drawVerticalGradient(matrix4f, actualX + actualWidth - 68, actualY + 29 + (5 * increment), 30, increment, new Color(255, 0, 255), new Color(255, 0, 0));
		
			// Draw Alpha Rectangle
			RenderUtils.drawVerticalGradient(matrix4f, actualX + actualWidth - 34 , actualY + 29, 30, actualHeight - 33, new Color(255, 255, 255), new Color(0, 0, 0));
			
			// Draw Outlines
			RenderUtils.drawOutline(matrix4f, actualX + 4, actualY + 29, actualWidth - 76, actualHeight - 33);
			RenderUtils.drawOutline(matrix4f, actualX + actualWidth - 68, actualY + 29, 30, actualHeight - 33);
			RenderUtils.drawOutline(matrix4f, actualX + actualWidth - 34, actualY + 29, 30, actualHeight - 33);
			
			// Draw Indicators
			RenderUtils.drawCircle(matrix4f, actualX + 4 + (saturation * (actualWidth - 72)), actualY + 29 + ((1.0f - luminance) * (actualHeight - 33)), 3, new Color(255, 255, 255, 255));
			RenderUtils.drawOutlinedBox(matrix4f, actualX + actualWidth - 68, actualY + 29 + ((hue / 360.0f) * (actualHeight - 33)), 30, 3, new Color(255, 255, 255, 255));
			RenderUtils.drawOutlinedBox(matrix4f, actualX + actualWidth - 34, actualY + 29 + (((255.0f - alpha) / 255.0f) * (actualHeight - 33)), 30, 3, new Color(255, 255, 255, 255));
		}
	}
}