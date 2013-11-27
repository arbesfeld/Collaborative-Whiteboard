package stroke;

import java.awt.Color;


import pixel.Pixel;
import util.Vector2;

public interface StrokeType {
    public Pixel[] paintPoint(Color color, int strokeWidth, int x, int y, Vector2 velocity);
}
