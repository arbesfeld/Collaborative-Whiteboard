package canvas;

import java.awt.Color;
import java.awt.Stroke;

import name.LayerIdentifier;

public interface Drawable {
    public int width();
    public int height();
    
    public void drawPixel(LayerIdentifier identifier, Pixel pixel);
    public void drawLine(LayerIdentifier identifier, Pixel pixelStart, Pixel pixelEnd, Stroke stroke, int symetry);
    public void drawFill(LayerIdentifier identifier, Pixel pixel);

    public Color getPixelColor(Pixel pixel);
}
