package rooms;

import gameLogic.Action;
import gameLogic.Room;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinesweeperRoom extends Room {
    private final int rows = 8;
    private final int cols = 8;
    private final int totalMines = 13;

    private boolean[][] bombs = new boolean[rows][cols];
    private boolean[][] flags = new boolean[rows][cols];
    private boolean[][] revealed = new boolean[rows][cols];
    private int[][] adjacentCounts = new int[rows][cols];

    private int hoveredRow = -1;
    private int hoveredCol = -1;

    private final int cellSize = 80;

    private double hoveredSceneX = -1;
    private double hoveredSceneY = -1;

    public MinesweeperRoom(String name, int id) {
        super(name, id);
        initGame();

        try {
            BufferedImage bg = javax.imageio.ImageIO.read(new java.io.File("./rooms/assets/minesweeper_background.png"));
            this.setBackground(bg);
        } catch (IOException e) {
            System.err.println("Error loading Minesweeper background: " + e.getMessage());
        }
    }

    public MinesweeperRoom(String name, int id, List<Room> linkedTo) {
        super(name, id, linkedTo);

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
            this.startX = (sceneWidth - gridWidth) / 2;
            this.startY = (sceneHeight - gridHeight) / 2;
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
                    g.setColor(Color.GRAY);
                }
                g.fillRect(x, y, cellSize, cellSize);

                // Border
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);

                // Revealed cell
                if (revealed[r][c]) {
                    if (bombs[r][c]) {
                        if (bombImage != null) {
                            g.drawImage(bombImage, x+10, y+10, cellSize-20, cellSize-20, null);
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
                    g.setColor(Color.RED);
                    g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
                    g.drawString("🚩", x + cellSize/4, y + 3*cellSize/4);
                }
            }
        }
    }

    @Override
    public List<Action> click(double x, double y) {
        List<Action> resultActions = new ArrayList<>();

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
            // Bombe cliquée = game lost
            return List.of(
                new Action.ShowHotbarText("BOOM! Vous avez perdu."),
                new Action.ChangeRoom(0) // TODO: change
            );
        } else {
            if (adjacentCounts[row][col] == 0) {
                revealNeighbors(row, col);
            }
            resultActions.add(new Action.ShowHotbarText("Case révélée."));
        }

        if (checkWin()) {
            resultActions.add(new Action.ShowHotbarText("Bravo, vous avez gagné !"));
            resultActions.add(new Action.ChangeRoom(0)); //TODO: change
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
    public void resetGame() {
        initGame(); // Réinitialise les bombes, drapeaux, etc.
        hoveredRow = -1;
        hoveredCol = -1;
    }
    public int getRemainingFlags() {
        int count = 0;
        for (boolean[] flag : flags) {
            for (int c = 0; c < flags[0].length; c++) {
                if (flag[c]) {
                    count++;
                }
            }
        }
        return totalMines - count;
    }
}