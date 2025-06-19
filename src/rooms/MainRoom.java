package rooms;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import gameLogic.Action;
import gameLogic.Rectangle;
import gameLogic.Room;
import items.Cle;

public class MainRoom extends Room {
  private static final Rectangle keyHitbox = new Rectangle(1400, 270, 163, 65);
  private static final Rectangle chestHitbox = new Rectangle(0, 625, 350, 235);
  private static final Rectangle doorHitbox = new Rectangle(620, 151, 600, 600);
  private static final Rectangle screenHitbox = new Rectangle(68, 288, 225, 120);

  private boolean keyCollected = false;
  private boolean remoteCollected = false;
  private boolean brickBreakWon = false;
  private boolean uniformCollected = false;

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
    // key
    if (keyHitbox.contains(x, y)) {
      if (keyCollected) {
        return List.of(new Action.ShowHotbarText("Vous avez déjà récupéré la clé."));
      } else {
        keyCollected = true;
        return List.of(new Action.ShowHotbarText("Vous avez trouvé une clé !"), new Action.CollectItem(new Cle()));
      }
    }

    // chest
    else if (chestHitbox.contains(x, y)) {
      if (keyCollected && !remoteCollected) {
        remoteCollected = true;
        return List.of(new Action.ShowHotbarText("Vous ouvrez le coffre avec la clé.\nVous trouvez une télécommande !"),
                       new Action.CollectItem(new items.Remote()));
      } else if (remoteCollected && !brickBreakWon) {
        return List.of(new Action.ShowHotbarText("Le coffre est vide"));
      } else if (brickBreakWon && !uniformCollected) {
        uniformCollected = true;
        return  List.of(new Action.ShowHotbarText("Il y avait un double fond !\nVous trouvez un uniforme !"),
                        new Action.CollectItem(new items.Uniform()));
      } else if (uniformCollected) {
        return List.of(new Action.ShowHotbarText("Le coffre est vraiment vide cette fois"));
      } else {
        return List.of(new Action.ShowHotbarText("Il vous faut une clé"));
      }
    }

    // screen
    else if (screenHitbox.contains(x, y)) {
      if (remoteCollected) {
        return  List.of(new Action.ShowHotbarText("Vous appuyez sur le bouton de la télécommande.\nL'écran s'allume !"),
                        new Action.ChangeRoom("Brick Break Room"));
      } else {
        return List.of(new Action.ShowHotbarText("L'écran est éteint"));
      }
    }

    // door
    else if (doorHitbox.contains(x, y)) {
      if (!uniformCollected) {
        return List.of(new Action.ShowHotbarText("Vérification d'identité en cours...\nVous n'avez pas d'uniforme, vous ne pouvez pas sortir !"));
      } else {
        return List.of(new Action.ShowHotbarText("Vous avez réussi à vous enfuir, Félicitation !"), new Action.ChangeRoom("Outside"));
      }
    }

    return null;
  }

  @Override
  public void handleAction(Action action) {
    if (action instanceof Action.BrickBreakWon) {
      this.brickBreakWon = true;
    }
  }
}
