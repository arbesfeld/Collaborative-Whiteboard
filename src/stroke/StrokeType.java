package stroke;

import java.awt.Color;

import util.Vector2;
import canvas.Drawable;
import canvas.command.DrawCommand;

public interface StrokeType {
    public DrawCommand[] paintLine(Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity);
}
