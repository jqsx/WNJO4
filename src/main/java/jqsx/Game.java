package jqsx;

import KanapkaEngine.Components.*;
import KanapkaEngine.Components.Renderer;
import KanapkaEngine.Engine;
import KanapkaEngine.Game.*;
import KanapkaEngine.Net.NetworkServer;
import KanapkaEngine.RenderLayers.Chunks;
import KanapkaEngine.Time;
import KanapkaEngine.UI.Image;
import KanapkaEngine.UI.Text;
import KanapkaEngine.UI.UI;
import KanapkaEngine.UI.UIComponent;
import jqsx.Blocks.DropType;
import jqsx.Blocks.DropTypeNoCol;
import jqsx.Blocks.FloorBlockType;
import jqsx.Net.NetworkInterface;
import jqsx.Net.Router;
import jqsx.World.ProceduralWorld;
import jqsx.scripts.*;
import jqsx.scripts.entities.Chick;
import jqsx.scripts.entities.Chicken;
import jqsx.scripts.entities.Player;
import jqsx.scripts.storage.Craft;
import jqsx.scripts.storage.ItemStack;
import jqsx.scripts.storage.Items;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game implements GameLogic {
    public static Game game;
    public static Engine engine;

    public static Font global_font;

    private static boolean started = false;
    public static void run() {
        game = new Game();

        Physics.gravity = new Vector2D(0, 0);

        try {
            global_font = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.loadStream("font/PressStart2P-Regular.ttf"));
        } catch (FontFormatException | IOException e) {

        }

        register_crafts();

        BlockManager.createBlock(new BlockData("block.png"));
        BlockManager.createBlock(new BlockData("shortgrass.png"));
        BlockManager.createBlock(new BlockData("tree.png"));

        // 3

        { // shore
            BufferedImage rocks = ResourceLoader.loadResource("MiniWorldSprites/Ground/Shore.png");
            BlockManager.createBlock(new FloorBlockType(rocks.getSubimage(0, 0, 16, 16)));
            BlockManager.createBlock(new FloorBlockType(rocks.getSubimage(16, 0, 16, 16)));
            BlockManager.createBlock(new FloorBlockType(rocks.getSubimage(32, 0, 16, 16)));
            BlockManager.createBlock(new FloorBlockType(rocks.getSubimage(48, 0, 16, 16)));
            BlockManager.createBlock(new FloorBlockType(rocks.getSubimage(64, 0, 16, 16)));
        }

        // 8

        { // trees
            BufferedImage trees = ResourceLoader.loadResource("MiniWorldSprites/Nature/Trees.png");
            BlockManager.createBlock(new DropType(trees.getSubimage(0, 0, 16, 16), Items.Wood));
            BlockManager.createBlock(new DropType(trees.getSubimage(16, 0, 16, 16), Items.Wood, Items.Leaf));
            BlockManager.createBlock(new DropType(trees.getSubimage(32, 0, 16, 16), Items.Wood, Items.Leaf));
            BlockManager.createBlock(new DropType(trees.getSubimage(48, 0, 16, 16), Items.Wood));

            for (int i = 9; i < 12; i++) {
                BlockManager.getBlockData(i).blockStrength = 3;
            }
        }

        // 12

        { // rocks
            BufferedImage rocks = ResourceLoader.loadResource("MiniWorldSprites/Nature/Rocks.png");
            BlockManager.createBlock(new DropType(rocks.getSubimage(0, 0, 16, 16), Items.Stone));
            BlockManager.createBlock(new DropType(rocks.getSubimage(16, 0, 16, 16), Items.Stone));
            BlockManager.createBlock(new DropType(rocks.getSubimage(32, 0, 16, 16), Items.Stone));

            BlockManager.createBlock(new DropType(rocks.getSubimage(0, 16, 16, 16), Items.Stone));
            BlockManager.createBlock(new DropType(rocks.getSubimage(16, 16, 16, 16), Items.Stone));
            BlockManager.createBlock(new DropType(rocks.getSubimage(32, 16, 16, 16), Items.Stone));

            for (int i = 13; i < 19; i++) {
                BlockManager.getBlockData(i).blockStrength = 50;
            }
        }

        // 18

        { // flowers
            BlockManager.createBlock(new DropTypeNoCol(ResourceLoader.loadResource("RedFlower.png")));
            BlockManager.createBlock(new DropTypeNoCol(ResourceLoader.loadResource("YellowFlower.png")));
        }

        // 20

        IndexRenderer.addImage(ResourceLoader.loadResource("tree.png"));

        EngineConfiguration config = new EngineConfiguration();
        config.height = 600;
        config.width = 800;
        config.FPSLIMIT = 60;
        config.window_title = "WNJO";

        engine = new Engine(game, config);
        engine.InitializeLayers();

//        engine.load(new SimpleViewController());
        engine.load(new PlayerInput());
        engine.load(new DelayDestroy());

        Chunks.DEACTIVATIONDELAY = 3.0;

        {
            BufferedImage icon = ResourceLoader.loadResource("logo.png");

            engine.getWindow().setIconImage(icon);
            if (Taskbar.isTaskbarSupported())
                Taskbar.getTaskbar().setIconImage(icon);
        }


        engine.RegisterRenderLayer(new InventoryDisplay());

        loadIntoScene();
    }

    private static void loadIntoScene() {
        Scene intro = new Scene();
        intro.load();
        SceneManager.loadScene(intro);

        engine.getWindow().setWorldBackdrop(new Color(0, 0, 0));

        Scheduler.delay(() -> {
            UI.currentlyDisplayed = new UI();
//            Game.startGame();
            Game.startLimbo();
        }, 2);

        {
            Image image = new Image();

            image.setImage(ResourceLoader.loadResource("logo.png"));

            image.size = new Vector2D(500, 500);

            image.pivot = UIComponent.Pivot.Center;
        }

        {
            Text test = new Text();

            test.setFont(global_font);

            test.setSize(30);

            test.setText("Made using KanapkaEngine");

            test.position = new Vector2D(0, 300);
        }

        {
            Text jqsx = new Text();

            jqsx.setFont(global_font);

            jqsx.setSize(25);

            jqsx.setText("< Made by JQSx >");

            jqsx.setColor(Color.RED);

            jqsx.position = new Vector2D(0, -70);

            jqsx.pivot = UIComponent.Pivot.Bottom;
        }

        {
            Text wnjo = new Text();

            wnjo.setFont(global_font);

            wnjo.setSize(100);

            wnjo.setText("WNJO");
        }
    }

    @Override
    public void Start() {

    }
    public static void startGame() {
/*
        Node system = new Node();
        system.addComponent(test);
        system.transform.setSize(new Vector2D(16, 16));

        system.append();
*/
        Scene sampleScene = new Scene(new ProceduralWorld());
        sampleScene.setGlobalSize(2);
        sampleScene.load();
        SceneManager.loadScene(sampleScene);
        started = true;

        engine.getWindow().setWorldBackdrop(new Color(195, 214, 87));

        {
            Player player = new Player(0);

            player.claimLocalAuthority();
        }

        networkInit();

//        for (int i = 0; i < 16; i++) {
//            Chicken chicken = new Chicken();
//
//            chicken.append();
//        }
//
//        for (int i = 0; i < 9; i++) {
//            Chick chick = new Chick();
//
//            chick.append();
//        }
    }


    public static void startLimbo() {
        Scene sampleScene = new Scene(new World());
        sampleScene.setGlobalSize(5);
        sampleScene.load();
        SceneManager.loadScene(sampleScene);
        engine.getWindow().setWorldBackdrop(Color.black);

        PlayerInput.isInventoryEnabled = false;

        engine.load(new NetworkInterface());
    }

    private double net_server_last = Time.time();
    @Override
    public void Update() {
        if (!started) return;
        if (net_server_last < Time.time()) {
            Router.positionSync.syncAllPlayers();
            net_server_last = Time.time() + 0.05;
        }
    }
    @Override
    public void End() {

    }
    private static void networkInit() {
        NetworkServer.StartServer();
    }

    private static void register_crafts() {
        new Craft("planks", new ItemStack(Items.Plank, 1), .25, new ItemStack(Items.Wood, 4));
        new Craft("debug", new ItemStack(Items.Debug, 1), .1, new ItemStack(Items.Leaf, 1));
        new Craft("workbench", new ItemStack(Items.WorkBench, 1), 4, new ItemStack(Items.Wood, 10), new ItemStack(Items.Stone, 5));
        new Craft("stoneblock", new ItemStack(Items.StoneBlock, 1), .4, new ItemStack(Items.Stone, 10));
        new Craft("iron", new ItemStack(Items.IronIngot, 1), 5, new ItemStack(Items.StoneBlock, 5));
        new Craft("iron_pickaxe", new ItemStack(Items.IronPickaxe, 1), 10, new ItemStack(Items.IronIngot, 3), new ItemStack(Items.Plank, 5));
    }
}
