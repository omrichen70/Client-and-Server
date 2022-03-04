package bgu.spl.net.api.bidi;


import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private int id;
    private Connections<Message> connections;
    private DataBase dataBase;
    private boolean shouldTerminate;
    private boolean loggedIn;
    private String username;

    public BidiMessagingProtocolImpl(DataBase dataBase){ //Constructor
        this.dataBase = dataBase;
        shouldTerminate = false;
        loggedIn = false;
        username = "";
    }

    @Override
    public void start(int connectionId, Connections<Message> connections) {
        this.id = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Message message) {
        message.act(this);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public void register(short opcode, String username, String password, String birthday){
        boolean isRegistered = dataBase.register(username, password, birthday, id); //if doesnt exist, register.
        if(!isRegistered){ //create a message based on the value
            ERRORMsg error = new ERRORMsg(opcode);
            connections.send(id, error);
        } else {
            ACKMsg ackMsg = new ACKMsg(opcode);
            connections.send(id, ackMsg);
        }
    }

    public void login(short opcode, String username, String password, int captcha){
        if(dataBase.isRegistered(username) && !dataBase.isLoggedIn(username) && dataBase.doesPasswordMatch(username, password) && captcha == 1){
            dataBase.logIn(username);
            loggedIn = true;
            this.username = username;
            dataBase.setConnectionId(username, id);
            ACKMsg ackMsg = new ACKMsg(opcode);
            connections.send(id, ackMsg);
            ConcurrentLinkedQueue<Message> messagesWaiting = dataBase.getUser(username).getMessagesWaiting();
            while (!messagesWaiting.isEmpty()){ //send all messages waiting
                Message msg = messagesWaiting.remove();
                connections.send(id, msg);
            }
        } else {
            ERRORMsg errorMsg = new ERRORMsg(opcode);
            connections.send(id, errorMsg);
        }
    }

    public void logout(short opcode){
        if(dataBase.isRegistered(username) && loggedIn){
            synchronized (dataBase.getUser(username)){
                dataBase.logOut(username);
                loggedIn = false;
                ACKMsg ackMsg = new ACKMsg(opcode);
                connections.send(id, ackMsg);
            }
        } else {
            ERRORMsg errorMsg = new ERRORMsg(opcode);
            connections.send(id, errorMsg);
        }
    }

    public void follow(short opcode, int whatToDo, String otherUserName){
        boolean ans = false;
        if(loggedIn && dataBase.isRegistered(otherUserName) && whatToDo == 0 && !dataBase.isFollowing(username, otherUserName) && !dataBase.getUser(otherUserName).isBlocked(username) && !dataBase.getUser(username).isBlocked(otherUserName)){ //FOLLOW
            ans = dataBase.addFollower(username, otherUserName);
        } else if(loggedIn && whatToDo == 1 && dataBase.isFollowing(username, otherUserName)){
            ans = dataBase.removeFollower(username, otherUserName);
        }
        if(ans){
            ACKFollowMsg ackMsg = new ACKFollowMsg(opcode, otherUserName); //AckMSG with username should be added
            connections.send(id, ackMsg);
        } else {
            ERRORMsg errorMsg = new ERRORMsg(opcode);
            connections.send(id, errorMsg);
        }

    }

    public void post(short opcode, String content){
        if(loggedIn){
            ACKMsg ackMsg = new ACKMsg(opcode);
            dataBase.addPost(username, content);
            Message notification = new NOTIFICATIONMsg(1, username, content);
            for(User user : dataBase.getUsers()){
                synchronized (user){
                    String tagged = "@" + user.getName();
                    if(content.contains(tagged) && !dataBase.getUser(user.getName()).isBlocked(username) && !dataBase.isFollowing(user.getName(), username)){
                        if(dataBase.isLoggedIn(user.getName())){
                            connections.send(user.getConnectionId(), notification);
                        } else {
                            user.getMessagesWaiting().add(notification);
                        }
                    }
                }
            }
            for(String user : dataBase.getMyFollowers(username)){
                synchronized (dataBase.getUser(user)){
                    if(dataBase.isLoggedIn(user)){
                        connections.send(dataBase.getUser(user).getConnectionId(), notification);
                    } else {
                        dataBase.getUser(user).getMessagesWaiting().add(notification);
                    }
                }
            }
            connections.send(id, ackMsg);
        } else {
            ERRORMsg errorMsg = new ERRORMsg(opcode);
            connections.send(id, errorMsg);
        }
    }

    public void pm(short opcode, String otherUsername, String content, String dateAndTime){
        if(loggedIn){
            if(dataBase.isRegistered(otherUsername)){
                if(dataBase.isFollowing(username, otherUsername)){
                    String filteredContent = filterMessage(content);
                    dataBase.addPm(username, filteredContent);
                    ACKMsg ackMsg = new ACKMsg(opcode);
                    Message notification = new NOTIFICATIONMsg(0, username, filteredContent);
                    synchronized (dataBase.getUser(otherUsername)){
                        if(dataBase.isLoggedIn(otherUsername)){
                            connections.send(dataBase.getUser(otherUsername).getConnectionId(), notification);
                        } else {
                            dataBase.getUser(otherUsername).getMessagesWaiting().add(notification);
                        }
                    }
                    connections.send(id, ackMsg);
                } else {
                    ERRORMsg errorMsg = new ERRORMsg(opcode);
                    connections.send(id, errorMsg);
                }
            } else {
                ERRORMsg errorMsg = new ERRORMsg(opcode);
                connections.send(id, errorMsg);
            }
        } else {
            ERRORMsg errorMsg = new ERRORMsg(opcode);
            connections.send(id, errorMsg);
        }
    }

    private String filterMessage(String content){
        String filtered = "";
        String againFiltered = "";
        for(String word : dataBase.getForbiddenWords()){
           filtered = content.replaceAll("(?<!\\S)" + word + "(?!\\S)", "<filtered>");
           againFiltered = filtered.replaceAll("(?<!\\S)" + word + "(?=[?!.@#$%^*&])", "<filtered>");
           content = againFiltered;
        }
        return againFiltered;
    }

    public void logStat(short opcode){
        if(dataBase.isRegistered(username) && loggedIn){
            ArrayList<short[]> stats = new ArrayList<>();
            for(User user : dataBase.getUsers()){
                if(user.getName() != username && !dataBase.getUser(user.getName()).isBlocked(username)&& !dataBase.getUser(username).isBlocked(user.getName()) && dataBase.isLoggedIn(user.getName())){
                    short[] userStats = user.getMyStats();
                    stats.add(userStats);
                }
            }
            ACKLogStatMsg ackLogStatMsg = new ACKLogStatMsg(opcode, stats);
            connections.send(id, ackLogStatMsg);
        } else {
            ERRORMsg errorMsg = new ERRORMsg(opcode);
            connections.send(id, errorMsg);
        }
    }

    public void stat(short opcode, String[] userNames){
        if(dataBase.isRegistered(username) && loggedIn){
            ArrayList<short[]> stats = new ArrayList<>();
            boolean stop = false;
            for(String s : userNames){
                if(s != username && dataBase.isRegistered(s) && !(dataBase.getUser(s).isBlocked(username)) && !(dataBase.getUser(username).isBlocked(s))){
                    short[] userStats = dataBase.getUser(s).getMyStats();
                    stats.add(userStats);
                } else {
                    stop = true;
                    break;
                }
            }
            if(!stop){
                ACKStatMsg ackStatMsg = new ACKStatMsg(opcode, stats);
                connections.send(id, ackStatMsg);
            } else {
                ERRORMsg errorMsg = new ERRORMsg(opcode);
                connections.send(id, errorMsg);
            }
        }else {
            ERRORMsg errorMsg = new ERRORMsg(opcode);
            connections.send(id, errorMsg);
        }
    }

    public void block(short opcode, String otherUsername) {
        if(loggedIn && dataBase.isRegistered(otherUsername)){
            dataBase.getUser(username).addBlockedUser(otherUsername);
            dataBase.removeFollower(otherUsername, username);
            dataBase.removeFollower(username, otherUsername);
            ACKMsg ackMsg = new ACKMsg(opcode);
            connections.send(id, ackMsg);
        } else {
            ERRORMsg errorMsg = new ERRORMsg(opcode);
            connections.send(id, errorMsg);
        }
    }
}
