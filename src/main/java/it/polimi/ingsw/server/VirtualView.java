package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.List;

public abstract class VirtualView {

    private final List<Client> clientList;

    public VirtualView() {
        this.clientList = new ArrayList<>();
    }

    public List<Client> getClientList() {
        return new ArrayList<>(clientList);
    }

    public void addClient(Client client) {
        clientList.add(client);
    }

    public void removeClientById(int id) {
        clientList.remove(id);
    }
}
