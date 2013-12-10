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
		this.visible=true;
	}
	
	public void setOpacity(double opacity){
		this.opacity = opacity;
	}
	public void setVisibility(Boolean visible){
		this.visible=visible;
	}
	public boolean getVisibility() {
		return this.visible;
	}
	
	public LayerIdentifier layerIdentifier() {
		return layerIdentifier;
	}
	
	public String toString() {
		return this.layerIdentifier.toString();
	}
}
