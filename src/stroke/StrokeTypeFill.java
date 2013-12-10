package stroke;

import java.awt.Color;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandFill;

public class StrokeTypeFill implements StrokeType {

    @Override
    public DrawCommand[] paintLine(Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity, int symetry) {
        if (x1 == x2 && y1 == y2) {
            DrawCommand command = new DrawCommandFill(new Pixel(x1, y1, color));
            return new DrawCommand[]{ command };
        }
        return new DrawCommand[]{ };
    } 
    
    @Override
    public String toString() {
        return "Fill";
    }

}
