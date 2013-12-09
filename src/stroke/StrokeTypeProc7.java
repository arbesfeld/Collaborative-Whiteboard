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
import canvas.command.DrawCommandPixel;

public class StrokeTypeProc7 implements StrokeType {

    @Override
    public DrawCommand[] paintLine(Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity) {
        List<DrawCommand> result = branchBulder(color, x1, y1, velocity);
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    private List<DrawCommand> branchBulder(Color color, int x, int y, Vector2 velocity) {
        List<DrawCommand> result = new LinkedList<DrawCommand>();
        BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        double length = 2 + Math.random() * .5;
        int j = (int) (2*length);
        int dx = (int)(Math.min(velocity.x()*j, 2));
        int dy = (int)(Math.min(velocity.y()*j, 2));
        result.add(new DrawCommandLine(new Pixel(x-dx, y-dy, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)), new Pixel(x, y, color), stroke));
        result.add(new DrawCommandLine(new Pixel(x, y, color), new Pixel(x+dx, y+dy, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)), stroke));
            
        if (Math.random() > .5) {
            result.addAll(branchBulder(color, x+(dx/2), y+(dy/2), new Vector2(velocity.y(), velocity.x())));
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "Crosshatch";
    }
    
}
