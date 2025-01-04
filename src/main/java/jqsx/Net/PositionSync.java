package jqsx.Net;

import KanapkaEngine.Components.Node;
import KanapkaEngine.Net.NetworkConnectionToClient;
import KanapkaEngine.Net.NetworkOperation;
import KanapkaEngine.Net.NetworkServer;
import KanapkaEngine.Net.Router.Route;
import jqsx.scripts.entities.player.Player;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.*;
import java.nio.ByteBuffer;

public class PositionSync extends Route {
    @Override
    public void ServerClient_IN(NetworkConnectionToClient conn, byte[] data) {
        super.ServerClient_IN(conn, data);

        NetSync.netObjects.foreach(nobj -> {
            if (nobj.getId() == conn.getId()) {
                new NetworkOperation(() -> {
                    nobj.netPosition = getPosition(data);
                });
            }
        });
    }

    private Vector2D getPosition(byte[] data) {
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));

        try {
            double x = stream.readDouble();
            double y = stream.readDouble();

            return new Vector2D(x, y);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void Client_IN(byte[] data) {
        super.Client_IN(data);

        ByteBuffer buffer = ByteBuffer.wrap(data);

        int nid = buffer.getInt();
        double x = buffer.getDouble();
        double y = buffer.getDouble();

        NetSync.netObjects.foreach(nobj -> {
            if (nobj.getId() == nid) {
                new NetworkOperation(() -> {
                    nobj.netPosition = getPosition(data);
                });
            }
        });
    }

    public void syncLocal() {
        if (Player.localPlayer != null)
            sendToServer(getPosition(Player.localPlayer));
    }

    private byte[] getPosition(Node node) {
        /**
         * double, double
         */
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putDouble(node.transform.getPosition().getX());
        buffer.putDouble(node.transform.getPosition().getY());

        return buffer.array();
    }

    public void syncAllPlayers() {
        int size = NetSync.netObjects.getSize();
        ByteBuffer buffer = ByteBuffer.allocate(4 + 20 * size);

        buffer.putInt(size);
        NetSync.netObjects.foreach(nobj -> {
            buffer.put(s_getPosForConn(nobj.getId(), nobj.getParent()));
        });

        byte[] array = buffer.array();

        NetworkServer.clients.forEach((conn) -> {
            sendToClient(conn, array);
        });
    }

    private byte[] s_getPosForConn(int conn, Node node) {
        /**
         * int - 4
         * pos - 16
         *
         */

        ByteBuffer buffer = ByteBuffer.allocate(20);

        buffer.putInt(conn);
        buffer.put(getPosition(node));

        return buffer.array();
    }
}
