package jqsx.scripts;

public class Player extends Entity {
    private int id;
    private boolean local = false;

    public int getId() {
        return id;
    }

    public boolean isLocalPlayer() {
        return local;
    }
}
