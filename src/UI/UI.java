package UI;

import gameLogic.Action;
import gameLogic.GameState;
import gameLogic.GameStatus;

import javax.swing.*;
import java.awt.*;

/**
 * The UI class is responsible for rendering the game interface.
 * It draws the current room, the hotbar, and the countdown timer.
 * The UI is designed to maintain a 16:9 aspect ratio and scales accordingly.
 */
public class UI extends JPanel {
  private final GameState game;

  // Aspect ratio 16:9
  private static final int ASPECT_RATIO_W = 16;
  private static final int ASPECT_RATIO_H = 9;
  private static final int BASE_HEIGHT = 1080; // Base height for rendering
  private static final int BASE_WIDTH = 1920;

  private static final int HOTBAR_HEIGHT = 100;
  private static final int HOTBAR_Y = BASE_HEIGHT - HOTBAR_HEIGHT;

  private static final int COUNTDOWN_WIDTH = 180;


  public UI(GameState gameState) {
    this.game = gameState;

    // repaint regularly to update the countdown
    new Timer(200, _ -> repaint()).start();

    addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        // Calcul du facteur d'échelle comme dans initGraphics
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int targetHeight = (int) (panelWidth * ASPECT_RATIO_H / (double) ASPECT_RATIO_W);
        if (targetHeight > panelHeight) {
          targetHeight = panelHeight;
        }
        double scale = targetHeight / (double) BASE_HEIGHT;

        // Passer le scale à la room
        Action action = game.getCurrentRoom().click(x, y, scale);
        game.executeAction(action);
        repaint();
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = initGraphics(g);

    game.update();

    game.getCurrentRoom().draw(g2, BASE_WIDTH, BASE_HEIGHT - HOTBAR_HEIGHT);
    game.inventory.draw(g2, 0, HOTBAR_Y, BASE_WIDTH - COUNTDOWN_WIDTH, HOTBAR_HEIGHT);
    game.countdown.draw(g2, BASE_WIDTH - COUNTDOWN_WIDTH, HOTBAR_Y, COUNTDOWN_WIDTH, HOTBAR_HEIGHT);

    if (game.status == GameStatus.LOST) {
      drawGameOver(g2);
    }

    g2.dispose();
  }

  private void drawGameOver(Graphics2D g2) {
    g2.setFont(new Font("Arial", Font.BOLD, 75));

    String gameOverText = "Game Over";

    FontMetrics fm = g2.getFontMetrics();
    int textWidth = fm.stringWidth(gameOverText);
    int textHeight = fm.getHeight();
    int ascent = fm.getAscent();

    int x = (BASE_WIDTH - textWidth) / 2;
    int y = (BASE_HEIGHT - textHeight) / 2;

    int padding = 40;
    int rectX = x - padding / 2;
    int rectY = y - padding / 2 + 7;
    int rectWidth = textWidth + padding;
    int rectHeight = textHeight + padding;

    g2.setColor(Color.GRAY);
    g2.fillRect(rectX, rectY, rectWidth, rectHeight);

    g2.setColor(Color.RED);
    g2.drawString(gameOverText, x, y + ascent);
  }

  /**
   * Initializes the graphics context for rendering.
   * It sets the aspect ratio, scales the graphics, and centers the content.
   *
   * @param g the Graphics object used to create the Graphics2D
   * @return a Graphics2D object with the right aspect ratio and scale
   */
  private Graphics2D initGraphics(Graphics g) {
    int panelWidth = getWidth();
    int panelHeight = getHeight();

    // for the aspect ratio 16:9
    int targetWidth = panelWidth;
    int targetHeight = (int) (panelWidth * ASPECT_RATIO_H / (double) ASPECT_RATIO_W);
    if (targetHeight > panelHeight) {
      targetHeight = panelHeight;
      targetWidth = (int) (panelHeight * ASPECT_RATIO_W / (double) ASPECT_RATIO_H);
    }

    int xOffset = (panelWidth - targetWidth) / 2;
    int yOffset = (panelHeight - targetHeight) / 2;

    // black background in case screen is not 16:9
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, panelWidth, panelHeight);

    // to translate in 19:9
    Graphics2D g2 = (Graphics2D) g.create();
    g2.translate(xOffset, yOffset);
    g2.setClip(0, 0, targetWidth, targetHeight);

    // Scaling
    double scale = targetHeight / (double) BASE_HEIGHT;
    g2.scale(scale, scale);

    return g2;
  }
}