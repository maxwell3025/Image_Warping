package src;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class BitMap {
	int[] data;
	int width;
	int height;

	public BitMap(int width, int height) {
		this.width = width;
		this.height = height;
		data = new int[width * height];
		Arrays.fill(data, -1);
	}

	public BitMap(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		data = image.getRGB(0, 0, width, height, data, 0, width);
	}

	public void copyToImage(BufferedImage b) {
		b.setRGB(0, 0, width, height, data, 0, width);
	}

	public void setRGB(int x, int y, int color) {
		data[x + y * width] = color;
	}

	public void setRGB(int x, int y, int a, int r, int g, int b) {
		int color = (a << 24) | (r << 16) | (g << 8) | (b);
		data[x + y * width] = color;
	}

	public int getRGB(int x, int y) {
		try{
		return data[x + y * width];
		}catch(Exception e){
			return 0;
		}
		}

	public void fill(int color) {
		Arrays.fill(data, color);
	}

	public void fill(int a, int r, int g, int b) {
		int color = (a << 24) | (r << 16) | (g << 8) | (b);
		Arrays.fill(data, color);
	}

	public void setRGB(int dstx, int dsty, int srcx, int srcy, BitMap src) {
		setRGB(dstx, dsty, src.getRGB(srcx, srcy));
	}

}
