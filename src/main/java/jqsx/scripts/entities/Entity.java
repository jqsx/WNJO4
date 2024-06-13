package jqsx.scripts.entities;

import KanapkaEngine.Components.Node;

public class Entity extends Node {
    private double health = 1;

    private double maxHealth = 1;

    public final void setHealth(double a) {
        double diff = a - health;

        if (diff > 0) {
            if (onHeal(diff)) {
                return;
            }
        }
        else {
            if (onDamage(diff)) return;
        }

        // TODO
    }

    public final void damage(double a) {
        if (onDamage(a)) return;

        // TODO
    }

    public boolean onHeal(double diff) {
        return false;
    }

    public boolean onDamage(double damage) {
        return false;
    }

    public final double getHealth() {
        return health;
    }

    public final void setMaxHealth(double a) {
        maxHealth = a;
    }

    public final double getMaxHealth() {
        return maxHealth;
    }
}
