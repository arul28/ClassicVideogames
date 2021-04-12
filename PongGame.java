package theclassics;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import static javafx.application.Application.launch;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
//importing all the necessary libraries
/**
 *
 * @author arul2
 */
public class PongGame extends TheClassics{
    
    private static final int width = 900;
    private static final int height = 700;
    
    private static final int userPaddleHeight = 80;
    private static final int userPaddleWidth = 10;
    private static final double ball = 20;
    
    private int ballSpeedX = 1;
    private int ballSpeedY = 1;
    
    private double userPositionY = height / 2;
    private double computerPositionY = height / 2;
    private int userPositionX = 0;
    private double computerPositionX = width - userPaddleWidth;
    
    private double ballPositionX = width / 2;
    private double ballPositionY = height / 2;
    
    private int userScore = 0;
    private int computerScore = 0;
    
    private boolean startGame;
    //defining all the necessary variables
    //these variables set the dimension and locations of all the important game elemnets, including the paddle, the screen itself, and the ball
    public void start(Stage PongStage){ //creating the start function
        TheClassics classic = new TheClassics();
        //this allows me to later on call the main menu method
        PongStage.setTitle("PONG"); //title of window
        Canvas background = new Canvas(width, height); //canvas sets the background
        GraphicsContext graphicsContext = background.getGraphicsContext2D(); //this helps draw the game
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e ->run(graphicsContext))); //this defines free form animation, animation for the pong ball
        timeline.setCycleCount(Timeline.INDEFINITE); //the number of cycles in the animation repeat forever
		
	    background.setOnMouseMoved(e ->  userPositionY  = e.getY()); //the paddle follows the mouse
	    background.setOnMouseClicked(e ->  startGame = true); //the game will start when mouse is clicked
        
        Button button = new Button();
        button.setText("Go Back");
        //adding a button to go back to the main menu
	    PongStage.setScene(new Scene(new StackPane(background, button))); //adding canvas and button to the scene
	    PongStage.show();
	    timeline.play();
        StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
        button.setOnAction(event -> {
            classic.start(PongStage);
        });
        //when the button is pressed, the game calls back to the main class, so the scene is overwritten with the info from the main scene (the game goes back to the main menu)
    }
    
    public void run(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.BLACK);
	graphicsContext.fillRect(0, 0, width, height);
	//setting background color and size
	graphicsContext.setFill(Color.WHITE);
	graphicsContext.setFont(Font.font("Arial", 30));
	//setting font color and size
	if(startGame) {
            //setting the balls movement
            ballPositionX+=ballSpeedX;
            ballPositionY+=ballSpeedY;
            //this is the opponent, it is very simple, just follows the ball
            if(ballPositionX < width - width  / 4) {
                computerPositionY = ballPositionY - userPaddleHeight / 2;
            }  
            else {
                computerPositionY =  ballPositionY > computerPositionY + userPaddleHeight / 2 ? computerPositionY += 1: computerPositionY - 1;
            }
	
		    //drawing the ball on the canvas
            graphicsContext.fillOval(ballPositionX, ballPositionY, ball, ball);
        }
        else { //if the game has not started i want the game to display a message which tells the user to click the screen to start the game
            graphicsContext.setStroke(Color.WHITE);
            graphicsContext.setTextAlign(TextAlignment.CENTER);
            graphicsContext.strokeText("Click to begin", width / 2, height / 2);
            //creating and formatting the beginning text
            ballPositionX = width / 2;
            ballPositionY = height / 2;
            //resetting the balls starting position
            
            //setting the balls starting speed and direction (signified by the -1 and 1)
            ballSpeedX = new Random().nextInt(2) == 0 ? 1: -1;
            ballSpeedY = new Random().nextInt(2) == 0 ? 1: -1;
        }
        //making sure the ball stays within the canvas
        if (ballPositionY > height || ballPositionY < 0) {
            ballSpeedY *=-1;
        }
        //the computer gets a point if you miss the ball
        if(ballPositionX < userPositionX - userPaddleWidth) {
            computerScore++;
            startGame = false;
	    }
        //this adds to your score by one if the opponent(computer) misses the ball
	    if(ballPositionX > computerPositionX + userPaddleWidth) {  
            userScore++;
            startGame = false;
	    }
        //this increases the speed of the ball everytime it hits the paddle, it also bounces the ball back
	    if( ((ballPositionX + ball > computerPositionX) && ballPositionY >= computerPositionY && ballPositionY <= computerPositionY + userPaddleHeight) || ((ballPositionX < userPositionX + userPaddleWidth) && ballPositionY >= userPositionY && ballPositionY <= userPositionY + userPaddleHeight)) {
            ballSpeedY += 1 * Math.signum(ballSpeedY);
            ballSpeedX += 1 * Math.signum(ballSpeedX);
            ballSpeedX *= -1;
            ballSpeedY *= -1;
	    }
        //drawing the score onto the canvas
	    graphicsContext.fillText(userScore + "\t\t\t\t\t\t\t\t" + computerScore, width / 2, 100);
	    //drawing the user paddle and the computers paddle onto the canvas
	    graphicsContext.fillRect(computerPositionX, computerPositionY, userPaddleWidth, userPaddleHeight);
	    graphicsContext.fillRect(userPositionX, userPositionY, userPaddleWidth, userPaddleHeight);
    }
    
    public static void main(String[] args) {
	    launch(args);
    }
    //main method which runs the game
    
}
