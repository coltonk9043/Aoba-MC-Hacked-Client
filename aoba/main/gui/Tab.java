package aoba.main.gui;

import aoba.main.misc.RenderUtils;
import net.minecraft.client.Minecraft;

public abstract class Tab {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected RenderUtils renderUtils = new RenderUtils();
	Minecraft mc = Minecraft.getInstance();
	
	public abstract void update(double mouseX, double mouseY, boolean mouseClicked) ;
	
	public abstract void draw(int scaledWidth, int scaledHeight, Color color);
	
	public void moveWindow(int x, int y) {
		this.x = (int) (this.x - x);
		this.y = (int) (this.y - y);
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}
