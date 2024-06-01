package jqsx.Net;

import KanapkaEngine.Components.Node;
import KanapkaEngine.Net.NetworkConnectionToClient;
import KanapkaEngine.Net.NetworkIdentity;
import KanapkaEngine.Net.NetworkServer;
import KanapkaEngine.Net.Router.Route;
import jqsx.scripts.NetSync;
import jqsx.scripts.Player;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.io.*;
import java.nio.ByteBuffer;

public class PositionSync extends Route {
    @Override
    public void ServerClient_IN(NetworkConnectionToClient conn, byte[] data) {
        super.ServerClient_IN(conn, data);

        for (int i = 0; i < NetSync.netObjects.size(); i++) {
            NetSync player = NetSync.netObjects.get(i);

            if (player.getId() == conn.getId())
                player.getParent().transform.setPosition(getPosition(data));
        }
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
        ByteBuffer buffer = ByteBuffer.allocate(4 + 20 * Player.players.size());

        buffer.putInt(NetSync.netObjects.size());
        for (int i = 0; i < NetSync.netObjects.size(); i++) {
            NetSync player = NetSync.netObjects.get(i);
            buffer.put(s_getPosForConn(player.getId(), player.getParent()));
        }

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
