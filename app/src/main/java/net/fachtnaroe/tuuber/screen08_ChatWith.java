package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.WebViewer;
//import com.google.appinventor.components.runtime.util;


public class screen08_ChatWith extends Form implements HandlesEventDispatching {

    private tuuber_Settings applicationSettings;
    private TextBox chatText;
    private VerticalArrangement ChatWith;
    private Button MainMenu;
    private HorizontalArrangement ChatHZ, ChatLabelHZ, SendHZ, pIDHZ, ChatsViewerHZ;
    private Button Send, Refresh;
    private Label ChatLabel, pID, OtherpIDLabel;
    private Notifier MessageSent_Notifier, MessageError_Notifier;
    private ListView Chat;
    private Web ChatWeb;
    private WebViewer ChatsViewer;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        ChatWith=new VerticalArrangement(this);
        ChatWith.WidthPercent(100);
        ChatWith.HeightPercent(100);
        this.BackgroundImage(applicationSettings.backgroundImageName);
        ChatsViewerHZ = new HorizontalArrangement(ChatWith);
        ChatsViewer = new WebViewer(ChatsViewerHZ);
        ChatsViewer.HeightPercent(60);
        ChatsViewer.GoToUrl(applicationSettings.default_baseURL +
                "?action=LIST&entity=chat&sessionID=" +
                applicationSettings.sessionID +
                "&initiator_pID=" +
                applicationSettings.pID +
                "&respondent_pID=" +
                applicationSettings.otherpIDforChat +
                "&showHtml=1" +
                "&iam=" + applicationSettings.pID
        );
        chatText = new TextBox(ChatWith);
        chatText.Text("");
        MainMenu= new Button(ChatWith);
        MainMenu.Text("Back to main");
        pIDHZ = new HorizontalArrangement(ChatWith);
        pID = new Label(pIDHZ);
        pID.Text(applicationSettings.pID);
        pID.Visible(true);
        OtherpIDLabel = new Label(pIDHZ);
        OtherpIDLabel.Text(applicationSettings.otherpIDforChat);
        OtherpIDLabel.Visible(true);
        SendHZ = new HorizontalArrangement(ChatWith);
        Send = new Button(SendHZ);
        Send.Text("Send");
        Refresh = new Button(SendHZ);
        Refresh.Text("Refresh");

        ChatWeb = new Web(ChatWith);

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (eventName.equals("Click")) {
            if (component.equals(MainMenu)) {
                startNewForm("screen03_MainMenu", null);
                return true;
            }
            else if (component.equals(Send)) {
                ChatWeb.Url(
                        applicationSettings.baseURL +
                                "?action=POST&entity=CHAT&sessionID=" +
                                applicationSettings.sessionID +
                                "&initiator_pID=" +
                                pID.Text() +
                                "&respondent_pID=" +
                                OtherpIDLabel.Text() +
                                "&status=open&text=" +
                                chatText.Text()
                );
                ChatWeb.Get();

                return true;
            }
            else if(component.equals(Refresh)) {
                callBackend();
            }
            return true;
        }
        return true;

    }

    void callBackend() {
        ChatsViewer.GoToUrl(applicationSettings.default_baseURL +
                "?action=LIST&entity=chat&sessionID=" +
                applicationSettings.sessionID +
                "&initiator_pID=" +
                applicationSettings.pID +
                "&respondent_pID=" +
                applicationSettings.otherpIDforChat +
                "&showHtml=1" +
                "&iam=" + applicationSettings.pID
        );
    }

}
