package jqsx.scripts;

import KanapkaEngine.Components.Chunk;
import KanapkaEngine.Components.ChunkNode;
import KanapkaEngine.Components.IndexRenderer;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Tree extends ChunkNode {
    public Tree(Chunk parent) {
        super(parent);

        transform.setPosition(parent.getPosition());

        IndexRenderer renderer = new IndexRenderer();
        renderer.id = 0;
        addComponent(renderer);
        transform.setSize(new Vector2D(48, 48));
    }
}
