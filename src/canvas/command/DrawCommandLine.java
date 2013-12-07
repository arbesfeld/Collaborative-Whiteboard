package canvas.command;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import canvas.Drawable;
import canvas.Pixel;

public class DrawCommandLine extends DrawCommand {
    private static final long serialVersionUID = 1777067036196897234L;
    
    private final Pixel pixelStart;
    private final Pixel pixelEnd;
    private transient BasicStroke stroke;
    
    public DrawCommandLine(Pixel pixelStart, Pixel pixelEnd, BasicStroke stroke) {
        this.pixelStart = pixelStart;
        this.pixelEnd = pixelEnd;
        this.stroke = stroke;
    }
    
    @Override
    public void drawOn(Drawable drawable) {
        drawable.drawLine(pixelStart, pixelEnd, stroke);
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
