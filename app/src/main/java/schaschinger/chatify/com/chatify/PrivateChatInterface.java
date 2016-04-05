package schaschinger.chatify.com.chatify;

/**
 * Created by thoma on 23/03/2016.
 */
public class PrivateChatInterface {

    private ClientInterface clientInterface;
    private Integer chatID;

    public PrivateChatInterface(ClientInterface clientInterface){
        setClientInterface(clientInterface);
    }

    public ClientInterface getClientInterface() {
        return clientInterface;
    }

    public void setClientInterface(ClientInterface clientInterface) {
        this.clientInterface = clientInterface;
    }

    public Integer getChatID() {
        return chatID;
    }

    public void setChatID(Integer chatID) {
        this.chatID = chatID;
    }
}
