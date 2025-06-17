package rooms;

import gameLogic.Action;
import gameLogic.Rectangle;
import gameLogic.Room;
import items.Cle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bedroom extends Room {
    private static final Rectangle doorHitbox = new Rectangle(928, 151, 536, 600);

    public Bedroom(String name, int id) {
        super(name, id);
        try {
            BufferedImage s = ImageIO.read(new File("./rooms/assets/bedroom.png"));
            this.setBackground(s);
        } catch (IOException e) {
            System.err.println("Error while loading room background : " + e.getMessage());
        }
    }

    @Override
    public void draw(java.awt.Graphics2D g, int width, int height) {
        super.draw(g, width, height);

        doorHitbox.draw(g);
    }

    @Override
    public Action click(int x, int y, double scale) {
        if (doorHitbox.contains(x, y, scale)) {
            System.out.println("Skalala, nous sommes partis!");
        }
        return null;
    }
}