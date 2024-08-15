package jqsx.scripts.entities.player;

import KanapkaEngine.Components.Block;
import jqsx.scripts.entities.Entity;

public interface Communicator {
    void damage(Block block);
    void damage(Entity other);
    void onBlockDamage(Block block);
    void onTakeDamage(float amount);
}
