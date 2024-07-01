package jqsx.scripts.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Statistics {
    private final List<Stat> stats = new ArrayList<>();
    public Statistics(Stat... stats) {
        for (Stat stat : stats)
            addStat(stat);
    }

    public void addStat(Type type, float value) {
        for (Stat stat : stats) {
            if (stat.type == type) {
                stat.value += value;
                return;
            }
        }

        stats.add(new Stat(type, value));
    }

    public void addStat(Stat stat) {
        addStat(stat.type, stat.value);
    }

    public static Stat create(Type type, float v) {
        return new Stat(type, v);
    }

    public final Stat[] getStats() {
        Stat[] arr = new Stat[stats.size()];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = stats.get(i);
        }

        return arr;
    }

    public void setStat(Type type, float value) {
        if (value == 0f) {
            removeStat(type);
            return;
        }

        for (Stat stat : stats) {
            if (stat.type == type) {
                stat.value = value;
                return;
            }
        }
    }

    public void removeStat(Type type) {
        stats.removeIf(stat -> stat.type == type);
    }

    public enum Type {
        AttackDamage, AttackSpeed, MiningDamage, MiningSpeed
    }

    public final float get(Type type) {
        for (Stat stat : stats) {
            if (stat.type == type)
                return stat.value;
        }
        return 0;
    }

    public static class Stat {
        private final Type type;
        private float value;

        private Stat(Type type, float value) {
            this.type = type;
            this.value = value;
        }

        public final float getValue() {
            return value;
        }

        public final Type getType() {
            return type;
        }
    }
}
