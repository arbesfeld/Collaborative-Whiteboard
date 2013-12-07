package stroke;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandPixel;

public class StrokeTypeFill implements StrokeType {

    @Override
    public DrawCommand[] paintLine(Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity) {
        List<DrawCommand> result = new LinkedList<DrawCommand>();
        HashSet<Pixel> pixels = new HashSet<Pixel>();
        result.addAll(fillAround(canvas, x1,  y1, canvas.getPixelColor(new Pixel(x1,y1,color)), color, pixels));
        return result.toArray(new DrawCommand[result.size()]);
    }
        
    private List<DrawCommand> fillAround(Drawable canvas, int x, int y, Color initialColor, Color newColor, HashSet<Pixel> pixels) {
        List<DrawCommand> result = new LinkedList<DrawCommand>();
            Pixel newPixel = new Pixel(x,y,newColor);
            //System.out.println(pixels.size());
            if (!pixels.contains(newPixel)) {
                if (canvas.getPixelColor(newPixel).equals(initialColor)) {
                    result.add(new DrawCommandPixel(newPixel));
                    pixels.add(newPixel);
                    result.addAll(fillAround(canvas, x + 1,  y, initialColor, newColor, pixels));
                    result.addAll(fillAround(canvas, x,  y + 1, initialColor, newColor, pixels));
                    result.addAll(fillAround(canvas, x - 1,  y, initialColor, newColor, pixels));
                    result.addAll(fillAround(canvas, x,  y - 1, initialColor, newColor, pixels));
                }
            }
      //  }
        return result;
    }
    
    @Override
    public String toString() {
        return "Fill";
    }

}
