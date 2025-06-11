package UI;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

import gameState.*;

public class UI extends JFrame {
  private static final String TITLE = "Escape Game 2000";
  protected Timer timer;
  protected int seconde, minute;
  protected JLabel timerLabel;

  public UI(GameState game) {
    this.setTitle(TITLE);

    // Set fullscreen
    GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice device = graphics.getDefaultScreenDevice();
    device.setFullScreenWindow(this);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Crée un panel racine avec GridBagLayout
    JPanel root = new JPanel(new GridBagLayout());
    root.setOpaque(false); // si tu veux voir le fond du jeu

    // Instanciation de la hotbar
    JPanel hotbar = new HotBar(game.inventory); // ta classe HotBar custom
    hotbar.setPreferredSize(new Dimension(800, 100)); // taille initiale, sera ajustée

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 0.1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.SOUTHWEST; // Sud-Ouest = en bas à gauche
    root.add(hotbar, gbc);

    // Timer label
    timerLabel = new JLabel("Temps restant : 60:00");
    timerLabel.setForeground(Color.RED); // texte blanc
    timerLabel.setFont(new Font("Arial", Font.BOLD, 20));

    GridBagConstraints gbcTimer = new GridBagConstraints();
    gbcTimer.gridx = 1;
    gbcTimer.gridy = 1;
    gbcTimer.weightx = 0.0;
    gbcTimer.weighty = 0.0;
    gbcTimer.anchor = GridBagConstraints.SOUTHEAST;
    gbcTimer.insets = new Insets(10, 10, 10, 10); // marges
    root.add(timerLabel, gbcTimer);

    this.setContentPane(root);
    this.revalidate();
    this.repaint();
    this.setVisible(true);

    minute = 59;
    seconde = 59;
    this.timer = new Timer(1000, e -> {
      seconde--;
      if (seconde <= 0) {
        minute--;
        seconde = 59;
      }
      timerLabel.setText("Temps restants: " + minute + ":" + seconde);
    });
    timer.start();
  }

}
