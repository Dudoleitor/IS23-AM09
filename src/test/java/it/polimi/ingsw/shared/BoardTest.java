package it.polimi.ingsw.shared;

import it.polimi.ingsw.shared.model.*;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private final String basePath = "BoardTests/";
    PartialMove partialMove = new PartialMove();

    @Test
    void boardConstructor() throws JsonBadParsingException {
        Board t1 = new Board(2);
        Board t2 = new Board(3);
        Board b1 = new Board(4);
        List<CommonGoal> cList = b1.getCommonGoals();
        List <JSONObject> jList = cList.stream().map(CommonGoal::toJson).collect(Collectors.toList());
        Board b2 = new Board(Jsonable.pathToJsonObject(basePath + "BoardTestPlayerIni.json", Board.class), jList);
        assertEquals(b1,b2);

        Exception e1 = assertThrows(BoardRuntimeException.class, () -> new Board(-1));
        assertEquals("Error while creating Board : invalid number of players", e1.getMessage());
        Exception e2 = assertThrows(JsonBadParsingException.class, () -> new Board(Jsonable.pathToJsonObject(basePath + "WrongPath.json", Board.class), null));
        assertEquals("Error while generating Board from JSON : file was not found", e2.getMessage());
        Exception e3 = assertThrows(JsonBadParsingException.class, () -> new Board(Jsonable.pathToJsonObject(basePath + "BoardBadConfiguration.json", Board.class),null));
        assertEquals("Error while creating Board : board is not a valid configuration for given num player", e3.getMessage());
        Exception e4 = assertThrows(JsonBadParsingException.class, () -> new Board(Jsonable.pathToJsonObject(basePath + "BoardBadJsonFile.json", Board.class),null));
        assertEquals("Error while creating Board : bad json parsing", e4.getMessage());
        Exception e5 = assertThrows(JsonBadParsingException.class, () -> new Board(Jsonable.pathToJsonObject(basePath + "BoardBadJsonFile2.json", Board.class),null));
        assertEquals("Error while creating Board : bad json parsing", e5.getMessage());

        List<JSONObject> l = new ArrayList<>();

        l.add(Jsonable.pathToJsonObject(basePath + "BoardBadGoals.json", CommonGoal.class));
        Exception e6 = assertThrows(CommonGoalRuntimeException.class, () -> new Board(Jsonable.pathToJsonObject(basePath + "BoardFillLowTilesTest.json", Board.class),l ));
        assertEquals("Error while creating CommonGoal : file Json not found" , e6.getMessage());
    }
    @Test
    void testFill() throws OutOfTilesException, JsonBadParsingException, BadPositionException {
        Board b1 = new Board(4);
        Board b2 = new Board(Jsonable.pathToJsonObject(basePath + "BoardFillLowTilesTest.json", Board.class), null);
        b1.fill();

        boolean noEmptyFound = true;
        for(int i = 0; noEmptyFound && i < b1.getNumRows(); i++){
            for(int j = 0; noEmptyFound && j < b1.getNumColumns(); j++){
                if(b1.getTile(i,j).equals(Tile.Empty)){
                    noEmptyFound = false;
                }
            }
        }
        assertTrue(noEmptyFound);

        Exception e = assertThrows(OutOfTilesException.class, b2::fill);
        assertEquals("The END is near : No more tiles left in the deck", e.getMessage());
    }

    @Test
    void getterTest() throws JsonBadParsingException {
        Board b = new Board(3);
        assertEquals(3, b.getNumPlayers());
        assertEquals(9, b.getNumColumns());
        assertEquals(9, b.getNumRows());
    }
    @Test
    void tileGetPickTest() throws JsonBadParsingException, BadPositionException {
        Board b = new Board(Jsonable.pathToJsonObject(basePath + "BoardGenericTest.json", Board.class), null);

        assertEquals(Tile.Trophy, b.getTile(new Position(4,0)));
        Exception e1 = assertThrows(BadPositionException.class, () -> b.getTile(new Position(-1, 0)));
        assertEquals("Error while getting tile from Board : illegal coordinates", e1.getMessage());
        Exception e2 = assertThrows(BoardRuntimeException.class, () -> b.getTile(null));
        assertEquals("Error while getting tile: pos is null pointer", e2.getMessage());

        Tile t = b.pickTile(4,0);
        assertEquals(Tile.Trophy, t);
        assertEquals(Tile.Empty, b.getTile(new Position(4,0)));
        Exception e3 = assertThrows(BadPositionException.class, () -> b.pickTile(new Position(-1, 0)));
        assertEquals("Error while picking tile from Board : illegal coordinates", e3.getMessage());
        Exception e4 = assertThrows(BoardRuntimeException.class, () -> b.pickTile(null));
        assertEquals("Error while picking tile: pos is null pointer", e4.getMessage());
        Exception e5 = assertThrows(BadPositionException.class, () -> b.pickTile(new Position(0,0)));
        assertEquals("Error while picking Tile : Chosen one is Invalid type", e5.getMessage());
    }
    @Test
    void freeSideTest() throws JsonBadParsingException, BadPositionException {
        Board b = new Board(Jsonable.pathToJsonObject(basePath + "BoardGenericTest.json", Board.class), null);
        assertTrue(b.hasFreeSide(4,0));
        assertTrue(b.hasFreeSide(3,1));
        assertFalse(b.hasFreeSide(4,1));
        assertFalse(b.hasFreeSide(0,0));
        assertFalse(b.hasFreeSide(2,4));
        Exception e = assertThrows(BoardRuntimeException.class, () -> b.hasFreeSide(0,-1));
        assertEquals("Error while checking hasFreeSide on board : Index Out Of Bounds", e.getMessage());
    }
    @Test
    void getValidPositionsTest0() throws JsonBadParsingException, BadPositionException, InvalidMoveException {
        Board b = new Board(Jsonable.pathToJsonObject(basePath + "BoardGenericTest.json", Board.class), null);
        assertTrue(b.hasFreeSide(4,0));
        assertTrue(b.hasFreeSide(5,5));
        assertFalse(b.hasFreeSide(4,1));

        List<Position> p = new ArrayList<>();
        for(int j = 1; j< 9; j++) {
            p.add(new Position(3,j));
        }
        p.add(new Position(4,0));
        p.add(new Position(4,8));

        for(int j = 0; j< 8; j++) {
            p.add(new Position(5,j));
        }

        assertEquals(p, b.getValidPositions(new PartialMove())); //TODO to complete after finished method
    }


    @Test
    void toJsonTest1() throws JsonBadParsingException {
        String jsonPath = Board.boardPathForNumberOfPlayers(2);
        JSONObject jsonBoardExpected = Jsonable.pathToJsonObject(jsonPath, Board.class);
        Board board = new Board(2);

        JSONObject jsonBoardToTest = board.toJson();
        jsonBoardExpected.remove("deck");  // Ignoring deck as it's randomly generated
        jsonBoardToTest.remove("deck");

        assertEquals(jsonBoardExpected.toJSONString(), jsonBoardToTest.toJSONString());
    }
    @Test
    void toJsonTest2() throws JsonBadParsingException, BadPositionException {
        String jsonPath = basePath + "BoardTestInsert.json";
        JSONObject jsonBoard = Jsonable.pathToJsonObject(jsonPath, Board.class);
        Board board = new Board(jsonBoard, new ArrayList<>());

        assertEquals(jsonBoard.toJSONString(), board.toJson().toJSONString());
    }

    @Test
    void getValidPositionsTest() throws OutOfTilesException, JsonBadParsingException, BadPositionException, InvalidMoveException {
            //partialMove.addPosition(pos3);
            String jsonPath = basePath + "BoardTestInsert.json";
            Board b = new Board(Jsonable.pathToJsonObject(jsonPath, Board.class), new ArrayList<>());
            b.fill();
            Position pos2 = new Position(0, 3);

            assertTrue(b.getValidPositions(partialMove).contains(pos2));
    }

    @Test
    void getValidPositionsTest1() throws OutOfTilesException, JsonBadParsingException, BadPositionException, InvalidMoveException {
        //partialMove.addPosition(pos3);
        String jsonPath = basePath + "BoardTestInsert.json";
        Board b = new Board(Jsonable.pathToJsonObject(jsonPath, Board.class), new ArrayList<>());
        b.fill();
        Position pos = new Position(0, 3);
        partialMove.addPosition(pos);
        Position pos1 = new Position(0, 4);

        assertTrue(b.getValidPositions(partialMove).contains(pos1));
    }

    @Test
    void getValidPositionsTest2() throws OutOfTilesException, JsonBadParsingException, BadPositionException, InvalidMoveException {

            String jsonPath = basePath + "BoardTestInsert.json";
            Board b = new Board(Jsonable.pathToJsonObject(jsonPath, Board.class), new ArrayList<>());
            b.fill();
            Position pos1 = new Position(8, 5);
            Position pos2 = new Position(7, 5);
            partialMove.addPosition(pos1);
            partialMove.addPosition(pos2);
            Position pos3 = new Position(6, 5);


            assertFalse(b.getValidPositions(partialMove).contains(pos3));
    }
    @Test
    void copyConstuctor() throws JsonBadParsingException {
        Board b1;
        Board b2;
        b1 = new Board(3);
        b2 = new Board(b1);
        assertTrue(b1.sameBoard(b2));
        assertEquals(b1.getCommonGoals(),b2.getCommonGoals());
        assertTrue(b1.equals(b2));

    }

    @Test
    void toFill() throws JsonBadParsingException {
        Board b0 = new Board(Jsonable.pathToJsonObject(
                basePath + "BoardToFill0.json",
                Board.class),
                null);
        assertTrue(b0.toFill()); //empty board is to fill

        Board b1 = new Board(Jsonable.pathToJsonObject(
                basePath + "BoardToFill1.json",
                Board.class),
        null);
        assertTrue(b1.toFill()); //board with no couple of adjacent is to fill

        Board b2 = new Board(Jsonable.pathToJsonObject(
                basePath + "BoardToFill2.json",
                Board.class),
                null);
        assertFalse(b2.toFill()); //two adjacent are sufficient non to fill
    }
}
