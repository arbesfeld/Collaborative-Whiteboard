package stroke;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;

public class StrokeTypeProc2 implements StrokeType {

    @Override
    public Pixel[] paintPoint(Drawable canvas, Color color, int strokeWidth, int x, int y, Vector2 velocity) {
        List<Pixel> result = new LinkedList<Pixel>();
        double rand = Math.random() * 2*Math.PI;
        double length = 1 + Math.random() * 2.5;
        for (int j = 0; j < strokeWidth * length; j++) {
            result.add(new Pixel((int)(x + Math.sin(rand)*j), (int)(y + Math.cos(rand)*j), new Color(color.getRed(),color.getGreen(), color.getBlue(), (int)(j/(float)(strokeWidth * length)*255))));
        }
        
        return result.toArray(new Pixel[result.size()]);
    }
    
    @Override
    public String toString() {
        return "Hair";
    }

}
