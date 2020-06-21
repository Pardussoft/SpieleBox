package tttoe;

import Utils.Constants;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class TicTacToe {
	
		/**
		 * Label to display player name
		 */
		private Label playerN;
		
		/**
		 * Size of grid on horizontal and vertical  direction
		 * 3 cells on every direction
		 */
	    private final int size = 3;
	    /**
		 * Width and height of every cell
		 */
	    private int cellSize = 300;
	    /**
		 * 2D matrix of cell objects. It is 3x3 matrix holds every cell
		 */
	    private final Cell[][] cells = new Cell[size][size];
	    
	    private boolean isFinished = false;

	    
	    public TicTacToe(Label player){
	    	playerN = player;
	    }
	    
	    
	    
	    private Cell buildRectangle(int x, int y, int size) {
	    	Cell rect = new Cell();
	        rect.setX(x * cellSize);
	        rect.setY(y * cellSize);
	        rect.setHeight(cellSize);
	        rect.setWidth(cellSize);
	        rect.setFill(Color.rgb(255, 255, 255, 0.3));
	        rect.setStroke(Color.WHITE);
	        rect.setStrokeWidth(10);
	        return rect;
	    }


		/**
		 * Function to show messages as a dialog
		 */
	    private void showAlert(String message) {
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle("");
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }

	   

		/**
		 * Function to check is there is winner 
		 */
	    private void checkWinner() {
	        if (this.isWinnerX()) {
	            this.showAlert("X is Winner! Start new game");
	            isFinished = true;
	        } else if (this.isWinnerO()) {
	            this.showAlert("O is winnerrr! Start new game");
	            isFinished = true;
	        }
	    }

	    /**
		 * Function to build mark O as a circle object
		 */
	    private Group buildMarkO(double x, double y, int size) {
	        Group group = new Group();
	        int radius = size / 2;
	        Circle circle = new Circle(x + radius, y + radius, radius - 10);
	        circle.setStroke(Color.WHITE);
	        circle.setStrokeWidth(4);
	        circle.setFill(Color.TRANSPARENT);
	        group.getChildren().add(circle);
	        return group;
	    }

	    /**
		 * Function to build mark X as a combination of the lines
		 */
	    private Group buildMarkX(double x, double y, int size) {
	        Group group = new Group();
	        
	        Line line1 = new Line(
                 x + 10, y  + 10,
                 x + size - 10, y + size - 10
         );
         Line line2 = new Line(
                 x + size - 10, y + 10,
                 x + 10, y + size - 10
         );
         
         line1.setStroke(Color.WHITE);
         line1.setStrokeWidth(4);
         line1.setFill(Color.TRANSPARENT);
         
         line2.setStroke(Color.WHITE);
         line2.setStrokeWidth(4);
         line2.setFill(Color.TRANSPARENT);
         
	        group.getChildren().addAll(
	        		line1,line2
	        );
	        return group;
	    }
	    
	    
	    public int player = 0;
	    
	    /**
		 * Adding mouse events to the scene to check clicks on every cell and create X or O marks accordingly
		 */
	    private EventHandler<MouseEvent> buildMouseEvent(Group panel) {
	        return event -> {
	        	Cell rect = (Cell) event.getTarget();
	           
	        	
	                if (event.getButton() == MouseButton.PRIMARY) {
	                	if(!isFinished) {
		                	if(player == 0) {
		                		if(!rect.hasMarkO() && !rect.hasMarkX()) {
				                    rect.take(true);
				                    panel.getChildren().add(
				                            this.buildMarkX(rect.getX(), rect.getY(), Constants.HEIGHT/3-20)
				                    );
				                    player = 1;
				                    playerN.setText("Player O");
		                		}
		                	}else {
		                		if(!rect.hasMarkO() && !rect.hasMarkX()) {
			                		rect.take(false);
				                    panel.getChildren().add(
				                            this.buildMarkO(rect.getX(), rect.getY(), Constants.HEIGHT/3-20)
				                    );
				                    player = 0;
				                    playerN.setText("Player X");
		                		}
		                	}
	                	}
	                }
	                this.checkWinner();
	             
	            
	        };
	    }

	    
	    /**
  		 * Building game area as a group and adding 9 cells to the specific locations
  		 * adding created cell to cells array 
  		 */
	    public Group buildGrid() {
	    	player = 0;
	        Group panel = new Group();
	        for (int y = 0; y != this.size; y++) {
	            for (int x = 0; x != this.size; x++) {
	            	Cell rect = this.buildRectangle(x, y, 300);
	                this.cells[y][x] = rect;
	                panel.getChildren().add(rect);
	                rect.setOnMouseClicked(this.buildMouseEvent(panel));
	            }
	        }
	        return panel;
	    }
	    

	    
	    
	    /**
		 * Method to check if there is horizontal, vertical or diagonal three consecutive X values 
		 * */
	    public boolean isWinnerX() {
	    	
	    	return ((cells[0][0].hasMarkX()&&cells[1][1].hasMarkX() && cells[2][2].hasMarkX()) ||
	    			(cells[0][0].hasMarkX()&&cells[0][1].hasMarkX() && cells[0][2].hasMarkX()) ||
	    			(cells[0][0].hasMarkX()&&cells[1][0].hasMarkX() && cells[2][0].hasMarkX()) ||
	    			(cells[2][0].hasMarkX()&&cells[2][1].hasMarkX() && cells[2][2].hasMarkX()) ||
	    			(cells[1][0].hasMarkX()&&cells[1][1].hasMarkX() && cells[1][2].hasMarkX()) ||
	    			(cells[0][1].hasMarkX()&&cells[1][1].hasMarkX() && cells[2][1].hasMarkX()) ||
	    			(cells[0][2].hasMarkX()&&cells[1][2].hasMarkX() && cells[2][2].hasMarkX()) );
	      
	    }
	    
	    
	    /**
	   	 * Method to check if there is horizontal, vertical or diagonal three consecutive O values 
	   	 * */
	    public boolean isWinnerO() {
	    	return ((cells[0][0].hasMarkO()&&cells[1][1].hasMarkO() && cells[2][2].hasMarkO()) ||
	    			(cells[0][0].hasMarkO()&&cells[0][1].hasMarkO() && cells[0][2].hasMarkO()) ||
	    			(cells[0][0].hasMarkO()&&cells[1][0].hasMarkO() && cells[2][0].hasMarkO()) ||
	    			(cells[2][0].hasMarkO()&&cells[2][1].hasMarkO() && cells[2][2].hasMarkO()) ||
	    			(cells[1][0].hasMarkO()&&cells[1][1].hasMarkO() && cells[1][2].hasMarkO()) ||
	    			(cells[0][1].hasMarkO()&&cells[1][1].hasMarkO() && cells[2][1].hasMarkO()) ||
	    			(cells[0][2].hasMarkO()&&cells[1][2].hasMarkO() && cells[2][2].hasMarkO()) );
	    }

}
