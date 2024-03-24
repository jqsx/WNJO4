package jqsx;

import KanapkaEngine.Components.*;
import KanapkaEngine.Components.Component;
import KanapkaEngine.Components.Renderer;
import KanapkaEngine.Engine;
import KanapkaEngine.Game.*;
import KanapkaEngine.RenderLayers.Chunks;
import KanapkaEngine.Time;
import jqsx.World.ProceduralWorld;
import jqsx.scripts.Player;
import jqsx.scripts.PlayerInput;
import jqsx.scripts.TestParticleSystem;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Game implements GameLogic {

    public static Game game;
    public static Engine engine;
    TestParticleSystem test = new TestParticleSystem();
    public static void run() {
        game = new Game();

        Physics.gravity = new Vector2D(0, 0);

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
        config.window_title = "WNJO";

        engine = new Engine(game, config);
        engine.InitializeLayers();

//        engine.load(new SimpleViewController());
        engine.load(new PlayerInput());

        Chunks.DEACTIVATIONDELAY = 3.0;

        engine.getWindow().setWorldBackdrop(new Color(195, 214, 87));
    }
    @Override
    public void Start() {
        Node system = new Node();
        system.addComponent(test);
        system.transform.setSize(new Vector2D(16, 16));

        system.append();

        {
            Player player = new Player();
            player.transform.setPosition(new Vector2D(0, 0));
            player.claimLocalAuthority();

            player.transform.setSize(new Vector2D(12, 12));

            player.addComponent(new Renderer());
            player.getRenderer().setTexture(ResourceLoader.loadResource("MiniWorldSprites/Characters/Soldiers/Melee/RedMelee/AssasinRed.png").getSubimage(0, 0, 16, 16));

            player.append();
        }

        {
            Node node = new Node();
            node.addComponent(new Renderer());
            node.getRenderer().setTexture(ResourceLoader.loadResource("wooden.png"));

            node.transform.setPosition(new Vector2D(150, 0));
            node.transform.setSize(new Vector2D(50, 50));

            node.addComponent(new Collider());

            node.append();
        }
    }

    @Override
    public void Update() {

    }

    @Override
    public void End() {

    }
}
