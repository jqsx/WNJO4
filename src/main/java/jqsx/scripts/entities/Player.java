package jqsx.scripts.entities;

import KanapkaEngine.Components.*;
import KanapkaEngine.Game.Input;
import KanapkaEngine.Net.NetworkIdentity;
import KanapkaEngine.Time;
import jqsx.scripts.NetSync;
import jqsx.scripts.TestParticleSystem;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
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
    private double dashDelay = 0.0;
    private TestParticleSystem dashcomp;
    public Player(int id) {
        super();

        addComponent(rb = new Rigidbody());
        addComponent(collider = new Collider());
        addComponent(identity = new NetworkIdentity(id));
        addComponent(sync = new NetSync());

        transform.setPosition(new Vector2D(0, 0));

        transform.setSize(new Vector2D(12, 12));

        addComponent(new Renderer());
        getRenderer().setTexture(ResourceLoader.loadResource("MiniWorldSprites/Characters/Soldiers/Melee/RedMelee/AssasinRed.png").getSubimage(0, 0, 16, 16));

        append();

        {
            dashcomp = new TestParticleSystem();

            Node dash = new Node(this);

            dash.transform.setSize(new Vector2D(16, 16));

            dash.addComponent(dashcomp);

            dash.append();

            //dash.transform.setPosition(new Vector2D(-16, -16));
        }

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

            if (dashDelay < Time.time() && Input.isKeyDown(' ')) {
                rb.addVelocity(new Vector2D(x * 600.0, y * 600.0));
                dashDelay = Time.time() + 0.5;

                for (int i = 0; i < 16; i++) {
                    dashcomp.Spawn();
                }
            }
            rb.setVelocity(new Vector2D(Mathf.Lerp(rb.getVelocity().getX(), x * 50.0, Time.deltaTime() * 15.0), Mathf.Lerp(rb.getVelocity().getY(), y * 50.0, Time.deltaTime() * 3.0)));

            Camera.main.setPosition(transform.getPosition().scalarMultiply(-1));
        }
    }

    public boolean isLocalPlayer() {
        return local;
    }

    @Override
    public void onRender(Graphics2D main, Vector2D position, Vector2D scale) {
        main.setColor(Color.red);
        main.drawString("ID " + id, (int) position.getX(), (int) position.getY());

        {
            Vector2D size = Chunk.getSize();
            main.drawString("P " + new Point((int) Math.round(transform.getPosition().getX() / size.getX()), (int) Math.round(transform.getPosition().getY() / size.getY())), (int) position.getX(), (int) position.getY() + 10);
        }
    }

    @Override
    public void onDestroy() {
        players.remove(this);
        if (local)
            localPlayer = null;
    }
}
