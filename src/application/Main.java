package application;

import javax.xml.datatype.Duration;

import Utils.Constants;
import hangman.Hangman;
import hangman.Hangman.Letter;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import snake.Snake;

//import javafx.util.Duration;
import static java.lang.Math.random;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;

import tttoe.TicTacToe;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class Main extends Application {

	/**
	 * 
	 * 
	 * */
	Hangman hangman = null;

	/**
	 * Left menu buttons
	 * 
	 */
	Button btnSettings, btnHome, btnHangman, btnTTToe, btnExit, btnSnake;

	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(Main.class.getResource("layout.fxml"));

			primaryStage.setMaximized(true);
			Screen screen = Screen.getPrimary();
			Rectangle2D bounds = screen.getVisualBounds();

			Constants.WIDTH = (int) bounds.getWidth() - 240;
			Constants.HEIGHT = (int) bounds.getHeight() - 40;

			primaryStage.setX(bounds.getMinX());
			primaryStage.setY(bounds.getMinY());
			primaryStage.setWidth(bounds.getWidth());
			primaryStage.setHeight(bounds.getHeight());

			Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Spiele Box");
			primaryStage.show();

			Pane gameAreaPane = (Pane) scene.lookup("#gameAreaPane");

			scene.lookup("#grdMenu").setStyle("-fx-effect: dropshadow(three-pass-box, cyan, 50, 0.2, 0, 0);");
			gameAreaPane.setStyle(
					"-fx-background-color: black; -fx-background-image: url(\"/images/RetroGames.jpg\"); -fx-background-repeat: no-repeat; -fx-background-position: center center; -fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);");

			scene.lookup("#grdMenu").setFocusTraversable(false);

			btnHome = (Button) scene.lookup("#btnHome");
			btnHome.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					activateMenuFocus(true);

					gameAreaPane.getChildren().clear();
					gameAreaPane.setStyle(
							"-fx-background-color: black; -fx-background-image: url(\"/images/RetroGames.jpg\"); -fx-background-repeat: no-repeat; -fx-background-position: center center; -fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);");
				}
			});

			btnExit = (Button) scene.lookup("#btnExit");
			btnExit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					System.exit(0);
				}
			});

			btnHangman = (Button) scene.lookup("#btnHangman");
			btnHangman.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					activateMenuFocus(true);
					
					gameAreaPane.getChildren().clear();

					gameAreaPane.setStyle(
							"-fx-background-color: black; -fx-background-image: url(\"/images/wall.png\"); -fx-background-repeat: no-repeat; -fx-background-position: center center; -fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);");

					try {
						hangman = new Hangman();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					gameAreaPane.getChildren().add(hangman.createContent());

					for (Button b : hangman.alphabet.values()) {
						b.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								
								if(hangman.hangman.lives.intValue()!=0) {

									// mark the letter 'used'
									b.setDisable(true);
	
									boolean found = false;
	
									for (Node n : hangman.letters) {
										Letter letter = (Letter) n;
										if (letter.isEqualTo(b.getText().toCharArray()[0])) {
											found = true;
											hangman.score.set(hangman.score.get()
													+ (int) (hangman.scoreModifier * hangman.POINTS_PER_LETTER));
											hangman.lettersToGuess.set(hangman.lettersToGuess.get() - 1);
											letter.show();
										}
									}
	
									if (!found) {
										hangman.hangman.takeAwayLife();
										hangman.scoreModifier = 1.0f;
										
//										if(hangman.hangman.lives.intValue()==0)
//											showAlert("Game Over! Start New Game");
											
									} else {
										hangman.scoreModifier += hangman.BONUS_MODIFIER;
									}
									
									
								}else {
									//showAlert("Game Over! Start New Game");
								}
							}
						});
					}
				}
			});

			btnSnake = (Button) scene.lookup("#btnSnake");
			btnSnake.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {

					activateMenuFocus(false);
					
					gameAreaPane.getChildren().clear();

					gameAreaPane.setStyle(
							"-fx-background-color: black; -fx-background-image: url(\"/images/wall.png\"); -fx-background-repeat: no-repeat; -fx-background-position: center center; -fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);");

					Text textScore = new Text();
					textScore.setFont(new Font("Courier", 32));
					textScore.setFill(Color.WHITE);

					Button restart = new Button("NEW GAME");

					HBox rowControls = new HBox(10, restart, textScore);
					rowControls.setAlignment(Pos.CENTER);
					restart.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {

							gameAreaPane.getChildren().clear();
							Snake snake = new Snake();
							textScore.setText("0 Points");
							VBox vb = snake.build(scene, textScore);
							vb.getChildren().add(rowControls);
							gameAreaPane.getChildren().add(vb);
						}
					});
					Snake snake = new Snake();
					VBox vb = snake.build(scene, textScore);
					vb.getChildren().add(rowControls);
					gameAreaPane.getChildren().add(vb);

				}
			});

			btnTTToe = (Button) scene.lookup("#btnTTToe");
			btnTTToe.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					gameAreaPane.getChildren().clear();
					activateMenuFocus(true);

					gameAreaPane.setStyle(
							"-fx-background-color: black; -fx-background-image: url(\"/images/wall.png\"); -fx-background-repeat: no-repeat; -fx-background-position: center center; -fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);");
					Label playerNumber = new Label();
					playerNumber.setText("Player 0");

					TicTacToe ticTac = new TicTacToe(playerNumber);

					BorderPane border = new BorderPane();
					border.setMinSize(scene.getWidth() - 240, scene.getHeight());

					HBox control = new HBox();
					control.setMinSize(scene.getWidth() - 240, scene.getHeight());
					control.setSpacing(10.0);
					control.setAlignment(Pos.BASELINE_CENTER);
					Button restart = new Button("NEW GAME");
					restart.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							border.getChildren().clear();
							border.setCenter(ticTac.buildGrid());
							border.setBottom(control);
						}
					});
					control.getChildren().addAll(restart);
					control.getChildren().addAll(playerNumber);
					border.setBottom(control);
					border.setCenter(ticTac.buildGrid());

					gameAreaPane.getChildren().add(border);

				}
			});

			btnSettings = (Button) scene.lookup("#btnSettings");

			Media media = new Media(new File("C:\\Users\\kerem\\OneDrive\\Desktop\\Java Projelerim\\Hauptfenster\\src\\res\\background.mp3").toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setAutoPlay(true);
			mediaPlayer.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mediaPlayer.setAutoPlay(true);
				}
			});

			btnSettings = (Button) scene.lookup("#btnSettings");
			btnSettings.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					gameAreaPane.getChildren().clear();

					ToggleButton musicButton = new ToggleButton();
					musicButton.setText("Music");
					musicButton.setMinSize(200, 100);
					musicButton.setOnAction(evt -> {
						if (mediaPlayer.getStatus() == Status.PLAYING)
							mediaPlayer.pause();
						else
							mediaPlayer.play();
					});

					ToggleButton soundButton = new ToggleButton();
					soundButton.setText("Sound");
					soundButton.setMinSize(200, 100);
					soundButton.setOnAction(evt -> {

					});

					VBox settings = new VBox(10);
					settings.getChildren().add(musicButton);
					settings.getChildren().add(soundButton);
					settings.setTranslateY(200);
					settings.setTranslateX(Constants.WIDTH / 2 - 100);
					settings.setStyle(
							"-fx-padding: 20 20 20 20; -fx-background-radius: 8;  -fx-background-color: black; -fx-effect: dropshadow(three-pass-box, cyan, 50, 0.2, 0, 0);");

					if (mediaPlayer.getStatus() == Status.PLAYING)
						musicButton.setSelected(true);

					gameAreaPane.getChildren().add(settings);
				}
			});

			btnHome.requestFocus();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 */
	private void activateMenuFocus(boolean isActivate) {
		if (isActivate) {
			btnHome.setFocusTraversable(true);
			btnHangman.setFocusTraversable(true);
			btnSnake.setFocusTraversable(true);
			btnTTToe.setFocusTraversable(true);
			btnSettings.setFocusTraversable(true);
			btnExit.setFocusTraversable(true);
		} else {
			btnHome.setFocusTraversable(false);
			btnHangman.setFocusTraversable(false);
			btnSnake.setFocusTraversable(false);
			btnTTToe.setFocusTraversable(false);
			btnSettings.setFocusTraversable(false);
			btnExit.setFocusTraversable(false);
		}
	}
	
	
	


	public static void main(String[] args) {
		launch(args);
	}
}
