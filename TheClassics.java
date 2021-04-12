package theclassics;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
//importing all necessary libraries
/**
 *
 * @author arul2
 */
//this is the main driver class, the main menu
//the user is presented with three buttons, each leading to one of the classic games
public class TheClassics extends Application {
    
    private boolean startGame;
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Pong");
        btn.setMaxWidth(200);
        btn.setMaxHeight(400);
        //creating a button which will lead to the Pong Game
        Button btn2 = new Button();
        btn2.setText("Snake");
        btn2.setMaxWidth(200);
        btn2.setMaxHeight(400);
        //creating a button which will lead to the Snake Game
        Button btn3 = new Button();
        btn3.setText("Galaga");
        btn3.setMaxWidth(200);
        btn3.setMaxHeight(400);
        //creating a button which will lead to the Galaga Game
        VBox vbox = new VBox(10); // creating a vbox, the space between each item in the vbox will be 10
        Label label = new Label("The Classics!"); //this label serves as the title which says "The Classics". I feel this is an appropriate tital for my project because all three of my games are classic games
        label.setMinWidth(100);
        label.setMinHeight(100);
        final double MAX_FONT_SIZE = 33.0; // defining the font size of my label
        label.setFont(new Font("Georgia", MAX_FONT_SIZE)); // setting up the font and font size of the label
        vbox.getChildren().add(label); //adding the label to the vbox
        vbox.getChildren().addAll(btn, btn2, btn3); //adding the three buttons to the vbox

        PongGame pong = new PongGame(); //this allows me to later on call the PongGame class, which contains all the code for the pong game
        
        GalagaGame galaga = new GalagaGame(); //allows me to later on call the GalagaGame class, which contains all the code for the galaga game
        
        btn.setOnAction(event -> {
            pong.start(primaryStage);  
        });
        //when this button is pressed, it runs the code for the pong game
        
        btn2.setOnAction(event -> {
            SnakeGame snake = new SnakeGame(); //allows me to later on call the SnakeGame class, which contains all the code for the snake game
            snake.start(primaryStage);  
        });
        //when this button is pressed, it runs the code for the snake game

        btn3.setOnAction(event -> {
            galaga.start(primaryStage);  
        });
        //when this button is pressed, it runs the code for the galaga game
        
        
        StackPane root = new StackPane(); //creating new stackpane
        
        Canvas canvas = new Canvas(400,  300); //creating new canvas for the background
        root.getChildren().add(canvas); //adding canvas to the stackpane
        

        root.setStyle("-fx-background-color: #7ba758"); //setting the background color
        
        Scene scene = new Scene(root, 400, 400);
        root.getChildren().add(vbox);
        root.setPadding(new Insets(500,10,500,105));
        //all this sets up the scene and its location 
        
        
        primaryStage.setTitle("The Classics!");
        primaryStage.setScene(scene);
        primaryStage.show();
        //display the scene
    }


    public static void main(String[] args) {
        launch(args);
        //Application.launch(GalagaGame.class);
    }
    //main class which launches the program
    
}
