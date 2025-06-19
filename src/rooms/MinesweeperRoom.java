package rooms;

import gameLogic.Action;
import gameLogic.Room;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinesweeperRoom extends Room {
    private static final int rows = 8;
    private static final int cols = 8;
    private static final int totalMines = 13;

    private boolean lost = false;
    private boolean won = false;

    private boolean[][] bombs = new boolean[rows][cols];
    private boolean[][] flags = new boolean[rows][cols];
    private boolean[][] revealed = new boolean[rows][cols];
    private int[][] adjacentCounts = new int[rows][cols];

    private int hoveredRow = -1;
    private int hoveredCol = -1;

    private static final int cellSize = 90;

    private double hoveredSceneX = -1;
    private double hoveredSceneY = -1;

    BufferedImage flagSprite;
    BufferedImage bombSprite;

    public MinesweeperRoom(String name, int id, List<Room> linkedTo) {
        super(name, id, linkedTo);

        try {
            setBackground(javax.imageio.ImageIO.read(new File("rooms/assets/screen.png")));
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
        }
        try {
            flagSprite = javax.imageio.ImageIO.read(new File("./rooms/assets/flag.png"));
        } catch (IOException e) {
            System.err.println("Error loading flag sprite: " + e.getMessage());
        }
        try {
            bombSprite = javax.imageio.ImageIO.read(new File("./rooms/assets/bomb.png"));
        } catch (IOException e) {
            System.err.println("Error loading bomb sprite: " + e.getMessage());
        }

        initGame();
    }

    private void initGame() {
        for (int r=0; r<rows; r++) {
            for (int c=0; c<cols; c++) {
                bombs[r][c] = false;
                flags[r][c] = false;
                revealed[r][c] = false;
                adjacentCounts[r][c] = 0;
            }
        }
        Random rand = new Random();
        int placed = 0;
        while (placed < totalMines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (!bombs[r][c]) {
                bombs[r][c] = true;
                placed++;
            }
        }
        for (int r=0; r<rows; r++) {
            for (int c=0; c<cols; c++) {
                adjacentCounts[r][c] = countAdjacentMines(r, c);
            }
        }
    }

    private int countAdjacentMines(int r, int c) {
        int count = 0;
        for (int dr=-1; dr<=1; dr++) {
            for (int dc=-1; dc<=1; dc++) {
                int nr = r+dr, nc = c+dc;
                if (nr>=0 && nr<rows && nc>=0 && nc<cols && bombs[nr][nc]) {
                    count++;
                }
            }
        }
        return count;
    }

    private static class GridTransform {
        final int gridWidth, gridHeight, startX, startY;
        final int cellSize, rows, cols;

        GridTransform(int rows, int cols, int cellSize, int sceneWidth, int sceneHeight) {
            this.rows = rows;
            this.cols = cols;
            this.cellSize = cellSize;
            this.gridWidth = cols * cellSize;
            this.gridHeight = rows * cellSize;
            this.startX = (sceneWidth - gridWidth) / 2 - 90;
            this.startY = (sceneHeight - gridHeight) / 2 - 10;
        }

        int col(double x) {
            return (int) ((x - startX) / cellSize);
        }

        int row(double y) {
            return (int) ((y - startY) / cellSize);
        }

        boolean isValidCell(int r, int c) {
            return r >= 0 && r < rows && c >= 0 && c < cols;
        }
    }

    @Override
    public void draw(Graphics2D g, int width, int height) {
        super.draw(g, width, height);

        GridTransform t = new GridTransform(rows, cols, cellSize, width, height);

        for (int r=0; r<rows; r++) {
            for (int c=0; c<cols; c++) {
                int x = t.startX + c * cellSize;
                int y = t.startY + r * cellSize;

                // Background color
                if (r == hoveredRow && c == hoveredCol) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(new Color(180, 180, 180));
                }
                g.fillRect(x, y, cellSize, cellSize);

                // Border
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);

                // Revealed cell
                if (revealed[r][c]) {
                    if (bombs[r][c]) {
                        if (bombSprite != null) {
                            g.drawImage(
                                bombSprite,
                                x + 1,
                                y + 1,
                                x + cellSize,
                                y + cellSize,
                                0,
                                0,
                                bombSprite.getWidth(),
                                bombSprite.getHeight(),
                                null
                            );
                        } else {
                            g.setColor(Color.RED);
                            g.fillOval(x+10, y+10, cellSize-20, cellSize-20);
                        }
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillRect(x+1, y+1, cellSize-2, cellSize-2);
                        int count = adjacentCounts[r][c];
                        if (count > 0) {
                            g.setColor(Color.BLACK);
                            g.setFont(new Font("Arial", Font.BOLD, 20));
                            FontMetrics fm = g.getFontMetrics();
                            String text = String.valueOf(count);
                            int tx = x + (cellSize - fm.stringWidth(text)) / 2;
                            int ty = y + (cellSize + fm.getAscent()) / 2 - 3;
                            g.drawString(text, tx, ty);
                        }
                    }
                }
                // Flagged cell
                else if (flags[r][c]) {
                    if (flagSprite != null) {
                        g.drawImage(
                            flagSprite,
                            x + 1,
                            y + 1,
                            x + cellSize,
                            y + cellSize,
                            0,
                            0,
                            flagSprite.getWidth(),
                            flagSprite.getHeight(),
                            null
                        );
                    } else {
                        // Draws a triangle
                        g.setColor(Color.RED);
                        int margin = cellSize / 5;
                        int[] xPoints = {
                            x + cellSize / 2,
                            x + margin,
                            x + cellSize - margin
                        };
                        int[] yPoints = {
                            y + margin,
                            y + cellSize - margin,
                            y + cellSize - margin
                        };
                        g.fillPolygon(xPoints, yPoints, 3);
                    }
                }
            }
        }
    }

    @Override
    public List<Action> click(double x, double y) {
        List<Action> resultActions = new ArrayList<>();

        if (lost) {
            initGame();
            lost = false;
            won = false;
            return List.of(new Action.ShowHotbarText("Partie réinitialisée."));
        }

        GridTransform t = new GridTransform(rows, cols, cellSize, 1920, 980);
        int col = t.col(x);
        int row = t.row(y);

        if (!t.isValidCell(row, col)) return List.of();

        if (flags[row][col] || revealed[row][col]) {
            // Ne rien faire si case déjà révélée ou drapeau posé
            return List.of();
        }

        revealed[row][col] = true;

        if (bombs[row][col]) {
            lost = true;
            won = false;
            return List.of(
                new Action.ShowHotbarText("BOOM! Vous avez perdu.\nCliquez pour recommencer.")
            );
        } else {
            if (adjacentCounts[row][col] == 0) {
                revealNeighbors(row, col);
            }
        }

        if (checkWin()) {
            won = true;
            resultActions.add(new Action.ShowHotbarText("Bravo, vous avez gagné !\nLa porte est ouverte."));
            resultActions.add(new Action.ChangeRoom("Bedroom"));
            resultActions.add(new Action.MinesweeperWon());
        }

        hoveredRow = row;
        hoveredCol = col;

        return resultActions;
    }

    private void revealNeighbors(int r, int c) {
        for (int dr=-1; dr<=1; dr++) {
            for (int dc=-1; dc<=1; dc++) {
                int nr = r+dr, nc = c+dc;
                if (isValidCell(nr, nc) && !revealed[nr][nc] && !bombs[nr][nc]) {
                    revealed[nr][nc] = true;
                    if (adjacentCounts[nr][nc] == 0) {
                        revealNeighbors(nr, nc);
                    }
                }
            }
        }
    }

    public List<Action> flagAt(double x, double y) {
        GridTransform t = new GridTransform(rows, cols, cellSize, 1920, 980);
        int col = t.col(x);
        int row = t.row(y);

        if (!t.isValidCell(row, col)) return null;

        if (!revealed[row][col]) {
            flags[row][col] = !flags[row][col];
            return List.of(
                new Action.ShowHotbarText(flags[row][col] ? "Drapeau posé" : "Drapeau retiré")
            );
        }

        return null;
    }

    public void hover(double x, double y) {
        GridTransform t = new GridTransform(rows, cols, cellSize, 1920, 980);
        int col = t.col(x);
        int row = t.row(y);

        if (t.isValidCell(row, col)) {
            hoveredRow = row;
            hoveredCol = col;
            hoveredSceneX = x;
            hoveredSceneY = y;
        } else {
            hoveredRow = -1;
            hoveredCol = -1;
            hoveredSceneX = -1;
            hoveredSceneY = -1;
        }
    }

    @Override
    public List<Action> handleKeyPressed(java.awt.event.KeyEvent e) {
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_A) {
            double hoverX = getHoveredSceneX();
            double hoverY = getHoveredSceneY();

            return flagAt(hoverX, hoverY);
        }

        return null;
    }

    private boolean isValidCell(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    private boolean checkWin() {
        for (int r=0; r<rows; r++) {
            for (int c=0; c<cols; c++) {
                if (!bombs[r][c] && !revealed[r][c]) return false;
            }
        }
        return true;
    }

    public double getHoveredSceneX() {
        return hoveredSceneX;
    }
    
    public double getHoveredSceneY() {
        return hoveredSceneY;
    }
}