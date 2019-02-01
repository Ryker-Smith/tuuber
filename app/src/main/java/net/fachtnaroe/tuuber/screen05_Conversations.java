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
    private Button ChatsScreen;
    private Label ContactsLabel, pID;

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

        EventDispatcher.registerEventForDelegation(this, "ChatsScreen", "Click");


    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(ChatsScreen) && eventName.equals("Click")) {
            switchForm("screen08_ChatWith");
            return true;
        }
        return true;
    }
}