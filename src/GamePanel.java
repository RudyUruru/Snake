import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;

    private static final int UNITS_MAX_SCREEN_CAPACITY = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    private static  int TIMER_DELAY = 70;
    private static final int SNAKE_START_BODY_SIZE = 6;

    private static final int[] X_SNAKE_BODY_COORDINATES = new int[UNITS_MAX_SCREEN_CAPACITY];
    private static final int[] Y_SNAKE_BODY_COORDINATES = new int[UNITS_MAX_SCREEN_CAPACITY];

    private int snakeSize = SNAKE_START_BODY_SIZE;

    private int feedEaten;

    private int feedLocationX;
    private int feedLocationY;

    private boolean running;
    private char snakeMoveDirection = 'R';

    private Timer timer;
    private Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new GamePanelKeyAdapter());
        startGame();

    }

    public void startGame() {
        spawnFeed();
        running = true;
        timer = new Timer(TIMER_DELAY, this);
        timer.start();
    }

    public void spawnFeed() {
        feedLocationX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        feedLocationY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        if (running) {
            graphics.setColor(Color.RED);
            graphics.fillOval(feedLocationX, feedLocationY, UNIT_SIZE, UNIT_SIZE);
            for (int i = 0; i < snakeSize; i++) {
                if (i == 0) {
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(X_SNAKE_BODY_COORDINATES[0], Y_SNAKE_BODY_COORDINATES[0], UNIT_SIZE, UNIT_SIZE);
                } else
                    graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                graphics.fillRect(X_SNAKE_BODY_COORDINATES[i], Y_SNAKE_BODY_COORDINATES[i], UNIT_SIZE, UNIT_SIZE);
            }
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + feedEaten, (SCREEN_WIDTH - metrics.stringWidth("Score"))/2, 25);
        }
        else {
            gameOver(graphics);
        }
    }

    public void move() {
        for (int i = snakeSize; i > 0; i--) {
            X_SNAKE_BODY_COORDINATES[i] = X_SNAKE_BODY_COORDINATES[i - 1];
            Y_SNAKE_BODY_COORDINATES[i] = Y_SNAKE_BODY_COORDINATES[i - 1];
        }

        switch (snakeMoveDirection) {
            case 'U':
                Y_SNAKE_BODY_COORDINATES[0] -= UNIT_SIZE;
                break;
            case 'D':
                Y_SNAKE_BODY_COORDINATES[0] += UNIT_SIZE;
                break;
            case 'R':
                X_SNAKE_BODY_COORDINATES[0] += UNIT_SIZE;
                break;
            case 'L':
                X_SNAKE_BODY_COORDINATES[0] -= UNIT_SIZE;
        }
    }

    public void gameOver(Graphics graphics) {

        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics gameOverMetrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over!", (SCREEN_WIDTH - gameOverMetrics.stringWidth("Game Over!"))/2, SCREEN_HEIGHT/2);
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 55));
        FontMetrics scoreMetrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + feedEaten, (SCREEN_WIDTH - scoreMetrics.stringWidth("Score"))/2, SCREEN_HEIGHT/2 + scoreMetrics.getFont().getSize() + UNIT_SIZE);

    }

    public void eatFeed() {
        if (X_SNAKE_BODY_COORDINATES[0] == feedLocationX && Y_SNAKE_BODY_COORDINATES[0] == feedLocationY) {
            snakeSize++;
            feedEaten++;
            TIMER_DELAY--;
            spawnFeed();
        }
    }

    public void checkCollisions() {
        for (int i = snakeSize; i > 0; i--) {
            if (X_SNAKE_BODY_COORDINATES[0] == X_SNAKE_BODY_COORDINATES[i] && Y_SNAKE_BODY_COORDINATES[0] == Y_SNAKE_BODY_COORDINATES[i]) {
                running = false;
            }
        }
        if (X_SNAKE_BODY_COORDINATES[0] < 0 || X_SNAKE_BODY_COORDINATES[0] > SCREEN_WIDTH) {
            running = false;
        }
        if (Y_SNAKE_BODY_COORDINATES[0] < 0 || Y_SNAKE_BODY_COORDINATES[0] > SCREEN_HEIGHT) {
            running = false;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            eatFeed();
            checkCollisions();
        }
        repaint();
    }

    private class GamePanelKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (snakeMoveDirection != 'D') {
                        snakeMoveDirection = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (snakeMoveDirection != 'U') {
                        snakeMoveDirection = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (snakeMoveDirection != 'R') {
                        snakeMoveDirection = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (snakeMoveDirection != 'L') {
                        snakeMoveDirection = 'R';
                    }
                    break;
            }

        }
    }
}
