package schaschinger.chatify.com.chatify;

import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by thoma on 22/03/2016.
 */
public class Client {

    private String userName, serverAddress;
    private int userPort;

    private int ID = -1;

    /*
    Socket as entrance into the network - UDP Protocol is used therefor we need a Datagram Socket;
     */
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private Thread send;

    public Client(String userName, String userAddress, int userPort){
        this.userName = userName;
        this.serverAddress = userAddress;
        this.userPort = userPort;
    }

    public String receive(){
        byte [] receivedData = new byte[65000];
        //Buffer
        DatagramPacket datagramPacket = new DatagramPacket(receivedData, receivedData.length);

        try{
            datagramSocket.receive(datagramPacket);
        }catch (IOException e){
            e.printStackTrace();
        }

        String receivedMessage = new String(datagramPacket.getData());
        return  receivedMessage;
    }

    public void send(final byte[] data){
        send = new Thread("Send thread"){
            public void run(){
                DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, userPort);
                try{
                    datagramSocket.send(packet);
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    public boolean openConnection(String userAddress, int userPort){


        try{
            datagramSocket = new DatagramSocket();
            inetAddress = InetAddress.getByName(userAddress);
        }catch(IOException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void close(){
        new Thread(){
            public void run(){
                synchronized (datagramSocket){
                    datagramSocket.close();
                }
            }
        }.start();
    }

    public String getUserName() {
        return userName;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getUserPort() {
        return userPort;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
