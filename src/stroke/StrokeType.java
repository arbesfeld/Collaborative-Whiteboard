package stroke;

import java.awt.Color;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;

public interface StrokeType {
    public Pixel[] paintPoint(Drawable canvas, Color color, int strokeWidth, int x, int y, Vector2 velocity);
}
