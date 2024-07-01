package jqsx.Blocks;

import KanapkaEngine.Components.BlockData;

import java.awt.image.BufferedImage;

public class SolidType extends BlockData {
    public SolidType(BufferedImage image) {
        super(image);
        hasCollision = true;
    }
}
