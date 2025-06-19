package items;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Uniform extends Item {

    public Uniform() {
        super("Uniform");

        try {
            BufferedImage s = ImageIO.read(new File("./items/assets/uniform.png"));
            super.setSprite(s);
        } catch (IOException e) {
            System.err.println("Error while loading room background : " + e.getMessage());
        }
    }
}
