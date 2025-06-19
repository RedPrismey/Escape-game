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



    @SuppressWarnings("FieldMayBeFinal")
    private BufferedImage bombImage;

    public MinesweeperRoom(String name, int id) {
        super(name, id);
        initGame();

        try {
            BufferedImage bg = javax.imageio.ImageIO.read(new java.io.File("./rooms/assets/minesweeper_background.png"));
            this.setBackground(bg);
        } catch (IOException e) {
            System.err.println("Error loading Minesweeper background: " + e.getMessage());
        }

        try {
            bombImage = javax.imageio.ImageIO.read(new java.io.File("./rooms/assets/bomb.png"));
        } catch (IOException e) {
            System.err.println("Error loading bomb image: " + e.getMessage());
            bombImage = null;
        }
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

    @Override
    public void draw(Graphics2D g, int width, int height) {
        super.draw(g, width, height);

        int gridWidth = cols * cellSize;
        int gridHeight = rows * cellSize;
        int startX = (width - gridWidth) / 2;
        int startY = (height - gridHeight) / 2;

        for (int r=0; r<rows; r++) {
            for (int c=0; c<cols; c++) {
                int x = startX + c * cellSize;
                int y = startY + r * cellSize;

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

        int gridWidth = cols * cellSize;
        int gridHeight = rows * cellSize;
        int startX = (1920 - gridWidth) / 2;
        int startY = (1080 - gridHeight) / 2;

        int col = (int) ((x - startX) / cellSize);
        int row = (int) ((y - startY) / cellSize);

        if (!isValidCell(row, col)) return List.of();

        if (flags[row][col] || revealed[row][col]) {
            // Ne rien faire si case déjà révélée ou drapeau posé
            return List.of();
        }

        revealed[row][col] = true;

        if (bombs[row][col]) {
            // Bombe cliquée = game lost
            resultActions.add(new Action.ShowHotbarText("BOOM! Vous avez perdu."));
            resultActions.add(new Action.ChangeRoom(-1)); // Par exemple -1 = fin partie
            // Réinitialiser la grille si tu veux (hors scope ici)
        } else {
            if (adjacentCounts[row][col] == 0) {
                // Révéler récursivement les cases voisines sans bombes adjacentes
                revealNeighbors(row, col);
            }
            resultActions.add(new Action.ShowHotbarText("Case révélée."));
        }

        if (checkWin()) {
            resultActions.add(new Action.ShowHotbarText("Bravo, vous avez gagné !"));
            resultActions.add(new Action.ChangeRoom(-1)); // Fin ou passage suivant
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
        List<Action> actions = new ArrayList<>();

        int gridWidth = cols * cellSize;
        int gridHeight = rows * cellSize;
        int startX = (1920 - gridWidth) / 2;
        int startY = (1080 - gridHeight) / 2;

        int col = (int) ((x - startX) / cellSize);
        int row = (int) ((y - startY) / cellSize);

        if (!isValidCell(row, col)) return List.of();

        if (!revealed[row][col]) {
            flags[row][col] = !flags[row][col];
            actions.add(new Action.ShowHotbarText(flags[row][col] ? "Drapeau posé" : "Drapeau retiré"));
        }

        return actions;
    }

    public void setHover(double x, double y) {
    int gridWidth = cols * cellSize;
    int gridHeight = rows * cellSize;
    int startX = (1920 - gridWidth) / 2;
    int startY = (1080 - gridHeight) / 2;

    int col = (int) ((x - startX) / cellSize);
    int row = (int) ((y - startY) / cellSize);

    if (isValidCell(row, col)) {
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