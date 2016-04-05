package schaschinger.chatify.com.chatify;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thoma on 23/03/2016.
 */
public class OnlineUsers {

    private ArrayList <String> onlineUsers;

    public OnlineUsers(){
        this.onlineUsers = new ArrayList<>();
    }

    public void update(String[] users){
        onlineUsers = null;
        for(String user : users){
            onlineUsers.add(user);
        }
    }

    public ArrayList getOnlineUsers(){
        return this.onlineUsers;
    }
}
