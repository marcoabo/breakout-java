package entities;

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
	private boolean ingame = true;
	
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
			paddle.keyPressed(e);
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
		
		
		
	}

}
