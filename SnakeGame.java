package theclassics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
//importing all necessary libraries
/**
 *
 * @author arul2
 */
public class SnakeGame extends TheClassics{
    
    static int snakeSpeed = 5;
    static int width = 20;
    static int height = 20;
    static int appleX = 0;
    static int appleY = 0;
    static int corner = 25;
    static List<Corner> snake = new ArrayList<>(); //these are the squares within the snake, they are in an array list
    static Dir direction = Dir.left;
    static boolean gameOver = false; //status of game
    static Random rand = new Random(); //random generator for the apples
    //defining all the necessary variables, including the apples location, the speed of the snake, and other important factors
    public enum Dir {
        left, right, up, down
    } //this enum has the different directions the snake could go

    public static class Corner {
        int x;
        int y;
        public Corner(int x, int y) {
        this.x = x;
        this.y = y;
        }
    }

    public void start(Stage primaryStage) {
        gameOver = false; //when the game starts, the game is not over
        TheClassics classic = new TheClassics(); //this allows me to call back to the main menu when needed
        try {
            newFood(); //calling newfood method to spawn apples
            VBox vbox = new VBox(); //creating a vbox for the elements
            Canvas canvas = new Canvas(width * corner, height * corner); //a canvas for the background
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D(); //drawing the snake
            vbox.getChildren().add(canvas); //adding the canvas to the vbox

            Button button = new Button();
            button.setText("Go Back");
            //creating a button to go back to the main menu
            vbox.getChildren().add(button);
            vbox.setAlignment(Pos.CENTER);


            new AnimationTimer() { //creating animation timer which will give the animation effect to my snake
                long lastTick = 0;

            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    animation(graphicsContext, primaryStage);
                    return;
                }

                if (now - lastTick > 1000000000 / snakeSpeed) { //using a large number to get more frames, this will make the snake look smoother when moving
                    lastTick = now;
                    animation(graphicsContext, primaryStage);
                }
            }

            }.start();

            Scene scene = new Scene(vbox, width * corner, height * corner + 25);

                          
            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> { //controlling the snake with your keyboard
                if (key.getCode() == KeyCode.W) {
                    direction = Dir.up;
                } //if W is pressed, the snake will move up
                if (key.getCode() == KeyCode.A) {
                    direction = Dir.left;
                } //if A is pressed, the snake will move left
                if (key.getCode() == KeyCode.S) {
                    direction = Dir.down;
                } //if S is pressed, the snake will move down
                if (key.getCode() == KeyCode.D) {
                    direction = Dir.right;
                } //if D is pressed, the snake will move right

            });

            snake.add(new Corner(width / 2, height / 2));
            snake.add(new Corner(width / 2, height / 2));
            snake.add(new Corner(width / 2, height / 2));
            //at the beginning, the snake will be just 3 corners (3 squares on the scene)
            primaryStage.setScene(scene);
            primaryStage.setTitle("SNAKE GAME");
            primaryStage.show();

            button.setOnAction(event -> {
                classic.start(primaryStage);
            }); //when the button is clicked, the screen will return to the main menu
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void animation(GraphicsContext graphicsContext, Stage primaryStage) {
        TheClassics classic = new TheClassics();
        boolean alive = true;
        if (gameOver && alive == true) { //if the game ends, the code displays a game over message
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.setFont(new Font("", 50));
            graphicsContext.fillText("GAME OVER", 100, 250);
            //defining the color, size, and placement of game over message
            alive = false;
            return;
        }

        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        switch (direction) { //this switch function moves the snake in the specified direction
            case up:
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
            break;
            case down:
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
            break;
            case left:
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
            break;
            case right:
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
            break;
        } //for each case, the code checks to see if the snake is outside the border of the scene, if it is then the game ends

        if (appleX == snake.get(0).x && appleY == snake.get(0).y) { //checks to see if the snake eats an apple
            snake.add(new Corner(-1, -1)); //if the snake eats an apple, the snake grows by adding a new corner to it
            newFood();//spawns a new apple when the old one is eaten
        }

        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) { //checks to see if the snake hits itself (if you turn into the snake's own body)
                gameOver = true; //the game ends if this is the case
            }
        }

        graphicsContext.setFill(Color.GREEN); //the background is green
        graphicsContext.fillRect(0, 0, width * corner, height * corner); //defining dimensions of the background using the variables i initialized earlier

                    
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font("", 30));
        graphicsContext.fillText("SCORE -- " + (snakeSpeed - 6), 10, 30);
        //defining the font size and location of the score tracker

                 
        Color appleColor = Color.RED;//defining the color of the apple

        graphicsContext.setFill(appleColor);
        graphicsContext.fillOval(appleX * corner, appleY * corner, corner, corner);
        //drawing the apple

                    
        for (Corner canvas : snake) { //drawing the snake onto the scene
            graphicsContext.setFill(Color.ORANGE);
            graphicsContext.fillRect(canvas.x * corner, canvas.y * corner, corner - 2, corner - 2);
        }
        //defining the color and location of the snake when the game starts
    }

    public static void newFood() {//spawns new food and makes sure it is not on top of the snakes current location
        start: while (true) {
        appleX = rand.nextInt(width);
        appleY = rand.nextInt(height);
        //this randomly spawn an apple somewhere on the field
        for (Corner canvas : snake) {
            if (canvas.x == appleX && canvas.y == appleY) {
                continue start;
                }
        }
        snakeSpeed++; //increasing the speed of the snake every time if gets an apple
        break;

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    //main method which was originally used when I was developing all three games seperately, no use for it now so it is blank

}
