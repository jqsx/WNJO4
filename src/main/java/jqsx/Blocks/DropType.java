package jqsx.Blocks;

import jqsx.scripts.storage.ItemStack;
import jqsx.scripts.storage.Items;

import java.awt.image.BufferedImage;

public class DropType extends SolidType implements Drops {
    private Items[] drops;
    public DropType(BufferedImage image, Items... drop) {
        super(image);
        this.drops = drop;
    }

    @Override
    public ItemStack[] getDrops() {
        ItemStack[] d = new ItemStack[drops.length];

        for (int i = 0; i < d.length; i++) {
            d[i] = new ItemStack(drops[i], 1);
        }

        return d;
    }
}
