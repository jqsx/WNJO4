package jqsx.scripts.storage;

import KanapkaEngine.Components.ResourceLoader;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Item {
    private static final HashMap<String, Item> itemMap = new HashMap<>();
    public final String id;
    public final String itemName;
    public final BufferedImage icon;
    public final int maxStack;
    public final Statistics statistics;
    public Item(String id, String itemName, String icon, int maxStack, Statistics statistics) {
        this.id = id;
        this.itemName = itemName;
        this.icon = ResourceLoader.loadResource(icon);
        this.maxStack = Math.max(maxStack, 1);
        this.statistics = statistics;

        itemMap.put(id, this);
    }

    public Item(String id, String itemName, String icon, int maxStack) {
        this(id, itemName, icon, maxStack, null);
    }

    public static Item get(String id) {
        return itemMap.get(id);
    }
}
