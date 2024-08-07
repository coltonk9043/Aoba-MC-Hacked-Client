package net.aoba.module;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private static final Map<String, Category> CATEGORIES = new HashMap<>();

    private final String name;

    private Category(String name) {
        this.name = name;
    }

    public static Category of(String name) {
    	String capitalized = name.toLowerCase();
    	if(name.length() >= 2)
    		capitalized = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        return CATEGORIES.computeIfAbsent(capitalized, Category::new);
    }

    public String getName() {
        return name;
    }

    public static Map<String, Category> getAllCategories() {
        return CATEGORIES;
    }
}