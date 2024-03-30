package jqsx.Blocks;

import KanapkaEngine.Components.BlockData;

import java.awt.image.BufferedImage;

public class FloorBlockType extends BlockData {
    public FloorBlockType(BufferedImage subimage) {
        super(subimage);
        hasCollision = false;
    }
}
