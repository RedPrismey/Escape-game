package items;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Remote extends Item {
  public Remote() {
    super("Télécommande");

    try {
      BufferedImage s = ImageIO.read(new File("./items/assets/remote.png"));
      super.setSprite(s);
    } catch (IOException e) {
      System.err.println("Error while loading room background : " + e.getMessage());
    }
  }
}
