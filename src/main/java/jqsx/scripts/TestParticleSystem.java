package jqsx.scripts;

import KanapkaEngine.Components.Particle;
import KanapkaEngine.Components.ParticleSystem;
import KanapkaEngine.Components.ResourceLoader;
import KanapkaEngine.Time;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.image.BufferedImage;

public class TestParticleSystem extends ParticleSystem<TestParticle> {

    private static BufferedImage[] spriteSheet = setupParticles();

    private double lastEmit = Time.time();

    @Override
    public double getLifeTime() {
        return 10;
    }

    @Override
    public void onParticleUpdate(TestParticle particle, double fixedDelta) {
        particle.addPosition(new Vector2D(20 * Math.sin((particle.getPosition().getX() + particle.getPosition().getY() + Time.time() * 30) / 30.0), 20).scalarMultiply(fixedDelta));
    }

    @Override
    public void onUpdate(double fixedDelta) {
        if (lastEmit + 0.1 < Time.time()) {
            SpawnOffset(new Vector2D(15 * (0.5 - Math.random()), 15 * (0.5 - Math.random())));
            lastEmit = Time.time();
        }
    }

    @Override
    public TestParticle createParticle(Vector2D offset) {
        return new TestParticle(getParent().transform.getPosition().add(offset));
    }

    @Override
    public BufferedImage getRender(Particle particle) {
        return spriteSheet[(int)(Math.floor((Time.time() + particle.getPosition().getX() * 0.02 + particle.getPosition().getY() * 0.02) * 4.0 % 4.0))];
    }

    private static BufferedImage[] setupParticles() {
        BufferedImage[] images = new BufferedImage[4];
        BufferedImage m = ResourceLoader.loadResource("fireParticles/particles1.png");
        for (int i = 0; i < 4; i++) {
            images[i] = m.getSubimage(16 * i + 16 * 11, 0, 16, 16);
        }
        return images;
    }
}
