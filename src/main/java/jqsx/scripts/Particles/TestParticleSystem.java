package jqsx.scripts.Particles;

import KanapkaEngine.Components.*;
import KanapkaEngine.Time;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.image.BufferedImage;

public class TestParticleSystem extends ParticleSystem<TestParticle> {

    private static BufferedImage[] spriteSheet = setupParticles();

    private double lastEmit = Time.time();

    @Override
    public double getLifeTime() {
        return 1;
    }

    @Override
    public void onParticleUpdate(TestParticle particle, double fixedDelta) {
//        double mx = Mathf.Noise.noise(particle.getPosition().getX() + Time.time() * 30, particle.getPosition().getY() + Time.time() * 60);
//        particle.addVelocity(new Vector2D(10 * mx, 20).scalarMultiply(fixedDelta));
    }

    @Override
    public void onSpawn(TestParticle instance) {
        super.onSpawn(instance);

        instance.addVelocity(new Vector2D((0.5 - Math.random()) * 10, (0.5 - Math.random()) * 20));
    }

    @Override
    public void onUpdate(double fixedDelta) {
        super.onUpdate(fixedDelta);
        /*if (lastEmit + 0.02 < Time.time()) {
            Spawn();//SpawnOffset(new Vector2D(15 * (0.5 - Math.random()), 15 * (0.5 - Math.random())));
            lastEmit = Time.time();
        }*/
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
        BufferedImage m = ResourceLoader.loadResource("fireParticles/All_Fire_Bullet_Pixel_16x16_06.png");
        for (int i = 0; i < 4; i++) {
            images[i] = m.getSubimage(16 * i + 16 * 6, 16 * 5, 16, 16);
        }
        return images;
    }
}
