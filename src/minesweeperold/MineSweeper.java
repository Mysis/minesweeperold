package minesweeperold;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MineSweeper extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        PlayingField game = new PlayingField(Constants.ROWS, Constants.COLUMNS, Constants.CELL_SIZE, Constants.MINES);
        
        game.getWin().addListener(o -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Minesweeper");
            alert.setHeaderText(null);
            alert.setContentText("You win!");

            alert.showAndWait();
        });
        
        Group grp = new Group(game.getVBoxContainer());
        
        Scene scene = new Scene(grp, 
                Constants.COLUMNS * Constants.CELL_SIZE + Constants.CELL_SPACING * (Constants.COLUMNS - 1), 
                Constants.ROWS * Constants.CELL_SIZE + Constants.CELL_SPACING * (Constants.ROWS - 1),
                Constants.BACKGROUND_COLOR
        );
        
        primaryStage.setTitle("Minesweeper");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
