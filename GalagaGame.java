package theclassics;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.canvas.Canvas;
//importing all the necessary libraries
/**
 *
 * @author arul2
 */
public class GalagaGame extends TheClassics{
    public int counter = 0;
    public boolean alive = true;
    private Pane root = new Pane();
    private double time = 0;
    private Sprite player = new Sprite(300, 750, 40, 40, "player", Color.GREEN); //drawing the sprite using the sprite method i created below
    //declaring and intitlizaing all the ncessary variables, these variables are for the game logic, the sprite itself, and the scene
    @Override
    public void start(Stage stage){ //start method
        Scene scene = new Scene(draw(stage));
        //drawing scene
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    player.Left();
                    break;
                case D:
                    player.Right();
                    break;
                case SPACE:
                    shoot(player);
                    break;
            }
            //the above switch function allows for the movement of the player using the A and D key on the keyboard. The spaceship also shoots with the spacebar
        });
        stage.setScene(scene);
        stage.show();
        
    }

    private Parent draw(Stage stage) {
        root.setPrefSize(600, 800);
        root.getChildren().add(player); //adding the sprite to the scene
        //defining the scene
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(stage);
            }
        };
        //creating an animation timer which loops over and over to give the animated effect, it calls the update method
        timer.start();
        enemy();
        return root;
    }
    
    private void update(Stage stage) {
        time += 0.016;
        TheClassics classic = new TheClassics();
        
        sprites().forEach(enemies -> { //this checks each sprite on the scene using a switch function
            switch (enemies.type) {
                case "enemybullet": //if the sprite is an enemy bullet, the bullet should go down towards the user
                    enemies.Down();
                    if (enemies.getBoundsInParent().intersects(player.getBoundsInParent()) && alive == true) { //if the enemy bullet hits the user, both te user and the bullet die
                        player.dead = true;
                        enemies.dead = true;
                        alive = false;
                        classic.start(stage); //when the user dies, this line rewrites the scene to the main menu scene by calling back to the driver class
                    }
                    break;
                case "playerbullet": //if the bullet is shot by the user, then move the bullet upwards towards the enemies
                    enemies.Up();
                    sprites().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                        if (enemies.getBoundsInParent().intersects(enemy.getBoundsInParent())) { //checking each enemy to see if it was hit by the users bullet, if it was, both the enemy and the bullet dies
                            enemy.dead = true;
                            enemies.dead = true;
                            counter ++; //this counter counts to see if you have killed all the enemies
                            if (counter == 5) {
                                classic.start(stage);
                            } 
                            //iff you have killed all the enemies, i call back to the driver program so you go back to the main menu
                        }
                    });
                    break;
                case "enemy": //creating very basic opponent logic
                    if (time > 2) {
                        if (Math.random() < 0.5) {
                            shoot(enemies);
                        }
                        //the enemies have a 50 percent chance of shooting every 2 seconds
                    }
                    break;
            }
        });
        root.getChildren().removeIf(n -> {
            Sprite enemies = (Sprite) n;
            return enemies.dead;
        }); //if a sprite is dead, remove it
        if (time > 2) {
            time = 0;
        }
        //resetting the timer
    }

    private void enemy() { //this method creates and puts 5 enemies on the scene
        for (int i = 0; i < 5; i++) { //i<5 (so 5 enemies will be drawn)
            Sprite enemies = new Sprite(90 + i*100, 150, 30, 30, "enemy", Color.ORANGE); //defining each enemy, and the spacing between them
            root.getChildren().add(enemies);//adding enemies to scene
        }
    }
    
    
    private void shoot(Sprite bullet) { //this method creates the bullets
        Sprite enemies = new Sprite((int) bullet.getTranslateX() + 20, (int) bullet.getTranslateY(), 5, 20, bullet.type + "bullet", Color.GREEN); //defining the bullets, the bullet.type tells my logic if the bullett was shot by the user or by the enemy
        root.getChildren().add(enemies); //adding bullets to the scene
    }

    private static class Sprite extends Rectangle { //making the sprite method for our user and for the enemies, I am using rectangles to keep it simple
        boolean dead = false;
        final String type;

        Sprite(int x, int y, int width, int height, String type, Color color) { //setting the parameters for the sprite
            super(width, height, color);
            this.type = type;
            setTranslateX(x);
            setTranslateY(y);
        }

        void Left() {
            setTranslateX(getTranslateX() - 5);
        }

        void Right() {
            setTranslateX(getTranslateX() + 5);
        }

        void Up() {
            setTranslateY(getTranslateY() - 5);
        }

        void Down() {
            setTranslateY(getTranslateY() + 5);
        }
        //defining movement for the "ships" and for the bulletts
    }

    public static void main(String[] args) {
        launch(args);
    }
    //main method which was originally used when I was developing all three games seperately, no use for it now so it is blank
    private List<Sprite> sprites() {
        return root.getChildren().stream().map(n -> (Sprite)n).collect(Collectors.toList());
    }//this is a helper function to the update method. I want to acess the sprites in the scene, but it is not possible without this helper method. The update method was only letting me see the nodes, and so this method solves that issue.
    
}
