package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import com.google.appinventor.components.runtime.ActivityStarter;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.PasswordTextBox;
import com.google.appinventor.components.runtime.TableArrangement;
import com.google.appinventor.components.runtime.TableLayout;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;
//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;

public class screen08_ChatWith extends Form implements HandlesEventDispatching {

    private tuuber_Settings applicationSettings;
    private VerticalArrangement ChatWith;
    private HorizontalArrangement ChatHZ, ChatLabelHZ, SendHZ, pIDHZ;
    private Button Send;
    private Label ChatLabel, pID;
    private ListView Chat;
    private Web ChatWeb;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        this.BackgroundImage(applicationSettings.backgroundImageName);
        ChatWith = new VerticalArrangement(this);
        pIDHZ = new HorizontalArrangement(ChatWith);
        pID = new Label(pIDHZ);
        pID.Text(applicationSettings.pID);
        ChatLabelHZ = new HorizontalArrangement(ChatWith);
        ChatLabel = new Label(ChatLabelHZ);
        ChatLabel.Text("ChatLog");
        ChatHZ = new HorizontalArrangement(ChatWith);
        Chat = new ListView(ChatHZ);
        SendHZ = new HorizontalArrangement(ChatWith);
        Send = new Button(SendHZ);
        Send.Text("Send");

        ChatWeb = new Web(this);


    }
}
