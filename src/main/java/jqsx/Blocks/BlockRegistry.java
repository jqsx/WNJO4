package jqsx.Blocks;

import KanapkaEngine.Components.BlockData;
import KanapkaEngine.Components.BlockManager;
import KanapkaEngine.Components.ResourceLoader;
import jqsx.scripts.storage.Items;

import java.awt.image.BufferedImage;
import java.util.Random;

public enum BlockRegistry {

    ROCKS(getRocks()),
    SHORE(getShore()),
    TREES(getTrees()),
    STUMP(getStump()),
    BLOCK(new BlockData("block.png")),
    SHORTGRASS(new BlockData("shortgrass.png")),
    FLOWERS(getFlowers());

    public final BlockData[] blocks;

    BlockRegistry(BlockData... blocks) {
        this.blocks = blocks;
    }

    public static void init() {
        for (BlockRegistry blockRegistry : BlockRegistry.values()) {
            for (BlockData blockData : blockRegistry.blocks) {
                BlockManager.createBlock(blockData);
            }
        }
    }

    public BlockData getRandom() {
        return blocks[new Random().nextInt(blocks.length)];
    }

    private static BlockData[] getShore() {
        BlockData[] data = new BlockData[5];
        { // shore
            BufferedImage rocks = ResourceLoader.loadResource("MiniWorldSprites/Ground/Shore.png");

            for (int i = 0; i < 5; i++) {
                data[i] = new FloorBlockType(rocks.getSubimage(i * 16, 0, 16, 16));
            }
        }

        return data;
    }

    private static BlockData[] getRocks() {
        BlockData[] data = new BlockData[6];
        { // rocks
            BufferedImage rocks = ResourceLoader.loadResource("MiniWorldSprites/Nature/Rocks.png");

            for (int i = 0; i < 3; i++) {
                data[i] = new DropType(rocks.getSubimage(i * 16, 0, 16, 16), Items.Stone);
                data[i].blockStrength = 50;
            }

            for (int i = 3; i < 6; i++) {
                data[i] = new DropType(rocks.getSubimage((i - 3) * 16, 16, 16, 16), Items.Stone);
                data[i].blockStrength = 50;
            }
        }

        return data;
    }

    private static BlockData[] getTrees() {
        BlockData[] data = new BlockData[3];
        { // trees
            BufferedImage trees = ResourceLoader.loadResource("MiniWorldSprites/Nature/Trees.png");
            BlockManager.createBlock(new DropType(trees.getSubimage(16, 0, 16, 16), Items.Wood, Items.Leaf));
            BlockManager.createBlock(new DropType(trees.getSubimage(32, 0, 16, 16), Items.Wood, Items.Leaf));
            BlockManager.createBlock(new DropType(trees.getSubimage(48, 0, 16, 16), Items.Wood, Items.Leaf));

            for (int i = 0; i < 3; i++) {
                data[i] = new DropType(trees.getSubimage(16 + i * 16, 0, 16, 16), Items.Wood, Items.Leaf);
                data[i].blockStrength = 3;
            }
        }

        return data;
    }

    private static BlockData[] getStump() {
        BufferedImage trees = ResourceLoader.loadResource("MiniWorldSprites/Nature/Trees.png");
        return new BlockData[] { new DropType(trees.getSubimage(0, 0, 16, 16), Items.Wood) };
    }

    private static BlockData[] getFlowers() {
        return new BlockData[] { new DropTypeNoCol(ResourceLoader.loadResource("RedFlower.png")), new DropTypeNoCol(ResourceLoader.loadResource("YellowFlower.png")) };
    }
}
