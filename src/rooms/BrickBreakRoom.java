package rooms;

import gameLogic.Action;
import gameLogic.Room;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BrickBreakRoom extends Room {
  private static final int GAME_WIDTH = 800;
  private static final int GAME_HEIGHT = 600;

  private static final int PADDLE_WIDTH = 120;
  private static final int PADDLE_HEIGHT = 18;
  private static final int PADDLE_Y = GAME_HEIGHT - 40;
  private static final int PADDLE_SPEED = 5;

  private static final int BALL_SIZE = 18;
  private double ballX = GAME_WIDTH / 2.0;
  private double ballY = GAME_HEIGHT / 2.0;

  private static final double BASE_X_SPEED = 4;
  private static final double BASE_Y_SPEED = -4;
  private double ballDX = BASE_X_SPEED;
  private double ballDY = BASE_Y_SPEED;

  private int paddleX = (GAME_WIDTH - PADDLE_WIDTH) / 2;

  private static final int BRICK_ROWS = 5;
  private static final int BRICK_COLS = 11;
  private static final int BRICK_WIDTH = 70;
  private static final int BRICK_HEIGHT = 28;
  private final boolean[][] bricks = new boolean[BRICK_ROWS][BRICK_COLS];

  private boolean gameWon = false;
  private boolean gameLost = false;

  private final Set<Integer> pressedKeys = new HashSet<>();
  private boolean started = false;

  private static BufferedImage crossSprite;

  private static final Rectangle crossHitbox = new Rectangle(1800, 50, 75, 75);

  public BrickBreakRoom(String name, int id, List<Room> linkedTo) {
    super(name, id, linkedTo);

    try {
      crossSprite = javax.imageio.ImageIO.read(new File("./rooms/assets/cross.png"));
    } catch (IOException e) {
      System.err.println("Error loading cross sprite: " + e.getMessage());
    }

    resetGame();
  }

  @Override
  public void draw(Graphics2D g, int width, int height) {
    if (started && !gameWon && !gameLost) {
      if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
        paddleX -= PADDLE_SPEED;
        if (paddleX < 0)
          paddleX = 0;
      }
      if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
        paddleX += PADDLE_SPEED;
        if (paddleX > GAME_WIDTH - PADDLE_WIDTH)
          paddleX = GAME_WIDTH - PADDLE_WIDTH;
      }
    }

    if (started) {
      updateGame();
    }

    g.setColor(Color.GRAY);
    g.fillRect(0, 0, width, height);

    int offsetX = (width - GAME_WIDTH) / 2;
    int offsetY = (height - GAME_HEIGHT) / 2;

    g.setColor(Color.DARK_GRAY);
    g.fillRoundRect(offsetX - 20, offsetY - 20, GAME_WIDTH + 40, GAME_HEIGHT + 40, 30, 30);

    g.setColor(Color.BLACK);
    g.fillRoundRect(offsetX, offsetY, GAME_WIDTH, GAME_HEIGHT, 30, 30);

    g.translate(offsetX, offsetY);

    int bricksTotalWidth = BRICK_COLS * BRICK_WIDTH;
    int bricksStartX = (GAME_WIDTH - bricksTotalWidth) / 2 - 8;

    for (int r = 0; r < BRICK_ROWS; r++) {
      for (int c = 0; c < BRICK_COLS; c++) {
        if (bricks[r][c]) {
          int brickX = bricksStartX + c * BRICK_WIDTH + 8;
          int brickY = r * BRICK_HEIGHT + 8;
          g.setColor(Color.ORANGE);
          g.fillRect(brickX, brickY, BRICK_WIDTH - 4, BRICK_HEIGHT - 4);
          g.setColor(Color.BLACK);
          g.drawRect(brickX, brickY, BRICK_WIDTH - 4, BRICK_HEIGHT - 4);
        }
      }
    }

    g.setColor(Color.CYAN);
    g.fillRoundRect(paddleX, PADDLE_Y, PADDLE_WIDTH, PADDLE_HEIGHT, 12, 12);

    if (ballY <= GAME_HEIGHT) {
      g.setColor(Color.WHITE);
      g.fillOval((int) ballX, (int) ballY, BALL_SIZE, BALL_SIZE);
    }

    g.setFont(new Font("Arial", Font.BOLD, 36));
    g.setColor(Color.GREEN);
    if (!started && !gameWon && !gameLost) {
      g.drawString("Appuyez sur une touche pour commencer", GAME_WIDTH / 2 - 370, GAME_HEIGHT / 2 - 40);
    } else if (gameWon) {
      g.drawString("Bravo !", GAME_WIDTH / 2 - 80, GAME_HEIGHT / 2);
    } else if (gameLost) {
      g.setColor(Color.RED);
      g.drawString("Perdu !", GAME_WIDTH / 2 - 70, GAME_HEIGHT / 2);
    }

    g.translate(-offsetX, -offsetY);

    g.setColor(Color.DARK_GRAY);
    g.fillRect(1400, 850, 500, 110);

    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 30));
    g.drawString("Déplacez avec les flèches", 1410, 890);
    g.drawString("Bougez la souris pour accélérer", 1410, 940);

    g.drawImage(crossSprite, 1800, 50, 75, 75, null);
  }

  @Override
  public List<Action> handleKeyPressed(KeyEvent e) {
    if (!started && !gameWon && !gameLost) {
      started = true;
    }
    if (gameLost) {
      resetGame();
      return null;
    }
    if (gameWon) {
      return List.of(new Action.ChangeRoom("Main Room"),
          new Action.ShowHotbarText("Vous entendez un bruit de click"),
          new Action.BrickBreakWon());
    }
    pressedKeys.add(e.getKeyCode());
    return null;
  }

  @Override
  public List<Action> click(double x, double y) {
    if (crossHitbox.contains(x, y)) {
      return List.of(new Action.ChangeRoom("Main Room"));
    }
    return null;
  }

  @Override
  public List<Action> handleKeyReleased(KeyEvent e) {
    pressedKeys.remove(e.getKeyCode());
    return new ArrayList<>();
  }

  private void resetGame() {
    for (int r = 0; r < BRICK_ROWS; r++)
      for (int c = 0; c < BRICK_COLS; c++)
        bricks[r][c] = true;
    ballX = GAME_WIDTH / 2.0;
    ballY = GAME_HEIGHT / 2.0;
    ballDX = BASE_X_SPEED;
    ballDY = BASE_Y_SPEED;
    paddleX = (GAME_WIDTH - PADDLE_WIDTH) / 2;
    gameWon = false;
    gameLost = false;
    started = false;
    pressedKeys.clear();
  }

  private void updateGame() {
    ballX += ballDX;
    ballY += ballDY;

    if (ballX < 0) {
      ballX = 0;
      ballDX = -ballDX;
    }
    if (ballX > GAME_WIDTH - BALL_SIZE) {
      ballX = GAME_WIDTH - BALL_SIZE;
      ballDX = -ballDX;
    }
    if (ballY < 0) {
      ballY = 0;
      ballDY = -ballDY;
    }

    if (ballY + BALL_SIZE >= PADDLE_Y && ballY + BALL_SIZE <= PADDLE_Y + PADDLE_HEIGHT &&
        ballX + BALL_SIZE >= paddleX && ballX <= paddleX + PADDLE_WIDTH) {
      double speed = Math.sqrt(ballDX * ballDX + ballDY * ballDY);
      double hitPos = (ballX + BALL_SIZE / 2.0 - paddleX) / PADDLE_WIDTH - 0.5;
      double angle = Math.toRadians(150 * hitPos);

      ballDX = speed * Math.sin(angle);
      ballDY = -Math.abs(speed * Math.cos(angle));
    }

    int bricksTotalWidth = BRICK_COLS * BRICK_WIDTH;
    int bricksStartX = (GAME_WIDTH - bricksTotalWidth) / 2;
    for (int r = 0; r < BRICK_ROWS; r++) {
      for (int c = 0; c < BRICK_COLS; c++) {
        if (bricks[r][c]) {
          int bx = bricksStartX + c * BRICK_WIDTH + 8;
          int by = r * BRICK_HEIGHT + 8;
          Rectangle brickRect = new Rectangle(bx, by, BRICK_WIDTH - 4, BRICK_HEIGHT - 4);
          Rectangle ballRect = new Rectangle((int) ballX, (int) ballY, BALL_SIZE, BALL_SIZE);
          if (brickRect.intersects(ballRect)) {
            bricks[r][c] = false;
            ballDY = -ballDY;
            break;
          }
        }
      }
    }

    boolean anyBrick = false;
    for (int r = 0; r < BRICK_ROWS; r++)
      for (int c = 0; c < BRICK_COLS; c++)
        if (bricks[r][c]) {
          anyBrick = true;
          break;
        }
    if (!anyBrick) {
      gameWon = true;
    }

    if (ballY > GAME_HEIGHT) {
      gameLost = true;
    }
  }
}
