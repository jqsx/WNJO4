package jqsx.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class NetworkServer implements Runnable {
    private ServerSocket socket;
    private Thread serverThread;
    private final List<NetworkClient> clients = new ArrayList<>();
    public NetworkServer() {
        try {
            socket = new ServerSocket(12894);

            serverThread = new Thread(this);
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                Socket client = socket.accept();
                clients.add(new NetworkClient(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToAllClients() {
        ByteBuffer buffer = ByteBuffer.allocate(6);
        short s = 30;
        buffer.putShort(s);
        buffer.putShort(2, s);

        byte[] data = buffer.array();
        for (int i = 0; i < clients.size(); i++) {
            clients.get(i).send(data);
        }
    }
}
