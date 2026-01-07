package de.daver.unigate.dimension.category;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryCache {

    private static final Map<String, Category> CACHE = new ConcurrentHashMap<>();

    public static boolean exists(String id) {
        return CACHE.containsKey(id);
    }

    public static Category get(String id) {
        return CACHE.get(id);
    }

    public static void put(Category category) {
        CACHE.put(category.id(), category);
    }
}
