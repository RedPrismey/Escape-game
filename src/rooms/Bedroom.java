package rooms;

import gameLogic.Action;
import gameLogic.Rectangle;
import gameLogic.Room;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;

public class Bedroom extends Room {
  private boolean minesweeperSolved = false;
  private boolean passwordValidated = false;

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
  }

  @Override
  public List<Action> click(double x, double y) {
    // door logic
    if (doorHitbox.contains(x, y)) {
      if (minesweeperSolved) {
        return List.of(new Action.ShowHotbarText("Skalala, nous sommes partis!"), new Action.ChangeRoom("Main Room"));
      } else if (!passwordValidated) {
        return List.of(new Action.ShowHotbarText("La porte est verouillée"));
      } else {
        return List.of(new Action.ShowHotbarText(
            "Bizarre, la porte est encore fermée.\nJe devrais peut-être revérifier la console"));
      }
    }

    // book logic
    else if (bookHitbox.contains(x, y)) {
      return List.of(
          new Action.ShowHotbarText("Vous lisez le livre"),
          new Action.ChangeRoom("Book Room"));

    }

    // screen logic
    else if (screenHitbox.contains(x, y)) {
      if (!passwordValidated) {
        return List.of(new Action.ChangeRoom("Password Room"), new Action.ShowHotbarText("Tiens, une console"));
      } else {
        return List.of(
            new Action.ShowHotbarText("? Pourquoi est-ce qu'il y a un démineur ?"), new Action.ChangeRoom("Mine Room"));
      }
    }

    return List.of();
  }

  @Override
  public void handleAction(Action action) {
    if (action instanceof Action.MinesweeperWon) {
      this.minesweeperSolved = true;
    } else if (action instanceof Action.PasswordValidated) {
      this.passwordValidated = true;
    }
  }
}
