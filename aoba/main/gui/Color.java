package aoba.main.gui;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;

public class Color {

	public int r;
	public int g;
	public int b;

	public float hue;
	public float saturation;
	public float value;
	
	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color(float hue) {
		this.setHSV(hue, 1f, 1f);
	}

	public void setHSV(float hue, float saturation, float value) {
		this.hue = hue;
		this.saturation = saturation;
		this.value = value;
		Vector3i vec = hsv2rgb(hue, saturation, value);
		this.r = vec.getX();
		this.g = vec.getY();
		this.b = vec.getZ();
	}

	// Sets the color based on R G B values from 0-255
	public void setRGB(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	// Sets the color based on R G B values from 0f-1.0f
	public void setRGB(float r, float g, float b) {
		this.r = (int) r * 255;
		this.g = (int) g * 255;
		this.b = (int) b * 255;
	}
	
	public String getColorAsString() {
		String rs = Integer.toString((int) (r));
		String gs = Integer.toString((int) (g));
		String bs = Integer.toString((int) (b));
		return rs + gs + bs;
	}

	public int getColorAsInt() {
		int R = (this.r << 16) & 0x00FF0000;
		int G = (this.g << 8) & 0x0000FF00;
		int B = this.b & 0x000000FF;

	    return 0xFF000000 | R | G | B;
		//return Integer.parseInt(rs + gs + bs);
	}

	public String getColorAsHex() {
		return String.format("#%06X", this.getColorAsInt());
	}
	
	public void setRed(int r) {
		this.r = r;
	}
	
	public void setGreen(int g) {
		this.g = g;
	}
	
	public void setBlue(int b) {
		this.b = b;
	}

	public static String rgbToString(int r, int g, int b) {
		String rs = Integer.toString((int) (r));
		String gs = Integer.toString((int) (g));
		String bs = Integer.toString((int) (b));
		return rs + gs + bs;
	}

	public static int rgbToInt(int r, int g, int b) {
		String rs = Integer.toString((int) (r));
		String gs = Integer.toString((int) (g));
		String bs = Integer.toString((int) (b));
		return Integer.parseInt(rs + gs + bs);
	}

	public static int convertRGBToHex(int r, int g, int b) {
		String strr = StringUtils.leftPad(Integer.toHexString(r), 2, '0');
		String strg = StringUtils.leftPad(Integer.toHexString(g), 2, '0');
		String strb = StringUtils.leftPad(Integer.toHexString(b), 2, '0');
		String string = strr + strg + strb;
		return Integer.parseInt(string, 16);
	}

	public static Vector3i convertHextoRGB(String hex) {
		String RString = hex.charAt(1) + "" + hex.charAt(2);
		String GString = hex.charAt(3) + "" + hex.charAt(4);
		String BString = hex.charAt(5) + "" + hex.charAt(6);

		float r = Integer.valueOf(RString, 16);
		float g = Integer.valueOf(GString, 16);
		float b = Integer.valueOf(BString, 16);
		return new Vector3i(r, g, b);
	}

	public static Vector3i hsv2rgb(float hue, float saturation, float value) {
		float h = (hue / 60);
		float chroma = value*saturation;
		float x = chroma * (1- Math.abs((h % 2) - 1));
		
		Vector3f rgbVec;
	    if(h >= 0 && h <= 1) {
	    	rgbVec = new Vector3f(chroma,x,0);
	    }else if(h >= 1 && h <= 2) {
	    	rgbVec = new Vector3f(x,chroma,0);
	    }else if(h >= 2 && h <= 3) {
	    	rgbVec = new Vector3f(0,chroma,x);
	    }else if(h >= 3 && h <= 4) {
	    	rgbVec = new Vector3f(0,x,chroma);
	    }else if(h >= 4 && h <= 5) {
	    	rgbVec = new Vector3f(x,0,chroma);
	    }else if(h >= 5 && h <= 6) {
	    	rgbVec = new Vector3f(chroma,0,x);
	    }else {
	    	rgbVec = null;
	    }
	    
	    if(rgbVec != null) {
	    	float m = value - chroma;
	 	    Vector3f rgb = new Vector3f(rgbVec.getX() + m, rgbVec.getY() + m, rgbVec.getZ() + m);
	 	    
	 	    return new Vector3i(255 * rgb.getX(),255 * rgb.getY(), 255 * rgb.getZ());
	    }
	    return null;
	}

}
