package it.polimi.ingsw.client.controller.gui;

import it.polimi.ingsw.shared.model.BadPositionException;
import it.polimi.ingsw.shared.model.Grid;
import it.polimi.ingsw.shared.model.Position;
import it.polimi.ingsw.shared.model.Tile;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import static it.polimi.ingsw.client.controller.gui.ClientControllerGUI.loadImage;

public class GridHandler {
    private AnchorPane anchor;
    private Canvas canvas;
    private Grid grid;
    double elemHeight;
    double elemWidth;

    public GridHandler(AnchorPane anchor, Canvas canvas, Grid grid){
        this.anchor = anchor;
        this.canvas = canvas;
        this.grid = grid;
        this.elemHeight = canvas.getHeight()/grid.getRows();
        this.elemWidth = canvas.getWidth()/ grid.getColumns();
    }
    public void resetGrid(Grid grid){
        this.grid = grid;
    }

    public void displayGrid(){
        try {
            for (int i = 0; i < grid.getRows(); i++) {
                for (int j = 0; j < grid.getColumns(); j++) {
                    if (!(grid.getTile(i, j) == Tile.Invalid) && !(grid.getTile(i, j) == Tile.Empty)) {
                        ImageView imageView = new ImageView();
                        imageView.setImage(loadImage("item_tiles/" + grid.getTile(i, j).toString() + "2.png"));
                        imageView.setFitHeight(elemHeight);
                        imageView.setFitWidth(elemWidth);
                        imageView.setLayoutX(canvas.getLayoutX() + j * elemWidth);
                        imageView.setLayoutY(canvas.getLayoutY() + i * elemHeight);
                        anchor.getChildren().add(imageView);
                    }
                }
            }
            canvas.toFront();
        } catch (BadPositionException e) {
            throw new RuntimeException("Invalid board in setBoard: " + e.getMessage());
        }
    }
    public Grid getGrid(){
        return grid;
    }

    public Position getPosition(MouseEvent mouseEvent){
        int column = (int) Math.floor(mouseEvent.getX()/elemWidth);
        int row = (int) Math.floor(mouseEvent.getY()/elemHeight);
        return new Position(row,column);
    }
}
