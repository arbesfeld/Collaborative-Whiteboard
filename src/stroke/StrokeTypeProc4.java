package stroke;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;

public class StrokeTypeProc4 implements StrokeType {

    @Override
    public Pixel[] paintPoint(Drawable canvas, Color color, int strokeWidth, int x, int y, Vector2 velocity) {
        List<Pixel> result = branchBulder(color, x, y, velocity);
        return result.toArray(new Pixel[result.size()]);
    }
    
    private List<Pixel> branchBulder(Color color, int x, int y, Vector2 velocity) {
        List<Pixel> result = new LinkedList<Pixel>();
        double length = 1 + Math.random() * 1.5;
        for (int j = 0; j < 2 * length; j++) {
            result.add(new Pixel((int)(x + Math.min(velocity.x()*j, 20)), (int)(y + Math.min(velocity.y()*j, 20)), new Color(color.getRed(),color.getGreen(), color.getBlue(), (int)(j/(float)(2 * length)*255))));
            if (Math.random() > .8) {
                result.addAll(branchBulder(color, (int)(x + Math.min(-velocity.x()*j, 20)), (int)(y + Math.min(velocity.y()*j, 20)), new Vector2(-velocity.x(), velocity.y())));
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "Follow Tree";
    }


}