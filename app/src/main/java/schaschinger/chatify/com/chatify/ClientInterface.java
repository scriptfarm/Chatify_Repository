package schaschinger.chatify.com.chatify;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by thoma on 23/03/2016.
 */
public class ClientInterface implements Runnable{

    private Client client;
    private Thread listen, run;
    private boolean running = false;

    //Storing all the currently online users
    OnlineUsers onlineUsers;

    //List of all Client child interfaces
    private ArrayList<PrivateChatInterface> privateChats;

    //Values the user has to enter at the beginning in order to start properly
    private String userName, userAddress;
    private Integer userPort;

    //Constructor
    public ClientInterface(String userName, String userAddress, int userPort){
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPort = userPort;

        this.onlineUsers = new OnlineUsers();

        //Set up means for communication over the web
        client = new Client(userName, userAddress, userPort);
        //Manage private Chats
        this.privateChats = new ArrayList<PrivateChatInterface>();

        boolean connect = client.openConnection(userAddress, userPort);
        if(!connect){
            Log.e("Error : ", "Connection could not be established!");
            send("Connection failed!", true);
        }

        send("Attempting a connection to : " + this.userAddress + " , Port : " + this.userPort +  " for " + this.userName, true);
        String toSend = "/c/" + userName + "/e/";
        client.send(toSend.getBytes());

        running = true;
        run = new Thread(this, "Running");
        run.start();
    }

    //For sending standard messages
    public void send(String messsage, boolean text){
        if(messsage.equals("")){
            return;
        }else{
            if(text){
                try {
                    if(StrongAES.getKey() != null) {
                        messsage = StrongAES.encryptString(client.getUserName() + " (P): " + messsage);
                    }else{
                        messsage = client.getUserName() + " (P): " + messsage;
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
                messsage = "/m/" + messsage + "/e/";
                Log.i("SENT : ", messsage);
            }
            client.send(messsage.getBytes());
        }
    }

    public void printConsole(String message){
        if(message != null){
            Log.i("INFO : ", message);
        }
    }

    //Map for received startup messages
    protected HashMap<Integer, String> rsum = new HashMap<Integer, String>();
    protected final String TAG = "ClientInterface.java ";

    public void listen(){
        listen = new Thread("Listen Thread"){
            public void run(){
                while(running){
                    //received message
                    Log.i(TAG, "LISTENING!");
                    String message = client.receive();

                    if(message.startsWith("/c/")){ //Received UUID from server
                        client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
                        printConsole("Successfully connected to server with your id of : " + client.getID());
                    }else if(message.startsWith("/m/")){ //Received message for public chat
                        String text = null;
                        try{
                            text = StrongAES.decryptByteArray(message.substring(3).split("/e/")[0].getBytes());
                        }catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e){
                            e.printStackTrace();
                        }
                        printConsole(text);
                    }else if(message.startsWith("/i/")){ //Ping to check whether or not the certain user is available / out of reach / or disconnected

                        String text = "/i/" + client.getID() + "/e/";
                        send(text ,false);

                    }else if(message.startsWith("/u/")){ //Updating the list of online Users

                        String [] users = message.split("/u/|/n/|/e/");
                        onlineUsers.update(Arrays.copyOfRange(users, 1, users.length-1));

                    }else if(message.startsWith("/nk/")){ //New key for message encryption received
                        Log.i("INFO: ", "Received key: " + message.split("/nk/|/e/")[1] + " - at local time : " +  new Date(System.currentTimeMillis()));
                        byte[] decodedKey = Base64.decode(message.split("/nk/|/e/")[1], message.split("/nk/|/e/")[1].length());
                        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
                        StrongAES.setKey(originalKey);
                    }else if(message.startsWith("/stupm/")){ //StartUp messages received
                        /*
                        Integer offSet = Integer.parseInt(message.split("/stupm/|/e/")[1]);
                        String actMessage = message.split("/stupm/|/e/")[2];
                        rsum.put(offSet, actMessage);

                        Log.i("INFO: " , "RSUM size = " + rsum.size());

                        if(rsum.size() == 25){
                            Integer MINKey = -1;
                            Integer MAXKey = -1;

                            Set<Integer> keys = rsum.keySet();
                            for(Integer i : keys){
                                if(i > MAXKey){
                                    MAXKey = i;
                                }else if(i < MINKey){
                                    MINKey = i;
                                }
                            }
                            for(int i = MINKey; i <= MAXKey; i++){
                                printConsole(rsum.get(i));
                            }
                            Log.i("INFO: ", "Fnished adding rsum...");
                        }
                        */
                    }else{
                        Log.i("UNHANDLED MESSAGE", message);
                    }
                }
            }
        };
        listen.start();
    }

    //Starting the main thread
    public void run(){
        listen();
    }

    public ClientInterface getClientInterface(){
        return this;
    }

    public Client getClient(){
        return this.client;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getUserPort() {
        return userPort;
    }

    @Override
    public String toString() {
        return "ClientInterface{" +
                "client=" + client +
                ", listen=" + listen +
                ", run=" + run +
                ", running=" + running +
                ", onlineUsers=" + onlineUsers +
                ", privateChats=" + privateChats +
                ", userName='" + userName + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userPort=" + userPort +
                ", rsum=" + rsum +
                '}';
    }
}
