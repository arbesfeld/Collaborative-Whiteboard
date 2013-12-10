package stroke;

import java.awt.BasicStroke;
import java.awt.Color;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandLine;

public final class StrokeTypeEraser implements StrokeType {

    @Override
    public DrawCommand[] paintLine(Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity, int symetry) {
        BasicStroke stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        DrawCommand command = new DrawCommandLine(new Pixel(x1, y1, Color.WHITE), new Pixel(x2, y2, color), stroke, symetry);
        return new DrawCommand[]{ command };
    }
    
    @Override
    public String toString() {
        return "Eraser";
    }

}
