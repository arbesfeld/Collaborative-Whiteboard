package util;
import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void testColorFromString() {
        // test created color from string
        Color color = Utils.colorFromString("red");
        assertTrue(color == Color.RED);
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void testInvalidcolorFromString() {
        // test incorrect color name
        Color color = Utils.colorFromString("read");
    }
    
    @Test
    public void testGenerateId() {
        // test if int id is generated
        int id = Utils.generateId();
        assertTrue(0 <= id && id < 10000000);
    }
    
}
