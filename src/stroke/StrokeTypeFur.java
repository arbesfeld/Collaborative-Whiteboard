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

public class StrokeTypeFur implements StrokeType {

    @Override
    public DrawCommand[] paintLine(Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity) {
        List<DrawCommand> result = new LinkedList<DrawCommand>();
        BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        int bound = 30;
        for (int i = -bound; i < bound; i++) {
            for (int j = -bound; j < bound; j++) {
                Pixel checkPixel =new Pixel(x1 + i, y1 + j, new Color(10,10,10,0));
                if (canvas.getPixelColor(checkPixel).equals(color)) {
                    double size = -Math.random() * 1.5;
                    int dx = i;
                    int dy = j;
                    int d = dx * dx + dy * dy;
                    if (Math.random() > .9) {
                        Pixel startPixel = new Pixel((int)(x1 + (dx * size)), (int)(y1 + (dy * size)), new Color(color.getRed(), color.getGreen(), color.getGreen(),0));
                        Pixel middlePixel = new Pixel(x1, y1, new Color(color.getRed(), color.getGreen(), color.getGreen(), 150));
                        Pixel endPixel = new Pixel((int)(x1+1 - (dx * size)), (int)(y1+j - (dy * size)), new Color(color.getRed(), color.getGreen(), color.getGreen(),0));
                        result.add(new DrawCommandLine(startPixel, middlePixel, stroke));
                        result.add(new DrawCommandLine(middlePixel, endPixel, stroke));
                    }
                }
            }
        }
        result.add(new DrawCommandLine(new Pixel(x1, y1, color), new Pixel(x2, y2, color), stroke));
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    @Override
    public String toString() {
        return "Fur";
    }
}
