package canvas.command;

import java.io.Serializable;

import canvas.Drawable;

public abstract class DrawCommand implements Serializable {
    private static final long serialVersionUID = -4765442741260871845L;
    
    public abstract void drawOn(Drawable drawable);
}
