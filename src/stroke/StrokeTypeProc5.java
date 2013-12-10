package stroke;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import name.LayerIdentifier;

import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandPixel;

public class StrokeTypeProc5 implements StrokeType {

    @Override
    public DrawCommand[] paintLine(LayerIdentifier identifier, Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity, int symetry) {
        List<DrawCommand> result = branchBulder(identifier, canvas, color, x1, y1, velocity);
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    private List<DrawCommand> branchBulder(LayerIdentifier identifier, Drawable canvas, Color color, int x, int y, Vector2 velocity) {
        List<DrawCommand> result = new LinkedList<DrawCommand>();
        double length = 1 + Math.random() * 1.5;
        for (int j = 0; j < 2 * length; j++) {
            Pixel pixel = new Pixel((int)(x + Math.min(velocity.x()*j, 20)), (int)(y + Math.min(velocity.y()*j, 20)), new Color(color.getRed(),color.getGreen(), color.getBlue(), (int)(j/(float)(2 * length)*255)));

            if (!canvas.getPixelColor(identifier, pixel).equals(pixel.color())) {
                result.add(new DrawCommandPixel(identifier, pixel));
            }
            
            if (Math.random() > .8) {
                result.addAll(branchBulder(identifier, canvas, color, (int)(x + Math.min(velocity.x()*j, 20)), (int)(y + Math.min(velocity.y()*j, 20)), new Vector2(velocity.y(), -velocity.x())));
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "Proc5";
    }


}