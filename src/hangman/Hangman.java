package hangman;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import Utils.Constants;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Hangman  {


    public static final Font DEFAULT_FONT = new Font("Courier", 56);

    public static final int POINTS_PER_LETTER = 100;
    public static final float BONUS_MODIFIER = 0.2f;

   
    public SimpleStringProperty word = new SimpleStringProperty();

 
    public SimpleIntegerProperty lettersToGuess = new SimpleIntegerProperty();

    public SimpleIntegerProperty score = new SimpleIntegerProperty();

    public float scoreModifier = 1.0f;

    public SimpleBooleanProperty playable = new SimpleBooleanProperty();

    public ObservableList<Node> letters;

    public HashMap<Character, Button> alphabet = new HashMap<Character, Button>();

    public HangmanImage hangman = new HangmanImage();

 

    
	Oyun oyun ;
	
	public Hangman () throws IOException {
		
		//oyun  = new Oyun(System.getProperty("user.dir") + "\\src\\res\\adamasma.txt");
//		System.out.println("----------------------");
//		System.out.println(oyun.aktuell.getKelime());
//		System.out.println(oyun.tip.toString());
	}

    public Parent createContent() {
        HBox rowLetters = new HBox();
        letters = rowLetters.getChildren();

        
        HBox rowAlphabet = new HBox(2);
        for (char c = 'A'; c <= 'Z'; c++) {
            Button t = new Button(String.valueOf(c));
            t.setFont(DEFAULT_FONT);
            t.setStyle("-fx-fill: #00ffff; -fx-font-size: 24px; -fx-font-weight:bold; -fx-font-family: Tahoma;");
            alphabet.put(c, t);
            rowAlphabet.getChildren().add(t);
        }
        
        playable.bind(hangman.lives.greaterThan(0).and(lettersToGuess.greaterThan(0)));
        playable.addListener((obs, old, newValue) -> {
            if (!newValue.booleanValue()) {
            	if(hangman.lives.intValue()==0) {
            		rowAlphabet.setDisable(true);
            		showAlert("Game Over! Start New Game");
            		score.set(0);
            	}
            	else {
            		rowAlphabet.setDisable(true);
            		showAlert("You Won! Start New Game");
            	}
                stopGame();
            }
        });




        Text textScore = new Text();
        textScore.textProperty().bind(score.asString().concat(" Points"));
        textScore.setFont(new Font("Courier", 32));
        textScore.setFill(Color.WHITE);
        
        
        Label guessText = new Label();
        
        Button btnAgain = new Button("NEW GAME");
        
        btnAgain.setOnAction(event -> startGame(rowAlphabet, guessText));

        HBox rowControls = new HBox(10, btnAgain, textScore);
        rowControls.setAlignment(Pos.CENTER);

        AnchorPane aPane = new AnchorPane();
        ImageView iView = new ImageView(new Image("/images/Tafel.png"));
        iView.setFitHeight(Constants.HEIGHT);
        iView.setFitWidth(Constants.WIDTH);
        aPane.getChildren().add(iView);
        
        
        
        
        
        
        startGame(rowAlphabet, guessText);
        
      
       
       
        aPane.getChildren().add(guessText);
        aPane.getChildren().add(rowLetters);
        aPane.getChildren().add(rowAlphabet);
        aPane.getChildren().add(rowControls);
        aPane.getChildren().add(hangman);
        
        AnchorPane.setTopAnchor(guessText, 50.0);
        AnchorPane.setLeftAnchor(guessText, Constants.WIDTH/2.0 -100);
        
        AnchorPane.setTopAnchor(rowLetters, 300.0);
        AnchorPane.setLeftAnchor(rowLetters, Constants.WIDTH/2.0-150);
        
        AnchorPane.setBottomAnchor(rowAlphabet, 200.0);
        AnchorPane.setLeftAnchor(rowAlphabet, 300.0);
        
        AnchorPane.setBottomAnchor(rowControls, 50.0);
        AnchorPane.setLeftAnchor(rowControls, Constants.WIDTH/2.0-150);
        
        AnchorPane.setTopAnchor(hangman, 300.0);
        AnchorPane.setRightAnchor(hangman, 240.0);
        
       
       
        return aPane;
    }

    public void stopGame() {
        for (Node n : letters) {
            Letter letter = (Letter) n;
            letter.show();
        }
    }

    public void startGame(HBox rowAlphabet, Label guessText)  {
    	
    	
    	rowAlphabet.setDisable(false);
    	
    	try {
			oyun  = new Oyun(System.getProperty("user.dir") + "\\src\\res\\adamasma.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	guessText.setText(oyun.tip.toString());
		System.out.println("----------------------");
		System.out.println(oyun.aktuell.getKelime());
		System.out.println(oyun.tip.toString());
    	
        for (Button t : alphabet.values()) {
            t.setDisable(false);
            //t.setFill(Color.WHITE);
        }

        hangman.reset();
        word.set(oyun.aktuell.getKelime().toUpperCase());////oyun.kelimeListesi.get(new Random().nextInt(oyun.kelimeListesi.size()-1)).toUpperCase());
        
        
        lettersToGuess.set(word.length().get());

        letters.clear();
        for (char c : word.get().toCharArray()) {
            letters.add(new Letter(c));
        }
    }

    public class HangmanImage extends Parent {
        private  final int SPINE_START_X = 100;
        private  final int SPINE_START_Y = 20;
        private  final int SPINE_END_X = SPINE_START_X;
        private  final int SPINE_END_Y = SPINE_START_Y + 50;

        /**
         * How many lives left
         */
        public SimpleIntegerProperty lives = new SimpleIntegerProperty();

        public HangmanImage() {
            Circle head = new Circle(20);
            head.setTranslateX(SPINE_START_X);
            head.setFill(Color.ALICEBLUE);

            Line spine = new Line();
            spine.setStartX(SPINE_START_X);
            spine.setStartY(SPINE_START_Y);
            spine.setEndX(SPINE_END_X);
            spine.setEndY(SPINE_END_Y);
            spine.setFill(Color.ALICEBLUE);
            spine.setStroke(Color.WHITE);
            spine	.setStrokeWidth(6);

            Line leftArm = new Line();
            leftArm.setStartX(SPINE_START_X);
            leftArm.setStartY(SPINE_START_Y);
            leftArm.setEndX(SPINE_START_X + 40);
            leftArm.setEndY(SPINE_START_Y + 10);
            leftArm.setFill(Color.ALICEBLUE);
            leftArm.setStroke(Color.WHITE);
            leftArm.setStrokeWidth(6);


            Line rightArm = new Line();
            rightArm.setStartX(SPINE_START_X);
            rightArm.setStartY(SPINE_START_Y);
            rightArm.setEndX(SPINE_START_X - 40);
            rightArm.setEndY(SPINE_START_Y + 10);
            rightArm.setFill(Color.ALICEBLUE);
            rightArm.setStroke(Color.WHITE);
            rightArm.setStrokeWidth(6);


            Line leftLeg = new Line();
            leftLeg.setStartX(SPINE_END_X);
            leftLeg.setStartY(SPINE_END_Y);
            leftLeg.setEndX(SPINE_END_X + 25);
            leftLeg.setEndY(SPINE_END_Y + 50);
            leftLeg.setFill(Color.ALICEBLUE);
            leftLeg.setStroke(Color.WHITE);
            leftLeg.setStrokeWidth(6);


            Line rightLeg = new Line();
            rightLeg.setStartX(SPINE_END_X);
            rightLeg.setStartY(SPINE_END_Y);
            rightLeg.setEndX(SPINE_END_X - 25);
            rightLeg.setEndY(SPINE_END_Y + 50);
            rightLeg.setFill(Color.ALICEBLUE);
            rightLeg.setStroke(Color.WHITE);
            rightLeg.setStrokeWidth(6);
          

            getChildren().addAll(head, spine, leftArm, rightArm, leftLeg, rightLeg);
            lives.set(getChildren().size());
        }

        public void reset() {
            getChildren().forEach(node -> node.setVisible(false));
            lives.set(getChildren().size());
        }

        public void takeAwayLife() {
            for (Node n : getChildren()) {
                if (!n.isVisible()) {
                    n.setVisible(true);
                    lives.set(lives.get() - 1);
                    break;
                }
            }
        }
    }

    public static class Letter extends StackPane {
        private Rectangle bg = new Rectangle(40, 6);
        private Text text;

        public Letter(char letter) {
            bg.setFill(Color.GRAY);
            bg.setStrokeDashOffset(4);
            bg.setStroke(Color.WHITE);

            text = new Text(String.valueOf(letter).toUpperCase());
            text.setFont(DEFAULT_FONT);
            text.setFill(Color.WHITE);
            text.setVisible(false);

            setAlignment(Pos.BOTTOM_CENTER);
            getChildren().addAll(bg, text);
        }

        public void show() {
            RotateTransition rt = new RotateTransition(Duration.seconds(1), bg);
            rt.setAxis(Rotate.Y_AXIS);
            rt.setToAngle(180);
            rt.setOnFinished(event -> text.setVisible(true));
            rt.play();
        }

        public boolean isEqualTo(char other) {
            return text.getText().equals(String.valueOf(other).toUpperCase());
        }
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
   
}