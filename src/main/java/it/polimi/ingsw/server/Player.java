package it.polimi.ingsw.server;

import it.polimi.ingsw.shared.Shelf;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Player {
    private final String name;
    private int commonGoalPoints;
    private final List<Integer> checkedCommonGoals;
    private final ServerShelf shelf;
    private final PlayerGoal goal;


    public Player(String name, ServerShelf shelf, PlayerGoal goal) {
        this(name, shelf, goal, 0, new ArrayList<>());
    }
    public Player(String name, ServerShelf shelf, PlayerGoal goal, int commonGoalPoints, List<Integer> checkedCommonGoals) {
        this.name = name;
        this.shelf = shelf;
        this.goal = goal;
        this.commonGoalPoints = commonGoalPoints;
        this.checkedCommonGoals = new ArrayList<>(checkedCommonGoals);
    }

    public String getName() {return name;}
    public Shelf getShelf() { return new Shelf(0,0); }  // TODO

    public List<Integer> getCheckedCommonGoals() {
        return new ArrayList<>(checkedCommonGoals);
    }

    public void checkCommonGoal(List<AbstractCommonGoal> goals) {
        for (AbstractCommonGoal goal : goals) {
            if (goal.check(shelf) && !checkedCommonGoals.contains(Integer.valueOf(goal.getId()))) {

            }
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
