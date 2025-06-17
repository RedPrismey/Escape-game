package items;

import java.awt.image.BufferedImage;

public class Item {
  private String name;
  private BufferedImage sprite;

  public Item(String name) {
    this.name = name;
  }

  public BufferedImage getSprite() {
    return sprite;
  }

  public void setSprite(BufferedImage s) {
    this.sprite = s;
  }

  public String getName() {
    return name;
  }
}
