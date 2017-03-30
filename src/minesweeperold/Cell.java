/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeperold;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Cell extends StackPane {

    private int surroundingMines;
    private final boolean isMine;
    private BooleanProperty isRevealed = new SimpleBooleanProperty(false);
    private BooleanProperty isFlagged = new SimpleBooleanProperty(false);
    private BooleanProperty mouseHovered = new SimpleBooleanProperty(false);

    public Cell(int size, boolean mine) {
        Rectangle shape = new Rectangle();
        getChildren().add(shape);
        shape.setWidth(size);
        shape.setHeight(size);
        Polygon flag = new Polygon();
        getChildren().add(flag);
        setAlignment(flag, Pos.CENTER);
        flag.setFill(Constants.FLAG_COLOR);
        flag.getPoints().addAll(new Double[]{
            0d, 0d,
            Constants.CELL_SIZE / 2d, Constants.CELL_SIZE / 4d,
            0d, Constants.CELL_SIZE / 2d
        });
        flag.visibleProperty().bind(isFlagged);
        isMine = mine;

        shape.fillProperty().bind(Bindings.when(isRevealed).then(Constants.CELL_REVEALED_COLOR).otherwise(Constants.CELL_COLOR));
        setOnMouseEntered(e -> mouseHovered.set(true));
        setOnMouseExited(e -> mouseHovered.set(false));
        setOnMouseReleased(e -> {
            if (mouseHovered.get()) {
                if (e.getButton() == MouseButton.PRIMARY) {
                    reveal();
                } else if (e.getButton() == MouseButton.SECONDARY && !isRevealed.get()) {
                    toggleIsFlagged();
                }
            }
        });
    }

    public void reveal() {
        isRevealed.set(true);
        isFlagged.set(false);
        if (!isMine) {
            if (surroundingMines > 0) {
                Label label = new Label(Integer.toString(surroundingMines));
                label.setAlignment(Pos.CENTER);
                getChildren().add(label);
            }
        } else {
            Circle mine = new Circle(Constants.CELL_SIZE / 4d, Constants.MINE_COLOR);
            getChildren().add(mine);
            setAlignment(mine, Pos.CENTER);
        }
    }

    public void setSurroundingMines(int i) {
        surroundingMines = i;
    }

    public boolean isMine() {
        return isMine;
    }

    public int getSurroundingMines() {
        return surroundingMines;
    }

    public void toggleIsFlagged() {
        isFlagged.set(!isFlagged.get());
    }

    public BooleanProperty getIsRevealed() {
        return isRevealed;
    }
}
