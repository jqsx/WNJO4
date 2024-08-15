package jqsx.Blocks;

import KanapkaEngine.Components.Block;
import KanapkaEngine.Components.BlockData;
import jqsx.scripts.storage.ItemStack;
import jqsx.scripts.storage.Items;

import java.awt.image.BufferedImage;

public class DropType extends SolidType implements Drops {
    private Items[] drops;

    private final BlockRegistry replacer;
    public DropType(BufferedImage image, Items... drop) {
        this(image, null, drop);
    }

    public DropType(BufferedImage image, BlockRegistry replace, Items... drop) {
        super(image);
        this.drops = drop;
        this.replacer = replace;
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
        if (replacer == null) {
            block.parent.setAir(block.point);
            return false;
        }
        block.parent.createBlock(replacer.getRandom().getID(), block.point);
        return true;
    }
}
