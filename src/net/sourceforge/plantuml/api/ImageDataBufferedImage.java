package net.sourceforge.plantuml.api;

import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.core.ImageData;

public class ImageDataBufferedImage implements ImageData {

	private final BufferedImage image;

	public ImageDataBufferedImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	@Override
	public int getWidth() {
		return image.getWidth();
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}

	@Override
	public boolean containsCMapData() {
		return false;
	}

	@Override
	public String getCMapData(String nameId) {
		return null;
	}

	@Override
	public String getWarningOrError() {
		return null;
	}

	@Override
	public int getStatus() {
		return 200;
	}
}
