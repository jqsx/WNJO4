package jqsx.scripts.storage;

import KanapkaEngine.Components.Collider;
import KanapkaEngine.Components.Rigidbody;
import jqsx.scripts.ItemDropRenderer;
import jqsx.scripts.NetSync;
import jqsx.scripts.entities.Entity;
import jqsx.scripts.entities.player.Player;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Objects;

public class ItemDrop extends Entity {
    private ItemStack stored;
    public ItemDrop(ItemStack itemStack) {
        super();

        this.stored = itemStack;
        setInvoulnerable(true);

        addComponent(new Collider());
        getCollider().noMass = true;
        addComponent(new Rigidbody());

        transform.setSize(new Vector2D(6, 6));

        addComponent(new ItemDropRenderer());

        addComponent(new NetSync());
    }

    public ItemStack getStored() {
        return stored;
    }

    public void setStored(ItemStack itemStack) {
        Objects.requireNonNull(itemStack);

        this.stored = itemStack;
    }

    public void pickup(Player player) {
        stored = player.getInventory().addItem(getStored());
        if (stored == null)
            Destroy();
    }
}
