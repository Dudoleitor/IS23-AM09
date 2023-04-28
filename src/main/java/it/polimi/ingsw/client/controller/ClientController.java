package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.shared.RemoteInterfaces.ClientRemote;

/**
 * This object is used to receive updates coming from
 * the server, it resides on the client. When RMI is
 * used, the object is wrapped inside ClientRMI and
 * methods are called directly from the server.
 * With TCP, proper adapters parse messages coming
 * from the network and call the methods.
 */
public interface ClientController extends ClientRemote {
}
