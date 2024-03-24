package jqsx.scripts;

import KanapkaEngine.Components.*;
import KanapkaEngine.Game.Input;
import KanapkaEngine.Time;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Player extends Entity {
    private int id;
    private boolean local = false;

    private Rigidbody rb;
    private Collider collider;

    public Player() {
        super();

        addComponent(rb = new Rigidbody());
        addComponent(collider = new Collider());
    }

    public int getId() {

        return id;
    }

    public void claimLocalAuthority() {
        local = true;
    }

    @Override
    public void Update() {
        if (local) {
            int x = (Input.isKeyDown('a') ? -1 : 0) + (Input.isKeyDown('d') ? 1 : 0);
            int y = (Input.isKeyDown('s') ? -1 : 0) + (Input.isKeyDown('w') ? 1 : 0);

            rb.setVelocity(new Vector2D(Mathf.Lerp(rb.getVelocity().getX(), x * 50.0, Time.deltaTime() * 15.0), Mathf.Lerp(rb.getVelocity().getY(), y * 50.0, Time.deltaTime() * 15.0)));

            Camera.main.setPosition(Mathf.Lerp(Camera.main.getPosition(), transform.getPosition().scalarMultiply(-1), Time.deltaTime() * 10.0));
        }
    }

    public boolean isLocalPlayer() {
        return local;
    }
}
