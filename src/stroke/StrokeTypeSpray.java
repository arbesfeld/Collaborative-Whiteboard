package stroke;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import util.Vector2;
import canvas.Pixel;

public class StrokeTypeSpray implements StrokeType {

    @Override
    public Pixel[] paintPoint(Color color, int strokeWidth, int x, int y, Vector2 velocity) {
        List<Pixel> result = new LinkedList<Pixel>();      
        int extraWidth = 8-(int)Math.sqrt(Math.pow(velocity.x(), 2) + Math.pow(velocity.y(), 2));
        if (extraWidth < 0) {extraWidth = 0;}
        for (int i = x+strokeWidth; i <= x+strokeWidth; i++) {
            for (int j = y-strokeWidth; j <= y+strokeWidth; j++) {
                if (Math.random() > .5) {
                    result.add(new Pixel(i, j, color));
                }
            }
        }  
        return result.toArray(new Pixel[result.size()]);
    }
    
    @Override
    public String toString() {
        return "Spray";
    }

}
