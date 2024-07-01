package jqsx.scripts.Particles;

import KanapkaEngine.Components.Node;
import KanapkaEngine.Components.Particle;
import KanapkaEngine.Components.ParticleSystem;
import KanapkaEngine.Components.ResourceLoader;
import KanapkaEngine.Game.Scheduler;
import com.sun.jdi.connect.Connector;
import jqsx.scripts.DelayDestroy;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class BreakParticle extends ParticleSystem<Particle> {
    public BreakParticle() {
        super();

        setTexture(ResourceLoader.loadResource("smoke.png"));
    }

    @Override
    public double getLifeTime() {
        return 1;
    }

    @Override
    public void onParent() {
        DelayDestroy.destroy(getParent(), 1);
        spawnPoof();
    }

    public void spawnPoof() {
        int offset = (int)Math.floor(360 * Math.random());
        for (int i = 0; i < 3; i++) {
            Particle particle = Spawn();

            double rad = Math.toRadians(i * 120 + offset);
            particle.addVelocity(new Vector2D(Math.cos(rad), Math.sin(rad)).scalarMultiply(10));
        }
    }

    @Override
    public void onParticleUpdate(Particle particle, double fixedDelta) {
        particle.addVelocity(new Vector2D(0, -fixedDelta * 10.0));
    }

    @Override
    public void onSpawn(Particle instance) {
        super.onSpawn(instance);

        instance.setPosition(getParent().transform.getPosition());
    }

    public static void createBreak(Vector2D position) {
        Node node = new Node();

        node.transform.setPosition(position);

        node.transform.setSize(new Vector2D(5, 5));

        node.addComponent(new BreakParticle());

        node.append();
    }
}
