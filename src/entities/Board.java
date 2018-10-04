package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import interfaces.Commons;

public class Board extends JPanel implements Commons{
	
	private Timer timer;
	private String message = "Game Over";
	private Ball ball;
	private Paddle paddle;
	private Brick[] bricks;
	private boolean inGame = true;
	
	public Board() {
		
		initBoard();
	}

	private void initBoard() {
		
		addKeyListener(new TAdapter());
		setFocusable(true);
		
		bricks = new Brick[N_OF_BRICKS];
		setDoubleBuffered(true);
		timer = new Timer();
		timer.scheduleAtFixedRate(new ScheduleTask(), DELAY, PERIOD);		
	}
	
	private class TAdapter extends KeyAdapter {
		
		@Override
		public void keyReleased(KeyEvent e) {
			paddle.keyReleased(e);
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			paddle.keyPressed(e);
		}
	}
	
	private class ScheduleTask extends TimerTask {
		
		@Override
		public void run() {
			
			ball.move();
			paddle.move();
			checkCollision();
			repaint();
		}
	}

	private void checkCollision() {
		
		// Ball fall to the bottom of the board
		if (ball.getRect().getMaxY() > Commons.BOTTOM_EDGE) {
			stopGame();
		}
		
		// All bricks are destroyed
		for (int i =0, j = 0; i < N_OF_BRICKS; i++) {
			
			if (bricks[i].isDestroyed()) {
				j++;
			}
		
			if (j == N_OF_BRICKS) {
				
				message = "Victory";
				stopGame();
			}
		}
		
		// Ball hits the paddle
		if (ball.getRect().intersects(paddle.getRect())) {
			
			int paddlePos = (int) paddle.getRect().getMinX();
			int ballPos = (int) ball.getRect().getMinX();
			
			int first = paddlePos + 8;
			int second = paddlePos + 16;
			int third = paddlePos + 24;
			int fourth = paddlePos + 32;
			
			if (ballPos < first) {				
				ball.setXDir(-1);
				ball.setYDir(-1);
			}
			
			if (ballPos >= first && ballPos < second) {
				ball.setXDir(-1);
				ball.setYDir(-1*ball.getYDir());
			}
			
			if (ballPos >= second && ballPos < third) {
				ball.setXDir(0);
				ball.setYDir(-1);
			}
			
			if (ballPos >= third && ballPos < fourth) {
				ball.setXDir(1);
				ball.setYDir(-1*ball.getYDir());
			}
			
			if (ballPos >= fourth) {
				ball.setXDir(1);
				ball.setYDir(-1);
			}
		}
		
		// Ball hits a brick
		for (int i = 0; i < N_OF_BRICKS; i++) {
			
			if (ball.getRect().intersects(bricks[i].getRect())) {
								
				int ballLeft = (int) ball.getRect().getMinX();
				int ballHeight = (int) ball.getRect().getHeight();
				int ballWidth = (int) ball.getRect().getWidth();
				int ballTop = (int) ball.getRect().getMinY();
				
				Point pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
				Point pointLeft = new Point(ballLeft - 1, ballTop);
				Point pointTop = new Point(ballLeft, ballTop - 1);
				Point pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);
				
				if (!bricks[i].isDestroyed()) {
					if (bricks[i].getRect().contains(pointRight))
						ball.setXDir(-1);
					else if (bricks[i].getRect().contains(pointLeft))
						ball.setXDir(1);
					
					if (bricks[i].getRect().contains(pointTop))
						ball.setYDir(1);
					else if (bricks[i].getRect().contains(pointBottom))
						ball.setYDir(-1);
					
					bricks[i].setDestroyed(true);
				}
			}
		}
	}

	private void stopGame() {
		
		inGame = false;
		timer.cancel();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		gameInit();
	}

	private void gameInit() {
		
		ball = new Ball();
		paddle = new Paddle();
		
		for (int i = 0, k = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				bricks[k] = new Brick(j*40 + 30, i*10 + 50);
				k++;
			}
		}		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		if (inGame)
			drawObjects(g2d);
		else
			gameFinished(g2d);
		
		Toolkit.getDefaultToolkit().sync();		
	}

	private void drawObjects(Graphics2D g2d) {
		
		g2d.drawImage(ball.getImage(), ball.getX(), ball.getY(), ball.getWidth(), ball.getHeigth(), this);
		
		g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeigth(), this);
		
		for (int i = 0; i < N_OF_BRICKS; i++)
			if (!bricks[i].isDestroyed())
				g2d.drawImage(bricks[i].getImage(), bricks[i].getX(), bricks[i].getY(), bricks[i].getWidth(), bricks[i].getHeigth(), this);	
	}
	
	private void gameFinished(Graphics2D g2d) {
		
		Font font = new Font("Verdana", Font.BOLD, 18);
		FontMetrics metr = this.getFontMetrics(font);
		
		g2d.setColor(Color.BLACK);
		g2d.setFont(font);
		g2d.drawString(message, (Commons.WIDTH - metr.stringWidth(message))/2, Commons.HEIGTH/2);
	}
}
