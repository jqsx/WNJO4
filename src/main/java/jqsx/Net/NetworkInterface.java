package jqsx.Net;

import KanapkaEngine.Components.Mathf;
import KanapkaEngine.Components.ResourceLoader;
import KanapkaEngine.Engine;
import KanapkaEngine.Game.Plugin;
import KanapkaEngine.Net.NetworkClient;
import KanapkaEngine.Net.NetworkServer;
import KanapkaEngine.UI.Image;
import KanapkaEngine.UI.Text;
import KanapkaEngine.UI.UI;
import KanapkaEngine.UI.UIComponent;
import jqsx.Game;
import jqsx.scripts.entities.Entity;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class NetworkInterface extends Plugin implements KeyListener {
    public static boolean isEnabled = true;

    public static int size = 30;

    private int selected = 0;
    private Container[] cycle = new Container[0];

    private Engine engine;

    private Container getMain() {
        if (cycle.length == 0)
            return null;
        return cycle[selected];
    }

    @Override
    public void Apply(Engine engine) {
        this.engine = engine;
        createMainMenu();
    }

    private void createMainMenu() {
        cycle = new Container[1];

        Container mainMenu = new Container();

        cycle[0] = mainMenu;

        mainMenu.text = "< Main Menu >";

        mainMenu.options.add(new Option("List Servers"));
        mainMenu.options.add(new Option("Host", this::createHostMenu));
        mainMenu.options.add(new Option("Quit", engine::End));

        recreateVisual();
    }

    private void createHostMenu() {
        cycle = new Container[1];

        Container mainMenu = new Container();

        cycle[0] = mainMenu;

        mainMenu.text = "< Host >";

        mainMenu.options.add(new Option("Host", this::host));
        mainMenu.options.add(new Option("Connect", this::connect));
        mainMenu.options.add(new Option("Back", this::createMainMenu));

        recreateVisual();
    }

    private void host() {
        NetworkServer.StartServer();
        Game.startGame();
    }

    private void connect() {
        NetworkClient.Connect("localhost");
        Game.startGame();
    }

    @Override
    public void Update() {

    }

    private static int keyIn(int selected, int key) {
        if (key == 83)
            selected += 1;
        else if (key == 87)
            selected -= 1;
        return selected;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        int max = cycle.length - 1;
        int temp = selected;
        if (key == 68)
            selected = Mathf.Clamp(selected + 1, 0, max);
        else if (key == 65)
            selected = Mathf.Clamp(selected - 1, 0, max);

        if (temp != selected) {
            recreateVisual();
        }

        Container main = getMain();
        if (main != null) {
            main.keyUpdate(key);

            if (key == 10) {
                Option m = main.get();

                if (m != null)
                    m.action.run();
            }
        }
    }

    private void recreateVisual() {
        Container container = getMain();

        if (container != null)
            container.createUI();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static class Container {
        public String text = "< Title >";
        public int selected = 0;

        private Text element;

        public List<Option> options = new ArrayList<>();

        public void keyUpdate(int key) {
            selected = Mathf.Clamp(keyIn(selected, key), 0, options.size() - 1);

            updateVisual();
        }

        public Option get() {
            return options.get(selected);
        }

        public boolean isSelected(Option option) {
            int i = 0;
            for (Option o : options) {
                if (o == option && i == selected) {
                    return true;
                }
                i++;
            }
            return false;
        }

        public void updateVisual() {
            for (Option option : options) {
                Text t = option.element;

                if (t != null) {
                    boolean s = isSelected(option);
                    t.setColor(s ? Color.WHITE.getRGB() : Color.GRAY.getRGB());
                    t.setText((s ? "> " : "") + option.text);
                }
            }
        }

        public void createUI() {
            UI.currentlyDisplayed = new UI();

            {
                Image image = new Image();

                image.setImage(ResourceLoader.loadResource("logo.png"));

                image.size = new Vector2D(500, 500);

                image.pivot = UIComponent.Pivot.Center;
            }

            int len = options.size();

            Text title = element = new Text();

            title.setText(text);

            title.setSize(size);

            title.setFont(Game.global_font);

            title.position = new Vector2D(0, -(len / 2.0) * size * 2.0);

            for (int i = 0; i < len; i++) {
                Option option = options.get(i);

                Text t = option.element = new Text();

                t.setText(option.text);

                t.setFont(Game.global_font);

                t.setColor(isSelected(option) ? Color.WHITE.getRGB() : Color.GRAY.getRGB());

                t.setSize(size);

                t.position = new Vector2D(0, -((len - 2) / 2.0) * size * 2.0 + i * size * 2.0);
            }
        }
    }

    public static class Option {
        public String text = "< Empty >";

        public Runnable action = () -> {};

        private Text element;

        public Option() {

        }

        public Option(String text) {
            this.text = text;
        }

        public Option(Runnable runnable) {
            this.action = runnable;
        }

        public Option(String text, Runnable runnable) {
            this.text = text;
            this.action = runnable;
        }

        public Option(Runnable runnable, String text) {
            this(text, runnable);
        }
    }
}
