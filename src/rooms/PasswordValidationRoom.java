package rooms;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import gameLogic.Action;
import gameLogic.Rectangle;
import gameLogic.Room;

public class PasswordValidationRoom extends Room {
  private static final String CORRECT_PASSWORD = "CHANGEthisPASSWORD";

  private StringBuilder input = new StringBuilder();

  private boolean validated = false;
  private boolean failed = false;

  private static BufferedImage crossSprite;

  private static final Rectangle crossHitbox = new Rectangle(1800, 50, 75, 75);

  public PasswordValidationRoom(String name, int id, List<Room> linkedTo) {
    super(name, id, linkedTo);

    try {
      BufferedImage s = ImageIO.read(new File("./rooms/assets/screen.png"));
      this.setBackground(s);
    } catch (IOException e) {
      System.err.println("Error while loading room background : " + e.getMessage());
    }

    try {
      crossSprite = javax.imageio.ImageIO.read(new File("./rooms/assets/cross.png"));
    } catch (IOException e) {
      System.err.println("Error loading cross sprite: " + e.getMessage());
    }

  }

  @Override
  public void draw(Graphics2D g, int width, int height) {
    super.draw(g, width, height);

    int startX = 510, startY = 120;
    int size = 720;

    g.setColor(new Color(0x00, 0x44, 0x16));
    g.fillRect(startX, startY, size, size);

    g.setColor(Color.BLACK);
    g.setFont(new Font("Consolas", Font.PLAIN, 27));
    g.drawString("prisonnier@spaceship.go> ./secure_password.sh", startX + 25, startY + 50);
    g.drawString("Entrez le mot de passe de la porte :", startX + 25, startY + 80);

    g.setFont(new Font("Consolas", Font.PLAIN, 27));
    g.drawString(input.toString(), startX + 25, startY + 110);

    if (validated) {
      g.setColor(new Color(0, 128, 0));
      g.setFont(new Font("Consolas", Font.PLAIN, 27));
      g.drawString("Mot de passe correct !", startX + 25, startY + 140);
    } else if (failed) {
      g.setColor(Color.RED);
      g.setFont(new Font("Consolas", Font.PLAIN, 27));
      g.drawString("Mot de passe incorrect.", startX + 25, startY + 140);
    }

    g.drawImage(crossSprite, 1800, 50, 75, 75, null);
  }

  @Override
  public List<Action> handleKeyPressed(java.awt.event.KeyEvent e) {
    if (validated)
      return List.of();
    int code = e.getKeyCode();
    char c = e.getKeyChar();

    if (code == java.awt.event.KeyEvent.VK_BACK_SPACE && !input.isEmpty()) {
      input.deleteCharAt(input.length() - 1);
      failed = false;
    } else if (code == java.awt.event.KeyEvent.VK_ENTER) {
      if (input.toString().equals(CORRECT_PASSWORD)) {
        validated = true;
        failed = false;
        return List.of(new Action.ChangeRoom("Bedroom"), new Action.PasswordValidated(),
            new Action.ShowHotbarText("Super, je vais ouvrir la porte !"));
      } else if (input.toString().equals("exit")) {
        input.setLength(0);
        return List.of(new Action.ChangeRoom("Bedroom"), new Action.ShowHotbarText("Vous quittez la console"));
      } else {
        failed = true;
        return List.of(new Action.ShowHotbarText(
            "Je devrais chercher le mot de passe.\nIl me semble que exit permet de quitter la console."));
      }
    } else if (Character.isLetterOrDigit(c) || Character.isWhitespace(c)
        || "!@#$%^&*()-_=+[]{};:'\",.<>/?".indexOf(c) >= 0) {
      if (failed) {
        input.setLength(0);
      }
      if (input.length() < 32) {
        input.append(c);
        failed = false;
      }
    }
    return null;
  }

  @Override
  public List<Action> click(double x, double y) {
    if (crossHitbox.contains(x, y)) {
      return List.of(new Action.ChangeRoom("Bedroom"));
    }
    return null;
  }
}
