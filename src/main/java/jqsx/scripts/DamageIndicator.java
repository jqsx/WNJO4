package jqsx.scripts;

import KanapkaEngine.Components.TSLinkedList;
import KanapkaEngine.Time;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class DamageIndicator {
    protected static TSLinkedList<DamageIndicator> indicators = new TSLinkedList<>();
    public Vector2D position;
    public String text;

    private double start = Time.time();

    public DamageIndicator(Vector2D position, String text) {
        this.position = position;
        this.text = text;

        indicators.addEnd(this);
    }

    public boolean isDead() {
        return start + 2 < Time.time();
    }
}
