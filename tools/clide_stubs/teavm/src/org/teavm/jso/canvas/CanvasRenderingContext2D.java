package org.teavm.jso.canvas;

import org.teavm.jso.JSObject;

/**
 * Compile-only stub of TeaVM's {@code CanvasRenderingContext2D}, covering
 * only the two methods PlantUML's PNG export path calls
 * (see clide_stubs/teavm/README.md).
 */
public interface CanvasRenderingContext2D extends JSObject {

	ImageData createImageData(int width, int height);

	void putImageData(ImageData imageData, int x, int y);

}
