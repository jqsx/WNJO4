package jqsx.scripts.Particles;

import KanapkaEngine.Components.*;
import KanapkaEngine.Game.Scheduler;
import jqsx.Blocks.BlockRegistry;
import jqsx.Blocks.DropType;
import jqsx.scripts.DamageIndicator;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Explosion extends ParticleSystem<Particle> {
    private int power;
    public Explosion(int power) {
        this.power = power;
        setTexture(ResourceLoader.loadResource("fireParticles/All_Fire_Bullet_Pixel_16x16_01.png").getSubimage(0, 17, 16, 16));
    }

    @Override
    public void onParent() {
        Scheduler.delay(() -> {
            getParent().Destroy();
        }, getLifeTime());

        int count = 6;
        double dir = 360.0 / count;

        Random random = new Random();

        for (int i = 0; i < count * power; i++) {
            Particle particle = SpawnOffset(getParent().transform.getPosition());

            double c = Math.toRadians(dir / 2.0 * i + random.nextDouble(-20, 20));

            int p = i - (i % count) + 1;

            particle.addVelocity(new Vector2D(Math.cos(c), Math.sin(c)).scalarMultiply(1 + random.nextDouble(0.0, 1.0)).scalarMultiply(p));
        }

        for (int i = 1; i <= power; i++) {
            AtomicInteger index = new AtomicInteger(i);
            Scheduler.delay(() -> {
                Block[] blocks = Physics.castBlocks(getParent().transform.getPosition(), new Vector2D(1, 1).scalarMultiply(index.get() * 16));

                for (Block block : blocks) {
                    if (block.getBlockData().hasCollision) {

                        double dist = Mathf.Clamp(power * 16 - Mathf.aDistance(getParent().transform.getPosition(), block.getCenter()), 0, power * 16);

                        double scaled = dist / (power * 16);

                        double damage = power * 5 * scaled;

                        block.damage += (int) Math.round(damage);

                        if (damage > 0) {
                            new DamageIndicator(block.getCenter(), " -" + Math.round(damage));
                        }

                        if (block.damage >= block.getBlockData().blockStrength) {
                            if (block.getBlockData() instanceof DropType dropType) {

                                if (!dropType.onBreak(block)) {
                                    block.parent.createBlock(BlockRegistry.SHORE.blocks[0].getID(), block.point);
                                }
                            }
                        }
                    }
                }

                Node[] nodes = Physics.castNode(getParent().transform.getPosition(), new Vector2D(1, 1).scalarMultiply(index.get() * 16));

                for (Node node : nodes) {
                    if (node.getRigidbody() != null) {
                        Rigidbody body = node.getRigidbody();
                        double dist = Mathf.Clamp(power * 16 - Mathf.aDistance(getParent().transform.getPosition(), node.transform.getPosition()), 0, power * 16);

                        double scaled = dist / (power * 16);

                        double damage = power * 5 * scaled;

                        Vector2D s = node.transform.getPosition().subtract(getParent().transform.getPosition());

                        double mag = Mathf.aDistance(s, Vector2D.ZERO);

                        if (mag != 0) {
                            s = s.scalarMultiply(1.0 / mag);

                            body.addVelocity(s.scalarMultiply(damage));
                        }
                    }
                }
            }, i / 15.0);

        }
    }

    @Override
    public double getLifeTime() {
        return 5;
    }

    @Override
    public void onParticleUpdate(Particle particle, double fixedDelta) {
        particle.setVelocity(particle.getVelocity().scalarMultiply(1.0 - fixedDelta));
    }

    @Override
    public void onUpdate(double fixedDelta) {
        super.onUpdate(fixedDelta);

        if (getParent() != null) {
            getParent().transform.setSize(Mathf.Lerp(getParent().transform.getSize(), new Vector2D(0.01, 0.01), fixedDelta));
        }
    }

    public static Node create(Vector2D at, int power) {
        Node node = new Node();
        node.transform.setPosition(at);
        node.addComponent(new Explosion(Mathf.Clamp(power, 1, 99)));

        node.transform.setSize(new Vector2D(16, 16));

        node.append();
        return node;
    }

    public static Node create(Vector2D at) {
        return create(at, 4);
    }
}
