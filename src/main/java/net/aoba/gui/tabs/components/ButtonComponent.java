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
import net.aoba.event.listeners.MouseClickListener;
import net.aoba.gui.IGuiElement;
import net.aoba.gui.colors.Color;
import net.aoba.misc.RenderUtils;
import net.aoba.utils.types.MouseAction;
import net.aoba.utils.types.MouseButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class ButtonComponent extends Component implements MouseClickListener {

	private String text;
	private Runnable onClick;
	private Color borderColor = new Color(128, 128, 128);
	private Color backgroundColor = new Color(96, 96, 96);
	private Color hoveredBackgroundColor = new Color(156, 156, 156);
	
	/**
	 * Constructor for button component.
	 * @param parent Parent Tab that this Component resides in.
	 * @param text Text contained in this button element.
	 * @param onClick OnClick delegate that will run when the button is pressed.
	 */
	public ButtonComponent(IGuiElement parent, String text, Runnable onClick) {
		super(parent);
		
		this.setLeft(2);
		this.setRight(2);
		this.setHeight(30);
		this.setBottom(10);
		
		this.text = text;
		this.onClick = onClick;
	}
	
	public ButtonComponent(IGuiElement parent, String text, Runnable onClick, Color borderColor, Color backgroundColor) {
		super(parent);
		
		this.setLeft(2);
		this.setRight(2);
		this.setHeight(30);
		this.setBottom(10);
		
		this.text = text;
		this.onClick = onClick;
		
		this.borderColor = borderColor;
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Sets the text of the button.
	 * @param text Text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Sets the OnClick delegate of the button.
	 * @param onClick Delegate to set.
	 */
	public void setOnClick(Runnable onClick) {
		this.onClick = onClick;
	}

	public void setBorderColor(Color color) {
		this.borderColor = color;
	}
	
	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
	
	/**
	 * Draws the button to the screen.
	 * @param offset The offset (Y location relative to parent) of the Component.
	 * @param drawContext The current draw context of the game.
	 * @param partialTicks The partial ticks used for interpolation.
	 * @param color The current Color of the UI.
	 */
	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		MatrixStack matrixStack = drawContext.getMatrices();
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		
		if(this.hovered)
			RenderUtils.drawOutlinedBox(matrix4f, actualX + 2, actualY, actualWidth - 4, actualHeight - 4, borderColor, hoveredBackgroundColor);
		else
			RenderUtils.drawOutlinedBox(matrix4f, actualX + 2, actualY, actualWidth - 4, actualHeight - 4, borderColor, backgroundColor);
		RenderUtils.drawString(drawContext, this.text, actualX + 8, actualY + 8, 0xFFFFFF);
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
	public void OnMouseClick(MouseClickEvent event) {
		if(event.button == MouseButton.LEFT && event.action == MouseAction.DOWN) {
			if(this.hovered && this.isVisible() && onClick != null)  {
				this.onClick.run();
			}
		}
	}
}
