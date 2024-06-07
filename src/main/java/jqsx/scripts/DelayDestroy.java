package jqsx.scripts;

import KanapkaEngine.Components.Node;
import KanapkaEngine.Game.Plugin;
import KanapkaEngine.Time;

import java.util.ArrayList;
import java.util.List;

public class DelayDestroy extends Plugin {
    private static final List<Data> toDestroy = new ArrayList<>();

    public static void destroy(Node node, double time) {
        double endTime = Time.time() + time;

        toDestroy.add(new Data(node, endTime));
    }

    private record Data(Node node, double endTime) {

    }

    @Override
    public void Update() {
        toDestroy.removeIf(data -> {
            if (data.endTime < Time.time()) {
                data.node.Destroy();
                return true;
            }
            return false;
        });
    }
}
