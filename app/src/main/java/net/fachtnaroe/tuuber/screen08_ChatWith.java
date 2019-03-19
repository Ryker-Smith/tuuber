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
    private tuuberCommonSubroutines tools;

    Clock timer_RefreshBackend;
    TextBox text_ChatLine;
    VerticalArrangement ChatWith;
    HorizontalArrangement hz_ChatLine, hz_PoolLine, hz_ChatsViewer, hz_toolbar;
    Button button_SendText, button_Refresh, button_MakePool, button_MainMenu;
    Label label_pID, DriverOrNavigatorLabel, PoolID;
    Notifier D_OR_N_ChoiceNotifier, MessageError_Notifier, MessageSent_Notifier;
    Web web_ChatLine, web_PoolDriver, web_PoolNavigator, web_NoPoolCreated, web_PoolCreated, web_GetTheRouteId;
    WebViewer webview_Chat;
    int int_RefreshBackendTimeInterval = 5000;
    String string_URLOfConversation, string_URLOfLink, string_ThisRouteId, string_ThisPoolID;
    Integer int_ClockCount=0;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            tools.dbg(e.toString());
        }
        tools = new tuuberCommonSubroutines(this);

        ChatWith=new VerticalArrangement(this);
        ChatWith.WidthPercent(100);
        ChatWith.HeightPercent(100);

        hz_toolbar = new HorizontalArrangement(ChatWith);
        button_MainMenu = new Button(hz_toolbar);
        button_MainMenu.Width(40);
        button_MainMenu.Height(40);
        button_MainMenu.Image(applicationSettings.ourLogo);

        label_pID =tools.fn_HeadingLabel(hz_toolbar, label_pID, applicationSettings.pID,"Chat");

        button_Refresh = new Button(hz_toolbar);
        button_Refresh.Width(40);
        button_Refresh.Height(40);
        button_Refresh.FontSize(8);
        button_Refresh.Image("buttonRefresh.png");

        hz_ChatsViewer = new HorizontalArrangement(ChatWith);
        hz_ChatsViewer.AlignHorizontal(ALIGNMENT_CENTER);
        hz_ChatsViewer.WidthPercent(100);
        webview_Chat = new WebViewer(hz_ChatsViewer);
        webview_Chat.HeightPercent(60);
        webview_Chat.WidthPercent(100);

        string_URLOfConversation =
                applicationSettings.baseURL +
                        "?action=LIST&entity=DISC&sessionID=" +
                        applicationSettings.sessionID +
                        "&showHtml=1" +
                        "&iam=" + applicationSettings.pID +
                        "&link_ID=" +
                        applicationSettings.CurrentLinkId;

        string_URLOfLink =
                applicationSettings.baseURL +
                        "?action=GET&entity=LINK&sessionID=" +
                        applicationSettings.sessionID +
                        "&showHtml=1" +
                        "&iam=" + applicationSettings.pID +
                        "&link_ID=" +
                        applicationSettings.CurrentLinkId;

        webview_Chat.HomeUrl(string_URLOfConversation);
        tools.dbg(webview_Chat.HomeUrl());
        hz_ChatLine = new HorizontalArrangement(ChatWith);
        hz_ChatLine.WidthPercent(100);
        text_ChatLine = new TextBox(hz_ChatLine);
        text_ChatLine.Text("");

        button_SendText = new Button(hz_ChatLine);
        button_SendText.Image("buttonSend.png");
        button_SendText.Width(35);
        button_SendText.Height(35);
        text_ChatLine.Width((this.Width() - button_SendText.getSetWidth() -1));

        hz_PoolLine = new HorizontalArrangement(ChatWith);
        button_MakePool = new Button(hz_PoolLine);
        button_MakePool.Text("Make Pool");

        D_OR_N_ChoiceNotifier = new Notifier(ChatWith);
        MessageError_Notifier = new Notifier(ChatWith);
        MessageSent_Notifier = new Notifier(ChatWith);
        web_ChatLine = new Web(ChatWith);
        web_PoolDriver = new Web(ChatWith);
        web_PoolNavigator = new Web(ChatWith);
        web_NoPoolCreated = new Web(ChatWith);
        web_PoolCreated = new Web(ChatWith);
        timer_RefreshBackend = new Clock(ChatWith);
        timer_RefreshBackend.TimerEnabled(false);
        timer_RefreshBackend.TimerInterval(int_RefreshBackendTimeInterval);
        timer_RefreshBackend.TimerEnabled(true);

        web_GetTheRouteId = new Web(this);
        web_GetTheRouteId.Url( string_URLOfLink );
        web_GetTheRouteId.Get();

        tools.button_CommonFormatting(40, button_MakePool);
        EventDispatcher.registerEventForDelegation(this, formName, "AfterChoosing");
        EventDispatcher.registerEventForDelegation(this, formName, "Timer");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        tools.dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("Timer")) {
            int_ClockCount++;
            callBackend();
        }
        else if (eventName.equals("AfterChoosing")) {
            if (component.equals(D_OR_N_ChoiceNotifier)) {
                D_OR_N_ChoiceNotifier.ShowMessageDialog("You have chosen " + params[0], "Chosen", "Ok");
                if (params[0].equals("Driver")) {
                    web_PoolDriver.Url(
                            applicationSettings.baseURL +
                                    "?action=GET&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&driver_pID=" +
                                    applicationSettings.pID +
                                    "&navigator_pID=" +
                                    applicationSettings.CurrentLinkId +
                                    "&rID=" +
                                    string_ThisRouteId
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
                                    applicationSettings.CurrentLinkId +
                                    "&rID=" +
                                    string_ThisRouteId
                    );
                    web_PoolNavigator.Get();
                    return true;
                }
            }
        }
        else if (eventName.equals("Click")) {
            if (component.equals(button_MainMenu)) {
                switchForm("screen03_MainMenu");
                return true;
            }
            else if (component.equals(button_SendText)) {
                if (text_ChatLine.Text().equals("")) {
//                    MessageError_Notifier.ShowMessageDialog("Fill in Textbox", "Error", "Ok");

                    callBackend();
                    return true;
                }
                else if (!text_ChatLine.Text().equals("")) {
                    web_ChatLine.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=DISC&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&iam=" +
                                    applicationSettings.pID +
                                    "&link_ID=" +
                                    applicationSettings.CurrentLinkId +
                                    "&text=" +
                                    text_ChatLine.Text()
                    );
                    web_ChatLine.Get();
                    return true;
                }
                return true;
            }
            else if (component.equals(button_Refresh)) {
                webview_Chat.GoToUrl( webview_Chat.CurrentUrl());
                callBackend();
            }
            else if (component.equals(button_MakePool)) {
                D_OR_N_ChoiceNotifier.ShowChooseDialog("Are you a driver, or a navigator?", "Question:", "Navigator", "Driver", false);
            }
            return true;
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_ChatLine)) {
                if (web_ResultGotText(params[1].toString(), params[3].toString())) {
                    text_ChatLine.BackgroundColor(Component.COLOR_NONE);
                    text_ChatLine.Text("");
//                    MessageSent_Notifier.ShowMessageDialog("Message Successfully Sent", "Message Sent", "Ok");
                    callBackend();
                } else {
                    text_ChatLine.BackgroundColor(Component.COLOR_RED);
                    MessageError_Notifier.ShowMessageDialog("Error sending message, try again later", "Error", "Ok");
                }
                return true;
            }
            if (component.equals(web_GetTheRouteId)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_GetTheRouteId(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_PoolDriver)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getPoolDriverList (status, textOfResponse);
                if (string_ThisPoolID.equals("")) {
                    web_NoPoolCreated.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&driver_pID=" +
                                    applicationSettings.pID +
                                    "&navigator_pID=" +
                                    applicationSettings.CurrentLinkId +
                                    "&rID=" +
                                    string_ThisRouteId +
                                    "&pool_Status=init"
                    );
                    web_NoPoolCreated.Get();
                    return true;
                }
                else if (!string_ThisPoolID.equals("")) {
                    web_PoolCreated.Url(
                            applicationSettings.baseURL +
                                    "?action=PUT&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&driver_pID=" +
                                    applicationSettings.pID +
                                    "&navigator_pID=" +
                                    applicationSettings.CurrentLinkId +
                                    "&rID=" +
                                    string_ThisRouteId +
                                    "&pool_ID=" +
                                    PoolID.Text() +
                                    "&pool_Status=open"
                    );
                    web_PoolCreated.Get();
                    return true;
                }
                return true;
            }
            else if (component.equals(web_PoolNavigator)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getPoolNavigatorList (status, textOfResponse);
                if (string_ThisPoolID.equals("")) {
                    web_NoPoolCreated.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&navigator_pID=" +
                                    applicationSettings.pID +
                                    "&driver_pID=" +
                                    applicationSettings.CurrentLinkId +
                                    "&rID=" +
                                    string_ThisRouteId +
                                    "&pool_Status=init"
                    );
                    web_NoPoolCreated.Get();
                    return true;
                }
                else if (!string_ThisPoolID.equals("")) {
                    web_PoolCreated.Url(
                            applicationSettings.baseURL +
                                    "?action=PUT&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&navigator_pID=" +
                                    applicationSettings.pID +
                                    "&driver_pID=" +
                                    applicationSettings.CurrentLinkId +
                                    "&rID=" +
                                    string_ThisRouteId +
                                    "&pool_ID=" +
                                    PoolID.Text() +
                                    "&pool_Status=open"
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
        webview_Chat.GoToUrl(string_URLOfConversation + "&clock=" + int_ClockCount);
        tools.dbg(string_URLOfConversation + "&clock=" + int_ClockCount);
    }

    void fn_GotText_GetTheRouteId(String status, String textOfResponse) {
        tools.dbg(textOfResponse);
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (parser.getInt("rID") > 0) {
                string_ThisRouteId = parser.getString("rID");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
        }
        else {
        }
    }

    public boolean web_ResultGotText(String status, String textOfResponse) {
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("result").equals("OK")) {
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
        tools.dbg(status);
        tools.dbg("PoolID: " + textOfResponse);
        /*
          Request pool info, here prcess reply:
          if no pool, send POST to make pool
          else get pool_ID, send PUT to amend status
           */
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("pool_ID").equals("")) {
                string_ThisPoolID = parser.getString("pool_ID");
            }
        }
        catch (JSONException e) {
        }
    }

    public void getPoolNavigatorList (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        tools.dbg(status);
        tools.dbg("PoolID: " + textOfResponse);
        /*
          Request pool info, here process reply:
          if no pool, send POST to make pool
          else get pool_ID, send PUT to amend status

           */
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("pool_ID").equals("")) {
                string_ThisPoolID = parser.getString("pool_ID");
            }
        }
        catch (JSONException e) {
        }
    }

}
