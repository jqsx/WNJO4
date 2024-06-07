package jqsx.scripts.storage;

import KanapkaEngine.Components.Mathf;

public class Inventory {
    private final int size;
    private final ItemStack[] items;

    public Inventory(int size) {
        this.size = Mathf.Clamp(size, 0, size);
        this.items = new ItemStack[size];
    }

    public int getSize() {
        return size;
    }

    public ItemStack getItem(int i) {
        return items[Mathf.Clamp(i, 0, size)];
    }

    public void setItem(int i, ItemStack itemStack) {
        items[Mathf.Clamp(i, 0, size)] = itemStack;
    }

    // TODO
    public ItemStack addItem(ItemStack itemStack) {
        return null;
    }
}
