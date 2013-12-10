package canvas.layer;

import name.LayerIdentifier;

public class LayerProperties {
	
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
