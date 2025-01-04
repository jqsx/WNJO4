package jqsx.Blocks;

import KanapkaEngine.Components.Block;
import KanapkaEngine.Components.BlockData;
import jqsx.Net.NetSync;
import jqsx.scripts.storage.Item;
import jqsx.scripts.storage.ItemDrop;
import jqsx.scripts.storage.ItemStack;
import jqsx.scripts.storage.Items;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.image.BufferedImage;

public class DropType extends SolidType implements Drops {
    private final Item[] drops;

    private final BlockData replacer;
    public DropType(BufferedImage image, Items... drop) {
        this(image, null, drop);
    }

    public DropType(BufferedImage image, Item... drop) {
        super(image);
        drops = drop;
        replacer = null;
    }

    public DropType(BufferedImage image, BlockData replace, Item... drop) {
        super(image);
        drops = drop;
        this.replacer = replace;
    }

    public DropType(BufferedImage image, BlockRegistry replace, Items... drop) {
        super(image);
        drops = new Item[drop.length];
        for (int i = 0; i < drop.length; i++) {
            drops[i] = drop[i].item;
        }
        if (replace != null)
            this.replacer = replace.getRandom();
        else replacer = null;
    }

    @Override
    public ItemStack[] getDrops() {
        ItemStack[] d = new ItemStack[drops.length];

        for (int i = 0; i < d.length; i++) {
            d[i] = new ItemStack(drops[i], 1);
        }

        return d;
    }

    public boolean onBreak(Block block) {
        if (block.damage < block.getBlockData().blockStrength) return false;
        if (replacer == null) {
            block.parent.setAir(block.point);
            drop(block.getCenter());
            return false;
        }
        block.parent.createBlock(replacer.getID(), block.point);
        drop(block.getCenter());
        return true;
    }

    private void drop(Vector2D at) {
        for (Item item : drops) {
            new ItemStack(item, 1).createDrop(at, NetSync.getFreeID());
        }
    }
}
