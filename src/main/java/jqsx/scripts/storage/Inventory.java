package jqsx.scripts.storage;

import KanapkaEngine.Components.Mathf;

import java.util.Objects;

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
        items[Mathf.Clamp(i, 0, size)] = itemStack != null ? itemStack.clone() : null;
    }

    public boolean contains(String id, int amount) {
        Objects.requireNonNull(id);
        int total = 0;

        for (int i = 0; i < size; i++) {
            ItemStack item = getItem(i);
            if (item == null)
                continue;
            if (Objects.equals(item.getId(), id)) {
                total += item.getAmount();
            }
        }

        return total >= amount;
    }

    public void remove(String id, int amount) {
        Objects.requireNonNull(id);
        for (int i = 0; i < size; i++) {
            ItemStack item = getItem(i);
            if (item == null)
                continue;
            if (item.getId().equals(id)) {
                amount -= item.getAmount();

                if (amount >= 0) {
                    setItem(i, null);
                }
                else {
                    item.setAmount(-amount);
                    if (item.getAmount() == 0) {
                        setItem(i, null);
                    }
                    return;
                }
            }
        }
    }

    public ItemStack addItem(ItemStack itemStack) {
        itemStack = itemStack.clone();
        for (int i = 0; i < size; i++) {
            ItemStack item = getItem(i);
            if (item != null && item.getId().equals(itemStack.getId())) {
                if (item.getAmount() == item.getItem().maxStack)
                    continue;
                int total = item.getAmount() + itemStack.getAmount();
                int remainder = total - item.getItem().maxStack;

                if (remainder <= 0) {
                    item.setAmount(total);
                    return null;
                }
                else {
                    item.setAmount(item.getItem().maxStack);
                    itemStack.setAmount(remainder);
                }
            }
        }
        for (int i = 0; i < size; i++) {
            ItemStack item = getItem(i);
            if (item == null) {
                setItem(i, itemStack);
                return null;
            }
        }
        return itemStack;
    }
}
