package it.polimi.ingsw.RMI;

import it.polimi.ingsw.server.Controller;
import it.polimi.ingsw.server.ControllerGenericException;
import it.polimi.ingsw.server.Player;

import java.util.List;
import java.util.Optional;

public class InactivityDetector extends Thread{
    private Controller controller;
    private boolean interrupted;
    private List<Player> players;
    private static int sleepTime = 10000;
    public InactivityDetector(Controller controller){
        this.controller = controller;
        interrupted = false;
    }
    @Override
    public void run(){
        while(!interrupted){
            players = controller.getPlayers();
            players.stream().forEach(x->x.setActive(false));
            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            updateActivity();
            System.out.println("Updated activity of players:");
            for(Player  p : controller.getPlayers()){
                System.out.println(p.getName() +" is " + (p.isActive()?"active":"inactive"));
            }
        }
    }
    public void interrupt(){
        interrupted = true;
    }
    public void setActivity(String player, boolean active){
        Optional<Player> target = players.stream().
                filter(p -> p.getName().equals(player)).
                findFirst();
        if(target.isPresent()){
            target.get().setActive(active);
        }
        else{
            throw new ControllerGenericException("No player with such name");
        }
    }

    public void updateActivity(){
        for(Player p : players){
            controller.setActivity(p.getName(),p.isActive());
        }
    }
}
