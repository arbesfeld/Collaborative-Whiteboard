package stroke;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;

public final class StrokeTypeEraser implements StrokeType {

    @Override
    public Pixel[] paintPoint(Drawable canvas, Color color, int strokeWidth, int x, int y, Vector2 velocity) {
        List<Pixel> result = new LinkedList<Pixel>();
        
        for (int i = x-strokeWidth/2; i <= x+strokeWidth/2; i++) {
            for (int j = y-strokeWidth/2; j <= y+strokeWidth/2; j++) {
                result.add(new Pixel(i, j, Color.WHITE));
            }
        }
        
        return result.toArray(new Pixel[result.size()]);
    }
    
    @Override
    public String toString() {
        return "Eraser";
    }

}
