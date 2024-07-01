package jqsx.scripts;

import KanapkaEngine.Components.Component;
import KanapkaEngine.Components.Renderable;
import KanapkaEngine.Components.Renderer;
import KanapkaEngine.Components.Rigidbody;
import KanapkaEngine.Time;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.Objects;

public class AnimatedEntity extends Renderer {
    private Rigidbody rb;
    private Orientation orientation = Orientation.Up;

    private boolean move = false;

    private double lastUpdate = Time.time();

    public AnimatedEntity(Rigidbody rb) {
        Objects.requireNonNull(rb);
        this.rb = rb;
    }

    private enum Orientation {
        Up(0, 1, 1), Down(0, -1, 0), Left(-1, 0, 2), Right(1, 0, 3);

        public final int x;
        public final int y;
        public final int row;
        Orientation(int x, int y, int row) {
            this.x = x;
            this.y = y;
            this.row = row;
        }
    }

    @Override
    public BufferedImage getRender() {
        BufferedImage image = getTexture();

        int m = 0;
        if (move)
            m = (int)Math.floor((Time.time() * 4.0) % 4.0);

        try {
            return image.getSubimage(m * 16, orientation.row * 16, 16, 16);
        }
        catch (RasterFormatException e) {
            System.out.println("m=" + m);
            return null;
        }
    }

    @Override
    public void Update() {
        if (lastUpdate < Time.time()) {
            lastUpdate = Time.time() + 1.0 / 50.0;

            recalculateOrientation();
        }
    }



    private void recalculateOrientation() {
        if (rb != null) {
            Vector2D velocity = rb.getVelocity();
            Vector2D vel = new Vector2D(Math.abs(velocity.getX()), Math.abs(velocity.getY()));

            Point direction = new Point(vel.getX() > vel.getY() ? (int) Math.signum(velocity.getX()) : 0, vel.getY() > vel.getX() ? (int) Math.signum(velocity.getY()) : 0);

            for (Orientation o : Orientation.values())
                if (o.x == direction.x && o.y == direction.y)
                    orientation = o;

            double mag = vel.getX() + vel.getY();

            move = mag > 2.0;
        }
    }
}
