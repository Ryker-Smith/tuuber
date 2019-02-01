package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;

public class screen05_Conversations extends Form implements HandlesEventDispatching {

    private tuuber_Settings applicationSettings;
    private Button Send, OtherChats;
    private HorizontalArrangement ChatHZ, SendHZ, OtherChatsHZ, ChatBoxHZ;
    private VerticalArrangement Conversation;
    private TextBox ChatBox;
    private ListView Contacts;



    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        this.BackgroundImage(applicationSettings.backgroundImageName);
        Conversation = new VerticalArrangement(this);
        ChatHZ = new HorizontalArrangement(Conversation);
        Contacts = new ListView(ChatHZ);

        ChatBoxHZ = new HorizontalArrangement(Conversation);
        ChatBox = new TextBox(ChatBoxHZ);

        SendHZ = new HorizontalArrangement(Conversation);
        Send = new Button(SendHZ);
        Send.Text("Send");

        OtherChatsHZ = new HorizontalArrangement(Conversation);
        OtherChats = new Button(OtherChatsHZ);
        OtherChats.Text("Other Chats");

    }
}