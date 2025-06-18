package rooms;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import gameLogic.Action;
import gameLogic.Room;

public class MainRoom extends Room {

  public MainRoom(String name, int id) {
    super(name, id);

    try {
      BufferedImage s = ImageIO.read(new File("./rooms/assets/machine_room.png"));
      this.setBackground(s);
    } catch (IOException e) {
      System.err.println("Error while loading room background : " + e.getMessage());
    }
  }

  public MainRoom(String name, int id, List<Room> linkedTo) {
    super(name, id, linkedTo);

    try {
      BufferedImage s = ImageIO.read(new File("./rooms/assets/machine_room.png"));
      this.setBackground(s);
    } catch (IOException e) {
      System.err.println("Error while loading room background : " + e.getMessage());
    }
  }

  @Override
  public void draw(Graphics2D g, int width, int height) {
    super.draw(g, width, height);
  }

  @Override
  public List<Action> click(double x, double y) {
    return null;
  }

}
