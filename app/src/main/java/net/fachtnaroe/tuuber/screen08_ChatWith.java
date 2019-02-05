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
import com.google.appinventor.components.runtime.WebViewer;
//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;

public class screen08_ChatWith extends Form implements HandlesEventDispatching {

    private tuuber_Settings applicationSettings;
    private TextBox chatText;
    private VerticalArrangement ChatWith;
    private Button MainMenu;
    private HorizontalArrangement ChatHZ, ChatLabelHZ, SendHZ, pIDHZ, ChatsViewerHZ;
    private Button Send;
    private Label ChatLabel, pID;
    private ListView Chat;
    private Web ChatWeb;
    private WebViewer ChatsViewer;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        ChatWith=new VerticalArrangement(this);
        ChatWith.WidthPercent(100);
        ChatWith.HeightPercent(100);
        this.BackgroundImage(applicationSettings.backgroundImageName);
        ChatWeb = new Web(this);
        ChatWeb.Url("https://fachtnaroe.net/tuuber-test?action=LIST&entity=chat&sessionID=a1b2c3d4&initiator_pID=15&respondent_pID=22");
        ChatsViewerHZ = new HorizontalArrangement(ChatWith);
        ChatsViewer = new WebViewer(ChatsViewerHZ);
        ChatsViewer.GoToUrl("https://fachtnaroe.net/tuuber-test?action=LIST&entity=chat&sessionID=a1b2c3d4&initiator_pID=15&respondent_pID=22");
        chatText = new TextBox(ChatWith);
        chatText.Text("This will be the (prettified) result of:" +
                "SELECT * FROM Chats WHERE " +
                "(End result of:" +
                "getting each line from Chat table where" +
                "initiator_pID/respondent_pID combination matches for the Conversation" +
                "selected on the Conversation screen.)");
        MainMenu= new Button(ChatWith);
        MainMenu.Text("Back to main");
        applicationSettings = new tuuber_Settings(this);
        this.BackgroundImage(applicationSettings.backgroundImageName);
        ChatWith = new VerticalArrangement(this);
        pIDHZ = new HorizontalArrangement(ChatWith);
        pID = new Label(pIDHZ);
        pID.Text(applicationSettings.pID);
        SendHZ = new HorizontalArrangement(ChatWith);
        Send = new Button(SendHZ);
        Send.Text("Send");




    }
}
