package jqsx.scripts.entities.player;

import KanapkaEngine.Components.Block;
import jqsx.scripts.entities.Entity;

public class ClientPlayer extends Player implements Communicator {
    public ClientPlayer(int id) {
        super(id);
    }

    @Override
    public void damage(Block block) {

    }

    @Override
    public void damage(Entity other) {

    }

    @Override
    public void onBlockDamage(Block block) {

    }

    @Override
    public void onTakeDamage(float amount) {

    }
}
