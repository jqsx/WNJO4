package jqsx.scripts;

import KanapkaEngine.Components.RenderLayer;
import KanapkaEngine.Components.RenderStage;
import KanapkaEngine.Game.Plugin;
import KanapkaEngine.Game.SceneManager;
import KanapkaEngine.Game.Window;
import jqsx.Game;
import jqsx.scripts.entities.Player;
import jqsx.scripts.storage.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class InventoryDisplay implements RenderLayer {
    private int most_last = 0;
    private int most_last_craft = 0;
    @Override
    public void Render(Graphics2D g) {
        if (!PlayerInput.isInventoryEnabled) return;
        if (Player.localPlayer != null) {
            Player player = Player.localPlayer;
            ItemStack selectedItem = null;
            Dimension screen = Window.getWindowSize();
            Inventory inventory = Player.localPlayer.getInventory();
            g.setFont(Game.global_font.deriveFont(screen.width / 40f));
            int most = 0;
            for (int i = 0; i < (PlayerInput.isIsInvOpen() ? inventory.getSize() : 5); i++) {
                int white = 0xffffff;
                int black = 0x000000;
                ItemStack itemStack = inventory.getItem(i);
                if (i == PlayerInput.getHighlightedSlot()) {
                    black = 0x777700;
                    selectedItem = itemStack;
                }
                else if (i == PlayerInput.getSelectedSlot()) {
                    black = 0x555555;
                    selectedItem = itemStack;
                }

                int column = (int)Math.floor(i / 5.0);
                int row = i % 5;
                Rectangle2D bounds = drawText(g,
                        "- " + (itemStack != null ? itemStack.getItem().itemName + " x" + itemStack.getAmount() : ""),
                        white, black,
                        new Point(-1, 0),
                        new Point(10 + most_last * column, (g.getFont().getSize() + 2) * row));

                if (bounds.getWidth() > most)
                    most = (int) bounds.getWidth();
            }

            most_last = most;

            if (PlayerInput.isCraftOpen() && PlayerInput.isIsInvOpen()) {
                int i = 0;
                int most_craft = 0;

                if (PlayerInput.getCraftRemainingDelay() > 0) {
                    StringBuilder builder = new StringBuilder();

                    for (int j = 0; j < 10; j++) {
                        if (j < Math.floor((PlayerInput.getCraftRemainingDelay() / PlayerInput.getCraftDelay()) * 10.0))
                            builder.append("#");
                        else
                            builder.append(" ");
                    }
                    drawText(g,
                            "< Crafting... >", 0xffffff, 0x000000,
                            new Point(-1, 0),
                            new Point(10, -8 * (g.getFont().getSize() + 2)));
                    drawText(g,
                            "< " + builder.toString() + " >", 0xffffff, 0x000000,
                            new Point(-1, 0),
                            new Point(10, -7 * (g.getFont().getSize() + 2)));
                    return;
                }

                for (Craft craft : Crafts.getAll()) {
                    int white = 0xffffff;
                    int black = 0x000000;
                    ItemStack itemStack = craft.result;
                    boolean selected = i == PlayerInput.getSelectedCraft();

                    if (selected)
                        black = 0x555555;

                    int index = i - PlayerInput.getSelectedCraft();

                    if (index >= -2 && index < 5) {
                        Rectangle2D bounds = drawText(g,
                                "> " + itemStack.getItem().itemName + " x" + itemStack.getAmount(), white, black,
                                new Point(-1, 0),
                                new Point(10, (g.getFont().getSize() + 2) * index - 7 * (g.getFont().getSize() + 2)));


                        if (index >= 0) {
                            if (bounds.getWidth() > most_craft)
                                most_craft = (int) bounds.getWidth();
                        }
                        if (selected) {
                            selectedItem = craft.result;
                            int font_size = (g.getFont().getSize() + 2);
                             drawText(g,
                                    "< Craft >", white, 0x333333,
                                    new Point(-1, -1),
                                    new Point((int) (bounds.getX() + most_last_craft + 10), (int) bounds.getY()));
                            drawText(g,
                                    "Ingredients:", white, 0x222222,
                                    new Point(-1, -1),
                                    new Point(
                                            (int) (bounds.getX() + most_last_craft + 10),
                                            (int) bounds.getY() + font_size));
                            int ing_i = 0;
                            for (ItemStack ing : craft.ingredients) {
                                drawText(g,
                                        "* " + ing.getItem().itemName + " x" + ing.getAmount(), white, 0x000000,
                                        new Point(-1, -1),
                                        new Point(
                                                (int) (bounds.getX() + most_last_craft + 10),
                                                (int) bounds.getY() + font_size * 2 + font_size * ing_i));
                                ing_i++;
                            }
                        }
                    }
                    i++;
                }
                most_last_craft = most_craft;
            }

            if (PlayerInput.isIsInvOpen()) {
                if (selectedItem != null) {
                    int single = (g.getFont().getSize() + 2);
                    int offset = single * 6;
                    drawText(g,
                            "- " + selectedItem.getItem().itemName,
                            0xffffff, 0x000000,
                            new Point(-1, 0),
                            new Point(10, offset));
                    if (selectedItem.getItem().statistics != null) {
                        Statistics.Stat[] stats = selectedItem.getItem().statistics.getStats();

                        int x = 1;
                        for (Statistics.Stat stat : stats) {
                            drawText(g,
                                    "- > " + stat.getType().name() + " : " + stat.getValue(),
                                    0xffffff, 0x000000,
                                    new Point(-1, 0),
                                    new Point(10, offset + single * x));
                            x++;
                        }
                    }
                }
            }

            if (player.getRemainingMiningDelay() > 0) {
                g.setFont(g.getFont().deriveFont(10f));
                StringBuilder builder = new StringBuilder();

                for (int j = 0; j < 10; j++) {
                    if (j < Math.floor((player.getRemainingMiningDelay() / player.getMiningDelay()) * 10.0))
                        builder.append("#");
                    else
                        builder.append(" ");
                }

                drawText(g, "> " + builder, 0xffffff, 0x000000, new Point(-1, 1), new Point(0, -100));
            }
        }
    }

    public Rectangle2D drawText(Graphics2D g, String text, int color, int color2, Point pivot, Point offset) {
        Dimension screen = Window.getWindowSize();
        Point move = new Point(screen.width + pivot.x * screen.width + offset.x, screen.height + pivot.y * screen.height + offset.y);

        Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);

        bounds.setRect(move.x - (bounds.getWidth() / 2.0) * (1 + pivot.x), move.y - (bounds.getHeight() / 2.0) * (1 + pivot.y), bounds.getWidth(), bounds.getHeight());

        Color c = new Color(color);
        Color darker = new Color(color2);

        g.setColor(darker);
        g.fillRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
        g.setColor(darker.brighter());
        g.setStroke(new BasicStroke(3f));
        g.drawRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
        g.setColor(c);
        g.drawString(text, (int) bounds.getX(), (int) (bounds.getY() + g.getFont().getSize()));

        return bounds;
    }

    @Override
    public RenderStage getStage() {
        return RenderStage.UI;
    }
}
