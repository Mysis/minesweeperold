/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweeperold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayingField {

    private BooleanProperty win = new SimpleBooleanProperty(false);
    private boolean lose = false;

    private VBox rowContainer;
    private List<List<Cell>> field;

    public PlayingField(int rows, int columns, int cellSize, int mines) {

        if (rows * columns < mines) {
            throw new ArithmeticException("the playing field isn't big enough to hold all the mines");
        }

        List<Boolean> minesBag = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (mines > 0) {
                    minesBag.add(true);
                    mines--;
                } else {
                    minesBag.add(false);
                }
            }
        }
        Collections.shuffle(minesBag);

        field = new ArrayList<>();
        rowContainer = new VBox(Constants.CELL_SPACING);
        for (int i = 0; i < columns; i++) {
            ArrayList<Cell> row = new ArrayList<>();
            HBox cellContainer = new HBox(Constants.CELL_SPACING);
            for (int j = 0; j < rows; j++) {
                Cell cell = new Cell(cellSize, minesBag.get(0));
                minesBag.remove(0);
                if (cell.isMine()) {
                    cell.getIsRevealed().addListener(o -> {
                        gameOver();
                        lose = true;
                    });
                } else {
                    cell.getIsRevealed().addListener(o -> {
                        cell.reveal();
                        if (cell.getSurroundingMines() == 0) {
                            revealEmptyCells(cell);
                        }
                        if (checkVictory()) {
                            victory();
                        }
                    });
                }
                row.add(cell);
                cellContainer.getChildren().add(cell);
            }
            field.add(row);
            rowContainer.getChildren().add(cellContainer);
        }

        for (int i = 0; i < field.size(); i++) {
            for (int j = 0; j < field.get(i).size(); j++) {
                if (!field.get(i).get(j).isMine()) {
                    int surroundingMines = 0;
                    List<Cell> surroundingCells = surroundingCells(field.get(i).get(j));
                    for (Cell cell : surroundingCells) {
                        if (cell.isMine()) {
                            surroundingMines++;
                        }
                    }
                    field.get(i).get(j).setSurroundingMines(surroundingMines);
                }
            }
        }
    }

    public void revealEmptyCells(Cell cell) {
        for (Cell outside : surroundingCells(cell)) {
            if (!outside.getIsRevealed().get()) {
                outside.reveal();
                if (outside.getSurroundingMines() == 0) {
                    revealEmptyCells(outside);
                }
            }
        }
    }

    public List<Cell> surroundingCells(Cell cell) throws IllegalArgumentException {
        List<Cell> cells = new ArrayList<>();
        int x = -2;
        int y = -2;
        for (int i = 0; i < field.size(); i++) {
            if (field.get(i).indexOf(cell) != -1) {
                x = field.get(i).indexOf(cell);
                y = i;
            }
        }
        if (x == -2 || y == -2) {
            throw new IllegalArgumentException("cell is not in field");
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    cells.add(field.get(y + i).get(x + j));
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }
        return cells;
    }

    public boolean checkVictory() {
        boolean win = false;
        if (!lose) {
            win = true;
            for (List<Cell> row : field) {
                for (Cell cell : row) {
                    if (!cell.isMine() && !cell.getIsRevealed().get()) {
                        win = false;
                    }
                }
            }
        }
        return win;
    }

    public void victory() {
        win.set(true);
        gameOver();
    }

    public void gameOver() {
        for (List<Cell> row : field) {
            for (Cell cell : row) {
                if (cell.isMine()) {
                    cell.reveal();
                }
            }
        }
    }

    public VBox getVBoxContainer() {
        return rowContainer;
    }

    public BooleanProperty getWin() {
        return win;
    }
}
