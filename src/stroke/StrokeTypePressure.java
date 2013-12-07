package stroke;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandPixel;

public final class StrokeTypePressure implements StrokeType {
    
    
    @Override
    public DrawCommand[] paintLine(Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity) {
        List<DrawCommand> result = new LinkedList<DrawCommand>();      
        int extraWidth = 8-(int)Math.sqrt(Math.pow(velocity.x(), 2) + Math.pow(velocity.y(), 2));
        if (extraWidth < 0) {extraWidth = 0;}
        for (int i = x1-strokeWidth/2 - extraWidth/2 ; i <= x1+strokeWidth/2 + extraWidth/2; i++) {
            for (int j = y1-strokeWidth/2 - extraWidth/2; j <= y1+strokeWidth/2 + extraWidth/2; j++) {
                Pixel pixel = new Pixel(i, j, color);
                if (!canvas.getPixelColor(pixel).equals(pixel.color())) {
                    result.add(new DrawCommandPixel(pixel));
                }
            }
        }
        
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    @Override
    public String toString() {
        return "Pressure";
    }

}