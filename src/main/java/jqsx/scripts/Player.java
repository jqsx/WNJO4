package jqsx.scripts;

import KanapkaEngine.Components.*;
import KanapkaEngine.Game.Input;
import KanapkaEngine.Net.NetworkIdentity;
import KanapkaEngine.Time;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements Renderable {

    public static Player localPlayer;

    public static List<Player> players = new ArrayList<>();

    private int id;
    private boolean local = false;

    private final Rigidbody rb;
    private final Collider collider;

    private final NetworkIdentity identity;
    private final NetSync sync;

    public Player(int id) {
        super();

        addComponent(rb = new Rigidbody());
        addComponent(collider = new Collider());
        addComponent(identity = new NetworkIdentity(id));
        addComponent(sync = new NetSync());


        players.add(this);
    }

    public int getId() {

        return id;
    }

    public void claimLocalAuthority() {
        local = true;
        localPlayer = this;
    }

    @Override
    public void Update() {
        if (local) {
            int x = (Input.isKeyDown('a') ? -1 : 0) + (Input.isKeyDown('d') ? 1 : 0);
            int y = (Input.isKeyDown('s') ? -1 : 0) + (Input.isKeyDown('w') ? 1 : 0);

            rb.setVelocity(new Vector2D(Mathf.Lerp(rb.getVelocity().getX(), x * 50.0, Time.deltaTime() * 15.0), Mathf.Lerp(rb.getVelocity().getY(), y * 50.0, Time.deltaTime() * 15.0)));

            Camera.main.setPosition(transform.getPosition().scalarMultiply(-1));
        }
    }

    public boolean isLocalPlayer() {
        return local;
    }

    @Override
    public void onRender(Graphics2D main, AffineTransform at) {
        main.setColor(Color.red);
        main.drawString("ID " + id, (int) at.getTranslateX(), (int) at.getTranslateY());
    }

    @Override
    public void onDestroy() {
        players.remove(this);
        if (local)
            localPlayer = null;
    }
}
