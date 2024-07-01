package jqsx.Blocks;

import jqsx.scripts.storage.Items;

import java.awt.image.BufferedImage;

public class DropTypeNoCol extends DropType {
    public DropTypeNoCol(BufferedImage image, Items... drop) {
        super(image, drop);
        hasCollision = false;
        floor = false;
    }
}
