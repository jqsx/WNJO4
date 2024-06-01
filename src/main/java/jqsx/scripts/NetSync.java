package jqsx.scripts;

import KanapkaEngine.Components.Component;
import KanapkaEngine.Net.NetworkIdentity;

import java.util.ArrayList;
import java.util.List;

public class NetSync extends Component {

    public static List<NetSync> netObjects = new ArrayList<>();

    private NetworkIdentity identity;

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
            netObjects.add(this);
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
}
