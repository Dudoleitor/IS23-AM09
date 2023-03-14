package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;

import java.util.*;

public abstract class AbstractCommonGoal {
    protected Stack<Integer> points;

    public abstract boolean check(Shelf shelf);

    protected void populatePointsStack(int number_of_players){
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
                //throw exception or do nothing?
        }
    }

    public int givePoints(Shelf shelf){
        if(check(shelf)){
            return points.pop();
        }
        else{
            return 0;
        }
    }

    public ArrayList<Integer> showPointsStack(){ //useful in debugging
        return new ArrayList<>(points);
    }
}
