package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum CommonGoalStrategy {
    EightEqualTiles(6, Predicates.NequalTiles(8)),
    EqualTilesInAllCorners(3, Predicates.equalTilesInAllCorners),
    EqualX(12, Predicates.equalX),
    FourGroupsOf4(11, Predicates.groups(4,4)),
    FourLineWith3Types(8, Predicates.fourLineWith3Types),
    FullLadder(10, Predicates.fullLadder),
    Ladders(7, Predicates.ladder),
    SixGroupsOf2(1, Predicates.groups(6,2)),
    ThreeColumnsWith3Types(4, Predicates.threeColumnsWith3Types),
    TwoAllDifferentColumns(5, Predicates.twoAllDifferentColumns),
    TwoAllDifferentLines(9, Predicates.twoAllDifferentLines),
    TwoSquares(2, Predicates.twoSquares),
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
                orElseThrow(); //TODO handle better
    }

    private static class Predicates {
        static Predicate<Shelf> equalTilesInAllCorners = (shelf) -> {
            try {
                return notEmptyAndEqual(shelf.getCorners());
            } catch (CommonGoalsException e) {
                throw new RuntimeException(e); //TODO handle better
            }
        };
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
                    try {
                        if (notEmptyAndEqual(getXShape(shelf, row, column))) {
                            return true;
                        }
                    } catch (CommonGoalsException e) {
                        throw new RuntimeException(e); //TODO handle better
                    } catch (ShelfGenericException e) {
                        throw new RuntimeException(e); //TODO handle better
                    }
                }
            }
            return false;
        };
        static Predicate<Shelf> fourLineWith3Types = (shelf) -> {
            Long result = IntStream.range(0, shelf.getRows())
                    .filter(row -> {
                        try {
                            return maxNTypes(shelf.allTilesInRow(row), 3);
                        } catch (CommonGoalsException e) {
                            throw new CommonGoalRuntimeException(e.getMessage());
                        }
                    })
                    .count();
            return Math.toIntExact(result) >= 4;
        };
        static Predicate<Shelf> fullLadder = (shelf) -> {//get all heights in order
            int[] heights = new int[shelf.getColumns()];
            for (int column = 0; column < shelf.getColumns(); column++) {
                try {
                    heights[column] = columnHeigth(shelf, column);
                } catch (ShelfGenericException e) {
                    throw new RuntimeException(e); //TODO handle better
                }
            }
            //check if they form a ladder
            return isLadder(heights);
        };
        static Predicate<Shelf> ladder = (shelf) -> {
            //generate ladders
            ArrayList<ArrayList<Tile>> ladders = null;
            try {
                ladders = generateLadders(shelf);
            } catch (ShelfGenericException e) {
                throw new RuntimeException(e); //TODO handle better
            }
            //check if at least one is composed of all equal tiles
            for (ArrayList<Tile> ladder : ladders) {
                try {
                    if (notEmptyAndEqual(ladder)) {
                        return true;
                    }
                } catch (CommonGoalsException e) {
                    throw new RuntimeException(e);//TODO handle better
                }
            }
            return false;
        };
        static Predicate<Shelf> threeColumnsWith3Types = (shelf) -> {
            Long result = IntStream.range(0, shelf.getColumns())
                    .filter(column -> {
                        try {
                            return maxNTypes(shelf.allTilesInColumn(column), 3);
                        } catch (CommonGoalsException e) {
                            throw new CommonGoalRuntimeException(e.getMessage());
                        }
                    })
                    .count();
            return Math.toIntExact(result) >= 3;
        };
        static Predicate<Shelf> twoAllDifferentColumns = (shelf) -> {
            Long result = IntStream.range(0, shelf.getColumns())
                    .filter(column -> {
                        try {
                            return notEmptyAndAllDifferent(shelf.allTilesInColumn(column));
                        } catch (CommonGoalsException e) {
                            throw new CommonGoalRuntimeException(e.getMessage());
                        }
                    })
                    .count();
            return Math.toIntExact(result) >= 2;
        };
        static Predicate<Shelf> twoAllDifferentLines = (shelf) -> {
            Long result = IntStream.range(0, shelf.getRows())
                    .filter(row -> {
                        try {
                            return notEmptyAndAllDifferent(shelf.allTilesInRow(row));
                        } catch (CommonGoalsException e) {
                            throw new CommonGoalRuntimeException(e.toString());
                        }
                    })
                    .count();
            return Math.toIntExact(result) >= 2;
        };
        static Predicate<Shelf> twoSquares = (shelf) -> {
            int columns = shelf.getColumns();
            int rows = shelf.getRows();
            int equalSquaresCounter = 0;
            boolean[][] visited = new boolean[rows][columns];
            //count all perfect squares (no overlap or adjacency)
            for (int row = 0; row < rows - 1; row++) {
                for (int column = 0; column < columns - 1; column++) {
                    try {
                        if (isPerfectSquare(shelf, visited, row, column)) {
                            markSquareAsUsed(visited, row, column);
                            equalSquaresCounter++;
                        }
                    } catch (CommonGoalsException e) {
                        throw new RuntimeException(e);//TODO handle better
                    } catch (ShelfGenericException e) {
                        throw new RuntimeException(e);//TODO handle better
                    }
                }
            }
            return equalSquaresCounter >= 2;
        };
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
                            try {
                                if (validIslandSize(shelf, new Position(row, column), alreadyChecked) >= size_of_groups) {
                                    groups_found++;
                                }
                            } catch (ShelfGenericException e) {
                                throw new RuntimeException(e);//TODO handle better
                            }
                        }
                    }
                    return groups_found >= number_of_groups;
                }
            };
        }
        static Predicate<Shelf> NequalTiles(int n){
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
                            } catch (ShelfGenericException e) {
                                throw new RuntimeException(e); //TODO handle better
                            }
                            try {
                                if (shelf.isValidTile(row, column)) {
                                    //increment counter
                                    counters.put(currentTile, counters.get(currentTile) + 1);
                                }
                            } catch (ShelfGenericException e) {
                                throw new RuntimeException(e);//TODO handle better
                            }
                        }
                    }
                    return counters.values().stream().filter(x -> x >= n).count() > 0;
                }
            };
        }
        //static private functions used in predicates

        /**
         * Checks if some tiles are all Equal and not Empty
         *
         * @param tiles an ArrayList of Tiles
         * @return true if there are no Empty Tiles and they are all equal
         * @throws CommonGoalsException
         */
        static private boolean notEmptyAndEqual(ArrayList<Tile> tiles) throws CommonGoalsException {
            try {
                return tiles.stream().distinct().count() == 1 && !tiles.contains(Tile.Empty);

            } catch (NullPointerException e) {
                throw new CommonGoalsException("Error while checking notEmptyAndEqual : tiles is null pointer");
            }
        }

        /**
         * Checks if some tiles are all different and not Empty
         *
         * @param tiles an ArrayList of Tiles
         * @return true if there are no Empty Tiles and they are all different
         * @throws CommonGoalsException if tiles is Null
         */
        static private boolean notEmptyAndAllDifferent(ArrayList<Tile> tiles) throws CommonGoalsException {
            try {
                return !tiles.contains(Tile.Empty) && tiles.stream().distinct().count() == tiles.size();
            } catch (NullPointerException e) {
                throw new CommonGoalsException("Error while checking notEmptyAndAllDifferent : tiles is null pointer");
            }
        }

        /**
         * Checks if some Tiles have maximum n types of Tiles and do not contain Empty Tiles
         *
         * @param tiles an ArrayList of Tiles
         * @param n     the maximum number of accepted types of Tiles in tiles
         * @return True if there are no empty Tiles and there are maximum n types of Tiles in tiles
         * @throws CommonGoalsException
         */
        static private boolean maxNTypes(ArrayList<Tile> tiles, int n) throws CommonGoalsException {
            try {
                return tiles.size() > 0 &&
                        !tiles.contains(Tile.Empty) &&
                        tiles.stream().distinct().count() <= n;

            } catch (NullPointerException e) {
                throw new CommonGoalsException("Error while checking maxThreeTypes : tiles is null pointer");
            }
        }

        static private int recursiveIslandVisit(Shelf shelf, Position position, boolean[][] visited, Tile type) throws ShelfGenericException {
            int row = position.getRow();
            int column = position.getColumn();
            int result = 0;
            if (shelf.isOutOfBounds(row, column) || visited[row][column] == true) {
                return 0;
            } else if (shelf.getTile(row, column).equals(type)) {
                visited[row][column] = true;
                result = 1;
                for (Position neighbour : position.neighbours()) {
                    result += recursiveIslandVisit(shelf, neighbour, visited, type);
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
        static private int validIslandSize(Shelf shelf, Position position, boolean[][] visited) throws ShelfGenericException {
            if (shelf.isValidTile(position)) {
                Tile islandType = shelf.getTile(position);
                return recursiveIslandVisit(shelf, position, visited, islandType);
            } else {
                return 0;
            }
        }

        static private ArrayList<Tile> getXShape(Shelf shelf, int row, int column) throws ShelfGenericException {
            ArrayList<Tile> tiles = new ArrayList<>();
            tiles.add(shelf.getTile(row, column));
            tiles.add(shelf.getTile(row + 1, column + 1));
            tiles.add(shelf.getTile(row + 2, column + 2));
            tiles.add(shelf.getTile(row, column + 2));
            tiles.add(shelf.getTile(row + 2, column));
            return tiles;
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

        static private int columnHeigth(Shelf shelf, int column) throws ShelfGenericException {
            return (int) shelf.allTilesInColumn(column).stream()
                    .filter(x -> !x.equals(Tile.Empty)).count();
        }

        static private ArrayList<ArrayList<Tile>> generateLadders(Shelf shelf) throws ShelfGenericException {
            int rows = shelf.getRows();
            int columns = shelf.getColumns();
            ArrayList<ArrayList<Tile>> ladders = new ArrayList<>();
            ArrayList<Tile> currentLadder = new ArrayList<>();

            //collect tiles in ascending ladders
            for (int initial_row = 0; initial_row < 2; initial_row++) { //value 2 is hardcoded
                currentLadder.clear();
                for (int i = 0; i < Math.min(rows, columns); i++) {
                    currentLadder.add(shelf.getTile(new Position(initial_row + i, i)));
                }
                ladders.add(new ArrayList<>(currentLadder));
            }

            //collect tiles in descending ladders
            for (int initial_row = 0; initial_row < 2; initial_row++) { //value 2 is hardcoded
                currentLadder.clear();
                for (int i = 0; i < Math.min(rows, columns); i++) {
                    currentLadder.add(shelf.getTile(new Position(initial_row + i, columns - 1 - i)));
                }
                ladders.add(new ArrayList<>(currentLadder));
            }
            return ladders;
        }

        static private ArrayList<Tile> get2x2Square(Shelf shelf, int row, int column) throws ShelfGenericException {
            ArrayList<Tile> tiles = new ArrayList<>();
            tiles.add(shelf.getTile(row, column));
            tiles.add(shelf.getTile(row + 1, column));
            tiles.add(shelf.getTile(row, column + 1));
            tiles.add(shelf.getTile(row + 1, column + 1));
            return tiles;
        }

        static private void markSquareAsUsed(boolean[][] visited, int row, int column) {
            visited[row][column] = true;
            visited[row + 1][column] = true;
            visited[row][column + 1] = true;
            visited[row + 1][column + 1] = true;
        }

        static private boolean isPerfectSquare(Shelf shelf, boolean[][] visited, int row, int column) throws CommonGoalsException, ShelfGenericException {
            return !(visited[row][column]) && !(visited[row + 1][column]) &&
                    !(visited[row][column + 1]) && !(visited[row + 1][column + 1])
                    &&
                    notEmptyAndEqual(get2x2Square(shelf, row, column)) &&
                    4 == validIslandSize(shelf, new Position(row, column), visited);
        }
    }
}


