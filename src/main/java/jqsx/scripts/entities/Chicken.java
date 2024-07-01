package jqsx.scripts.entities;

import KanapkaEngine.Components.*;
import KanapkaEngine.Time;
import jqsx.scripts.AnimatedEntity;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Random;

public class Chicken extends Entity {
    private AudioClip chickenSound;
    private double move_delay = Time.time();
    private Rigidbody rb;

    private double soundDelay = Time.time() + Math.random() * 4.0;
    private AudioSource source;
    public Chicken() {
        super();

        if (chickenSound == null) {
            chickenSound = ResourceLoader.loadAudio("sound/chickan.wav");
        }

        addComponent(rb = new Rigidbody());
        addComponent(new Collider());

        addComponent(source = new AudioSource());

        source.clip = chickenSound;
        source.falloffDistance = 250f;

        getCollider().noMass = true;

        addComponent(new AnimatedEntity(getRigidbody()));

        getRenderer().setTexture(ResourceLoader.loadResource("MiniWorldSprites/Animals/Chicken.png"));

        transform.setSize(new Vector2D(10, 10));
    }

    @Override
    public void Update() {
        if (move_delay < Time.time()) {
            move_delay = Time.time() + 0.5 + Math.random() * 0.3;

            Vector2D direction = switch (new Random().nextInt(4)) {
                case 0 -> new Vector2D(0, 1);
                case 1 -> new Vector2D(1, 0);
                case 2 -> new Vector2D(0, -1);
                default -> new Vector2D(-1, 0);
            };

            rb.setVelocity(direction.scalarMultiply(20 + Math.random() * 20));
        }

        if (soundDelay < Time.time()) {
            source.play();

            soundDelay = Time.time() + Math.random() * 4.0;
        }

        rb.setVelocity(Mathf.Lerp(rb.getVelocity(), new Vector2D(0, 0), Time.deltaTime()));
    }
}
