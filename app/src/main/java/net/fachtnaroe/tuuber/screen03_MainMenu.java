package net.fachtnaroe.tuuber;

import android.graphics.Color;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.TableArrangement;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.WebViewer;

import org.json.JSONException;
import org.json.JSONObject;

public class screen03_MainMenu extends Form implements HandlesEventDispatching {

    private Button button_Routes, button_Matches, button_Pools, button_Conversations, button_Settings, button_TsAndCs, button_Experimental, button_LogOut;

    tuuber_Settings applicationSettings;
    tuuberCommonSubroutines tools;
    VerticalScrollArrangement MainMenu;
    Notifier notifier_Messages;
    Integer int_ColWidth, int_MenuStartRow=1;
    Web web_VersionCheck, web_MessageCheck;
    TableArrangement menu;
    Button[] button_Pad;
    WebViewer webview_Message;
    Clock clock_Main;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        tools=new tuuberCommonSubroutines(this);
        tools.dbg("Debug session: " + applicationSettings.IsDeveloperSession);
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            tools.dbg(e.toString());
        }
        clock_Main = new Clock(this);
        MainMenu = new VerticalScrollArrangement(this);
        MainMenu.WidthPercent(100);
        MainMenu.HeightPercent(100);
        notifier_Messages = new Notifier(MainMenu);

        menu=new TableArrangement(MainMenu);
        int_ColWidth = (this.Width()/3);
        int_MenuStartRow=3;

        menu.Columns(3);
        menu.Rows(16 + (int_MenuStartRow-1) );
        int int_NumButtonsToPad=7;

        menu.WidthPercent(100);
        menu.HeightPercent(100);

        //
        Label label_ScreenName = new Label(menu);
        if (applicationSettings.IsAdminSession) {
            label_ScreenName.Text("__Administrator__");
        }
        else if (applicationSettings.IsDeveloperSession) {
            label_ScreenName.Text("__Developer__");
        }
        else {
            label_ScreenName.Text("Main Menu");
        }
        label_ScreenName.TextAlignment(Component.ALIGNMENT_CENTER);
        label_ScreenName.TextColor(Color.parseColor(applicationSettings.string_ButtonColor));
        label_ScreenName.FontSize(applicationSettings.int_ButtonTextSize);
        label_ScreenName.Column(1);
        label_ScreenName.Row(int_MenuStartRow-2);

        Button button_PadTop=new Button(menu);
        button_PadTop.Column(1);
        button_PadTop.Row(int_MenuStartRow-1);
        button_PadTop.Text("");
        button_PadTop.Height(10);
        button_PadTop.Visible(true);
        button_PadTop.BackgroundColor(Component.COLOR_NONE);
        button_PadTop.WidthPercent(25);
        Image img_logo = new Image(menu);
        img_logo.Picture(applicationSettings.ourLogo);
        img_logo.Width(100);
        img_logo.Height(100);
        img_logo.Visible(true);
        img_logo.ScalePictureToFit(false);
        img_logo.Row(0);
        img_logo.Column(1);

        Integer int_SideOffset=15;
        Button button_PadLeft=new Button(menu);
        button_PadLeft.Column(0);
        button_PadLeft.Row(int_MenuStartRow);
        button_PadLeft.Text("");
        button_PadLeft.Height(10);
        button_PadLeft.Visible(true);
        button_PadLeft.BackgroundColor(Component.COLOR_NONE);
        button_PadLeft.WidthPercent(25);

        Button button_PadRight=new Button(menu);
        button_PadRight.Column(2);
        button_PadRight.Row(int_MenuStartRow);
        button_PadRight.Text("");
        button_PadRight.Height(10);
        button_PadRight.Visible(true);
        button_PadRight.BackgroundColor(Component.COLOR_NONE);
        button_PadRight.WidthPercent(25);

        button_Routes = new Button(menu);
        button_Routes.Row(int_MenuStartRow);
        button_Pad=new Button[int_NumButtonsToPad];
        for (int i=0;i< int_NumButtonsToPad; i++) {
            button_Pad[i] = new Button(menu);
            button_Pad[i].BackgroundColor(Component.COLOR_NONE);
            button_Pad[i].TextColor(Component.COLOR_BLACK);
            button_Pad[i].Text(String.valueOf(i));
            button_Pad[i].Height(10);
            button_Pad[i].Width(20);
            button_Pad[i].Column(1);
            button_Pad[i].Row(( int_MenuStartRow+1) + (i*2) );
        }

        button_Matches = new Button(menu);
        button_Matches.Row(int_MenuStartRow+2);
        button_Conversations = new Button(menu);
        button_Conversations.Row(int_MenuStartRow+4);
        button_Pools= new Button(menu);
        button_Pools.Row(int_MenuStartRow+6);
        button_Settings = new Button(menu);
        button_Settings.Row(int_MenuStartRow+8);
        button_TsAndCs = new Button(menu);
        button_TsAndCs.Row(int_MenuStartRow+10);
        button_Experimental = new Button(menu);
        button_Experimental.Row(int_MenuStartRow+12);
        if (!(applicationSettings.IsDeveloperSession || applicationSettings.IsAdminSession)) {
            button_Experimental.Visible(false);
        }

        button_LogOut = new Button(menu);
        button_LogOut.Row(int_MenuStartRow+14);

        tools.button_CommonFormatting(50,
                button_Routes, button_Matches,
                button_Conversations, button_Settings,
                button_Pools,
                button_TsAndCs, button_Experimental,
                button_LogOut);

        MainMenu.AlignHorizontal(Component.ALIGNMENT_CENTER);
        web_VersionCheck=new Web(this);
        web_MessageCheck=new Web(this);

        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "OtherScreenClosed" );
        fn_VersionCheck();
        fn_UI_Text();
    }

    void fn_UI_Text() {
        button_Routes.Text(tools.fn_téacs_aistriú("routes"));
        button_Matches.Text(tools.fn_téacs_aistriú("matches"));
        button_Conversations.Text(tools.fn_téacs_aistriú("conversations"));
        button_Pools.Text(tools.fn_téacs_aistriú("pools"));
        button_Settings.Text(tools.fn_téacs_aistriú("settings"));
        button_TsAndCs.Text(tools.fn_téacs_aistriú("terms_and_conditions"));
        button_Experimental.Text(tools.fn_téacs_aistriú("experimental"));
        button_LogOut.Text(tools.fn_téacs_aistriú("logout"));
    }

    void fn_VersionCheck() {
//        web_VersionCheck.Url(
//                applicationSettings.baseURL
//                        +"&cmd=vercheck"
//                        + "&ver="
//                        + applicationSettings.versionCode
//                );
//        web_VersionCheck.Get();
        web_MessageCheck.Url(
                applicationSettings.baseURL
                        +"?cmd=msgchk"
                        + "&ver="
                        + applicationSettings.versionCode
        );
        web_MessageCheck.Get();
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {

        tools.dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("BackPressed")) {
            return true;
        }
        else if (eventName.equals("OtherScreenClosed")) {
                applicationSettings.get();
                fn_UI_Text();
                this.recreate();
                return true;
        }
        else if (eventName.equals("Click")) {
            if (component.equals(button_Matches)) {
                switchFormWithStartValue("screen04_Matches",null   );
                return true;
            }
            else if (component.equals(button_Conversations)) {
                switchFormWithStartValue("screen05_Conversations",null );
                return true;
            }
            else if (component.equals(button_Routes)) {
                switchFormWithStartValue("screen07_Routes",null);
                return true;
            }
            else if (component.equals(button_Pools)) {
                switchFormWithStartValue("screen11_Pools",null);
                return true;
            }
            else if (component.equals(button_Settings)) {
                switchFormWithStartValue("screen09_Settings",null);
                this.recreate();
                return true;
            }
            else if (component.equals(button_TsAndCs)) {
                switchFormWithStartValue("screen10_TermsAndConditions","1");
                return true;
            }
            else if (component.equals(button_Experimental)) {
                switchFormWithStartValue("experimental_doNotUseThis",null);
                return true;
            }
            else if (component.equals(button_LogOut)) {
                screen01_Splash.finishApplication();
                screen02_Login.finishApplication();
                screen03_MainMenu.finishApplication();
                screen04_Matches.finishApplication();
                screen05_Conversations.finishApplication();
                screen06_Register.finishApplication();
                screen07_Routes.finishApplication();
                screen08_ChatWith.finishApplication();
                screen09_Settings.finishApplication();
                this.finishApplication();
                switchForm("screen02_Login");
                System.exit(0);
                return true;
            }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_VersionCheck) || (component.equals(web_MessageCheck))) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_VersionOrMessage (component, status, textOfResponse);
            }
        }
        return false;
    }

    void fn_GotText_VersionOrMessage(Component c, String status, String textOfResponse){
        tools.dbg(status);
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            tools.dbg(textOfResponse);
            if (!parser.getString("result").equals("OK")) {
                if (c.equals(web_MessageCheck)) {
                    // there's a message so
                    // add another row to the table,

                    menu.Rows( menu.Rows()  + 1);
                    webview_Message = new WebViewer(menu);
                    webview_Message.HomeUrl(
                            applicationSettings.baseURL
                                    +"?cmd=msg"
                                    + "&ver="
                                    + applicationSettings.versionCode
                    );

                    button_LogOut.Row( button_LogOut.Row() + 1);

                    webview_Message.Row(button_LogOut.Row() - 1);
                    webview_Message.Row(int_MenuStartRow-2);
                    webview_Message.Column(button_LogOut.Column());
                    webview_Message.Height( button_LogOut.Height() );
                    webview_Message.Width( button_LogOut.Width() );
                    webview_Message.FollowLinks(true);

                    webview_Message.GoToUrl(
                            applicationSettings.baseURL
                            +"?cmd=msg"
                            + "&ver="   + applicationSettings.versionCode
                            + "&viewH=" + Integer.valueOf(button_LogOut.Height())
                            + "&viewW=" + Integer.valueOf(button_LogOut.Width())
                    );


                }
            }
        }
        catch (JSONException e) {
            notifier_Messages.ShowAlert("JSON exception (" + textOfResponse + ")");
        }
    }


}
