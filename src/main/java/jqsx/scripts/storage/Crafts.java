package jqsx.scripts.storage;

import java.util.Collection;
import java.util.HashMap;

public class Crafts {
    private static HashMap<String, Craft> crafts = new HashMap<>();

    public static Collection<Craft> getAll() {
        return crafts.values();
    }

    public static int getCraftCount() {
        return crafts.size();
    }

    protected static void register(Craft craft) {
        crafts.put(craft.id, craft);
    }

    public static Craft get(String id) {
        return crafts.get(id);
    }

    private Crafts() {}
}
