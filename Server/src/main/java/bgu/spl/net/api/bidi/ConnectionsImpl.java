package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;

import java.io.IOException;
import java.util.HashMap;

public class ConnectionsImpl<T> implements Connections<T>{

    private HashMap<Integer, ConnectionHandler> clients;

    public ConnectionsImpl(){
        clients = new HashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        try{
            if(clients.containsKey(connectionId)){
                clients.get(connectionId).send(msg);
                return true;
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void broadcast(T msg) {
        try{
            for(ConnectionHandler ch : clients.values()){
                ch.send(msg);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void disconnect(int connectionId) {
        clients.remove(connectionId);
    }

    public void addNewClient(int connectionId, ConnectionHandler<T> handler){
        clients.put(connectionId, handler);
    }

}
