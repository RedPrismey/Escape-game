package rooms;

import gameLogic.Room;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Outside extends Room {
    public Outside(String name, int id, List<Room> linkedTo) {
        super(name, id, linkedTo);

        try {
            BufferedImage s = ImageIO.read(new File("./rooms/assets/outside.png"));
            this.setBackground(s);
        } catch (IOException e) {
            System.err.println("Error while loading room background : " + e.getMessage());
        }
    }
}
