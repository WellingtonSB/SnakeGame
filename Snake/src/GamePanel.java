import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 900;
	static final int SCREEN_HEIGHT = 650;
	static final int UNIT_SIZE = 50;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
	static final int DELAY = 160;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 1;
	int nivel = 1;
	int applesEaten;
	int appleX;
	int appleY;
	char direction = 'D';
	boolean running = false;
	Timer timer;
	Random random;

	GamePanel() {
		random = new Random(2);
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
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
			/*
			 * for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) { g.drawLine(i*UNIT_SIZE, 0,
			 * i*UNIT_SIZE, SCREEN_HEIGHT); g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH,
			 * i*UNIT_SIZE); }
			 */
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					// g.setColor(new
					// Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.white);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 6,
					g.getFont().getSize());

			g.setColor(Color.green);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metric = getFontMetrics(g.getFont());
			g.drawString("NIVEL: " + nivel, (SCREEN_WIDTH - metric.stringWidth("NIVEL: " + nivel)) / 2,
					g.getFont().getSize());

		} else {
			gameOver(g);
		}

	}

	public void newApple() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

	}

	public void nivels() {

		if (applesEaten == 10) {
			timer.setDelay(timer.getDelay() - 10);
			nivel = nivel + 1;
		}
		if (applesEaten == 20) {
			timer.setDelay(timer.getDelay() - 20);
			nivel++;
		}
		if (applesEaten == 30) {
			timer.setDelay(timer.getDelay() - 30);
			nivel++;
		}
		if (applesEaten == 40) {
			timer.setDelay(timer.getDelay() - 40);
			nivel++;
		}
		if (applesEaten == 50) {
			timer.setDelay(timer.getDelay() - 50);
			nivel++;
		}

	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		switch (direction) {
		case 'W':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'S':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'A':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'D':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}

	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
			nivels();
		}
	}

	public void checkCollisions() {
		// checks if head collides with body
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// check if head touches left border
		if (x[0] < 0) {
			running = false;
		}
		// check if head touches right border
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		// check if head touches top border
		if (y[0] < 0) {
			running = false;
		}
		// check if head touches bottom border
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}

		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		
		//nivel
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Nivel: " + nivel, (SCREEN_WIDTH - metrics1.stringWidth("Nivel:" + nivel))/ 2, SCREEN_HEIGHT / 7);
		
				
		// Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("SCORE: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("SCORE: " + applesEaten)) / 2,
				g.getFont().getSize());
		// Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics3.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'D') {
					direction = 'A';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'A') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'S') {
					direction = 'W';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'W') {
					direction = 'S';
				}
				break;
			}
		}
	}
}
