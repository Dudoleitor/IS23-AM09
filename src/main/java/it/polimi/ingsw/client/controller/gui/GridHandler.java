package it.polimi.ingsw.client.controller.gui;

import it.polimi.ingsw.shared.model.BadPositionException;
import it.polimi.ingsw.shared.model.Grid;
import it.polimi.ingsw.shared.model.Position;
import it.polimi.ingsw.shared.model.Tile;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static it.polimi.ingsw.client.controller.gui.ClientControllerGUI.loadImage;

public class GridHandler {
    private AnchorPane anchor;
    private Canvas canvas;
    private ImageView[][] matrix;
    int rows;
    int columns;
    double elemHeight;
    double elemWidth;

    public GridHandler(AnchorPane anchor, Canvas canvas, Grid grid){
        this.anchor = anchor;
        this.canvas = canvas;
        resetGrid(grid);
    }
    public void resetGrid(Grid grid){
        if(grid == null){
            throw new RuntimeException("Null grid");
        }

        deleteAllImages();

        columns = grid.getColumns();
        rows = grid.getRows();

        this.elemHeight = canvas.getHeight()/rows;
        this.elemWidth = canvas.getWidth()/columns;

        this.matrix = new ImageView[rows][columns];

        for(int row = 0; row < rows; row++){
            for(int col = 0; col < columns; col++){
                try {
                    if(!(grid.getTile(row,col) == Tile.Empty || grid.getTile(row,col) == Tile.Invalid)) {
                        matrix[row][col] = createImageView(
                                new Position(row,col),
                                grid.getTile(row,col));
                    }
                }
                catch (BadPositionException e) {} //do nothing
            }
        }
    }

    public void deleteAllImages(){
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0){
            return;
        }
        List<ImageView> toRemove = Arrays.stream(matrix).filter(Objects::nonNull).flatMap(Arrays::stream).collect(Collectors.toList());
        anchor.getChildren().removeAll(toRemove);
    }

    private void appendImages(){
        List<Node> anchorChildren= anchor.getChildren();

        for(int row = 0; row < rows; row++){
            for(int col = 0; col < columns; col++){
                if(matrix[row][col] != null){
                    anchorChildren.add(matrix[row][col]);
                }
            }
        }
    }

    public void displayGrid(){
        appendImages();
        canvas.toFront();
    }

    public void displayGridBehind(ImageView image){
        appendImages();
        image.toFront();
        canvas.toFront();
    }

    public ImageView createImageView(Position pos, Tile tile){
        if(tile == null || tile == Tile.Empty || tile == Tile.Invalid){
            throw new RuntimeException("Bad image creation request");
        }
        ImageView imageView = new ImageView();
        imageView.setImage(loadImage("item_tiles/" + tile + "2.png"));
        imageView.setFitHeight(elemHeight);
        imageView.setFitWidth(elemWidth);
        imageView.setLayoutX(getElemX(pos.getColumn()));
        imageView.setLayoutY(getElemY(pos.getRow()));

        return imageView;
    }

    public void putTile(Position pos, Tile tile){
        if (tile != null && !(tile == Tile.Invalid) && !(tile == Tile.Empty)) {

            ImageView imageView = createImageView(pos,tile);

            matrix[pos.getRow()][pos.getColumn()] = imageView;

            anchor.getChildren().add(imageView);

            canvas.toFront();
        }
    }

    public void putTileBehind(ImageView img, Position pos, Tile tile){
        putTile(pos, tile);
        img.toFront();
        canvas.toFront();
    }

    public void removeTile(Position pos){
        anchor.getChildren().remove(matrix[pos.getRow()][pos.getColumn()]);
        matrix[pos.getRow()][pos.getColumn()] = null;
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

}
