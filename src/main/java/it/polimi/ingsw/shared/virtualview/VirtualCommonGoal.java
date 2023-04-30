package it.polimi.ingsw.shared.virtualview;

import it.polimi.ingsw.server.clientonserver.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This object is used on the server to send
 * updates to clients when something changes
 * in the model.
 */
public class VirtualCommonGoal extends VirtualView {

    public VirtualCommonGoal() {
        super();
    }

    /**
     * This method is used when a player achieves
     * the common goals and pops points from
     * the stack.
     * It is also used to init the common goal.
     * @param id ID of the common goal
     * @param points List with points that
     *               can still be achieved
     */
    public void refresh(int id, List<Integer> points) {
        for(Client cl: getClientList()) {
           cl.refreshCommonGoal(id, points);
        }
    }
}
