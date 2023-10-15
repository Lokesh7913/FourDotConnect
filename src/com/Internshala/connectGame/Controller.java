package com.Internshala.connectGame;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

	private static final int COLUMNS=7;
	private static final int ROWS=6;
	private static final int CIRCLE_DIAMETER=80;
	private static final String disc_Color1= "#FF0000";
	private static final String disc_Color2= "#000000";

	private static String FIRST_PLAYER="First Player";
	private static String SECOND_PLAYER="Second Player";

	private boolean isFirstPlayerTurn=true;
	private boolean isAllowedToInsert = true;

	//2D ARRAy
	Disc[][] insertDiscArray=new Disc[ROWS][COLUMNS];

	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertDiscPane;

	@FXML
	public VBox dashBoardPane;

	@FXML
	public Label playerNameLabel;

	@FXML
	public TextField FirstNameField;

	@FXML
	public TextField SecondNameField;

	@FXML
	public Button SetNameButton;


	public  void setNames(){

	if((FirstNameField.getText().equalsIgnoreCase("")
			|| SecondNameField.getText().equalsIgnoreCase("")) ){

		playerNameLabel.setText(isFirstPlayerTurn? FIRST_PLAYER:SECOND_PLAYER);

	}else {
			FIRST_PLAYER = FirstNameField.getText();
			SECOND_PLAYER = SecondNameField.getText();
			playerNameLabel.setText(isFirstPlayerTurn? FIRST_PLAYER:SECOND_PLAYER);
			System.out.println(FIRST_PLAYER);
			System.out.println(SECOND_PLAYER);

		}
			}

	public void playArea(){
		Shape rectangle=addCircleHole();
		rectangle.setFill(Color.LIGHTSLATEGRAY);
		rootGridPane.add(rectangle,0,1);
		//insertDiscPane.getChildren().add(reactangle);

		//Add ClickableColumn
		List<Rectangle> rectangleList =clickableColumn();
		for (Rectangle rectangleColumn:rectangleList) {
			rootGridPane.add(rectangleColumn,0,1);
		}

	}

	private List<Rectangle> clickableColumn(){
		Rectangle rectangleColumn=new Rectangle();
		List<Rectangle> rectangleList=new ArrayList<>();
		for (int col = 0; col <COLUMNS ; col++) {
			rectangleColumn=new Rectangle(CIRCLE_DIAMETER,(ROWS + 1) * CIRCLE_DIAMETER);
			rectangleColumn.setFill(Color.TRANSPARENT);
			rectangleColumn.setTranslateX(col * (CIRCLE_DIAMETER + 8) + CIRCLE_DIAMETER / 4);
			//Hoover Effect by mouse
			Rectangle finalRectangleColumn = rectangleColumn;
			rectangleColumn.setOnMouseEntered(event -> finalRectangleColumn.setFill(Color.valueOf("#eeeeee26")));
			rectangleColumn.setOnMouseExited(event -> finalRectangleColumn.setFill(Color.TRANSPARENT));

			final int column=col;
			rectangleColumn.setOnMouseClicked(event -> {
				if (isAllowedToInsert){
					isAllowedToInsert = false;
					insertDisc(new Disc(isFirstPlayerTurn),column);
				}
			});
			rectangleList.add(rectangleColumn);

		}
		return rectangleList;
	}

	private void insertDisc(Disc disc,int col){
		int row = ROWS - 1;
		while (row >= 0){
			if (getDiscIfPresent(row , col) == null)
				break;
			row --;
		}
		if (row < 0)
			return;

		insertDiscArray [row][col]=disc;
		insertDiscPane.getChildren().add(disc);

		disc.setTranslateX(col*(CIRCLE_DIAMETER + 8) + CIRCLE_DIAMETER / 4);
		//disc.setTranslateY(row*(CIRCLE_DIAMETER + 8) + CIRCLE_DIAMETER / 4);

		int currentRow=row;

		TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.3),disc);
		translateTransition.setToY(row*(CIRCLE_DIAMETER + 8) + CIRCLE_DIAMETER / 4);
		translateTransition.play();
		translateTransition.setOnFinished(event -> {
			isAllowedToInsert = true;
			//Game End Method
			if ( gameEnded(currentRow ,col)){
				gameOver();
			}
			System.out.println("Hello I am in");
			isFirstPlayerTurn=!isFirstPlayerTurn;
			playerNameLabel.setText(isFirstPlayerTurn? FIRST_PLAYER:SECOND_PLAYER);
		});
	}


	private boolean gameEnded(int currentRow, int col) {

		//Vertical Range
		List<Point2D> verticalPoints = IntStream.rangeClosed(currentRow-3,currentRow+3)
				.mapToObj(x-> new Point2D(x, col)).collect(Collectors.toList());

		//Horizontal Range
		List<Point2D>HorizontallPoints = IntStream.rangeClosed(col-3,col+3)
				.mapToObj(y-> new Point2D(currentRow, y)).collect(Collectors.toList());

		//Diagonal Points
		Point2D StartPoint1 = new Point2D(currentRow-3,col +3);
		List<Point2D> diagonal1 =IntStream.rangeClosed(0,6)
				                 .mapToObj( i -> StartPoint1.add(i,-i))
					                 .collect(Collectors.toList());
		//diagonal 2
		Point2D StartPoint2 = new Point2D(currentRow-3,col -3);
		List<Point2D> diagonal2 =IntStream.rangeClosed(0,6)
				.mapToObj( i -> StartPoint2.add(i , i))
				.collect(Collectors.toList());



		boolean isEnded = checkCombination(verticalPoints) || checkCombination(HorizontallPoints) || checkCombination(diagonal1) || checkCombination(diagonal2);

		return isEnded;

	}

	private boolean checkCombination(List<Point2D> points){
		int chain = 0;
		for (Point2D point : points) {
			int rowIndexForArray = (int) point.getX();
			int columnIndexForArray = (int) point.getY();

			Disc disc =getDiscIfPresent(rowIndexForArray , columnIndexForArray);
			if (disc != null && disc.isFirstPlayerMove == isFirstPlayerTurn) {
				chain++;
				if (chain == 4) {
					return true;
				}
			}else {
				chain=0;
			}
		}
		return false;
	}

	private Disc getDiscIfPresent(int row, int column) {
		if (row >= ROWS || row < 0 || column >= COLUMNS || column < 0)
			return null;
		return insertDiscArray[row][column];
	}

	private void gameOver() {
		String winner = isFirstPlayerTurn? FIRST_PLAYER:SECOND_PLAYER;
		System.out.println("Winner is : "  +winner);

		Alert WinnerAlert=new Alert(Alert.AlertType.INFORMATION);
		WinnerAlert.setTitle("Winner Announcement");
		WinnerAlert.setHeaderText("Winner is : "  +winner+"!");
		WinnerAlert.setContentText("Do you want to play again?");
		//Button's
		ButtonType yesbtn = new ButtonType("Yes");
		ButtonType nobtn = new ButtonType("No");
		WinnerAlert.getButtonTypes().setAll(yesbtn , nobtn);

		Platform.runLater(() ->{
			Optional <ButtonType> btnClicked = WinnerAlert.showAndWait();
			if (btnClicked.isPresent() && btnClicked.get() == yesbtn){
				reStartGame();
			}
			else {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	public void reStartGame() {
		insertDiscPane.getChildren().clear();
		for (int row = 0; row < insertDiscArray.length; row++) {
			for (int col = 0; col < insertDiscArray[row].length; col++) {
				insertDiscArray[row][col] = null;
			}
		}
		FirstNameField.clear();
		SecondNameField.clear();
		FIRST_PLAYER = "First Player";
		SECOND_PLAYER = "Second Player";
		isFirstPlayerTurn=true;
		playerNameLabel.setText(FIRST_PLAYER);
		playArea();
	}


	private class Disc extends Circle{
		private final boolean isFirstPlayerMove;

		public Disc(boolean isFirstPlayerTurn){

			this.isFirstPlayerMove = isFirstPlayerTurn;
			setRadius(CIRCLE_DIAMETER/2);
			setCenterX(CIRCLE_DIAMETER/2);
			setCenterY(CIRCLE_DIAMETER/2);
			setFill(isFirstPlayerTurn? Color.valueOf(disc_Color1):Color.valueOf(disc_Color2));
		}
	}

	private Shape addCircleHole(){
		Shape rectangle=new Rectangle((COLUMNS + 1) * CIRCLE_DIAMETER,(ROWS + 1) * CIRCLE_DIAMETER);
		Circle circle=new Circle();
		for (int row = 0; row < ROWS; row++) {

			for (int col = 0; col <COLUMNS ; col++) {
				circle.setRadius(CIRCLE_DIAMETER / 2);
				circle.setCenterX(CIRCLE_DIAMETER / 2);
				circle.setCenterY(CIRCLE_DIAMETER / 2);
				circle.setSmooth(true);

				circle.setTranslateX(col*(CIRCLE_DIAMETER + 8) + CIRCLE_DIAMETER / 4);
				circle.setTranslateY(row*(CIRCLE_DIAMETER + 8) + CIRCLE_DIAMETER / 4);
				rectangle=Shape.subtract(rectangle,circle);
			}
		}
		return rectangle;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		SetNameButton.setOnAction( event -> {
				setNames();
		});
	}
}
