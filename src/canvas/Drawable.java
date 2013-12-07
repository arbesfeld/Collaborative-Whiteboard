package canvas;

import java.awt.Color;
import java.awt.Stroke;

public interface Drawable {
    public int width();
    public int height();
    
    public void drawPixel(Pixel pixel);
    public void drawLine(Pixel pixelStart, Pixel pixelEnd, Stroke stroke);
    public Color getPixelColor(Pixel pixel);
}
