package net.brainstorm_labs.spacegame;

import java.util.ArrayList;

public class Chat {
    ArrayList<String> log = new ArrayList();

    public void addMsg(int id, String msg){
        if(id == Network.playerID){
            log.add("You: " + msg);
        } else{
            log.add(Play.players.get(id).name + ": " + msg);
        }
    }


}
