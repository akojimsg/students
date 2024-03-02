package com.codechallenge;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class AppTest {

    @Test
    public void testMapWithListWithNoFilter() throws Exception {
        List<String> list = List.of("a;apple", "b;bananas", "c;citrus", "m;mango", "p;pineapple");
        Map<String, String> map = App.createMapFromCollection(list, "");
        assertEquals(5, map.size());
        assertEquals("apple", map.get("a"));
        assertEquals("pineapple", map.get("p"));
    }

    @Test
    public void testMapWithListWithFilter() throws Exception {
        List<String> list = List.of("a;apple", "b;bananas", "c;citrus", "m;mango", "p;pineapple");
        Map<String, String> map = App.createMapFromCollection(list, "c");
        assertEquals(4, map.size());
        assertEquals("apple", map.get("a"));
        assertNull(map.get("c"));
    }

    @Test
    public void testMapWithInvalidInput() throws Exception {
        List<String> list = List.of("a-apple", "b-bananas", "c-citrus", "m-mango", "p;pineapple");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            App.createMapFromCollection(list, "c");
        });

        String expectedMessage = "java.lang.Exception: Item: 'a-apple' is not a valid input";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
