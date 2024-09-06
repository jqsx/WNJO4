package jqsx.scripts.entities.player;

import KanapkaEngine.Components.*;
import KanapkaEngine.Engine;
import KanapkaEngine.Game.Input;
import KanapkaEngine.Net.NetworkIdentity;
import KanapkaEngine.Time;
import jqsx.Blocks.DropType;
import jqsx.Net.NetworkInterface;
import jqsx.scripts.DelayDestroy;
import jqsx.Net.NetSync;
import jqsx.scripts.Particles.BreakParticle;
import jqsx.scripts.Particles.Explosion;
import jqsx.scripts.Particles.TestParticleSystem;
import jqsx.scripts.PlayerInput;
import jqsx.scripts.entities.Entity;
import jqsx.scripts.storage.*;
import jqsx.scripts.storage.Container;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements Renderable {
    public static Player localPlayer;
    public static List<Player> players = new ArrayList<>();
    private final Container container;
    private int selectedSlot = 0;
    private boolean local = false;
    private final Rigidbody rb;
    private final Collider collider;
    private final NetworkIdentity identity;
    private final NetSync sync;
    private double dashDelay = 0.0;
    private TestParticleSystem dashcomp;
    private double breakDelay = Time.time();
    private static byte[] breaking;
    public Player(int connid) {
        super();

        addComponent(rb = new Rigidbody());
        addComponent(collider = new Collider());
        addComponent(identity = new NetworkIdentity(connid));
        addComponent(sync = new NetSync());

        transform.setPosition(new Vector2D(0, 0));

        transform.setSize(new Vector2D(12, 12));

        addComponent(container = new Container(new Inventory(15)));

        addComponent(new Renderer());
        getRenderer().setTexture(ResourceLoader.loadResource("MiniWorldSprites/Characters/Soldiers/Melee/RedMelee/AssasinRed.png").getSubimage(0, 0, 16, 16));

        append();

        if (breaking == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ResourceLoader.loadStream("sound/breaking.wav").transferTo(baos);
                breaking = baos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        {
            dashcomp = new TestParticleSystem();

            Node dash = new Node(this);

            dash.transform.setSize(new Vector2D(5, 5));

            dash.addComponent(dashcomp);

            dash.append();

            //dash.transform.setPosition(new Vector2D(-16, -16));
        }

        players.add(this);
    }

    public int getId() {
        return identity.getNetID();
    }

    public int getNetSyncId() {
        return sync.getId();
    }

    public void claimLocalAuthority() {
        local = true;
        localPlayer = this;
    }

    public Inventory getInventory() {
        return container.getInventory();
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public float getRemainingMiningDelay() {
        return (float) Math.max(breakDelay - Time.time(), 0f);
    }

    public float getMiningDelay() {
        float mining_speed = Math.max(1f + getMainHandStat(Statistics.Type.MiningSpeed) / 10f, 0f);
        return 1f / mining_speed;
    }

    @Override
    public void Update() {
        if (local) {
            int x = 0;
            int y = 0;

            if (PlayerInput.canMove() && !NetworkInterface.isEnabled) {
                x = (Input.isKeyDown('a') ? -1 : 0) + (Input.isKeyDown('d') ? 1 : 0);
                y = (Input.isKeyDown('s') ? -1 : 0) + (Input.isKeyDown('w') ? 1 : 0);
            }

            if (dashDelay < Time.time() && Input.isKeyDown(' ')) {
//                rb.addVelocity(new Vector2D(x, y).scalarMultiply(1600.0));
                dashDelay = Time.time() + 0.7;
//
//                for (int i = 0; i < 16; i++) {
//                    Scheduler.delay(() -> {
//                        TestParticle particle = dashcomp.Spawn();
//
//                        particle.addVelocity(rb.getVelocity().scalarMultiply(-0.1));
//                    }, i / 32.0);
//                }

                Explosion.create(transform.getPosition());
            }
            rb.setVelocity(new Vector2D(Mathf.Lerp(rb.getVelocity().getX(), x * 50.0, Time.deltaTime() * 10.0), Mathf.Lerp(rb.getVelocity().getY(), y * 50.0, Time.deltaTime() * 10.0)));

//            Camera.main.setPosition(Mathf.Lerp(Camera.main.getPosition(), transform.getPosition().scalarMultiply(-1), Time.deltaTime() * 15.0));

            {
                double mag = Mathf.aDistance(Camera.main.getWorldPosition(), transform.getPosition());

                Vector2D direction = transform.getPosition().subtract(Camera.main.getWorldPosition()).scalarMultiply(1.0 / mag);

                if (mag > 0)
                    Camera.main.setPosition(Camera.main.getPosition().add(direction.scalarMultiply(-Mathf.Clamp(mag, 0, 500) * Time.deltaTime())));
            }

            if (breakDelay < Time.time() && Input.isButtonDown(1) && PlayerInput.canMove()) {

                Dimension screen = KanapkaEngine.Game.Window.getWindowSize();

                if (Engine.isMacOS())
                    screen = new Dimension((int) (screen.width / 2.0), (int) (screen.height / 2.0));

                Vector2D direction = (Input.getMousePosition().subtract(new Vector2D(screen.width, screen.height))).normalize();
                direction = new Vector2D(direction.getX(), -direction.getY());
                Block[] blocks = Physics.castBlocks(transform.getPosition().add(direction.scalarMultiply(10)), new Vector2D(8, 8));

                if (blocks.length == 0) {
                    breakDelay = Time.time() + 0.05;
                }
                else {
                    breakDelay = Time.time() + getMiningDelay();
                }

                for (Block block : blocks) {
                    BreakParticle.createBreak(block.getCenter());
                    playBreakingSound();

                    int mining_damage = 1;
                    mining_damage += (int) getMainHandStat(Statistics.Type.MiningDamage);

                    block.damage += mining_damage;

                    if (block.damage >= block.getBlockData().blockStrength) {
                        if (block.getBlockData() instanceof DropType dropType) {
                            dropType.onBreak(block);
                        }
                        else block.parent.setAir(block.point);
                    }
                }
            }

            World.setBlock(transform.getPosition().add(new Vector2D(10, 0)), 0);

            if (breakDelay < Time.time() && Input.isKeyDown('e') && PlayerInput.canMove()) {
                Node[] nodes = Physics.castNode(transform.getPosition(), new Vector2D(50, 50));

                for (Node node : nodes) {
                    if (node instanceof ItemDrop drop) {
                        drop.pickup(this);
                    }
                }

                breakDelay = Time.time() + 0.3;
            }
        }
    }

    private float getMainHandStat(Statistics.Type type) {
        ItemStack handItem = PlayerInput.getSelectedItem();
        if (handItem != null) {
            if (handItem.getItem().statistics != null) {
                return handItem.getItem().statistics.get(type);
            }
        }
        return 0f;
    }

    public void drop(int i, boolean all) {
        Inventory inventory = getInventory();

        ItemStack item = inventory.getItem(i);

        if (item == null) return;

        if (item.getAmount() > 1) {
            if (all) {
                item.createDrop(transform.getPosition(), NetSync.getFreeID());
                inventory.setItem(i, null);
            }
            else {
                ItemStack one = new ItemStack(item.getId(), 1);
                one.createDrop(transform.getPosition(), NetSync.getFreeID());
                item.setAmount(item.getAmount() - 1);
            }
        }
        else {
            item.createDrop(transform.getPosition(), NetSync.getFreeID());
            inventory.setItem(i, null);
        }
    }

    private void playBreakingSound() {
        Node node = new Node();

        node.transform.setPosition(transform.getPosition());

        AudioSource source;

        node.addComponent(source = new AudioSource());

        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(breaking)));
                source.clip = new AudioClip(clip);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            }

            if (source.clip != null) {
                source.play();

                node.append();
                DelayDestroy.destroy(node, source.clip.clip.getMicrosecondLength() / Math.pow(10.0, 6.0));
            }
        }).start();
    }

    private InputStream cloneInputStream(InputStream stream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            stream.transferTo(baos);
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    public boolean isLocalPlayer() {
        return local;
    }

    @Override
    public void onRender(Graphics2D main, Vector2D position, Vector2D scale) {

    }

    @Override
    public void onDestroy() {
        players.remove(this);
        if (local)
            localPlayer = null;
    }
}
