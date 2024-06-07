package jqsx.World;

import KanapkaEngine.Components.*;
import KanapkaEngine.Game.SceneManager;

import java.awt.*;
import java.util.Random;

public class ProceduralWorld extends World {

    private final Mathf.NoiseGenerator generator = new Mathf.NoiseGenerator(new Random().nextDouble());
    private final Mathf.NoiseGenerator n_generator = new Mathf.NoiseGenerator(new Random().nextDouble());

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
                    double n = n_generator.noise((chunk_block_space.x + i) * 5.3, (chunk_block_space.y - j) * 5.3) * 10.0;
                    double b = generator.noise((chunk_block_space.x + i) * .7, (chunk_block_space.y - j) * .7) * 10.0;
                    if (b > 0.5) {
                        new Block(chunk, new Point(i, j), 4 + (int)Math.floor(4 * Mathf.Clamp01((b - 0.5) / 4.0)));
                    }
                    else if (n > 1.5 && b < 0.2) {
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
