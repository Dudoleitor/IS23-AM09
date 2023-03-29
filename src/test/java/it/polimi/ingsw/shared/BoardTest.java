package it.polimi.ingsw.shared;

import it.polimi.ingsw.server.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    PartialMove partialMove = new PartialMove();

    @Test
    void boardConstructor() throws CommonGoalsException, BoardGenericException {
        Board t1 = new Board(2);
        Board t2 = new Board(3);
        Board b1 = new Board(4);
        List<CommonGoal> cList = b1.getCommonGoals();
        List <JSONObject> jList = cList.stream().map(CommonGoal::toJson).collect(Collectors.toList());
        Board b2 = new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardTestPlayerIni.json"), jList);
        assertEquals(b1,b2);

        Exception e1 = assertThrows(BoardRuntimeException.class, () -> new Board(-1));
        assertEquals("Error while creating Board : invalid number of players", e1.getMessage());
        Exception e2 = assertThrows(BoardRuntimeException.class, () -> new Board(Board.pathToJsonObject("src/test/resources/BoardTests/WrongPath.json"), null));
        assertEquals("Error while generating Board from Json : file was not found", e2.getMessage());
        Exception e3 = assertThrows(BoardGenericException.class, () -> new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardBadConfiguration.json"),null));
        assertEquals("Error while creating board : board is not a valid configuration for given num player", e3.getMessage());
        Exception e4 = assertThrows(BoardGenericException.class, () -> new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardBadJsonFile.json"),null));
        assertEquals("Error while creating Board : bad json parsing", e4.getMessage());
        Exception e5 = assertThrows(BoardGenericException.class, () -> new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardBadJsonFile2.json"),null));
        assertEquals("Error while creating Board : bad json parsing", e5.getMessage());

        List<JSONObject> l = new ArrayList<>();

        l.add(CommonGoalsFactory.pathToJsonObject("src/test/resources/BoardTests/BoardBadGoals.json"));
        Exception e6 = assertThrows(BoardGenericException.class, () -> new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardFillLowTilesTest.json"),l ));
        assertEquals("Error while creating board : common goal exception" , e6.getMessage());
    }
    @Test
    void testFill() throws CommonGoalsException, BoardGenericException, IOException, ParseException, OutOfTilesException {
        Board b1 = new Board(4);
        Board b2 = new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardFillLowTilesTest.json"), null);
        b1.fill();
        System.out.println(b1); //to check for fill

        boolean noEmptyFound = true;
        for(int i = 0; noEmptyFound && i < b1.getNumRows(); i++){
            for(int j = 0; noEmptyFound && j < b1.getNumColumns(); j++){
                if(b1.getTile(i,j).equals(Tile.Empty)){
                    noEmptyFound = false;
                }
            }
        }
        assertTrue(noEmptyFound);

        Exception e = assertThrows(OutOfTilesException.class, () -> b2.fill());
        assertEquals("The END is near : No more tiles left in the deck", e.getMessage());
    }

    @Test
    void getterTest() throws CommonGoalsException, IOException, ParseException, BoardGenericException {
        Board b = new Board(3);
        assertEquals(3, b.getNumPlayers());
        assertEquals(9, b.getNumColumns());
        assertEquals(9, b.getNumRows());
    }
    @Test
    void tileGetPickTest() throws BoardGenericException, CommonGoalsException {
        Board b = new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardGenericTest.json"), null);

        assertEquals(Tile.Trophy, b.getTile(new Position(4,0)));
        Exception e1 = assertThrows(BoardGenericException.class, () -> b.getTile(new Position(-1, 0)));
        assertEquals("Error while getting tile from Board : illegal coordinates", e1.getMessage());
        Exception e2 = assertThrows(BoardRuntimeException.class, () -> b.getTile(null));
        assertEquals("Error while getting tile: pos is null pointer", e2.getMessage());

        Tile t = b.pickTile(4,0);
        assertEquals(Tile.Trophy, t);
        assertEquals(Tile.Empty, b.getTile(new Position(4,0)));
        Exception e3 = assertThrows(BoardGenericException.class, () -> b.pickTile(new Position(-1, 0)));
        assertEquals("Error while picking tile from Board : illegal coordinates", e3.getMessage());
        Exception e4 = assertThrows(BoardRuntimeException.class, () -> b.pickTile(null));
        assertEquals("Error while picking tile: pos is null pointer", e4.getMessage());
        Exception e5 = assertThrows(BoardGenericException.class, () -> b.pickTile(new Position(0,0)));
        assertEquals("Error while picking Tile : Chosen one is Invalid type", e5.getMessage());
    }
    @Test
    void freeSideTest() throws BoardGenericException, CommonGoalsException {
        Board b = new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardGenericTest.json"), null);
        assertTrue(b.hasFreeSide(4,0));
        assertTrue(b.hasFreeSide(3,1));
        assertFalse(b.hasFreeSide(4,1));
        assertFalse(b.hasFreeSide(0,0));
        assertFalse(b.hasFreeSide(2,4));
        Exception e = assertThrows(BoardRuntimeException.class, () -> b.hasFreeSide(0,-1));
        assertEquals("Error while checking hasFreeSide on board : Index Out Of Bounds", e.getMessage());
    }
    @Test
    void getValidPositionsTest0() throws BoardGenericException{
        Board b = new Board(Board.pathToJsonObject("src/test/resources/BoardTests/BoardGenericTest.json"), null);
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
    void toJsonTest1() throws CommonGoalsException, IOException, ParseException, BoardGenericException {
        String jsonPath = Board.boardPathForNumberOfPlayers(2);
        JSONObject jsonBoardExpected = Board.pathToJsonObject(jsonPath);
        Board board = new Board(2);

        JSONObject jsonBoardToTest = board.toJson();
        jsonBoardExpected.remove("deck");  // Ignoring deck as it's randomly generated
        jsonBoardToTest.remove("deck");

        assertEquals(jsonBoardExpected.toJSONString(), jsonBoardToTest.toJSONString());
    }
    @Test
    void toJsonTest2() throws CommonGoalsException, IOException, ParseException, BoardGenericException {
        String jsonPath = "src/test/resources/BoardTests/BoardTestInsert.json";
        JSONObject jsonBoard = Board.pathToJsonObject(jsonPath);
        Board board = new Board(jsonBoard, new ArrayList<>());

        assertEquals(jsonBoard.toJSONString(), board.toJson().toJSONString());
    }

    @Test
    void getValidPositionsTest() throws BoardGenericException, OutOfTilesException {
            //partialMove.addPosition(pos3);
            String jsonPath = "src/test/resources/BoardTests/BoardTestInsert.json";
            Board b = new Board(Board.pathToJsonObject(jsonPath), new ArrayList<>());
            b.fill();
            Position pos2 = new Position(0, 3);


            assertTrue(b.getValidPositions(partialMove).contains(pos2));




    }

    @Test
    void getValidPositionsTest2() throws BoardGenericException, OutOfTilesException, PartialMoveException {

            String jsonPath = "src/test/resources/BoardTests/BoardTestInsert.json";
            Board b = new Board(Board.pathToJsonObject(jsonPath), new ArrayList<>());
            b.fill();
            Position pos1 = new Position(8, 5);
            Position pos2 = new Position(7, 5);
            partialMove.addPosition(pos1);
            partialMove.addPosition(pos2);
            Position pos3 = new Position(6, 5);


            assertFalse(b.getValidPositions(partialMove).contains(pos3));



    }

}
