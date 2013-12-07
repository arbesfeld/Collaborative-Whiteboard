package stroke;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandLine;

public class StrokeTypeSquares implements StrokeType {

    @Override
    public DrawCommand[] paintLine(Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity) {
        List<DrawCommand> result = new LinkedList<DrawCommand>();
        BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        int cx = Math.round(x1 / (float)100) * 100;
        int cy = Math.round(y1 / (float)100) * 100;
        
        int dx = (cx - x1) * 3;
        int dy = (cy - y1) * 3;

        for (int i = 0; i < 4; i++)
        {
                Pixel startPixel = new Pixel(cx, cy, new Color(color.getRed(), color.getGreen(), color.getGreen(), 30));
                Pixel endPixel = new Pixel((int)(cx + Math.random() * dx), (int)(cy + Math.random() * dy), new Color(0,0,0,0));
                result.add(new DrawCommandLine(startPixel, endPixel, stroke));
        } 
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    @Override
    public String toString() {
        return "Squares";
    }
}
