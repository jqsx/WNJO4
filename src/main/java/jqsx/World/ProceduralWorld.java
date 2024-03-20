package jqsx.World;

import KanapkaEngine.Components.*;
import KanapkaEngine.Game.Scene;
import KanapkaEngine.Game.SceneManager;
import jqsx.scripts.Tree;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ProceduralWorld extends World {

    private final Mathf.NoiseGenerator generator = new Mathf.NoiseGenerator(400);

    @Override
    public void onGet(Point p) {
        if (!hasChunk(p.x, p.y))
            generateChunkAt(p.x, p.y);
    }

    private void generateChunkAt(int x, int y) {
        new Thread(() -> {
            Chunk chunk = Chunk.build(new Point(x, y), this);
            int size = SceneManager.getCurrentlyLoaded().getChunkSize();
            Point chunk_block_space = new Point(x * size, y * size);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    double n = Mathf.Noise.noise((chunk_block_space.x + i) * 5.3, (chunk_block_space.y - j) * 5.3) * 10.0;
                    double b = generator.noise((chunk_block_space.x + i) * .7, (chunk_block_space.y - j) * .7) * 10.0;
                    if (b > 0.5) {
                        new Block(chunk, new Point(i, j), 4 + (int)Math.floor(4 * Mathf.Clamp01((b - 0.5) / 4.0)));
                    }
                    else if (n > 1.5 && b < 0.2 && j % 2 == 0) {
                        new Block(chunk, new Point(i, j), 9 + (int)Math.floor(Mathf.Clamp01(n - 1.5) / 2.0 * 3));
                    }
                    else if (n < -1.5 && b < .2 ) {
                        new Block(chunk, new Point(i, j), 12 + (int)Math.floor(Mathf.Clamp01(Math.abs(n) - 1.5) * 6));
                    }
                }
            }
            chunk.ready();

        }).start();
    }
}
