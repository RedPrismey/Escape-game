package gameState;

import java.awt.*;

public class Countdown {
    private int secondsLeft;
    private long lastUpdate;
    private boolean running;

    public Countdown(int seconds) {
        this.secondsLeft = seconds;
        this.running = false;
    }

    public void start() {
        this.running = true;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void stop() {
        this.running = false;
    }

    public void update() {
        if (!running) return;
        long now = System.currentTimeMillis();
        int elapsed = (int) ((now - lastUpdate) / 1000);
        if (elapsed > 0) {
            secondsLeft = Math.max(0, secondsLeft - elapsed);
            lastUpdate += elapsed * 1000L;
        }
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public void draw(Graphics g, int x, int y, int width, int height) {
        update();
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, height / 2));
        String text = String.format("%02d:%02d", secondsLeft / 60, secondsLeft % 60);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g.drawString(text, x + (width - textWidth) / 2, y + (height + textHeight) / 2 - 4);
    }
}

