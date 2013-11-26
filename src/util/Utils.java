package util;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Random;

public class Utils {
    public static Color colorFromString(String colorString) {
        try {
            Field field = Class.forName("java.awt.Color").getField(colorString);
            return (Color)field.get(null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Color " + colorString + " not defined.");
        }
    }
    
    public static int generateId() {
        Random rand = new Random();
        return rand.nextInt(10000000);
    }
}
