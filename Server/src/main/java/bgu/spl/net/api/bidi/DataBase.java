package bgu.spl.net.api.bidi;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataBase {
    private ConcurrentHashMap<String, User> users;
    private ConcurrentHashMap<String, String> registered;
    private ConcurrentLinkedQueue<String> loggedInUsers;
    private ArrayList<String> forbiddenWords;


    public DataBase(ArrayList<String> forbiddenWords){
        users = new ConcurrentHashMap<>();
        registered = new ConcurrentHashMap<>();
        loggedInUsers = new ConcurrentLinkedQueue<>();
        this.forbiddenWords = forbiddenWords;
    }

    public boolean register(String name, String password, String birthday, int connId){
        if(!registered.containsKey(name)){
            User user = new User(name, password, birthday, connId);
            registered.put(name, password);
            users.put(name, user);
            return true;
        }
        return false;
    }


    public boolean logIn(String name){
        synchronized (users.get(name)){
            if(!loggedInUsers.contains(name)){
                loggedInUsers.add(name);
                return true;
            }
        }
        return false;
    }

    public boolean logOut(String name){
        return loggedInUsers.remove(name);
    }

    public  boolean isRegistered(String username){
        return users.containsKey(username);
    }

    public boolean doesPasswordMatch(String username, String password){
        return registered.get(username).equals(password);
    }

    public boolean isFollowing(String followerName, String nameToFollow){
        return users.get(followerName).getFollowing().contains(nameToFollow);
    }

    public boolean addFollower(String followerName, String nameToFollow){
        if(users.containsKey(followerName) & users.containsKey(nameToFollow)){
            users.get(followerName).getFollowing().add(nameToFollow); //followerName following nametofollow
            users.get(nameToFollow).getFollowers().add(followerName); //other side
            return true;
        }
        return false;
    }

    public boolean removeFollower(String followerName, String nameToUnFollow){
        if(users.containsKey(followerName) & users.containsKey(nameToUnFollow)){
            users.get(followerName).getFollowing().remove(nameToUnFollow); //followerName following nametofollow
            users.get(nameToUnFollow).getFollowers().remove(followerName); //other side
            return true;
        }
        return false;
    }

    public void addPost(String username, String post){
        users.get(username).getPosts().add(post);
    }

    public void addPm(String username, String pm){
        users.get(username).getPrivateMessages().add(pm);
    }

    public LinkedList<User> getUsers(){
        LinkedList<User> usersList = new LinkedList<>(users.values());
        return usersList;
    }

    public ConcurrentLinkedQueue<String> getMyFollowers(String username){
        return users.get(username).getFollowers();
    }

    public User getUser(String username){
        User toReturn = users.get(username);
        return toReturn;
    }

    public boolean isLoggedIn(String username){
        return loggedInUsers.contains(username);
    }

    public ArrayList<String> getForbiddenWords(){
        return forbiddenWords;
    }

    public void setConnectionId(String username, int id){
        getUser(username).setConnectionId(id);
    }



}
