package rooms;

import gameLogic.Action;
import gameLogic.Room;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PasswordValidationRoom extends Room {
    private static final String CORRECT_PASSWORD = "CHANGEthisPASSWORD";
    private StringBuilder input = new StringBuilder();
    private boolean validated = false;
    private boolean failed = false;

    public PasswordValidationRoom(String name, int id, List<Room> linkedTo) {
        super(name, id, linkedTo);

        try {
            BufferedImage s = ImageIO.read(new File("./rooms/assets/screen.png"));
            this.setBackground(s);
        } catch (IOException e) {
            System.err.println("Error while loading room background : " + e.getMessage());
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
    }

    @Override
    public List<Action> handleKeyPressed(java.awt.event.KeyEvent e) {
        if (validated) return List.of();
        int code = e.getKeyCode();
        char c = e.getKeyChar();

        if (code == java.awt.event.KeyEvent.VK_BACK_SPACE && !input.isEmpty()) {
            input.deleteCharAt(input.length() - 1);
            failed = false;
        } else if (code == java.awt.event.KeyEvent.VK_ENTER) {
            if (input.toString().equals(CORRECT_PASSWORD)) {
                validated = true;
                failed = false;
                return List.of(new Action.ChangeRoom("Bedroom"), new Action.PasswordValidated(), new Action.ShowHotbarText("Super, je vais ouvrir la porte !"));
            } else if (input.toString().equals("exit")) {
                input.setLength(0);
                return List.of(new Action.ChangeRoom("Bedroom"), new Action.ShowHotbarText("Vous quittez la console"));
            } else {
                failed = true;
                input.setLength(0);
                return  List.of(new Action.ShowHotbarText("Je devrais chercher le mot de passe.\nIl me semble que exit permet de quitter la console."));
            }
        } else if (Character.isLetterOrDigit(c) || Character.isWhitespace(c) || "!@#$%^&*()-_=+[]{};:'\",.<>/?".indexOf(c) >= 0) {
            if (input.length() < 32) {
                input.append(c);
                failed = false;
            }
        }
        return null;
    }
}
