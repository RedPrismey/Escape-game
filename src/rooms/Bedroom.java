package rooms;

import gameLogic.Action;
import gameLogic.Rectangle;
import gameLogic.Room;
import items.Cle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Bedroom extends Room {
  private boolean bookPickedUp = false;

  private static final Rectangle doorHitbox = new Rectangle(928, 151, 536, 600);
  private static final Rectangle bookHitbox = new Rectangle(672, 600, 180, 40);
  private static final Rectangle screenHitbox = new Rectangle(1491, 297, 253, 165);

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
    bookHitbox.draw(g);
    screenHitbox.draw(g);
  }

  @Override
  public List<Action> click(int x, int y, double scale) {
    // door logic
    if (doorHitbox.contains(x, y, scale)) {
      return List.of(new Action.ShowHotbarText("Skalala, nous sommes partis!"), new Action.ChangeRoom(1));
    }

    // book logic
    else if (bookHitbox.contains(x, y, scale)) {
      if (!bookPickedUp) {
        bookPickedUp = true;

        return List.of(
            new Action.ShowHotbarText("Vous avez ramassé le livre."),
            new Action.CollectItem(new Cle()));
      } else {
        return List.of(new Action.ShowHotbarText("Vous avez déjà ramassé le livre"));
      }
    }

    // screen logic
    else if (screenHitbox.contains(x, y, scale)) {
      return List.of(
          new Action.ShowHotbarText("Wow un truc avec la console"));
    }

    return List.of();
  }
}
