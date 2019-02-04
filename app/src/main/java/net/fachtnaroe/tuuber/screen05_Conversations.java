package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;

public class screen05_Conversations extends Form implements HandlesEventDispatching {

    private tuuber_Settings applicationSettings;
    private VerticalArrangement Conversations;
    private HorizontalArrangement ContactsHZ, ContactsLabelHZ, ChatsScreenHZ, pIDHZ;
    private ListView Contacts;
    private String baseURL = "https://fachtnaroe.net/tuuber-test";
    private Button ChatsScreen;
    private Label ContactsLabel, pID;
    private Web ContactWeb;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        this.BackgroundImage(applicationSettings.backgroundImageName);
        Conversations = new VerticalArrangement(this);
        pIDHZ = new HorizontalArrangement(Conversations);
        pID = new Label(pIDHZ);
        pID.Text(applicationSettings.pID);
        ContactsLabelHZ = new HorizontalArrangement(Conversations);
        ContactsLabel = new Label(ContactsLabelHZ);
        ContactsLabel.Text("Contacts List");
        ContactsHZ = new HorizontalArrangement(Conversations);
        Contacts = new ListView(ContactsHZ);
        ChatsScreenHZ = new HorizontalArrangement(Conversations);
        ChatsScreen = new Button(ChatsScreenHZ);
        ChatsScreen.Text("Chat");

        ContactWeb = new Web(this);

        EventDispatcher.registerEventForDelegation(this, "ChatsScreen", "Click");
        EventDispatcher.registerEventForDelegation(this, "ContactWeb", "GotText");

    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(ChatsScreen) && eventName.equals("Click")) {
            switchForm("screen08_ChatWith");
            return true;
        }
        ContactWeb.Url(
                baseURL +
                        "action=LIST&entity=chat&sessionID=a1b2c3d4&initiator_pID=15&respondent_pID=22"
        );
        ContactWeb.Get();
        if (component.equals(ContactWeb) && eventName.equals("GotText")){
            
        }
        return true;
    }
}