package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import android.graphics.Color;

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
    Notifier notifier_Drive_OR_Navigate_Choice, notifier_MessageError, notifier_MessageSent, notifier_PrecautionsTaken;
    Web web_ChatLine, web_PoolMakeNew, web_PoolNavigator, web_NoPoolCreated, web_PoolCreated, web_GetTheRouteId;
    WebViewer webview_Chat;
    int int_RefreshBackendTimeInterval = 5000;
    String string_URLOfConversation, string_URLOfLink, string_ThisRouteId, string_ThisPoolDriverID, string_ThisPoolNavigatorID, string_DriverOrNavigator, string_Precautions, WhoIsDriving;;
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

        notifier_Drive_OR_Navigate_Choice = new Notifier(ChatWith);
        notifier_MessageError = new Notifier(ChatWith);
        notifier_MessageSent = new Notifier(ChatWith);
        notifier_PrecautionsTaken=new Notifier(ChatWith);
        web_ChatLine = new Web(ChatWith);
        web_PoolMakeNew = new Web(ChatWith);
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
            if (component.equals(notifier_Drive_OR_Navigate_Choice)) {
                String string_Precautions;
                string_DriverOrNavigator=(String)params[0];
                if (string_DriverOrNavigator.equals("Driver")) {
                    string_Precautions= "I am a licenced driver; the vehicle I will use is fully insured and is roadworthy.";
                    WhoIsDriving = applicationSettings.pID;
                }
                else {
                    string_Precautions="I acknowledge that it is my responsibility to verify the identity of the driver, and that it is my responsibility to confirm that the vehicle is insured and roadworthy.";
                    WhoIsDriving="other";
                }
                notifier_PrecautionsTaken.ShowChooseDialog(string_Precautions, "Question:", "I accept", "Cancel", false);

            }
            else if (component.equals(notifier_PrecautionsTaken)) {
                String string_PrecautionsTaken=(String)params[0];
                // better handling of responses to questions is required, esp re localization
                if (string_PrecautionsTaken.equals("I accept")) {
                    notifier_MessageError.BackgroundColor(Color.parseColor(applicationSettings.string_ColorGood));
                    notifier_MessageError.ShowAlert("You're the " + string_DriverOrNavigator + "\nSaving your preference now.");
                    web_PoolMakeNew.Url(
                            applicationSettings.baseURL +
                                    "?action=POST&entity=pool&sessionID=" +
                                    applicationSettings.sessionID +
                                    "&link_ID=" +
                                    applicationSettings.CurrentLinkId +
                                    "&driver=" +
                                    WhoIsDriving +
                                    "&pID=" + applicationSettings.pID
                    );
                    web_PoolMakeNew.Get();
                }
                else {
                    notifier_MessageError.BackgroundColor(Color.parseColor(applicationSettings.string_ColorBad));
                    notifier_MessageError.ShowAlert("Pooling abandoned");
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
//                    notifier_MessageError.ShowMessageDialog("Fill in Textbox", "Error", "Ok");

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
                notifier_Drive_OR_Navigate_Choice.ShowChooseDialog("Are you the driver, or the navigator?", "Question:", "Navigator", "Driver", false);
            }
            return true;
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_ChatLine)) {
                if (web_ResultGotText(params[1].toString(), params[3].toString())) {
                    text_ChatLine.BackgroundColor(Component.COLOR_NONE);
                    text_ChatLine.Text("");
                    callBackend();
                } else {
                    text_ChatLine.BackgroundColor(Component.COLOR_RED);
                    notifier_MessageError.ShowMessageDialog("Error sending message, try again later", "Error", "Ok");
                }
                return true;
            }
            else if (component.equals(web_GetTheRouteId)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_GetTheRouteId(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_PoolMakeNew)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getPoolDriverList (status, textOfResponse);
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

    void dbg(String debugMsg) { System.err.print("~~~> " + debugMsg + " <~~~\n");  }

    public void getPoolDriverList (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        tools.dbg(status);
        tools.dbg("PoolID: " + textOfResponse);
        /*
          Request pool info, here prcess reply:
          if no pool, send POST to make pool
          else get pool_ID, send PUT to amend status
           */
        dbg("Error at DriverID");

        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("pool_ID").equals("")) {
                string_ThisPoolDriverID = parser.getString("pool_ID");
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
        dbg("Error at NavigatorID");
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("pool_ID").equals("")) {
                string_ThisPoolNavigatorID = parser.getString("pool_ID");
            }
        }
        catch (JSONException e) {
        }
    }

}
