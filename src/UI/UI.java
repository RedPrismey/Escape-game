package UI;

import gameLogic.Action;
import gameLogic.GameState;
import gameLogic.GameStatus;
import java.awt.*;
import java.util.List;
import javax.swing.*;

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

    // Mouse listener fusionné qui gère MinesweeperRoom en priorité
    addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent e) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Compute target width/height (your logic)
        int targetWidth = panelWidth;
        int targetHeight = (int) (panelWidth * ASPECT_RATIO_H / (double) ASPECT_RATIO_W);
        if (targetHeight > panelHeight) {
          targetHeight = panelHeight;
          targetWidth = (int) (panelHeight * ASPECT_RATIO_W / (double) ASPECT_RATIO_H);
        }

        // Compute offset and scale
        int xOffset = (panelWidth - targetWidth) / 2;
        int yOffset = (panelHeight - targetHeight) / 2;
        double scale = targetHeight / (double) BASE_HEIGHT;

        int mouseX = e.getX();
        int mouseY = e.getY();

        if (mouseX >= xOffset && mouseX <= xOffset + targetWidth &&
            mouseY >= yOffset && mouseY <= yOffset + targetHeight) {

          double sceneX = (mouseX - xOffset) / scale;
          double sceneY = (mouseY - yOffset) / scale;

          if (game.getCurrentRoom() instanceof rooms.MinesweeperRoom msRoom) {
            // Appelle la méthode click spécifique qui renvoie une liste d'actions
            List<Action> actions = msRoom.click(sceneX, sceneY);
            game.executeAction(actions);
            repaint();
          } else if (game.status == GameStatus.PLAYING) {
            List<Action> actions = game.getCurrentRoom().click(sceneX, sceneY);
            game.executeAction(actions);
            repaint();
          }
        }
      }
    });

    // Ajoute le listener pour le mouvement souris (hover)
    addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
      @Override
      public void mouseMoved(java.awt.event.MouseEvent e) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int targetWidth = panelWidth;
        int targetHeight = (int) (panelWidth * ASPECT_RATIO_H / (double) ASPECT_RATIO_W);
        if (targetHeight > panelHeight) {
          targetHeight = panelHeight;
          targetWidth = (int) (panelHeight * ASPECT_RATIO_W / (double) ASPECT_RATIO_H);
        }

        int xOffset = (panelWidth - targetWidth) / 2;
        int yOffset = (panelHeight - targetHeight) / 2;
        double scale = targetHeight / (double) BASE_HEIGHT;

        int mouseX = e.getX();
        int mouseY = e.getY();

        if (mouseX >= xOffset && mouseX <= xOffset + targetWidth &&
            mouseY >= yOffset && mouseY <= yOffset + targetHeight) {

          double sceneX = (mouseX - xOffset) / scale;
          double sceneY = (mouseY - yOffset) / scale;

          if (game.getCurrentRoom() instanceof rooms.MinesweeperRoom msRoom) {
            msRoom.setHover(sceneX, sceneY);
            repaint();
          }
        }
      }
    });

    // Key listener pour poser/enlever drapeaux avec la touche A (par exemple)
    addKeyListener(new java.awt.event.KeyAdapter() {
      @Override
      public void keyPressed(java.awt.event.KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyCode());
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_A) {
          if (game.getCurrentRoom() instanceof rooms.MinesweeperRoom msRoom) {
            // Pose/enlève drapeau sur la case survolée (implémente la méthode flagAt dans MinesweeperRoom)
            double hoverX = msRoom.getHoveredSceneX();
            double hoverY = msRoom.getHoveredSceneY();
            List<Action> actions = msRoom.flagAt(hoverX, hoverY);
            game.executeAction(actions);
            repaint();
          }
        }
      }
    });

    setFocusable(true);
    requestFocusInWindow();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = initGraphics(g);

    game.update();

    // White background for the hotbar
    g2.setColor(Color.WHITE);
    g2.fillRect(0, HOTBAR_Y, BASE_WIDTH - COUNTDOWN_WIDTH, HOTBAR_HEIGHT);

    game.getCurrentRoom().draw(g2, BASE_WIDTH, BASE_HEIGHT - HOTBAR_HEIGHT);
    game.inventory.draw(g2, 0, HOTBAR_Y, BASE_WIDTH - COUNTDOWN_WIDTH, HOTBAR_HEIGHT);
    game.countdown.draw(g2, BASE_WIDTH - COUNTDOWN_WIDTH, HOTBAR_Y, COUNTDOWN_WIDTH, HOTBAR_HEIGHT);

    drawHotbarText(g2, game.getHotbarText());

    if (game.status == GameStatus.LOST) {
      drawGameOver(g2);
    }

    g2.dispose();
  }

  private void drawHotbarText(Graphics2D g, String text) {
    if (text == null) {
      return;
    }

    g.setFont(new Font("Arial", Font.BOLD, 36));
    g.setColor(Color.BLACK);

    FontMetrics fm = g.getFontMetrics();
    int x = 700;
    int y = HOTBAR_Y + (HOTBAR_HEIGHT + fm.getAscent()) / 2 - 10;

    g.drawString(text, x, y);
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

  private Graphics2D initGraphics(Graphics g) {
    int panelWidth = getWidth();
    int panelHeight = getHeight();

    int targetWidth = panelWidth;
    int targetHeight = (int) (panelWidth * ASPECT_RATIO_H / (double) ASPECT_RATIO_W);
    if (targetHeight > panelHeight) {
      targetHeight = panelHeight;
      targetWidth = (int) (panelHeight * ASPECT_RATIO_W / (double) ASPECT_RATIO_H);
    }

    int xOffset = (panelWidth - targetWidth) / 2;
    int yOffset = (panelHeight - targetHeight) / 2;

    g.setColor(Color.BLACK);
    g.fillRect(0, 0, panelWidth, panelHeight);

    Graphics2D g2 = (Graphics2D) g.create();
    g2.translate(xOffset, yOffset);
    g2.setClip(0, 0, targetWidth, targetHeight);

    double scale = targetHeight / (double) BASE_HEIGHT;
    g2.scale(scale, scale);

    return g2;
  }
}