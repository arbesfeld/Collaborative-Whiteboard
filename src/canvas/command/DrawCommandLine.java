package canvas.command;

import java.awt.BasicStroke;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import canvas.Drawable;
import canvas.Pixel;

public class DrawCommandLine extends DrawCommand {
    private static final long serialVersionUID = 1777067036196897234L;
    
    private final Pixel pixelStart;
    private final Pixel pixelEnd;
    private transient BasicStroke stroke;
    private final int symetry;
    
    public DrawCommandLine(Pixel pixelStart, Pixel pixelEnd, BasicStroke stroke, int symetry) {
        this.pixelStart = pixelStart;
        this.pixelEnd = pixelEnd;
        this.stroke = stroke;
        this.symetry = symetry;
    }
    
    @Override
    public void drawOn(Drawable drawable) {
        drawable.drawLine(pixelStart, pixelEnd, stroke, symetry);
    }
    
    private synchronized void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeFloat(stroke.getLineWidth());
        out.writeInt(stroke.getEndCap());
        out.writeInt(stroke.getLineJoin());
    }

    private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        float width = in.readFloat();
        int endCap = in.readInt();
        int lineJoin = in.readInt();
        stroke = new BasicStroke(width, endCap, lineJoin);
    }
}
