package jqsx;

import KanapkaEngine.Components.BlockData;
import KanapkaEngine.Components.BlockManager;
import KanapkaEngine.Components.SimpleViewController;
import KanapkaEngine.Engine;
import KanapkaEngine.Game.*;
import jqsx.World.ProceduralWorld;
import jqsx.scripts.PlayerInput;

import java.awt.*;

public class Game implements GameLogic {

    public static Game game;
    public static Engine engine;

    public static void run() {
        game = new Game();

        Scene sampleScene = new Scene(new ProceduralWorld());
        sampleScene.load();
        SceneManager.loadScene(sampleScene);

        BlockManager.createBlock(new BlockData("block.png"));
        BlockManager.createBlock(new BlockData("shortgrass.png"));

        EngineConfiguration config = new EngineConfiguration();
        config.height = 600;
        config.width = 800;
        config.FPSLIMIT = 9999;

        engine = new Engine(game, config);

        engine.InitializeLayers();

        engine.load(new SimpleViewController());
        engine.load(new PlayerInput());

        engine.getWindow().setWorldBackdrop(new Color(99, 153, 107));
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
