package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;

import java.util.*;

public abstract class AbstractCommonGoal {
    private Stack<Integer> points;

    public abstract boolean check(Shelf shelf);

    public abstract int givePoints();

    public void populatePointsStack(int number_of_players){
        this.points = new Stack<>();
        switch (number_of_players){
            case 2:
                points.push(4);
                points.push(8);
                break;
            case 3:
                points.push(4);
                points.push(6);
                points.push(8);
                break;
            case 4:
                points.push(2);
                points.push(4);
                points.push(6);
                points.push(8);
                break;
            default:
                //return exception or do nothing?
        }
    }

    public int getPoints(){ //check condition?
        return points.pop();
    }

    public ArrayList<Integer> showPointsStack(){ //usefull in debugging
        return new ArrayList<>(points);
    }
}
