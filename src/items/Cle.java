package items;

import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.IOException;

public class Cle extends Item {
  public Cle() {
    super("Clé");

    try {
      URL url = getClass().getResource("./assets/cle.jpg");
      BufferedImage s = ImageIO.read(url);
      super.setSprite(s);
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("IOException, probably can't read sprite");
    }
  }
}
