package stroke;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;

public final class StrokeTypeBasic implements StrokeType {

    @Override
    public Pixel[] paintPoint(Drawable canvas, Color color, int strokeWidth, int x, int y, Vector2 velocity) {
        List<Pixel> result = new LinkedList<Pixel>();
        for (int j = y-strokeWidth/2; j <= y+strokeWidth/2; j++) {
            int width = (int)Math.sqrt(Math.abs(strokeWidth/2 - Math.pow(j - y,2)));
            for (int i = x - (strokeWidth/2 - width); i <= x + strokeWidth/2 - width; i++) {
                result.add(new Pixel(i, j, color));
            }
        }
        
        return result.toArray(new Pixel[result.size()]);
    }
    
    @Override
    public String toString() {
        return "Basic";
    }

}