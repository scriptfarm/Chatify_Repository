package schaschinger.chatify.com.chatify;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Act_Login extends AppCompatActivity {

    /*
    Attributes
     */
    Button btnLetsGo, btnDialogAgree, btnDialogCancel;
    EditText userName, serverIP, serverPort;
    String sUserName, sServerIP, sServerPort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__login);

        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //Determine important components from xml file;
    protected void initialize(){
        this.btnLetsGo = (Button)findViewById(R.id.btnLetsGo);
        this.userName = (EditText)findViewById(R.id.etUsername);
        this.serverIP = (EditText)findViewById(R.id.etServerIP);
        this.serverPort = (EditText)findViewById(R.id.etServerPort);


        btnLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    sUserName = String.valueOf(userName.getText());
                    sServerIP = String.valueOf(serverIP.getText());
                    sServerPort = String.valueOf(serverIP.getText());
                }catch(NullPointerException | NumberFormatException e){
                    openDialog();
                }

                getInformation();

                if(sUserName != "" && sServerIP != "" && sServerPort != "" && sUserName.length() >= 1 && sServerIP.length() >= 1 && sServerPort.length() >= 1) {
                    startMainActivity(sUserName, sServerIP, sServerPort);
                }else{
                    openDialog();
                }
            }
        });

    }

    protected void getInformation(){
        Log.i("INFO : ", "Username = " + this.sUserName + " , ServerIP = " + this.sServerIP + " , ServerPort = " + this.serverPort);
    }

    public final static String EXTRA_USERNAME = "schaschinger.chatify.com.chatify.USERNAME";
    public final static String EXTRA_SERVERIP = "schaschinger.chatify.com.chatify.SERVERIP";
    public final static String EXTRA_SERVERPORT = "schaschinger.chatify.com.chatify.SERVERPORT";

    private void startMainActivity(String username, String serverip, String serverport){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_SERVERIP, serverip);
        intent.putExtra(EXTRA_SERVERPORT, serverport);
        startActivity(intent);
        this.finish();

    }


    private void openDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.notfilleddialog);

        this.btnDialogAgree = (Button)dialog.findViewById(R.id.dialog_ok);
        this.btnDialogCancel = (Button)dialog.findViewById(R.id.dialog_cancel);

        dialog.setTitle("Empty field!");

        btnDialogAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
