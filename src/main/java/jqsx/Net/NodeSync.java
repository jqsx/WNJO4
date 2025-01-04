package jqsx.Net;

import KanapkaEngine.Net.NetworkConnectionToClient;
import KanapkaEngine.Net.NetworkIdentity;
import KanapkaEngine.Net.NetworkServer;
import KanapkaEngine.Net.Router.Route;
import jqsx.Game;
import jqsx.scripts.entities.player.ClientPlayer;
import jqsx.scripts.entities.player.ServerPlayer;
import jqsx.scripts.storage.ItemDrop;
import jqsx.scripts.storage.ItemStack;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;

public class NodeSync extends Route {
    @Override
    public void ServerClient_IN(NetworkConnectionToClient conn, byte[] data) {

    }

    @Override
    public void Client_IN(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        int type = buffer.getInt();

        clientRecieve(type, buffer.array());
    }

    public void create_server_player(NetworkConnectionToClient conn) {
        ServerPlayer player = new ServerPlayer(conn.getId());

        for (NetworkConnectionToClient connection : NetworkServer.clients)
            send_player(connection, player);

        send_auth(conn, player.getId());
    }

    public void send_player(NetworkConnectionToClient conn, ServerPlayer player) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(player.getId());
        sendClientData(conn, ClientOperation.create_client_player, buffer.array());
    }

    public void create_client_player(int nid) {
        ClientPlayer player = new ClientPlayer(nid);
    }

    public void send_auth(NetworkConnectionToClient conn, int nid) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(nid);
        sendClientData(conn, ClientOperation.receive_auth, buffer.array());
    }

    public void receive_auth(int nid) {
        NetSync.netObjects.foreach(nobj -> {
            if (nobj.getId() == nid)
                nobj.hasAuthority = true;
        });
    }

    public void create_server_itemdrop(String id, int amount, Vector2D at) {
        ItemDrop drop = new ItemStack(id, amount).createDrop(at, NetSync.getFreeID());

        for (NetworkConnectionToClient connection : NetworkServer.clients)
            send_client_itemdrop(connection, drop);
    }

    public void send_client_itemdrop(NetworkConnectionToClient conn, ItemDrop drop) {
        byte[] item_id = drop.getStored().getItem().id.getBytes();
        int size = Integer.BYTES + item_id.length + drop.getStored().getAmount() + Double.BYTES * 2;

        ByteBuffer buffer = ByteBuffer.allocate(size);

        buffer.putInt(drop.getComponent(NetworkIdentity.class).getNetID());
        buffer.put(item_id);
        buffer.putInt(drop.getStored().getAmount());
        Vector2D at = drop.getParent().transform.getPosition();
        buffer.putDouble(at.getX());
        buffer.putDouble(at.getY());

        sendClientData(conn, ClientOperation.create_client_itemdrop, buffer.array());
    }

    public void create_client_itemdrop(int nid, String id, int amount, Vector2D at) {
        new ItemStack(id, amount).createDrop(at, nid);
    }

    public void destroy_server_player(int id) {
        NetSync.netObjects.foreach(netSync -> {
            if (netSync.getId() == id)
                netSync.getParent().Destroy();
        });

        for (NetworkConnectionToClient connection : NetworkServer.clients)
            send_destroy_player(connection, id);
    }

    public void send_destroy_player(NetworkConnectionToClient conn, int id) {
        sendClientData(conn, ClientOperation.destroy_client_player, wrapIntToByteArray(id));
    }

    public void destroy_client_player(int id) {
        NetSync.netObjects.foreach(netSync -> {
            if (netSync.getId() == id)
                netSync.getParent().Destroy();
        });
    }

    public void send_destroy_itemdrop_client(NetworkConnectionToClient conn, int id) {
        sendClientData(conn, ClientOperation.destroy_client_itemdrop, wrapIntToByteArray(id));
    }

    public void destroy_client_itemdrop(int id) {
        NetSync.netObjects.foreach(netSync -> {
            if (netSync.getId() == id)
                netSync.getParent().Destroy();
        });
    }

    public void destroy_server_itemdrop(int id) {
        NetSync.netObjects.foreach(netSync -> {
            if (netSync.getId() == id)
                netSync.getParent().Destroy();
        });

        for (NetworkConnectionToClient connection : NetworkServer.clients)
            send_destroy_itemdrop_client(connection, id);
    }

    private void clientRecieve(int type, byte[] data) {
        ClientOperation.values()[type].accept(this, data);
    }

    private void sendClientData(NetworkConnectionToClient conn, ClientOperation operation, byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + data.length);
        buffer.putInt(operation.ordinal());
        buffer.put(data);
        sendToClient(conn, buffer.array());
    }

    public enum ClientOperation {
        create_client_player((sync, buffer) -> {
            sync.create_client_player(buffer.getInt());
        }), receive_auth((sync, buffer) -> {
            sync.receive_auth(buffer.getInt());
        }), create_client_itemdrop((sync, buffer) -> {
            int nid = buffer.getInt();

            int str_len = buffer.getInt();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < str_len; i++) {
                builder.append(buffer.getChar());
            }
            int amount = buffer.getInt();
            double x = buffer.getDouble();
            double y = buffer.getDouble();
            sync.create_client_itemdrop(nid, builder.toString(), amount, new Vector2D(x, y));
        }), destroy_client_player((sync, buffer) -> {
            sync.destroy_client_player(buffer.getInt());
        }),destroy_client_itemdrop((sync, buffer) -> {
            sync.destroy_client_itemdrop(buffer.getInt());
        });

        final BiConsumer<NodeSync, ByteBuffer> runnable;

        ClientOperation(BiConsumer<NodeSync, ByteBuffer> runnable) {
            this.runnable = runnable;
        }

        public void accept(NodeSync sync, byte[] data) {
            runnable.accept(sync, ByteBuffer.wrap(data));
        }
    }

    private ByteBuffer wrapIntToBuffer(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(i);

        return buffer;
    }

    private byte[] wrapIntToByteArray(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(i);

        return buffer.array();
    }

    private void sendToAll(ClientOperation operation, byte[] data) {
        for (NetworkConnectionToClient connection : NetworkServer.clients)
            sendClientData(connection, operation, data);
    }

    @Override
    public void onServerClientConnect(NetworkConnectionToClient conn) {
        super.onServerClientConnect(conn);

        create_server_player(conn);
    }

    @Override
    public void onServerClientDisconnect(NetworkConnectionToClient conn) {
        super.onServerClientDisconnect(conn);

        destroy_server_player(conn.getId());
    }

    @Override
    public void onClientDisconnect() {
        super.onClientDisconnect();

        Game.startLimbo();
    }
}
