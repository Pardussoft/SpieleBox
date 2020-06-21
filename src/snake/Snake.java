package snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Utils.Constants;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Snake {
	/**
	 * Speed of the snake
	 */
	int speed = 5;
	/**
	 * color of the food
	 */
	int foodcolor = 0;
	/**
	 * Number of blocks on horizontal and vertical direction
	 */
	int width = 30;
	int height = 30;
	/**
	 * Position of the current food
	 */
	int foodX = 0;
	int foodY = 0;
	/**
	 * size of every blocks in snake body
	 */
	int blockSize = 30;
	
	/**
	 * ArrayList that holds snake blocks
	 */
	List<Block> snake = new ArrayList<>();
	
	/**
	 * Dir object to check moving direction of the snake
	 */
	Dir direction = Dir.left;
	
	/**
	 * flag to set and check game over status
	 */
	boolean gameOver = false;
	
	/**
	 * Random object to create random numbers
	 */
	Random rand = new Random();
	
	/**
	 * The score text which is delivered as a copy of the Text object from main
	 */
	Text textScore;
	
	/**
	 * Animation timer object which is static due to clear optimization
	 */
	static AnimationTimer at;

	
	/**
	 * Direction enum
	 */
	public enum Dir {
		left, right, up, down
	}

	/**
	 * Block class that has the positions of the block as parameters
	 */
	public class Block {
		int x;
		int y;

		public Block(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}

	/**
	 * building game area which is VBox object and adding canvas on it
	 */
	public VBox build(Scene scene, Text textScore) {
		this.textScore = textScore;
		
		//creating new food on start
		newFood();

		VBox root = new VBox(10);
		
		//Creating canvas which has the size of the game area
		Canvas c = new Canvas(width * blockSize, height * blockSize);
		GraphicsContext gc = c.getGraphicsContext2D();
		
		
		root.getChildren().add(c);
		
		//controlling if the game is restarted or played before and stoping background animation timer
		if(at!=null )
			at.stop();
		
		//Initializing animation timer
		at = new AnimationTimer() {
			long lastTick = 0;

			public void handle(long now) {
				if (lastTick == 0) {
					lastTick = now;
					tick(gc);
					return;
				}

				if (now - lastTick > 1000000000 / speed) {
					lastTick = now;
					tick(gc);
				}
			}

		};
		at.start();

		// Adding control commands to the scene
		scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
			if (key.getCode() == KeyCode.UP) {
				direction = Dir.up;
			}
			if (key.getCode() == KeyCode.LEFT) {
				direction = Dir.left;
			}
			if (key.getCode() == KeyCode.DOWN) {
				direction = Dir.down;
			}
			if (key.getCode() == KeyCode.RIGHT) {
				direction = Dir.right;
			}

		});

		// Create snake which has a length of three
		snake.add(new Block(width / 2, height / 2));
		snake.add(new Block(width / 2, height / 2));
		snake.add(new Block(width / 2, height / 2));

		root.setTranslateX(Constants.WIDTH / 2 - width * blockSize / 2);

		return root;
	}

	/**
	 * The function that is calling on every tick of the AnimationTimer
	 */
	public void tick(GraphicsContext gc) {

		textScore.setText(String.valueOf(speed - 6).concat(" Points"));
		// If game is over draw "Game Over" text on the center of the game area
		if (gameOver) {
			gc.setFill(Color.RED);
			gc.setFont(new Font("", 48));
			gc.fillText("GAME OVER", width * blockSize / 2-100, 300);
			return;
		}

		//Move snake blocks one by one
		//Move every next block to the place of the block before that block
		for (int i = snake.size() - 1; i >= 1; i--) {
			snake.get(i).x = snake.get(i - 1).x;
			snake.get(i).y = snake.get(i - 1).y;
		}

		
		//Control direction value and update head position acordingly
		switch (direction) {
			case up:
				snake.get(0).y--;
				//Control if there is collison with border
				if (snake.get(0).y < 0) {
					gameOver = true;
				}
				break;
			case down:
				snake.get(0).y++;
				//Control if there is collison with border
				if (snake.get(0).y > height) {
					gameOver = true;
				}
				break;
			case left:
				snake.get(0).x--;
				//Control if there is collison with border
				if (snake.get(0).x < 0) {
					gameOver = true;
				}
				break;
			case right:
				snake.get(0).x++;
				//Control if there is collison with border
				if (snake.get(0).x > width) {
					gameOver = true;
				}
				break;

		}

		// Eat food if the head of the snake is at over the food
		if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
			snake.add(new Block(-1, -1));
			newFood();
		}

		// Game over on collision by itself
		for (int i = 1; i < snake.size(); i++) {
			if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
				gameOver = true;
			}
		}

		// Drawing background and border of the game area
		gc.clearRect(0, 0, width * blockSize, height * blockSize);
		gc.setFill(Color.rgb(255, 255, 255, 0.3));
		gc.setStroke(Color.WHITE);
		gc.setLineWidth(20.0);
		gc.strokeRect(0, 0, width * blockSize, height * blockSize);
		gc.fillRect(0, 0, width * blockSize, height * blockSize);

		
		


		// random foodcolor
		Color cc = Color.WHITE;

		//Control color code and assign color to the food object
		switch (foodcolor) {
			case 0:
				cc = Color.PURPLE;
				break;
			case 1:
				cc = Color.LIGHTBLUE;
				break;
			case 2:
				cc = Color.YELLOW;
				break;
			case 3:
				cc = Color.PINK;
				break;
			case 4:
				cc = Color.ORANGE;
				break;
		}  
		gc.setFill(cc);
		gc.fillOval(foodX * blockSize, foodY * blockSize, blockSize, blockSize);

		// Drawing snake on canvas
		for (Block c : snake) {
			gc.setFill(Color.GREEN);
			gc.fillRect(c.x * blockSize, c.y * blockSize, blockSize - 2, blockSize - 2);

		}

	}

	/**
	 * Creting new food on random place in scene
	 * if created random place is on snake try to create new place
	 * if creating is successful increase speed by 1 and create random color
	 */
	public void newFood() {
		start: while (true) {
			foodX = rand.nextInt(width-20)+10;
			foodY = rand.nextInt(height-20)+10;

			for (Block c : snake) {
				if (c.x == foodX && c.y == foodY) {
					continue start;
				}
			}
			foodcolor = rand.nextInt(5);
			speed++;
			break;

		}
	}
}
