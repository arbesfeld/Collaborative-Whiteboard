package stroke;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandPixel;

public class StrokeTypeFill implements StrokeType {

    @Override
    public DrawCommand[] paintLine(Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity) {
        List<DrawCommand> result = new LinkedList<DrawCommand>();
        if (x1 == x2 && y1 == y2) {
            HashSet<Pixel> pixels = new HashSet<Pixel>();
            Queue<Pixel> queue = new LinkedList<Pixel>();
            queue.add(new Pixel(x1, y1, color));
            Color initialColor = canvas.getPixelColor(new Pixel(x1,y1,color));
            
            while (!queue.isEmpty()) {
                Pixel newPixel = queue.remove();
                if (!pixels.contains(newPixel)) {
                    if (canvas.getPixelColor(newPixel).equals(initialColor)) {        
                        result.add(new DrawCommandPixel(newPixel));
                        pixels.add(newPixel);
                        for (int i = -1; i <= 2; i++) {
                            for (int j = -1; j <= 2; j++) {
                                if (!pixels.contains(new Pixel(newPixel.x() + i,  newPixel.y() + j, color))) {
                                    queue.add(new Pixel(newPixel.x() + i,  newPixel.y() + j, color));
                                }
                            }
                        }   
                    }
                }
            }
        }
        return result.toArray(new DrawCommand[result.size()]);
    }
        
    
    @Override
    public String toString() {
        return "Fill";
    }

}
