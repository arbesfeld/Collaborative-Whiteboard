package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import name.LayerIdentifier;
import util.Utils;
import canvas.layer.Layer;
import canvas.layer.LayerAdjustment;

public class Canvas extends DrawableBase {
    private static final long serialVersionUID = -6329493755553689791L;

    private static final String BASE_NAME = "Default";
    
    // image where the user's drawing is stored
    private final List<Layer> layers;
    private final Map<LayerIdentifier, Layer> layerSet;
    private LayerIdentifier currentLayer;
    
    /**
     * Make a canvas. Only used by the server.
     * @param width width in pixels
     * @param height height in pixels
     */
    public Canvas(int width, int height) {
        super(width, height);
        this.layers = new LinkedList<Layer>();
        this.layerSet = new HashMap<LayerIdentifier, Layer>();
        addLayer(new LayerIdentifier(Utils.generateId(), BASE_NAME));
    }
    
    public void checkRep() {
    	assert layers.size() == layerSet.size();
    }
    
    public synchronized void addLayer(LayerIdentifier layerIdentifier) {
    	Layer layer = new Layer(width, height, layerIdentifier, layers.size());
    	layerSet.put(layerIdentifier, layer);
    	layers.add(layer);
    	this.currentLayer = layerIdentifier;
    	checkRep();
    }
    
    public synchronized void adjustLayer(LayerIdentifier layerIdentifier, LayerAdjustment adjustment) {
    	int level = layerSet.get(layerIdentifier).level();

    	int modifier = 0;
    	switch (adjustment) {
    	case UP:
    		modifier = 1;
    		break;
    	case DOWN:
    		modifier = -1;
    	}
    	
    	int newLevel = level + modifier;
    	
    	if (newLevel < 0 || newLevel >= layers.size()) {
    		return;
    	}
    	
    	Collections.swap(layers, level, newLevel);
    	checkRep();
    }
    
    public Layer[] layers() {
    	checkRep();
    	return layers.toArray(new Layer[layers.size()]);
    }
    
    @Override
    public synchronized void drawPixel(Pixel pixel) {
    	layerSet.get(currentLayer).drawPixel(pixel);
    }
    
    @Override
    public synchronized void drawLine(Pixel pixelStart, Pixel pixelEnd, Stroke stroke, int symetry) {
    	layerSet.get(currentLayer).drawLine(pixelStart, pixelEnd, stroke, symetry);
    }
    
    @Override
    public synchronized void drawFill(Pixel pixel) {
    	layerSet.get(currentLayer).drawFill(pixel);
    }

    @Override
	public synchronized Color getPixelColor(Pixel pixel) {
    	for (Layer layer : layers) {
    	    Color pixelColor = layer.getPixelColor(pixel);
    	    if (!pixelColor.equals(Color.WHITE)) {
    	    	return pixelColor;
    	    }
    	}
    	return Color.WHITE;
	}

    @Override
    public synchronized void paintOnGraphics(Graphics g) {
    	for (Layer layer : layers) {
    	    layer.paintOnGraphics(g);
    	}
    }
}
