package jqsx.scripts.storage;

import KanapkaEngine.Components.Component;

public class Container extends Component {

    private final Inventory inventory;

    public Inventory getInventory() {
        return inventory;
    }

    public Container(Inventory inventory) {
        this.inventory = inventory;
    }

}
