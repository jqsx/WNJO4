package jqsx.net;

import java.io.*;
import java.net.Socket;

public class NetworkClient implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private Thread thread;

    public NetworkClient(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Failed connection with client " + socket.getLocalAddress().getHostAddress());
            e.printStackTrace();
        }

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                short ID = in.readShort();
                byte[] data = new byte[4096];
                int length = in.read(data);
                System.out.println("Read this much: " + length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] data) {
        try {
            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
