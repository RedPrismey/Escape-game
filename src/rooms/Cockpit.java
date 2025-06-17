package rooms;

import gameLogic.Action;
import gameLogic.Rectangle;
import gameLogic.Room;
import items.Cle;

import java.util.ArrayList;

public class Cockpit extends Room {
    private Rectangle keyHitbox = new Rectangle(0, 0, 50, 50); // Hitbox for the key

    public Cockpit(String name, int id) {
        super(name, id);
    }

    public Cockpit(String name, int id, ArrayList<Room> linkedTo) {
        super(name, id, linkedTo);
    }

    @Override
    public void draw(java.awt.Graphics2D g, int width, int height) {
        super.draw(g, width, height);
        keyHitbox.draw(g);
    }

    /**
     * Returns the action to perform when the player clicks on a specific point in the room.
     */
    @Override
    public Action click(int x, int y, double scale) {
        if (keyHitbox.contains(x, y, scale)) {
            return new Action.CollectItem(new Cle());
        }
        return null;
    }
}