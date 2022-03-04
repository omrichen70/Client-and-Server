package bgu.spl.net.api.bidi;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {
    private String name;
    private String password;
    private String birthday;
    private Integer connectionId;
    private ConcurrentLinkedQueue<String> followers;
    private ConcurrentLinkedQueue<String> following;
    private ConcurrentLinkedQueue<String> posts;
    private ConcurrentLinkedQueue<String> privateMessages;
    private ConcurrentLinkedQueue<Message> messagesWaiting;
    private ConcurrentLinkedQueue<String> blockedUsers;



    public User(String name, String password, String birthday, int connId){
        this.name = name;
        this.password = password;
        this.birthday = birthday;
        this.connectionId = connId;
        this.followers = new ConcurrentLinkedQueue<>();
        this.following = new ConcurrentLinkedQueue<>();
        this.posts = new ConcurrentLinkedQueue<>();
        this.privateMessages = new ConcurrentLinkedQueue<>();
        this.messagesWaiting = new ConcurrentLinkedQueue<>();
        this.blockedUsers = new ConcurrentLinkedQueue<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public Integer getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int id){
        connectionId = id;
    }

    public ConcurrentLinkedQueue<String> getFollowers() {
        return followers;
    }

    public ConcurrentLinkedQueue<String> getFollowing() {
        return following;
    }

    public ConcurrentLinkedQueue<String> getPosts() {
        return posts;
    }

    public ConcurrentLinkedQueue<String> getPrivateMessages() {
        return privateMessages;
    }

    public ConcurrentLinkedQueue<Message> getMessagesWaiting() {
        return messagesWaiting;
    }

    public short[] getMyStats(){
        short[] stats = new short[4];

        String[] birthdaySplitted = birthday.split("-");

        LocalDate birth = LocalDate.of(Integer.valueOf(birthdaySplitted[2]), Integer.valueOf(birthdaySplitted[1]), Integer.valueOf(birthdaySplitted[0]));
        LocalDate now = LocalDate.now();

        stats[0] = (short) Period.between(birth, now).getYears();
        stats[1] = (short) posts.size();
        stats[2] = (short) followers.size();
        stats[3] = (short) following.size();

        return stats;
    }

    public void addBlockedUser(String username){
        blockedUsers.add(username);
    }

    public boolean isBlocked(String username){
        return blockedUsers.contains(username);
    }




}
