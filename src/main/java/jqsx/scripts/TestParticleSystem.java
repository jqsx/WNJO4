package jqsx.scripts;

import KanapkaEngine.Components.Particle;
import KanapkaEngine.Components.ParticleSystem;
import KanapkaEngine.Components.ResourceLoader;
import KanapkaEngine.Time;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.image.BufferedImage;

public class TestParticleSystem extends ParticleSystem<TestParticle> {

    private static BufferedImage[] spriteSheet = setupParticles();

    @Override
    public double getLifeTime() {
        return 3;
    }

    @Override
    public void onParticleUpdate(TestParticle particle, double fixedDelta) {
        particle.addPosition(new Vector2D(100 * (0.5 - Math.random()), 100 * (0.5 - Math.random())).scalarMultiply(fixedDelta));
    }

    @Override
    public TestParticle createParticle(Vector2D offset) {
        return new TestParticle(getParent().transform.getPosition().add(offset));
    }

    @Override
    public BufferedImage getRender(Particle particle) {
        return spriteSheet[(int)(Math.floor(Time.time() * 16.0 % 4.0))];
    }

    private static BufferedImage[] setupParticles() {
        BufferedImage[] images = new BufferedImage[4];
        BufferedImage m = ResourceLoader.loadResource("fireParticles/particles1.png");
        for (int i = 0; i < 4; i++) {
            images[i] = m.getSubimage(16 * i + 16 * 6, 16 * 7, 16, 16);
        }
        return images;
    }
}
