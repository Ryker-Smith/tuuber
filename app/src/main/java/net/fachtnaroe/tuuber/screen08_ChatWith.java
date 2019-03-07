package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.CheckBox;
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

import org.json.JSONException;
import org.json.JSONObject;
//import com.google.appinventor.components.runtime.util;


public class screen08_ChatWith extends Form implements HandlesEventDispatching {

    private tuuber_Settings applicationSettings;
    private TextBox chatText;
    private VerticalArrangement ChatWith;
    private HorizontalArrangement ChatHZ, ChatLabelHZ, SendHZ, PoolHZ, CheckBoxHZ, pIDHZ, ChatsViewerHZ, toolbarHz;
    private Button Send, Refresh, Pool, MainMenu;
    private Label ChatLabel, pID, OtherpIDLabel;
    private Notifier MessageSent_Notifier, MessageError_Notifier;
    private ListView Chat;
    private CheckBox DriverCB, NavigatorCB;
    private Web ChatWeb, PoolWeb;
    private WebViewer ChatsViewer;
    Integer status=-1;

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
        ChatsViewer.HeightPercent(40);
        ChatsViewer.HomeUrl(applicationSettings.default_baseURL +
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
        pIDHZ = new HorizontalArrangement(ChatWith);
        OtherpIDLabel = new Label(pIDHZ);
        OtherpIDLabel.Text(applicationSettings.otherpIDforChat);
        OtherpIDLabel.Visible(true);
        SendHZ = new HorizontalArrangement(ChatWith);
        Send = new Button(SendHZ);
        Send.Text("Send");
        CheckBoxHZ = new HorizontalArrangement(ChatWith);
        DriverCB = new CheckBox(CheckBoxHZ);
        DriverCB.Text("Driver");
        NavigatorCB = new CheckBox(CheckBoxHZ);
        NavigatorCB.Text("Navigator");
        PoolHZ = new HorizontalArrangement(ChatWith);
        Pool = new Button(PoolHZ);
        Pool.Text("Make Pool");


        ChatWeb = new Web(ChatWith);
        PoolWeb = new Web(ChatWith);

        EventDispatcher.registerEventForDelegation(this, formName, "Changed");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (eventName.equals("Changed")) {
            if (component.equals(DriverCB)) {
                if (DriverCB.Checked() == true) {
                    NavigatorCB.Checked(false);
                }
                return true;
            }
            else if (component.equals(NavigatorCB)) {
                if (NavigatorCB.Checked() == true) {
                    DriverCB.Checked(false);
                }
                return true;
            }
            return true;
        }
        else if (eventName.equals("Click")) {
            if (component.equals(MainMenu)) {
                finish();
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
                                applicationSettings.otherpIDforChat +
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
                if(DriverCB.Checked()) {
                    PoolWeb.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&driver_pID=" +
                                    applicationSettings.pID +
                                    "&navigator_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=2"
                    );
                    PoolWeb.Get();
                    return true;
                }
                if(NavigatorCB.Checked()) {
                    PoolWeb.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&driver_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&navigator_pID=" +
                                    applicationSettings.pID +
                                    "&rID=2"
                    );
                    PoolWeb.Get();
                    return true;
                }
            }
            return true;
        }
        else if (eventName.equals("GotText")) {
            if(component.equals(ChatWeb)) {
                if (web_ResultGotText(params[1].toString(),params[3].toString())) {
                    chatText.Text("");
                    callBackend();
                }
                else {
                    chatText.BackgroundColor(Component.COLOR_RED);
                    MessageError_Notifier.ShowMessageDialog("Error sending message, try again later", "Error", "Ok");
                }
                return true;
            }
            return true;
        }
        return true;

    }

    void callBackend() {
        ChatsViewer.GoHome();
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

    public boolean web_ResultGotText(String status, String textOfResponse) {
        String temp=new String();
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            temp = parser.getString("line_ID");
            if (!parser.getString("line_ID").equals("-1")) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            return false;
        }
        else {
            return false;
        }
    }
}
