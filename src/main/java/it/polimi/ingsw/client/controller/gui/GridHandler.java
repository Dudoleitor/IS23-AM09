package it.polimi.ingsw.client.controller.gui;

import it.polimi.ingsw.shared.model.BadPositionException;
import it.polimi.ingsw.shared.model.Grid;
import it.polimi.ingsw.shared.model.Position;
import it.polimi.ingsw.shared.model.Tile;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.controller.gui.ClientControllerGUI.loadImage;

public class GridHandler {
    private AnchorPane anchor;
    private Canvas canvas;
    private Grid grid;
    double elemHeight;
    double elemWidth;
    List<Position> validPositions;

    public GridHandler(AnchorPane anchor, Canvas canvas, Grid grid){
        this.anchor = anchor;
        this.canvas = canvas;
        this.grid = grid;
        this.elemHeight = canvas.getHeight()/grid.getRows();
        this.elemWidth = canvas.getWidth()/ grid.getColumns();
        this.validPositions = new ArrayList<>();
    }

    public void setValidPositions(List<Position> validPositions){
        if(validPositions != null){
            this.validPositions = validPositions;
        }
    }
    public void resetGrid(Grid grid){
        this.grid = grid;
            List<Node> toDelete =  anchor.getChildren()
                    .stream()
                    .filter(node -> node.getClass() == ImageView.class &&
                            isInsideCanvas(node))
                    .collect(Collectors.toList());

            for(Node tile : toDelete){
                anchor.getChildren().remove(tile);
            }
    }

    private boolean isInsideCanvas(Node node){
        return node.getLayoutX() >= canvas.getLayoutX() &&
                node.getLayoutX() <= canvas.getLayoutX()+canvas.getWidth()-elemWidth &&
                node.getLayoutY() >= canvas.getLayoutY() &&
                node.getLayoutY() <= canvas.getLayoutY()+canvas.getHeight()-elemHeight;

    }

    public void displayGrid(){
        try {
            addImagesToScene();
            canvas.toFront();
        } catch (BadPositionException e) {
            throw new RuntimeException("Invalid board in setBoard: " + e.getMessage());
        }
    }

    public void displayGridBehind(ImageView image){
        try {
            addImagesToScene();
            image.toFront();
            canvas.toFront();
        } catch (BadPositionException e) {
            throw new RuntimeException("Invalid board in setBoard: " + e.getMessage());
        }
    }

    private void addImagesToScene() throws BadPositionException {
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getColumns(); col++) {
                if (!(grid.getTile(row, col) == Tile.Invalid) && !(grid.getTile(row, col) == Tile.Empty)) {
                    ImageView imageView = new ImageView();
                    imageView.setImage(loadImage("item_tiles/" + grid.getTile(row, col).toString() + "2.png"));
                    imageView.setFitHeight(elemHeight);
                    imageView.setFitWidth(elemWidth);
                    imageView.setLayoutX(getElemX(col));
                    imageView.setLayoutY(getElemY(row));
                    imageView.getStyleClass().add("validTile");
                    anchor.getChildren().add(imageView);
                }
            }
        }
    }

    public Grid getGrid(){
        return grid;
    }

    private double getElemX(int column){
        return  canvas.getLayoutX() + column * elemWidth;
    }

    private double getElemY(int row){
        return canvas.getLayoutY() + row * elemHeight;
    }

    public Position getPosition(MouseEvent mouseEvent){
        int column = (int) Math.floor(mouseEvent.getX()/elemWidth);
        int row = (int) Math.floor(mouseEvent.getY()/elemHeight);
        return new Position(row,column);
    }

    public void removeTile(Position pos){
        Node toDelete =  anchor.getChildren()
                .stream()
                .filter(node ->
                        node.getClass() == ImageView.class &&
                        node.getLayoutX() == getElemX(pos.getColumn()) &&
                        node.getLayoutY() == getElemY(pos.getRow()))
                .findFirst().orElse(null);
        if(toDelete != null){
            anchor.getChildren().remove(toDelete);
        }
    }

}
