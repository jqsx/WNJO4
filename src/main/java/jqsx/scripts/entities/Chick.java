package jqsx.scripts.entities;

import KanapkaEngine.Components.ResourceLoader;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Chick extends Chicken {
    public Chick() {
        super();

        transform.setSize(new Vector2D(10, 10));

        getRenderer().setTexture(ResourceLoader.loadResource("MiniWorldSprites/Animals/Chick.png"));
    }
}
