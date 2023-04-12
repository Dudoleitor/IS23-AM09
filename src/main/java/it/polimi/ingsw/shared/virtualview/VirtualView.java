package it.polimi.ingsw.shared.virtualview;

import it.polimi.ingsw.shared.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used on the server to send updates to
 * the clients when something changes in the model.
 * Each VirtualView has a list of clients and
 * will update each one of them.
 */
public abstract class VirtualView {
    private List<Client> clientList;

    public VirtualView() {
        this.clientList = new ArrayList<>();
    }

    public List<Client> getClientList() {
        return new ArrayList<>(clientList);
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }
}
