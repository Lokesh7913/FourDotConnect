package com.Internshala.connectGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MyMain extends Application {

	Controller controller = new Controller();


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("app_layout.fxml"));
		GridPane rootGridPane = loader.load();


		controller = loader.getController();
		controller.playArea();

		MenuBar menuBar = createMenu();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

		Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
		menuPane.getChildren().addAll(menuBar);


		Scene scene = new Scene(rootGridPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect4 game");
		primaryStage.setResizable(false);
		primaryStage.show();

	}

	private MenuBar createMenu() {

		//File menu
		Menu fileMenu = new Menu("File");
		//File menu item- New
		MenuItem newGame = new MenuItem("New Game");
		//New Item Event
		newGame.setOnAction(event -> {
			controller.reStartGame();
		});
		//File menu item- Restart
		MenuItem restartGame = new MenuItem("Restart Game");
		//Restart Item Event
		restartGame.setOnAction(event -> {
			controller.reStartGame();
		});
		//Separator line
		SeparatorMenuItem separator = new SeparatorMenuItem();
		//File menu item- Exit
		MenuItem exitGame = new MenuItem("Exit Game");
		//Exit Item Event
		exitGame.setOnAction(event -> {
			closeApp();
		});
		//Add Items to File Menu
		fileMenu.getItems().addAll(newGame, restartGame, separator, exitGame);

		//Help Menu
		Menu helpMenu = new Menu("Help");
		//Help menu item- About Game
		MenuItem aboutGame = new MenuItem("About Game");
		//About Game Event
		aboutGame.setOnAction(event -> {
			gameDescribtion();
		});
		//Separator line
		SeparatorMenuItem separator1 = new SeparatorMenuItem();
		//Help menu item- About Developer
		MenuItem aboutMe = new MenuItem("About Me");
		//About Me Information
		aboutMe.setOnAction(event -> {
			aboutDev();
		});
		//Add Items to Help Menu
		helpMenu.getItems().addAll(aboutGame, separator1, aboutMe);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, helpMenu);
		return menuBar;
	}

	private void aboutDev() {
		Alert aboutme = new Alert(Alert.AlertType.INFORMATION);
		aboutme.setTitle("About Developer");
		aboutme.setHeaderText("Lokesh Chand");
		aboutme.setContentText("Hello Everyone,Its my First Desktop Application 'Connect4 game'.I hope you enjoying it as like me." +
				" I love this game to play at my Free time with my nearest & dearest." +
				"Its very Interesting game to play with Friends and Family," +
				'\n' + "Thanks & Regards ");
		aboutme.show();
	}

	private void gameDescribtion() {
		Alert gameInfo = new Alert(Alert.AlertType.INFORMATION);
		gameInfo.setTitle("About Connect4");
		gameInfo.setHeaderText("How to play");
		gameInfo.setContentText("Connect Four is a two-player connection game in " +
				"which the players first choose a color and then take turns dropping" +
				" colored discs from the top into a seven-column, six-row vertically " +
				"suspended grid. The pieces fall straight down, occupying the next " +
				"available space within the column. The objective of the game is to" +
				" be the first to form a horizontal, vertical, or diagonal line of four" +
				" of one's own discs. Connect Four is a solved game. The first player can" +
				" always win by playing the right moves.");
		gameInfo.show();
	}

	private void closeApp() {
		Platform.exit();
		System.exit(0);
	}

	private void restart() {
		controller.reStartGame();
	}

}
