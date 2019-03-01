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
    private HorizontalArrangement ChatHZ, ChatLabelHZ, SendHZ, pIDHZ, ChatsViewerHZ, toolbarHz;
    private Button Send, Refresh, Pool, MainMenu;
    private Label ChatLabel, pID, OtherpIDLabel;
    private Notifier MessageSent_Notifier, MessageError_Notifier;
    private ListView Chat;
    private Web ChatWeb, PoolWeb;
    private WebViewer ChatsViewer;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        ChatWith=new VerticalArrangement(this);
        ChatWith.WidthPercent(100);
        ChatWith.HeightPercent(100);

        toolbarHz = new HorizontalArrangement(ChatWith);
        MainMenu = new Button(toolbarHz);
        MainMenu.Width(40);
        MainMenu.Height(40);
        MainMenu.Image("buttonHome.png");
        pID = new Label(toolbarHz);
        pID.Text("I am user: #" + applicationSettings.pID);
        pID.Height(40);
        pID.FontSize(20);
        pID.WidthPercent(70);
        pID.TextAlignment(Component.ALIGNMENT_CENTER);
        Refresh = new Button(toolbarHz);
        Refresh.Width(40);
        Refresh.Height(40);
        Refresh.FontSize(8);
        Refresh.Image("buttonRefresh.png");

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
                applicationSettings.OtherpIDforChat +
                "&showHtml=1" +
                "&iam=" + applicationSettings.pID
        );
        chatText = new TextBox(ChatWith);
        chatText.Text("");
        pIDHZ = new HorizontalArrangement(ChatWith);
        OtherpIDLabel = new Label(pIDHZ);
        OtherpIDLabel.Text(applicationSettings.OtherpIDforChat);
        OtherpIDLabel.Visible(true);
        SendHZ = new HorizontalArrangement(ChatWith);
        Send = new Button(SendHZ);
        Send.Text("Send");
        Pool = new Button(SendHZ);
        Pool.Text("Pool Request");

        ChatWeb = new Web(ChatWith);
        PoolWeb = new Web(ChatWith);

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
                                applicationSettings.pID +
                                "&respondent_pID=" +
                                applicationSettings.OtherpIDforChat +
                                "&status=open&text=" +
                                chatText.Text()
                );
                ChatWeb.Get();

                return true;
            }
            else if(component.equals(Refresh)) {
                callBackend();
            }
            else if(component.equals(Pool)) {
                PoolWeb.Url(
                        applicationSettings.baseURL +
                                "?action=&entity=pools"
                );
                PoolWeb.Get();
            }
            return true;
        }
        else if (eventName.equals("GotText")) {
            if(component.equals(ChatWeb)) {
                chatText.Text("");
                return true;
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
                applicationSettings.OtherpIDforChat +
                "&showHtml=1" +
                "&iam=" + applicationSettings.pID
        );
    }

}
