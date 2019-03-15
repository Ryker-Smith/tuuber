package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
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
    private Clock TimerRefreshBackend;
    private TextBox chatText;
    private VerticalArrangement ChatWith;
    private HorizontalArrangement SendHZ, PoolHZ, pIDHZ, ChatsViewerHZ, toolbarHz;
    private Button Send, Refresh, Pool, MainMenu;
    private Label pID, OtherpIDLabel, DriverOrNavigatorLabel, PoolID;
    private Notifier D_OR_N_ChoiceNotifier, MessageError_Notifier, MessageSent_Notifier;
    private Web ChatWeb, PoolWebDriver, PoolWebNavigator, NoPoolCreatedWeb, PoolCreatedWeb;
    private WebViewer ChatsViewer;
    int TimeToRefresh = 5000;


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
        ChatsViewerHZ.AlignHorizontal(ALIGNMENT_CENTER);
        ChatsViewerHZ.WidthPercent(100);
        ChatsViewer = new WebViewer(ChatsViewerHZ);
        ChatsViewer.HeightPercent(40);
        ChatsViewer.WidthPercent(100);
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
        chatText.WidthPercent(100);
        pIDHZ = new HorizontalArrangement(ChatWith);
        OtherpIDLabel = new Label(pIDHZ);
        OtherpIDLabel.Text(applicationSettings.otherpIDforChat);
        OtherpIDLabel.Visible(true);
        DriverOrNavigatorLabel = new Label(pIDHZ);
        PoolID = new Label(pIDHZ);

        SendHZ = new HorizontalArrangement(ChatWith);
        SendHZ.WidthPercent(100);
        SendHZ.AlignHorizontal(ALIGNMENT_OPPOSITE);
        Send = new Button(SendHZ);
        Send.Text("Send");
        PoolHZ = new HorizontalArrangement(ChatWith);
        Pool = new Button(PoolHZ);
        Pool.Text("Make Pool");

        D_OR_N_ChoiceNotifier = new Notifier(ChatWith);
        MessageError_Notifier = new Notifier(ChatWith);
        MessageSent_Notifier = new Notifier(ChatWith);
        ChatWeb = new Web(ChatWith);
        PoolWebDriver = new Web(ChatWith);
        PoolWebNavigator = new Web(ChatWith);
        NoPoolCreatedWeb = new Web(ChatWith);
        PoolCreatedWeb = new Web(ChatWith);
        TimerRefreshBackend = new Clock(ChatWith);
        TimerRefreshBackend.TimerEnabled(false);
        TimerRefreshBackend.TimerInterval(TimeToRefresh);
        TimerRefreshBackend.TimerEnabled(true);

        EventDispatcher.registerEventForDelegation(this, formName, "Timer");
        EventDispatcher.registerEventForDelegation(this, formName, "Changed");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterChoosing");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (eventName.equals("Timer")) {
            callBackend();
        }
        if (eventName.equals("AfterChoosing")) {
            if (component.equals(D_OR_N_ChoiceNotifier)) {
                D_OR_N_ChoiceNotifier.ShowMessageDialog("You have chosen " + params[0], "Chosen", "Ok");
                DriverOrNavigatorLabel.Text((String) params[0]);
                if (params[0].equals("Driver")) {
                    PoolWebDriver.Url(
                            applicationSettings.baseURL +
                                    "?action=GET&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&driver_pID=" +
                                    applicationSettings.pID +
                                    "&navigator_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=22"
                    );
                    PoolWebDriver.Get();
                    return true;
                }
                if (params[0].equals("Navigator")) {
                    PoolWebNavigator.Url(
                            applicationSettings.baseURL +
                                    "?action=GET&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&navigator_pID=" +
                                    applicationSettings.pID +
                                    "&driver_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=22"
                    );
                    PoolWebNavigator.Get();
                    return true;
                }
            }
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
            else if (component.equals(Refresh)) {
                callBackend();
            }
            else if (component.equals(Pool)) {
                D_OR_N_ChoiceNotifier.ShowChooseDialog("Are you a driver, or a navigator?", "Question:", "Navigator", "Driver", false);
            }
            return true;
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(ChatWeb)) {
                if (web_ResultGotText(params[1].toString(), params[3].toString())) {
                    chatText.Text("");
                    MessageSent_Notifier.ShowMessageDialog("Message Successfully Sent", "Message Sent", "Ok");
                } else {
                    chatText.BackgroundColor(Component.COLOR_RED);
                    MessageError_Notifier.ShowMessageDialog("Error sending message, try again later", "Error", "Ok");
                }
                return true;
            }
            else if (component.equals(PoolWebDriver)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getPoolDriverList (status, textOfResponse);
                if (PoolID.Text().equals("")) {
                    NoPoolCreatedWeb.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&driver_pID=" +
                                    applicationSettings.pID +
                                    "&navigator_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=22"
                    );
                    NoPoolCreatedWeb.Get();
                    return true;
                }
                else if (!PoolID.Text().equals("")) {
                    PoolCreatedWeb.Url(
                            applicationSettings.baseURL +
                                    "?action=PUT&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&driver_pID=" +
                                    applicationSettings.pID +
                                    "&navigator_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=22" +
                                    "&pool_ID=" +
                                    PoolID.Text() +
                                    "&pool_Status=1"
                    );
                    PoolCreatedWeb.Get();
                    return true;
                }
                return true;
            }
            else if (component.equals(PoolWebNavigator)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getPoolNavigatorList (status, textOfResponse);
                if (PoolID.Text().equals("")) {
                    NoPoolCreatedWeb.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&navigator_pID=" +
                                    applicationSettings.pID +
                                    "&driver_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=22"
                    );
                    NoPoolCreatedWeb.Get();
                    return true;
                }
                else if (!PoolID.Text().equals("")) {
                    PoolCreatedWeb.Url(
                            applicationSettings.baseURL +
                                    "?action=PUT&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&navigator_pID=" +
                                    applicationSettings.pID +
                                    "&driver_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=22" +
                                    "&pool_ID=" +
                                    PoolID.Text() +
                                    "&pool_Status=1"
                    );
                    PoolCreatedWeb.Get();
                    return true;
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

    public void getPoolDriverList (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        dbg(status);
        dbg("PoolID: " + textOfResponse);
        /*
          Request pool info, here prcess reply:
          if no pool, send POST to make pool
          else get pool_ID, send PUT to amend status

           */
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("pool_ID").equals("")) {
                PoolID.Text(parser.getString("pool_ID"));
            }
        }
        catch (JSONException e) {
        }
    }

    public void getPoolNavigatorList (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        dbg(status);
        dbg("PoolID: " + textOfResponse);
        /*
          Request pool info, here prcess reply:
          if no pool, send POST to make pool
          else get pool_ID, send PUT to amend status

           */
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("pool_ID").equals("")) {
                PoolID.Text(parser.getString("pool_ID"));
            }
        }
        catch (JSONException e) {
        }
    }

    void dbg (String debugMsg) {
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }
}
