package stroke;

import java.awt.Color;

import util.Vector2;
import canvas.Pixel;

public interface StrokeType {
    public Pixel[] paintPoint(Color color, int strokeWidth, int x, int y, Vector2 velocity);
}
