package canvas.layer;

import java.io.Serializable;

import name.LayerIdentifier;

public class LayerProperties implements Serializable {
	
	private LayerIdentifier layerIdentifier;
	private double opacity;
	private boolean visible;
	
	public LayerProperties(LayerIdentifier layerIdentifier){
		this.layerIdentifier = layerIdentifier;
		this.opacity = 1.0;
	}
	
	public void setOpacity(double opacity){
		this.opacity = opacity;
	}
	public void setVisibility(Boolean visible){
		this.visible=visible;
	}
}
