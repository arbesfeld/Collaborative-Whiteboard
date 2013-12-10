package canvas.command;

import java.io.Serializable;

import name.LayerIdentifier;
import canvas.Drawable;

public abstract class DrawCommand implements Serializable {
    private static final long serialVersionUID = -4765442741260871845L;
    
    protected final LayerIdentifier id;
    
    public DrawCommand(LayerIdentifier id) {
    	this.id = id;
    }
    public LayerIdentifier id() {
    	return id;
    }
    
    public abstract void drawOn(Drawable drawable);
}
