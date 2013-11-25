package util;

import java.awt.Color;
import java.lang.reflect.Field;

public class Utils {
    public static Color colorFromString(String colorString) {
        try {
            Field field = Class.forName("java.awt.Color").getField(colorString);
            return (Color)field.get(null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Color " + colorString + " not defined.");
        }
    }
}
