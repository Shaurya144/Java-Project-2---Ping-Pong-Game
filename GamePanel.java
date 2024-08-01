import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{

	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH,GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int Padles_WIDTH = 25;
	static final int Padles_HEIGHT = 100;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Padles Padles1;
	Padles Padles2;
	Ball ball;
	Score score;
	
	GamePanel(){
		newPadless();
		newBall();
		score = new Score(GAME_WIDTH,GAME_HEIGHT);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void newBall() {
		random = new Random();
		ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),random.nextInt(GAME_HEIGHT-BALL_DIAMETER),BALL_DIAMETER,BALL_DIAMETER);
	}
	public void newPadless() {
		Padles1 = new Padles(0,(GAME_HEIGHT/2)-(Padles_HEIGHT/2),Padles_WIDTH,Padles_HEIGHT,1);
		Padles2 = new Padles(GAME_WIDTH-Padles_WIDTH,(GAME_HEIGHT/2)-(Padles_HEIGHT/2),Padles_WIDTH,Padles_HEIGHT,2);
	}
	public void paint(Graphics g) {
		image = createImage(getWidth(),getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);
	}
	public void draw(Graphics g) {
		Padles1.draw(g);
		Padles2.draw(g);
		ball.draw(g);
		score.draw(g);
Toolkit.getDefaultToolkit().sync(); // I forgot to add this line of code in the video, it helps with the animation

	}
	public void move() {
		Padles1.move();
		Padles2.move();
		ball.move();
	}
	public void checkCollision() {
		
		//bounce ball off top & bottom window edges
		if(ball.y <=0) {
			ball.setYDirection(-ball.yVelocity);
		}
		if(ball.y >= GAME_HEIGHT-BALL_DIAMETER) {
			ball.setYDirection(-ball.yVelocity);
		}
		//bounce ball off Padles
		if(ball.intersects(Padles1)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //optional for more difficulty
			if(ball.yVelocity>0)
				ball.yVelocity++; //optional for more difficulty
			else
				ball.yVelocity--;
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		if(ball.intersects(Padles2)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++; //optional for more difficulty
			if(ball.yVelocity>0)
				ball.yVelocity++; //optional for more difficulty
			else
				ball.yVelocity--;
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		//stops Paddles at window edges
		if(Padles1.y<=0)
			Padles1.y=0;
		if(Padles1.y >= (GAME_HEIGHT-Padles_HEIGHT))
			Padles1.y = GAME_HEIGHT-Padles_HEIGHT;
		if(Padles2.y<=0)
			Padles2.y=0;
		if(Padles2.y >= (GAME_HEIGHT-Padles_HEIGHT))
			Padles2.y = GAME_HEIGHT-Padles_HEIGHT;
		//give a player 1 point and creates new Padles & ball
		if(ball.x <=0) {
			score.player2++;
			newPadless();
			newBall();
			System.out.println("Player 2: "+score.player2);
		}
		if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
			score.player1++;
			newPadless();
			newBall();
			System.out.println("Player 1: "+score.player1);
		}
	}
	public void run() {
		//game loop
		long lastTime = System.nanoTime();
		double amountOfTicks =60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now -lastTime)/ns;
			lastTime = now;
			if(delta >=1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}
	public class AL extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			Padles1.keyPressed(e);
			Padles2.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			Padles1.keyReleased(e);
			Padles2.keyReleased(e);
		}
	}
}