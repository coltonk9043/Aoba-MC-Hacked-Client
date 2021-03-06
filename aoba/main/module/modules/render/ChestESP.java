package aoba.main.module.modules.render;

import org.lwjgl.glfw.GLFW;

import aoba.main.gui.Color;
import aoba.main.misc.RainbowColor;
import aoba.main.module.Module;
import aoba.main.settings.BooleanSetting;
import aoba.main.settings.SliderSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;

public class ChestESP extends Module {
	private Color currentColor;
	private Color color;
	private RainbowColor rainbowColor;

	public SliderSetting hue = new SliderSetting("Hue", "chestesp_hue", 4, 0, 360, 1);
	public BooleanSetting rainbow = new BooleanSetting("Rainbow", "chestesp_rainbow");
	public SliderSetting effectSpeed = new SliderSetting("Effect Spd", "chestesp_effectspeed", 4, 1, 20, 0.1);
	
	public ChestESP() {
		this.setName("ChestESP");
		this.setBind(new KeyBinding("key.chestesp", GLFW.GLFW_KEY_UNKNOWN, "key.categories.aoba"));
		this.setCategory(Category.Render);
		this.setDescription("Allows the player to see Chests with an ESP.");
		color = new Color(hue.getValueFloat());
		currentColor = color;
		rainbowColor = new RainbowColor();
		this.addSetting(hue);
		this.addSetting(rainbow);
		this.addSetting(effectSpeed);
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onToggle() {

	}

	@Override
	public void onUpdate() {
		if(this.rainbow.getValue()) {
			this.rainbowColor.update(this.effectSpeed.getValueFloat());
			this.currentColor = this.rainbowColor.getColor();
		}else {
			this.color.setHSV(hue.getValueFloat(), 1f, 1f);
			this.currentColor = color;
		}
	}

	@Override
	public void onRender() {
		for (TileEntity entity : mc.world.loadedTileEntityList) {
			if(entity instanceof ChestTileEntity) {
				float r = currentColor.r;
				float g = currentColor.g;
				float b = currentColor.b;
				this.getRenderUtils().TileEntityESPBox(entity, r / 255f, g / 255f, b / 255f);
			}
		}
	}

	@Override
	public void onSendPacket(IPacket<?> packet) {
		
	}

	@Override
	public void onReceivePacket(IPacket<?> packet) {
		
		
	}

}
