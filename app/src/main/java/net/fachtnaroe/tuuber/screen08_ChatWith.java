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
    Clock timer_RefreshBackend;
    TextBox text_ChatLine;
    VerticalArrangement ChatWith;
    HorizontalArrangement hz_ChatLine, hz_PoolLine, pIDHZ, hz_ChatsViewer, hz_toolbar;
    Button button_SendText, button_Refresh, button_MakePool, MainMenu;
    Label pID, OtherpIDLabel, DriverOrNavigatorLabel, PoolID;
    Notifier D_OR_N_ChoiceNotifier, MessageError_Notifier, MessageSent_Notifier;
    Web web_Chat, web_PoolDriver, web_PoolNavigator, web_NoPoolCreated, web_PoolCreated;
    WebViewer webview_Chat;
    int int_RefreshBackendTimeInterval = 5000;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        ChatWith=new VerticalArrangement(this);
        this.BackgroundImage(applicationSettings.backgroundImageName);
        ChatWith.WidthPercent(100);
        ChatWith.HeightPercent(100);

        hz_toolbar = new HorizontalArrangement(ChatWith);
        MainMenu = new Button(hz_toolbar);
        MainMenu.Width(40);
        MainMenu.Height(40);
        MainMenu.Image("buttonHome.png");

        pID = new Label(hz_toolbar);
        pID.Text("I am user: #" + applicationSettings.pID);
        pID.Height(40);
        pID.FontSize(20);
        pID.WidthPercent(70);
        pID.TextAlignment(Component.ALIGNMENT_CENTER);

        button_Refresh = new Button(hz_toolbar);
        button_Refresh.Width(40);
        button_Refresh.Height(40);
        button_Refresh.FontSize(8);
        button_Refresh.Image("buttonRefresh.png");


        hz_ChatsViewer = new HorizontalArrangement(ChatWith);
        hz_ChatsViewer.AlignHorizontal(ALIGNMENT_CENTER);
        hz_ChatsViewer.WidthPercent(100);
        webview_Chat = new WebViewer(hz_ChatsViewer);
        webview_Chat.HeightPercent(40);
        webview_Chat.WidthPercent(100);

        webview_Chat.HomeUrl(
                applicationSettings.baseURL +
                "?action=LIST&entity=DISC&sessionID=" +
                applicationSettings.sessionID +
                "&showHtml=1" +
                "&iam=" + applicationSettings.pID +
                "&link_ID=" +
                applicationSettings.otherpIDforChat
        );
        dbg(webview_Chat.HomeUrl());
        hz_ChatLine = new HorizontalArrangement(ChatWith);
        hz_ChatLine.WidthPercent(100);
//        hz_ChatLine.AlignHorizontal(ALIGNMENT_OPPOSITE);
        text_ChatLine = new TextBox(hz_ChatLine);
        text_ChatLine.Text("");
        text_ChatLine.WidthPercent(85);
        button_SendText = new Button(hz_ChatLine);
        button_SendText.Text("but");
        button_SendText.WidthPercent(15);

        //        pIDHZ = new HorizontalArrangement(ChatWith);
