package it.polimi.ingsw.server;

import it.polimi.ingsw.server.clientonserver.Client;

public interface NetworkExceptionHandler {
    /**
     * This function is used to handle network exceptions thrown by RMI or the socket.
     * @param client Client object
     * @param e Exception thrown
     */
    public void handleNetworkException(Client client, Exception e);
}
