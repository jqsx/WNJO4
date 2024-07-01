package jqsx.scripts;

import KanapkaEngine.Components.*;
import KanapkaEngine.Engine;
import KanapkaEngine.Game.Input;
import KanapkaEngine.Game.Plugin;
import KanapkaEngine.Game.SceneManager;
import KanapkaEngine.Game.Scheduler;
import KanapkaEngine.Time;
import jqsx.scripts.entities.Player;
import jqsx.scripts.storage.Craft;
import jqsx.scripts.storage.Crafts;
import jqsx.scripts.storage.ItemStack;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;

public class PlayerInput extends Plugin implements KeyListener, MouseListener, MouseWheelListener {

    private AudioClip click;

    public static boolean isInventoryEnabled = true;

    public static boolean isCraftOpen() {
        return isCraftOpen;
    }

    @Override
    public void Apply(Engine engine) {
        click = ResourceLoader.loadAudio("sound/click.wav");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'z') {
            SceneManager.getCurrentlyLoaded().scene_world.setWorldName("Procedural");
            SceneManager.getCurrentlyLoaded().scene_world.saveWorld();
        }

        updateInput(e.getKeyCode());
    }

    private static int selectedSlot = 0;
    private static int selectedCraft = 0;

    public static int getSelectedCraft() {
        return selectedCraft;
    }
    private static boolean isInvOpen = false;
    private static boolean isCraftOpen = false;

    private static int highlightedSlot = -1;

    public static int getHighlightedSlot() {
        return highlightedSlot;
    }

    public static boolean isIsInvOpen() {
        return isInvOpen;
    }

    public static int getSelectedSlot() {
        return selectedSlot;
    }

    public static boolean canMove() {
        return !isIsInvOpen();
    }

    private static double lastCraft = Time.time();
    private static double craftdelay = 0;

    public static double getCraftRemainingDelay() {
        return Math.max(lastCraft - Time.time(), 0);
    }

    public static double getCraftDelay() {
        return craftdelay;
    }

    public static ItemStack getSelectedItem() {
        if (Player.localPlayer == null) return null;
        Player player = Player.localPlayer;

        return player.getInventory().getItem(selectedSlot);
    }

    private void updateInput(int key) {
        if (!isInventoryEnabled) return;
        if (Player.localPlayer == null) return;
        Player player = Player.localPlayer;
        int max = isInvOpen ? player.getInventory().getSize() - 1 : 4;

        if (getCraftRemainingDelay() > 0) return;

        if (!canMove()) {
            if (!isCraftOpen()) {
                if (key == 83)
                    selectedSlot = Mathf.Clamp(selectedSlot + 1, 0, max);
                else if (key == 87)
                    selectedSlot = Mathf.Clamp(selectedSlot - 1, 0, max);
                else if (key == 68)
                    selectedSlot = Mathf.Clamp(selectedSlot + 5, 0, max);
                else if (key == 65)
                    selectedSlot = Mathf.Clamp(selectedSlot - 5, 0, max);
            }
            else {
                max = Crafts.getCraftCount() - 1;
                if (key == 83)
                    selectedCraft = Mathf.Clamp(selectedCraft + 1, 0, max);
                else if (key == 87)
                    selectedCraft = Mathf.Clamp(selectedCraft - 1, 0, max);
                else if (key == 68)
                    selectedCraft = Mathf.Clamp(selectedCraft + 5, 0, max);
                else if (key == 65)
                    selectedCraft = Mathf.Clamp(selectedCraft - 5, 0, max);
            }
        }

        if (key == 73) {
            isInvOpen = !isInvOpen;
            if (!isInvOpen) {
                selectedSlot = Mathf.Clamp(selectedSlot, 0, 4);
                isCraftOpen = false;
            }
        }

        if (isInvOpen) {
            if (key == 8) {
                player.drop(selectedSlot, false);
            }
            else if (key == 127)
                player.drop(selectedSlot, true);
        }

        if (key > 47 && key < 58) {
            if (key == 48)
                selectedSlot = Mathf.Clamp(9, 0, max);
            else {
                int index = key - 49;

                selectedSlot = Mathf.Clamp(index, 0, max);
            }
        }

        if (key == 67 && isInvOpen) {
            isCraftOpen = !isCraftOpen;
        }

        if (key == 10) {
            if (isCraftOpen()) {
                int i = 0;
                for (Craft craft : Crafts.getAll()) {
                    if (selectedCraft == i) {
                        if (craft.canCraft(player.getInventory())) {
                            click.start();

                            Scheduler.delay(() -> {
                                craft.craft(player.getInventory());
                            }, craft.delay);

                            lastCraft = Time.time() + craft.delay;
                            craftdelay = craft.delay;
                        }
                        return;
                    }
                    i++;
                }
            }
            else {
                if (highlightedSlot == selectedSlot) {
                    highlightedSlot = -1;
                    return;
                }
                if (highlightedSlot != -1) {
                    ItemStack one = player.getInventory().getItem(highlightedSlot);
                    ItemStack two = player.getInventory().getItem(selectedSlot);
                    if (one == null ||
                            two == null || (!one.getId().equals(two.getId()))) {

                        ItemStack temp = player.getInventory().getItem(highlightedSlot);
                        player.getInventory().setItem(highlightedSlot, player.getInventory().getItem(selectedSlot));
                        player.getInventory().setItem(selectedSlot, temp);
                    }
                    else {
                        int total = two.getAmount() + one.getAmount();

                        if (total > two.getItem().maxStack) {
                            two.setAmount(two.getItem().maxStack);
                            int remainder = total - two.getItem().maxStack;
                            one.setAmount(remainder);
                            if (remainder <= 0) {
                                player.getInventory().setItem(highlightedSlot, null);
                            }
                        }
                        else {
                            two.setAmount(total);

                            player.getInventory().setItem(highlightedSlot, null);
                        }
                    }
                    click.start();
                    highlightedSlot = -1;
                }
                else highlightedSlot = selectedSlot;
            }
        }
    }

    private double scrollDelay = Time.time();

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (Player.localPlayer == null) return;
        Player player = Player.localPlayer;
        int max = isInvOpen ? player.getInventory().getSize() - 1 : 4;
        if (scrollDelay < Time.time()) {
            selectedSlot = Mathf.Clamp(selectedSlot + (int)Math.signum(e.getWheelRotation()), 0, max);

            scrollDelay = Time.time() + 0.05;
        }
    }
}
