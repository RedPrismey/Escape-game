package rooms;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import gameLogic.Action;
import gameLogic.Rectangle;
import gameLogic.Room;

public class Book extends Room {
  private static final Rectangle crossHitbox = new Rectangle(1600, 80, 80, 75);

  public Book(String name, int id, List<Room> linkedTo) {
    super(name, id, linkedTo);

    try {
      BufferedImage s = ImageIO.read(new File("./rooms/assets/book.png"));
      this.setBackground(s);
    } catch (IOException e) {
      System.err.println("Error while loading room background : " + e.getMessage());
    }
  }

  @Override
  public void draw(Graphics2D g, int width, int height) {
    super.draw(g, width, height);

    g.setColor(Color.BLACK);
    g.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 30));
    String texte = "Jeudi 7 Juin 2372 :\n\nCher journal,\nCa fait aujourd'hui 5 mois que je suis enfermé dans ce vaisseau, au milieu de l'espace.\nJe n'ai plus d'espoir de m'en sortir,\nmais j'ai remarqué que les gardes ne nettoyaient pas le vaisseau.\n\nDonc pour les futurs prisonniers, voici un message :\nYr pbqr qr yn cbegr rfg : PUNATRguvfCNFFJBEQ. \nFv ibhf neevirm à ibhf épunccre ninag yr qépbyyntr, ibhf fheivierm";

    String[] lignes = texte.split("\n");
    FontMetrics metrics = g.getFontMetrics();
    int lineHeight = metrics.getHeight();
    for (int i = 0; i < lignes.length; i++) {
      g.drawString(lignes[i], 300, 170 + i * lineHeight);
    }
  }

  @Override
  public List<Action> click(double x, double y) {
    if (crossHitbox.contains(x, y)) {
      return List.of(new Action.ShowHotbarText("Vous en avez assez lu"), new Action.ChangeRoom("Bedroom"));
    }

    return super.click(x, y);
  }

}
