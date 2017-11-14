package dto;

import java.net.InetAddress;

public class BroadcastInfo {

    private int blackboard_port;
    private InetAddress blackboard_adresse;

    public InetAddress getBlackboard_adresse() {
        return blackboard_adresse;
    }
    public void setAdresse(InetAddress blackboard_adresse) {
        this.blackboard_adresse = blackboard_adresse;
    }

    public void setBlackboard_adresse(InetAddress blackboard_adresse) {
        this.blackboard_adresse = blackboard_adresse;
    }

    public int getBlackboard_port() {
        return blackboard_port;
    }

    public void setBlackboard_port(int blackboard_port) {
        this.blackboard_port = blackboard_port;
    }

    public BroadcastInfo(int blackboard_port) {
        this.blackboard_port = blackboard_port;
    }

    @Override
    public String toString() {
        return "BroadcastInfo{" +
                "blackboard_port=" + blackboard_port +
                ", adresse=" + blackboard_adresse +
                '}';
    }
    public String getURL(){
        return "http:/"+blackboard_adresse+":"+blackboard_port;
    }

    public BroadcastInfo() {
    }
}
