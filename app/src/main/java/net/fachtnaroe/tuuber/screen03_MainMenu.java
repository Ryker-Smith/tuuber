package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.TableArrangement;
import com.google.appinventor.components.runtime.VerticalArrangement;

public class screen03_MainMenu extends Form implements HandlesEventDispatching {

    private Button button_Routes, button_Matches, button_Conversations, button_Settings, button_TsAndCs, button_Experimental, button_LogOut;

    tuuber_Settings applicationSettings;
    VerticalArrangement MainMenu;
    Notifier MessagesPopup;
    Integer int_ColWidth;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        dbg("Debug session: " + applicationSettings.IsDebugSession);
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            dbg(e.toString());
        }

        MainMenu = new VerticalArrangement(this);
        MainMenu.WidthPercent(100);
        MainMenu.HeightPercent(100);
        MessagesPopup = new Notifier(MainMenu);

        TableArrangement menu=new TableArrangement(MainMenu);

        int_ColWidth = (this.Width()/3);

        menu.Columns(3);
        menu.Rows(14);
        menu.WidthPercent(100);
        menu.HeightPercent(100);

        Integer int_SideOffset=15;
        Button button_PadLeft=new Button(menu);
        button_PadLeft.Column(0);
        button_PadLeft.Row(0);
        button_PadLeft.Text("");
        button_PadLeft.Visible(true);
        button_PadLeft.BackgroundColor(Component.COLOR_NONE);
        button_PadLeft.WidthPercent(25);

        Button button_PadRight=new Button(menu);
        button_PadRight.Column(2);
        button_PadRight.Row(0);
        button_PadRight.Text("");
        button_PadRight.Visible(true);
        button_PadRight.BackgroundColor(Component.COLOR_NONE);
        button_PadRight.WidthPercent(25);

        button_Routes = new Button(menu);
        button_Routes.Text("Routes");
        button_Routes.Row(1);

        int int_NumButtonsToPad=6;
//        if (applicationSettings.IsDebugSession) {
//            int_NumButtonsToPad--;
//        }
        Button[] button_Pad=new Button[int_NumButtonsToPad];
        for (int i=0;i< int_NumButtonsToPad; i++) {
            button_Pad[i] = new Button(menu);
            button_Pad[i].BackgroundColor(Component.COLOR_NONE);
            button_Pad[i].TextColor(Component.COLOR_BLACK);
            button_Pad[i].Text(String.valueOf(i));
            button_Pad[i].Height(10);
            button_Pad[i].Width(20);
            button_Pad[i].Column(1);
            button_Pad[i].Row(2 + (i*2) );
        }

        button_Matches = new Button(menu);
        button_Matches.Text("Matches");
        button_Matches.Row(3);

        button_Conversations = new Button(menu);
        button_Conversations.Text("Conversations");
        button_Conversations.Row(5);

        button_Settings = new Button(menu);
        button_Settings.Text("Settings");
        button_Settings.Row(7);

        button_TsAndCs = new Button(menu);
        button_TsAndCs.Text("Terms & Conditions");
        button_TsAndCs.Row(9);

        button_Experimental = new Button(menu);
        button_Experimental.Text("Experimental Stuff");
        button_Experimental.Row(11);

        if (!applicationSettings.IsDebugSession) {
            button_Experimental.Visible(false);
        }

        button_LogOut = new Button(menu);
        button_LogOut.Text("Log Out");
        button_LogOut.Row(13);

        button_CommonFormatting(
                button_Routes, button_Matches,
                button_Conversations, button_Settings,
                button_TsAndCs, button_Experimental,
                button_LogOut);

        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "OtherScreenClosed" );
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {

        dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("BackPressed")) {
            return true;
        }
        else if (eventName.equals("OtherScreenClosed")) {
            if (params[0].equals("screen09_Settings")) {
                applicationSettings.get();
                return true;
            }
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
            else if (component.equals(button_Settings)) {
                switchFormWithStartValue("screen09_Settings",null);
                return true;
            }
            else if (component.equals(button_TsAndCs)) {
                switchFormWithStartValue("screen10_TermsAndConditions",null);
                return true;
            }
            else if (component.equals(button_Experimental)) {
                switchFormWithStartValue("experimental_doNotUseThis",null);
                return true;
            }
            else if (component.equals(button_LogOut)) {
                screen05_Conversations.finishApplication();

                screen07_Routes.finishApplication();
                screen06_Register.finishApplication();
                this.finishApplication();
                switchForm("screen02_Login");
                System.exit(0);
                return true;
            }
        }
        return false;
    }

    void dbg (String debugMsg) {
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }

    void button_CommonFormatting(Button... b) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i=0;
        int len = b.length;
//        https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
        while ((i < len) && (b[i] != null)) {
            b[i].WidthPercent(50);
            b[i].BackgroundColor(Component.COLOR_BLACK);
            b[i].FontBold(false);
            b[i].TextColor(Component.COLOR_WHITE);
            b[i].Shape(BUTTON_SHAPE_ROUNDED);
            b[i].FontSize(12);
            b[i].Column(1);
            b[i].Width(int_ColWidth);
            i++;
        }
    }

}
