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
	
	private LayerProperties(LayerIdentifier layerIdentifier, double opacity, boolean visible) {
		this.layerIdentifier=layerIdentifier;
		this.opacity=opacity;
		this.visible=visible;
		
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
	public double getOpacity() {
		return this.opacity;
	}
	
	public LayerIdentifier layerIdentifier() {
		return layerIdentifier;
	}
	
	public String toString() {
		return this.layerIdentifier.toString();
	}
	
	public LayerProperties clone() {
		return new LayerProperties(this.layerIdentifier, this.opacity, this.visible);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((layerIdentifier == null) ? 0 : layerIdentifier.hashCode());
		long temp;
		temp = Double.doubleToLongBits(opacity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (visible ? 1231 : 1237);
		return result;
	}

	

}
