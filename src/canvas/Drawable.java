package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;

public interface Drawable {
    public int width();
    public int height();
    
    public void drawPixel(Pixel pixel);
    public void drawLine(Pixel pixelStart, Pixel pixelEnd, Stroke stroke);
    public void drawFill(Pixel pixel);
    public void paintComponent(Graphics g);
    public Color getPixelColor(Pixel pixel);
}
