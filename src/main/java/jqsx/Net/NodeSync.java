package jqsx.Net;

import KanapkaEngine.Net.NetworkConnectionToClient;
import KanapkaEngine.Net.Router.Route;
import jqsx.scripts.entities.player.ClientPlayer;
import jqsx.scripts.entities.player.ServerPlayer;
import jqsx.scripts.storage.ItemDrop;

public class NodeSync extends Route {
    @Override
    public void ServerClient_IN(NetworkConnectionToClient conn, byte[] data) {

    }

    @Override
    public void Client_IN(byte[] data) {

    }

    public void create_server_player(NetworkConnectionToClient conn) {
        ServerPlayer player = new ServerPlayer(conn.getId());


    }

    public void create_client_player(int nid, int id) {
        ClientPlayer player = new ClientPlayer(nid);
    }

    public void create_server_itemdrop(String id, int amount) {

    }

    public void create_client_itemdrop(int nid, String id, int amount) {

    }

    public void destroy_server_player(int id) {

    }

    public void destroy_client_player(int id) {

    }

    public void destroy_server_itemdrop(int id) {

    }

    public void destroy_client_itemdrop(int id) {

    }

    public void send_create_player_client(NetworkConnectionToClient conn, ServerPlayer player) {

    }

    public void send_destroy_player_client(NetworkConnectionToClient conn, ServerPlayer player) {

    }

    public void send_destroy_itemdrop_client(NetworkConnectionToClient conn, ItemDrop itemDrop) {

    }
}
