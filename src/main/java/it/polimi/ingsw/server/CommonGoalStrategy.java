package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum CommonGoalStrategy {
    EightEqualTiles(6, Predicates.nEqualTiles(8)),
    EqualTilesInAllCorners(3, Predicates.equalTilesInAllCorners),
    EqualX(12, Predicates.equalX),
    FourGroupsOf4(11, Predicates.groups(4,4)),
    FourLineWith3Types(8, Predicates.rows(4,Predicates.maxNtypes(3))),
    FullLadder(10, Predicates.fullLadder),
    Ladders(7, Predicates.ladder),
    SixGroupsOf2(1, Predicates.groups(6,2)),
    ThreeColumnsWith3Types(4, Predicates.columns(3,Predicates.maxNtypes(3))),
    TwoAllDifferentColumns(5, Predicates.columns(2,Predicates.notEmptyAndAllDifferent)),
    TwoAllDifferentLines(9, Predicates.rows(2,Predicates.notEmptyAndAllDifferent)),
    TwoSquares(2, Predicates.nSquares(2)),
    ;
    private final int id;
    private final Predicate<Shelf> check;

    CommonGoalStrategy(int id, Predicate<Shelf> check) {
        this.id = id;
        this.check = check;
    }

    int getId() {
        return id;
    }

    Predicate<Shelf> getCheck() {
        return check;
    }

    public static CommonGoalStrategy findById(int id) {
        return Arrays.stream(values()).
                filter(x -> x.getId() == id).
                findFirst().
                orElseThrow();
    }

    //PREDICATES PASSED TO COMMON GOALS
    private static class Predicates {
        //PREDICATES ON GROUPS OF TILES (called by most of Predicate<Shelf>)
        static Predicate<List<Tile>> notEmptyAndEqual = new Predicate<List<Tile>>() {
            @Override
            public boolean test(List<Tile> tiles) {
                try {
                    return tiles.stream().distinct().count() == 1 && !tiles.contains(Tile.Empty);

                } catch (NullPointerException e) {
                    throw new CommonGoalRuntimeException("Error while checking notEmptyAndEqual : tiles is null pointer");
                }
            }
        };
        static Predicate<List<Tile>> notEmptyAndAllDifferent = new Predicate<List<Tile>>() {
            @Override
            public boolean test(List<Tile> tiles) {
                try {
                    return !tiles.contains(Tile.Empty) && tiles.stream().distinct().count() == tiles.size();
                } catch (NullPointerException e) {
                    throw new CommonGoalRuntimeException("Error while checking notEmptyAndAllDifferent : tiles is null pointer");
                }
            }
        };
        static Predicate<List<Tile>> maxNtypes(int n){
            return new Predicate<List<Tile>>() {
                @Override
                public boolean test(List<Tile> tiles) {
                    try {
                        return tiles.size() > 0 &&
                                !tiles.contains(Tile.Empty) &&
                                tiles.stream().distinct().count() <= n;

                    } catch (NullPointerException e) {
                        throw new CommonGoalRuntimeException("Error while checking maxThreeTypes : tiles is null pointer");
                    }
                }
            };
        };

        //ACTUAL PREDICATES
        /**
         * is true when the shelf contains n equal tiles
         */
        static Predicate<Shelf> nEqualTiles(int n){
            return new Predicate<Shelf>() {
                @Override
                public boolean test(Shelf shelf) {
                    Tile currentTile;
                    //initialize counters for all tyle of Tiles
                    HashMap<Tile, Integer> counters = new HashMap<>();
                    for (Tile tile : Tile.values()) {
                        counters.put(tile, 0);
                    }
                    //count all tiles in different counters
                    for (int row = 0; row < shelf.getRows(); row++) {
                        for (int column = 0; column < shelf.getColumns(); column++) {
                            try {
                                currentTile = shelf.getTile(row, column);
                            } catch (BadPositionException e) {
                                throw new CommonGoalRuntimeException("Error in test"); //TODO handle better
                            }
                            if (shelf.isValidTile(row, column)) {
                                //increment counter
                                counters.put(currentTile, counters.get(currentTile) + 1);
                            }
                        }
                    }
                    return counters.values().stream().filter(x -> x >= n).count() > 0;
                }
            };
        }

        /**
         * is true when n lines test true on the predicate
         */
        static Predicate<Shelf> rows(int number, Predicate<List<Tile>> toCheck){
            return new Predicate<Shelf>(){
                @Override
                public boolean test(Shelf shelf){
                    Long result = IntStream.range(0, shelf.getRows())
                            .filter(row -> toCheck.test(shelf.allTilesInRow(row)))
                            .count();
                    return Math.toIntExact(result) >= number;
                }
            };
        }

        /**
         * is true when n columns test true on the predicate
         */
        static Predicate<Shelf> columns(int number, Predicate<List<Tile>> toCheck){
            return new Predicate<Shelf>(){
                @Override
                public boolean test(Shelf shelf){
                    Long result = IntStream.range(0, shelf.getColumns())
                            .filter(column -> toCheck.test(shelf.allTilesInColumn(column)))
                            .count();
                    return Math.toIntExact(result) >= number;
                }
            };
        }

        /**
         * is true when the shelf contains n perfect squares
         */
        static Predicate<Shelf> nSquares(int n){
            return new Predicate<Shelf>(){
                @Override
                public boolean test(Shelf shelf){
                    int columns = shelf.getColumns();
                    int rows = shelf.getRows();
                    int equalSquaresCounter = 0;
                    boolean[][] visited = new boolean[rows][columns];
                    //count all perfect squares (no overlap or adjacency)
                    for (int row = 0; row < rows - 1; row++) {
                        for (int column = 0; column < columns - 1; column++) {
                            if (isPerfectSquare(shelf, visited, row, column)) {
                                markSquareAsUsed(visited, row, column);
                                equalSquaresCounter++;
                            }
                        }
                    }
                    return equalSquaresCounter >= 2;
                }
            };
        }
        static private ArrayList<Tile> get2x2Square(Shelf shelf, int row, int column) {
            try {
                ArrayList<Tile> tiles = new ArrayList<>();
                tiles.add(shelf.getTile(row, column));
                tiles.add(shelf.getTile(row + 1, column));
                tiles.add(shelf.getTile(row, column + 1));
                tiles.add(shelf.getTile(row + 1, column + 1));
                return tiles;
            } catch (BadPositionException e) {
                throw new CommonGoalRuntimeException("Error in get2x2Square");
            }
        }
        static private void markSquareAsUsed(boolean[][] visited, int row, int column) {
            visited[row][column] = true;
            visited[row + 1][column] = true;
            visited[row][column + 1] = true;
            visited[row + 1][column + 1] = true;
        }
        static private boolean isPerfectSquare(Shelf shelf, boolean[][] visited, int row, int column) {
            return !(visited[row][column]) && !(visited[row + 1][column]) &&
                    !(visited[row][column + 1]) && !(visited[row + 1][column + 1])
                    &&
                    notEmptyAndEqual.test(get2x2Square(shelf, row, column)) &&
                    4 == validIslandSize(shelf, new Position(row, column), visited);
        }

        /**
         * is true when the shelf contains n groups of the required size
         */
        static Predicate<Shelf> groups(int number_of_groups, int size_of_groups){
            return new Predicate<Shelf>() {
                @Override
                public boolean test(Shelf shelf) {
                    int rows = shelf.getRows();
                    int columns = shelf.getColumns();
                    boolean[][] alreadyChecked = new boolean[rows][columns];
                    int groups_found = 0;
                    for (int row = 0; row < rows; row++) {
                        for (int column = 0; column < columns; column++) {
                            if (validIslandSize(shelf, new Position(row, column), alreadyChecked) >= size_of_groups) {
                                groups_found++;
                            }
                        }
                    }
                    return groups_found >= number_of_groups;
                }
            };
        }
        static private int recursiveIslandVisit(Shelf shelf, Position position, boolean[][] visited, Tile type) {
            int row = position.getRow();
            int column = position.getColumn();
            int result = 0;
            if (shelf.isOutOfBounds(row, column) || visited[row][column]) {
                return 0;
            } else {
                try {
                    if (shelf.getTile(row, column).equals(type)) {
                        visited[row][column] = true;
                        result = 1;
                        for (Position neighbour : position.neighbours()) {
                            result += recursiveIslandVisit(shelf, neighbour, visited, type);
                        }
                    }
                } catch (BadPositionException e) {
                    throw new CommonGoalRuntimeException("Error in recursiveIslandVisit");
                }
            }
            return result;
        }

        /**
         * Returns the size of the "island" of Tiles with the same color in which the selected tile is in.
         * If the Tile is Empty or Invalid it returns 0. Visited Tiles are marked on visited
         *
         * @param shelf    the player's shelf
         * @param position is the position of the Tile
         * @param visited  a matrix of booleans that keeps track of the visited Tiles
         * @return the size of the island
         */
        static private int validIslandSize(Shelf shelf, Position position, boolean[][] visited) {
            if (shelf.isValidTile(position)) {
                Tile islandType = null;
                try {
                    islandType = shelf.getTile(position);
                } catch (BadPositionException e) {
                    throw new CommonGoalRuntimeException("Error in validIslandSize");
                }
                return recursiveIslandVisit(shelf, position, visited, islandType);
            } else {
                return 0;
            }
        }

        /**
         * is true when all corners of the shelf are not Empty and Equal
         */
        static Predicate<Shelf> equalTilesInAllCorners = (shelf) -> {
            return Predicates.notEmptyAndEqual.test(shelf.getCorners());
        };

        /**
         * is true when the shelf contains an "x" shape of non-empty and equals tiles
         */
        static Predicate<Shelf> equalX = (shelf) -> {
            int columns = shelf.getColumns();
            int rows = shelf.getRows();
            //Shelf too small to complete goal
            if (columns < 3 || rows < 3) {
                return false;
            }
            //Look for an "x" shape of tiles that are equal and not Empty
            for (int row = 0; row < rows - 2; row++) {
                for (int column = 0; column < columns - 2; column++) {
                    if (Predicates.notEmptyAndEqual.test(getXShape(shelf, row, column))) {
                        return true;
                    }
                }
            }
            return false;
        };
        static private ArrayList<Tile> getXShape(Shelf shelf, int row, int column) {
            try {
                ArrayList<Tile> tiles = new ArrayList<>();
                tiles.add(shelf.getTile(row, column));
                tiles.add(shelf.getTile(row + 1, column + 1));
                tiles.add(shelf.getTile(row + 2, column + 2));
                tiles.add(shelf.getTile(row, column + 2));
                tiles.add(shelf.getTile(row + 2, column));
                return tiles;
            } catch (BadPositionException e) {
                throw new CommonGoalRuntimeException("Error in getXshape");
            }
        }

        /**
         * is true when the tiles form a ladder in the shelf
         */
        static Predicate<Shelf> fullLadder = (shelf) -> {//get all heights in order
            int[] heights = new int[shelf.getColumns()];
            for (int column = 0; column < shelf.getColumns(); column++) {
                heights[column] = columnHeigth(shelf, column);
            }
            //check if they form a ladder
            return isLadder(heights);
        };
        static private int columnHeigth(Shelf shelf, int column){
            return (int) shelf.allTilesInColumn(column).stream()
                    .filter(x -> !x.equals(Tile.Empty)).count();
        }
        static private boolean isLadder(int[] heights) {
            if (heights.length == 1) {
                return true;
            }
            //check if heights form an ascending ladder
            boolean ascending = true;
            int expected = heights[0] + 1;
            for (int i = 1; i < heights.length; i++) {
                ascending = ascending && heights[i] == expected;
                expected++;
            }

            //check if heights form a descending ladder
            boolean descending = true;
            expected = heights[0] - 1;
            for (int i = 1; i < heights.length; i++) {
                descending = descending && heights[i] == expected;
                expected--;
            }
            return ascending || descending;
        }

        /**
         * is true when the shelf contains a ladder of non-empty and equal tiles
         */
        static Predicate<Shelf> ladder = (shelf) -> {
            //generate ladders
            ArrayList<ArrayList<Tile>> ladders = null;
            ladders = generateLadders(shelf);
            //check if at least one is composed of all equal tiles
            for (ArrayList<Tile> ladder : ladders) {
                if (Predicates.notEmptyAndEqual.test(ladder)) {
                    return true;
                }
            }
            return false;
        };
        static private ArrayList<ArrayList<Tile>> generateLadders(Shelf shelf) {
            int rows = shelf.getRows();
            int columns = shelf.getColumns();
            ArrayList<ArrayList<Tile>> ladders = new ArrayList<>();
            ArrayList<Tile> currentLadder = new ArrayList<>();

            //collect tiles in ascending ladders
            for (int initial_row = 0; initial_row < 2; initial_row++) { //value 2 is hardcoded
                currentLadder.clear();
                for (int i = 0; i < Math.min(rows, columns); i++) {
                    try {
                        currentLadder.add(shelf.getTile(new Position(initial_row + i, i)));
                    } catch (BadPositionException e) {
                        throw new CommonGoalRuntimeException("Error in generateLadders");
                    }
                }
                ladders.add(new ArrayList<>(currentLadder));
            }

            //collect tiles in descending ladders
            for (int initial_row = 0; initial_row < 2; initial_row++) { //value 2 is hardcoded
                currentLadder.clear();
                for (int i = 0; i < Math.min(rows, columns); i++) {
                    try {
                        currentLadder.add(shelf.getTile(new Position(initial_row + i, columns - 1 - i)));
                    } catch (BadPositionException e) {
                        throw new CommonGoalRuntimeException("Error in generateLadders");
                    }
                }
                ladders.add(new ArrayList<>(currentLadder));
            }
            return ladders;
        }
    }
}