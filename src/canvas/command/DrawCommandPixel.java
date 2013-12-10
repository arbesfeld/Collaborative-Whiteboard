package canvas.command;

import name.LayerIdentifier;
import canvas.Drawable;
import canvas.Pixel;

public class DrawCommandPixel extends DrawCommand {
    private static final long serialVersionUID = 1777067036196897284L;
    
    private final Pixel pixel;
    
    public DrawCommandPixel(LayerIdentifier id, Pixel pixel) {
    	super(id);
        this.pixel = pixel;
    }
    
    @Override
    public void drawOn(Drawable drawable) {
        drawable.drawPixel(id, pixel);
    }
}
