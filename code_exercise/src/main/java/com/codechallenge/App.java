package com.codechallenge;

import java.util.*;

// Create a method that takes a collection of strings where each string has a semi-colon
// separated pair of tokens, and returns a map, where the key is the 1st token in each string
// and the value is the 2nd token in each string, filtering out any keys that match a value that is also passed to the method.
public class App {
    public static Map<String, String> createMapFromCollection(Collection<String> collection, String filter) throws Exception {
        Map <String, String> map = new HashMap<>();
        collection.stream().forEach(item -> {
            StringTokenizer tokenizer = new StringTokenizer(item, ";");
            if(tokenizer.countTokens() != 2){
                try {
                    throw new Exception(String.format("Item: '%s' is not a valid input", item));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            String key = tokenizer.nextToken();
            String value = tokenizer.nextToken();

            if(!key.equals(filter)) map.put(key, value);
        });
        return map;
    }

    public static void main(String... args) {
        System.out.println("Code Challenge solution of Crystal IT Java Candidates 2022");
        System.out.println("-----------------------------------------------------------");

        Collection collection = List.of("1;a", "2;b", "3;c", "4;d", "age;94", "gender;female", "somekey;somevalue");
        Map<String, String> map = null;
        try {
            System.out.println("Map from collection without any filtered value ...");
            map = createMapFromCollection(collection, "");
            System.out.println(map.toString());

            System.out.println("Map from collection with a filter on somekey ...");
            map = createMapFromCollection(collection, "somekey");
            System.out.println(map.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
