package entities;

import javax.swing.ImageIcon;

import interfaces.Commons;

public class Ball extends Sprite implements Commons {
	
	private int xdir;
	private int ydir;
	
	public Ball() {
		
		xdir = 1;
		ydir = -1;
		
		ImageIcon ii = new ImageIcon("resources/ball.png");
		image = ii.getImage();
		
		i_width = image.getWidth(null);
		i_heigth = image.getHeight(null);
		
		resetState();
	}	

	public int getYDir() {
		return ydir;
	}

	public void setYDir(int ydir) {
		this.ydir = ydir;
	}

	public void setXDir(int xdir) {
		this.xdir = xdir;
	}

	private void resetState() {
		
		x = INIT_BALL_X;
		y = INIT_BALL_Y;	
	}
	
	public void move() {
		
		x += xdir;
		y += ydir;
		
		if (x == 0)
			setXDir(1);
		
		if (x == WIDTH - i_width)
			setXDir(-1);
		
		if (y == 0)
			setYDir(1);
	}
}
