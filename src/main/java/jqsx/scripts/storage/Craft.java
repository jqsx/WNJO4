package jqsx.scripts.storage;

import java.util.Objects;

public class Craft {
    public final ItemStack[] ingredients;
    public final ItemStack result;
    public final String id;

    public final double delay;

    public Craft(String id, ItemStack result, double delay, ItemStack... ingredients) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(result);
        Objects.requireNonNull(ingredients);
        this.result = result;
        this.ingredients = ingredients;
        this.id = id;
        this.delay = delay;

        Crafts.register(this);
    }

    public Craft(String id, ItemStack result, ItemStack... ingredients) {
        this(id, result, 0, ingredients);
    }

    public boolean canCraft(Inventory inventory) {
        for (ItemStack i : ingredients) {
            if (!inventory.contains(i.getId(), i.getAmount()))
                return false;
        }
        return true;
    }

    public void craft(Inventory inventory) {
        if (canCraft(inventory)) {
            for (ItemStack i : ingredients) {
                inventory.remove(i.getId(), i.getAmount());
            }

            inventory.addItem(result);
        }
    }
}
