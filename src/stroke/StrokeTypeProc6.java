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

public class StrokeTypeProc6 implements StrokeType {

    @Override
    public DrawCommand[] paintLine(Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity) {
        List<DrawCommand> result = new LinkedList<DrawCommand>();
        BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        int bound = 30;
        for (int i = -bound; i < bound; i++) {
            for (int j = -bound; j < bound; j++) {
                Pixel checkPixel =new Pixel(x1 + i, y1 + j, new Color(color.getRed(), color.getGreen(),color. getBlue(), 0));
                if (canvas.getPixelColor(checkPixel).equals(color)) {
                    if (Math.random() > .95) {
                        result.add(new DrawCommandLine(new Pixel(x1, y1, color), new Pixel(x1 + i/2, y1 + j/2, color), stroke));
                        result.add(new DrawCommandLine(new Pixel(x1, y1, color), checkPixel, stroke));
                    }
                }
            }
        }
        result.add(new DrawCommandLine(new Pixel(x1, y1, color), new Pixel(x2, y2, color), stroke));
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    @Override
    public String toString() {
        return "Sketch";
    }

}
