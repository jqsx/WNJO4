package jqsx.scripts;

import KanapkaEngine.Components.Node;
import KanapkaEngine.Components.Renderable;
import KanapkaEngine.Components.Renderer;
import KanapkaEngine.Components.ResourceLoader;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;

public class logo extends Node implements Renderable {
    public logo() {
        super();

        Renderer renderer = new Renderer();

        addComponent(renderer);

        renderer.setTexture(ResourceLoader.loadResource("logo.png"));

        transform.setSize(new Vector2D(50, 50));
        transform.setPosition(new Vector2D(-40, 0));
    }

    @Override
    public void onRender(Graphics2D graphics2D, Vector2D vector2D, Vector2D vector2D1) {
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(graphics2D.getFont().deriveFont(30f));
        double shiftx = vector2D1.getX() / 2.0 - graphics2D.getFontMetrics().getStringBounds("Kanapka Studios", graphics2D).getX() / 2.0;
        Point point = new Point((int) (vector2D.getX() + shiftx), (int) (vector2D.getY() + vector2D1.getY() + 20));
        graphics2D.drawString("Kanapka Studios", point.x, point.y);

        graphics2D.drawString("Made using Kanapka Engine", point.x, + point.y + 50);
    }
}
