package jqsx.scripts;

import KanapkaEngine.Components.Renderer;
import jqsx.scripts.storage.ItemDrop;

import java.awt.image.BufferedImage;

public class ItemDropRenderer extends Renderer {
    @Override
    public BufferedImage getRender() {
        if (getParent() instanceof ItemDrop drop) {
            return drop.getStored().getItem().icon;
        }
        else return super.getRender();
    }
}
