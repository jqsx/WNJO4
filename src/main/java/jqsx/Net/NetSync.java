package jqsx.Net;

import KanapkaEngine.Components.Component;
import KanapkaEngine.Components.Mathf;
import KanapkaEngine.Components.TSLinkedList;
import KanapkaEngine.Net.NetworkIdentity;
import KanapkaEngine.Time;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.Random;

public class NetSync extends Component {

    public boolean hasAuthority = false;

    public static TSLinkedList<NetSync> netObjects = new TSLinkedList<>();

    private NetworkIdentity identity;

    protected Vector2D netPosition = Vector2D.ZERO;

    public int getId() {
        return identity.getNetID();
    }

    public NetSync() {
        super();
    }

    @Override
    public void onParent() {
        super.onParent();

        this.identity = getParent().getComponent(NetworkIdentity.class);

        if (identity != null)
            netObjects.addEnd(this);
    }

    @Override
    public void Update() {
        if (getParent() != null) {
            if (Mathf.aDistance(getParent().transform.getPosition(), netPosition) < 200 && !hasAuthority)
                getParent().transform.setPosition(Mathf.Lerp(getParent().transform.getPosition(), netPosition, Time.deltaTime() * 10f));
            else getParent().transform.setPosition(netPosition);
        }
    }

    @Override
    public void onOrphan() {
        super.onOrphan();

        netObjects.remove(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        netObjects.remove(this);
    }

    public static int getFreeID() {
        Random random = new Random();

        int test = random.nextInt();

        TSLinkedList<NetSync>.Element last = netObjects.getRoot();
        while (last != null) {
            if (last.getValue().getId() == test) {
                last = netObjects.getRoot();
                test = random.nextInt();
            }

            last = last.getNext();
        }

        return test;
    }
}
