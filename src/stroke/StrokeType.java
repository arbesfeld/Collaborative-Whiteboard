package stroke;

import java.awt.Color;

import canvas.Vector2;

import pixel.Pixel;

public interface StrokeType {
    public Pixel[] paintPoint(Color color, int strokeWidth, int x, int y, Vector2 velocity);
}
