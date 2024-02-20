package jqsx.scripts;

import KanapkaEngine.Engine;
import KanapkaEngine.Game.Plugin;
import jqsx.net.NetworkClient;
import jqsx.net.NetworkServer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.Socket;

public class PlayerInput extends Plugin implements KeyListener, MouseListener {
    NetworkServer server = new NetworkServer();

    @Override
    public void Apply(Engine engine) {
        try {
            Socket socket = new Socket("localhost", 12894);
            NetworkClient client = new NetworkClient(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        server.sendToAllClients();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
