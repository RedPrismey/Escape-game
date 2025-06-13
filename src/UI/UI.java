package UI;

import gameState.GameState;

import javax.swing.*;
import java.awt.*;


public class UI extends JPanel {
  private final GameState game;
  private final HotBar hotbar;
  private final Countdown countdown;

  // Aspect ratio 16:9
  private static final int ASPECT_RATIO_W = 16;
  private static final int ASPECT_RATIO_H = 9;
  private static final int BASE_HEIGHT = 1080; // Hauteur de référence pour le rendu interne

  public UI(GameState gameState) {
    this.game = gameState;
    this.hotbar = new HotBar(game.inventory);
    this.countdown = new Countdown(300); // 5 minutes par exemple
    this.countdown.start();
    // Rafraîchissement régulier pour animer le compte à rebours
    new Timer(200, e -> repaint()).start();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int panelWidth = getWidth();
    int panelHeight = getHeight();

    // Pour l'aspect ratio 16:9
    int targetWidth = panelWidth;
    int targetHeight = (int) (panelWidth * ASPECT_RATIO_H / (double) ASPECT_RATIO_W);
    if (targetHeight > panelHeight) {
      targetHeight = panelHeight;
      targetWidth = (int) (panelHeight * ASPECT_RATIO_W / (double) ASPECT_RATIO_H);
    }

    int xOffset = (panelWidth - targetWidth) / 2;
    int yOffset = (panelHeight - targetHeight) / 2;

    // Fond noir pour quand l'écran n'est pas 16:9
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, panelWidth, panelHeight);

    // Pour translater le rendu dans la zone 16:9
    Graphics2D g2 = (Graphics2D) g.create();
    g2.translate(xOffset, yOffset);
    g2.setClip(0, 0, targetWidth, targetHeight);

    // Scaling
    double scale = targetHeight / (double) BASE_HEIGHT;
    g2.scale(scale, scale);

    int hotbarHeight = 100;
    int hotbarY = BASE_HEIGHT - hotbarHeight;
    int baseWidth = (int) (BASE_HEIGHT * ASPECT_RATIO_W / (double) ASPECT_RATIO_H);
    int countdownWidth = 180;
    // Dessine la room sur la partie haute
    game.getCurrentRoom().draw(g2, baseWidth, BASE_HEIGHT - hotbarHeight);
    // Dessine la hotbar (moins large pour laisser la place au timer)
    hotbar.draw(g2, 0, hotbarY, baseWidth - countdownWidth, hotbarHeight);
    // Dessine le timer à droite de la hotbar
    countdown.draw(g2, baseWidth - countdownWidth, hotbarY, countdownWidth, hotbarHeight);

    g2.dispose();
  }
}
