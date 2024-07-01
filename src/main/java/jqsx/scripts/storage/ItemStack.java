package jqsx.scripts.storage;

import KanapkaEngine.Components.Mathf;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Objects;

public class ItemStack {
    private String id;
    private int amount;

    public String[] tags = new String[0];

    public ItemStack(String id, int amount) {
        this(Item.get(id), amount);
    }

    public ItemStack(Item item, int amount) {
        Objects.requireNonNull(item);
        id = item.id;
        this.amount = Mathf.Clamp(amount, 1, getItem().maxStack);
    }

    public ItemStack(Items item, int amount) {
        this(item.item, amount);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    public Item getItem() {
        return Item.get(id);
    }

    public ItemDrop createDrop(Vector2D position) {
        ItemDrop drop = new ItemDrop(this);
        drop.transform.setPosition(position);

        drop.name = getItem().itemName;

        drop.append();

        return drop;
    }

    public ItemStack clone() {
        ItemStack clone = new ItemStack(id, amount);

        clone.tags = tags;

        return clone;
    }
}
