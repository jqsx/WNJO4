package jqsx;

import KanapkaEngine.Components.*;
import KanapkaEngine.Engine;
import KanapkaEngine.Game.*;
import KanapkaEngine.RenderLayers.Chunks;
import jqsx.World.ProceduralWorld;
import jqsx.scripts.PlayerInput;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Game implements GameLogic {

    public static Game game;
    public static Engine engine;

    public static void run() {
        game = new Game();

        Scene sampleScene = new Scene(new ProceduralWorld());
        sampleScene.setGlobalSize(4);
        sampleScene.load();
        SceneManager.loadScene(sampleScene);

        BlockManager.createBlock(new BlockData("block.png"));
        BlockManager.createBlock(new BlockData("shortgrass.png"));
        BlockManager.createBlock(new BlockData("tree.png"));

        // 3

        { // shore
            BufferedImage rocks = ResourceLoader.loadResource("MiniWorldSprites/Ground/Shore.png");
            BlockManager.createBlock(new BlockData(rocks.getSubimage(0, 0, 16, 16)));
            BlockManager.createBlock(new BlockData(rocks.getSubimage(16, 0, 16, 16)));
            BlockManager.createBlock(new BlockData(rocks.getSubimage(32, 0, 16, 16)));
            BlockManager.createBlock(new BlockData(rocks.getSubimage(48, 0, 16, 16)));
            BlockManager.createBlock(new BlockData(rocks.getSubimage(64, 0, 16, 16)));
        }

        // 8

        { // trees
            BufferedImage trees = ResourceLoader.loadResource("MiniWorldSprites/Nature/Trees.png");
            BlockManager.createBlock(new BlockData(trees.getSubimage(0, 0, 16, 16)));
            BlockManager.createBlock(new BlockData(trees.getSubimage(16, 0, 16, 16)));
            BlockManager.createBlock(new BlockData(trees.getSubimage(32, 0, 16, 16)));
            BlockManager.createBlock(new BlockData(trees.getSubimage(48, 0, 16, 16)));
        }

        // 12

        { // rocks
            BufferedImage rocks = ResourceLoader.loadResource("MiniWorldSprites/Nature/Rocks.png");
            BlockManager.createBlock(new BlockData(rocks.getSubimage(0, 0, 16, 16)));
            BlockManager.createBlock(new BlockData(rocks.getSubimage(16, 0, 16, 16)));
            BlockManager.createBlock(new BlockData(rocks.getSubimage(32, 0, 16, 16)));

            BlockManager.createBlock(new BlockData(rocks.getSubimage(0, 16, 16, 16)));
            BlockManager.createBlock(new BlockData(rocks.getSubimage(16, 16, 16, 16)));
            BlockManager.createBlock(new BlockData(rocks.getSubimage(32, 16, 16, 16)));
        }

        // 18

        IndexRenderer.addImage(ResourceLoader.loadResource("tree.png"));

        EngineConfiguration config = new EngineConfiguration();
        config.height = 600;
        config.width = 800;
        config.FPSLIMIT = 9999;

        engine = new Engine(game, config);
        engine.InitializeLayers();

        engine.load(new SimpleViewController());
        engine.load(new PlayerInput());

        Chunks.DEACTIVATIONDELAY = 3.0;

        engine.getWindow().setWorldBackdrop(new Color(195, 214, 87));
    }

    @Override
    public void Start() {

    }

    @Override
    public void Update() {

    }

    @Override
    public void End() {

    }
}
