package jqsx.scripts.storage;

public enum Items {
    Stone(new Item("stone", "Stone", "Material/stone.png", 20)),
    StoneBlock(new Item("stoneblock", "Stone Block", "Material/icons/all/stoneblock_01a.png", 10)),
    Leaf(new Item("leaf", "Leaf", "Material/leaf.png", 50)),
    WorkBench(new Item("workbench", "WorkBench", "block.png", 2)),
    IronIngot(new Item("iron_ingot", "Iron", "Material/icons/all/ingot_01c.png", 15)),
    IronPickaxe(new Item("iron_pickaxe", "Iron Pickaxe", "Material/icons/all/staff_03e.png", 1,
            new Statistics(Statistics.create(Statistics.Type.MiningDamage, 10)))),
    Wood(new Item("logs", "Logs", "Material/wood.png", 50)),
    Debug(new Item("debug", "DEBUG_ADMIN_ITEM", "none.png", 999, new Statistics(
            Statistics.create(Statistics.Type.MiningDamage, 999),
            Statistics.create(Statistics.Type.AttackDamage, 999),
            Statistics.create(Statistics.Type.MiningSpeed, 999),
            Statistics.create(Statistics.Type.AttackSpeed, 999)))),
    Plank(new Item("plank", "Plank", "Material/icons/all/plank_01a.png", 30));
    public final Item item;
    Items(Item item) {
        this.item = item;
    }
}
