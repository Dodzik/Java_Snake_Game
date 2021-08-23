import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.Time;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    //width of game board
    static final int SCREEN_WIDTH = 600;
    //height of game board
    static final int SCREEN_HEIGHT = 600;
    //size of tiles
    static final int UNIT_SIZE = 20;
    //number of tiles
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    //game tickrate
    static final int DELAY = 75;
    //game tiles
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    //number of snake parts
    int bodyParts = 6;
    //Score
    int applesEaten;
    //coordinates of apple
    int appleX;
    int appleY;
    //direction of the snake's movement
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    boolean pressed = false;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.RED);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.yellow);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.setColor(Color.BLACK);
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.green);
            g.fillOval(appleX, appleY, (UNIT_SIZE), (UNIT_SIZE));

            g.setColor(Color.yellow);
            g.setFont(new Font("MS Comic Con", Font.BOLD, 30));
            FontMetrics font = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - font.stringWidth("Score")) / 2, SCREEN_HEIGHT - g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }

    public void checkCollisions() {
        //head collsion with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        //head collsion with left border
        if (x[0] < 0) {
            running = false;
        }

        //head collsion with right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //head collsion with top border
        if (y[0] < 0) {
            running = false;
        }

        //head collsion with bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }


    }

    public void gameOver(Graphics g) {
        g.setColor(Color.yellow);
        g.setFont(new Font("MS Comic Con", Font.BOLD, 75));
        FontMetrics font = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - font.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);

        g.setFont(new Font("MS Comic Con", Font.BOLD, 15));
        FontMetrics font2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - font2.stringWidth("Score: " + applesEaten)) / 2, (int) (1.5 * SCREEN_HEIGHT / 2));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
            pressed = false;
        }
        repaint();


    }


    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (!pressed) {
                switch (e.getKeyCode()) {

                    case KeyEvent.VK_A:
                        if (direction != 'R') {
                            direction = 'L';
                            pressed = true;

                        }
                        break;
                    case KeyEvent.VK_W:
                        if (direction != 'D') {
                            direction = 'U';
                            pressed = true;
                        }
                        break;

                    case KeyEvent.VK_D:
                        if (direction != 'L') {
                            direction = 'R';
                            pressed = true;
                        }
                        break;
                    case KeyEvent.VK_S:
                        if (direction != 'U') {
                            direction = 'D';
                            pressed = true;
                        }
                        break;
                }
            }
        }
    }
}
