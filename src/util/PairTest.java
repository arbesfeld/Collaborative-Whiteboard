package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PairTest {
    
    @Test
    public void testEquals(){
        Pair<Integer, String> pair1 = new Pair<Integer, String>(5, "hello");
        Pair<Integer, String> pair2 = new Pair<Integer, String>(5, "hello");
        Pair<Integer, String> pair3 = new Pair<Integer, String>(4, "hello");
        Pair<String, String> pair4 = new Pair<String, String>("hello", "world");
        
        assertTrue(pair1.equals(pair1));
        assertTrue(pair1.equals(pair2));
        assertTrue(pair2.equals(pair1));
        
        assertFalse(pair1.equals(pair3));
        assertFalse(pair3.equals(pair1));
        assertFalse(pair3.equals(pair2));
        
        assertFalse(pair4.equals(pair1));
    }
    
    @Test 
    public void testHashCode(){
        Pair<Integer, String> pair1 = new Pair<Integer, String>(40, "foo");
        Pair<Integer, String> pair2 = new Pair<Integer, String>(40, "foo");
        assertTrue(pair1.hashCode() == pair2.hashCode());
    }
}
