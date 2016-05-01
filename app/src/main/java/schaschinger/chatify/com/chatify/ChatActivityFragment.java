package schaschinger.chatify.com.chatify;


import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by thoma on 01/05/2016.
 */
public class ChatActivityFragment extends Fragment {

    private static final String TAG = "ChatActivity";
    private ChatArrayAdapter adp;
    private ListView list;
    private EditText chatText;
    private Button send;
    Intent intent;
    private boolean side = false;
    private ViewGroup rootView;

    private boolean initialized = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null){
            rootView = (ViewGroup) inflater.inflate(
                    R.layout.main, container, false);
            setListeners();
        }

        setRetainInstance(true);


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }



    public void setListeners(){

        send = (Button) rootView.findViewById(R.id.btn);
        list = (ListView) rootView.findViewById(R.id.listview);
        adp = new ChatArrayAdapter(getContext(), R.layout.chat);
        list.setAdapter(adp);

        chatText = (EditText) rootView.findViewById(R.id.chat_text);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode ==
                        KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(adp);

        adp.registerDataSetObserver(new DataSetObserver() {
            public void OnChanged() {
                super.onChanged();
                list.setSelection(adp.getCount() - 1);
            }
        });

        chatText.setText(" - ");
        sendChatMessage();
    }

    private boolean sendChatMessage(){
        if(!chatText.getText().toString().equals("")) {
            adp.add(new ChatMessage(side, chatText.getText().toString()));
            chatText.setText("");
            side = !side;
        }
        return true;
    }
}
