package tttoe;

import javafx.scene.shape.Rectangle;


/**
 * Cell object that is extending javafx Rectangle
 * which represents every cell of TicTacToe board
 * */
public class Cell extends Rectangle {
	
	/**
	 * Value of Cell initially empty 
	 * */
    private boolean markX = false;
    private boolean markO = false;


    public void take(boolean markX) {
            this.markX = markX;
            this.markO = !markX;
    }

    public boolean hasMarkX() {
        return this.markX;
    }

    public boolean hasMarkO() {
        return this.markO;
    }
}