//        OtherpIDLabel = new Label(pIDHZ);
//        OtherpIDLabel.Text(applicationSettings.otherpIDforChat);
//        OtherpIDLabel.Visible(true);
//        DriverOrNavigatorLabel = new Label(pIDHZ);
//        PoolID = new Label(pIDHZ);

        hz_PoolLine = new HorizontalArrangement(ChatWith);
        button_MakePool = new Button(hz_PoolLine);
        button_MakePool.Text("Make button_MakePool");

        D_OR_N_ChoiceNotifier = new Notifier(ChatWith);
        MessageError_Notifier = new Notifier(ChatWith);
        MessageSent_Notifier = new Notifier(ChatWith);
        web_Chat = new Web(ChatWith);
        web_PoolDriver = new Web(ChatWith);
        web_PoolNavigator = new Web(ChatWith);
        web_NoPoolCreated = new Web(ChatWith);
        web_PoolCreated = new Web(ChatWith);
        timer_RefreshBackend = new Clock(ChatWith);
        timer_RefreshBackend.TimerEnabled(false);
        timer_RefreshBackend.TimerInterval(int_RefreshBackendTimeInterval);
        timer_RefreshBackend.TimerEnabled(false);

        EventDispatcher.registerEventForDelegation(this, formName, "Timer");
        EventDispatcher.registerEventForDelegation(this, formName, "Changed");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterChoosing");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("Timer")) {
            callBackend();
        }
        else if (eventName.equals("AfterChoosing")) {
            if (component.equals(D_OR_N_ChoiceNotifier)) {
                D_OR_N_ChoiceNotifier.ShowMessageDialog("You have chosen " + params[0], "Chosen", "Ok");
                DriverOrNavigatorLabel.Text((String) params[0]);
                if (params[0].equals("Driver")) {
                    web_PoolDriver.Url(
                            applicationSettings.baseURL +
                                    "?action=GET&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&driver_pID=" +
                                    applicationSettings.pID +
                                    "&navigator_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=22"
                    );
                    web_PoolDriver.Get();
                    return true;
                }
                if (params[0].equals("Navigator")) {
                    web_PoolNavigator.Url(
                            applicationSettings.baseURL +
                                    "?action=GET&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&navigator_pID=" +
                                    applicationSettings.pID +
                                    "&driver_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=22"
                    );
                    web_PoolNavigator.Get();
                    return true;
                }
            }
        }
        else if (eventName.equals("Click")) {
            if (component.equals(MainMenu)) {
                finish();
                return true;
            }
            else if (component.equals(button_SendText)) {
                if (text_ChatLine.Text().equals("")) {
                    MessageError_Notifier.ShowMessageDialog("Fill in Textbox", "Error", "Ok");
                    callBackend();
                    return true;
                }
                else if (!text_ChatLine.Text().equals("")) {
                    web_Chat.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=CHAT&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&initiator_pID=" +
                                    applicationSettings.pID +
                                    "&respondent_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&status=open&text=" +
                                    text_ChatLine.Text()
                    );
                    web_Chat.Get();
                    return true;
                }
                return true;
            }
            else if (component.equals(button_Refresh)) {
                callBackend();
            }
            else if (component.equals(button_MakePool)) {
                D_OR_N_ChoiceNotifier.ShowChooseDialog("Are you a driver, or a navigator?", "Question:", "Navigator", "Driver", false);
            }
            return true;
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_Chat)) {
                if (web_ResultGotText(params[1].toString(), params[3].toString())) {
                    text_ChatLine.Text("");
                    MessageSent_Notifier.ShowMessageDialog("Message Successfully Sent", "Message Sent", "Ok");
                } else {
                    text_ChatLine.BackgroundColor(Component.COLOR_RED);
                    MessageError_Notifier.ShowMessageDialog("Error sending message, try again later", "Error", "Ok");
                }
                return true;
            }
            else if (component.equals(web_PoolDriver)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getPoolDriverList (status, textOfResponse);
                if (PoolID.Text().equals("")) {
                    web_NoPoolCreated.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&driver_pID=" +
                                    applicationSettings.pID +
                                    "&navigator_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=22"
                    );
                    web_NoPoolCreated.Get();
                    return true;
                }
                else if (!PoolID.Text().equals("")) {
                    web_PoolCreated.Url(
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
                    web_PoolCreated.Get();
                    return true;
                }
                return true;
            }
            else if (component.equals(web_PoolNavigator)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getPoolNavigatorList (status, textOfResponse);
                if (PoolID.Text().equals("")) {
                    web_NoPoolCreated.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&navigator_pID=" +
                                    applicationSettings.pID +
                                    "&driver_pID=" +
                                    applicationSettings.otherpIDforChat +
                                    "&rID=22"
                    );
                    web_NoPoolCreated.Get();
                    return true;
                }
                else if (!PoolID.Text().equals("")) {
                    web_PoolCreated.Url(
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
                    web_PoolCreated.Get();
                    return true;
                }
                return true;
            }
            return true;
        }
        return true;
    }

    void callBackend() {
        webview_Chat.GoHome();

//        webview_Chat.GoToUrl(applicationSettings.default_baseURL +
//                "?action=LIST&entity=chat&sessionID=" +
//                applicationSettings.sessionID +
//                "&initiator_pID=" +
//                applicationSettings.pID +
//                "&respondent_pID=" +
//                applicationSettings.otherpIDforChat +
//                "&showHtml=1" +
//                "&iam=" + applicationSettings.pID
//        );
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
          Request pool info, here process reply:
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
