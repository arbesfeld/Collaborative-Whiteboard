package canvas.layer;

import static org.junit.Assert.assertEquals;


import name.LayerIdentifier;

import org.junit.Test;


public class LayerPropertiesTest {
    @Test
    public void testLayerIdentifier() {
        // test getting layerIdentifier
        LayerIdentifier layerId = new LayerIdentifier(5, "Layer");
        LayerProperties layerProperties = new LayerProperties(layerId);
        assertEquals(layerProperties.layerIdentifier(), layerId);
    }
}
