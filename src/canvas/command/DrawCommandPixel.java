package canvas.command;

import canvas.Drawable;
import canvas.Pixel;

public class DrawCommandPixel extends DrawCommand {
    private static final long serialVersionUID = 1777067036196897284L;
    
    private final Pixel pixel;
    
    public DrawCommandPixel(Pixel pixel) {
        this.pixel = pixel;
    }
    
    @Override
    public void drawOn(Drawable drawable) {
        drawable.drawPixel(pixel);
    }
}
