package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.util.AlignmentUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class screen03_MainMenu extends Form implements HandlesEventDispatching {

    private Button Routes, Matches, Conversations, Settings, Terms;

    tuuber_Settings applicationSettings;
    VerticalArrangement MainMenu;
    Notifier MessagesPopup;
    ArrayList<HorizontalArrangement> hz;//= new Array ();

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        MainMenu = new VerticalArrangement(this);
        MainMenu.Image(applicationSettings.backgroundImageName);;
        MainMenu.WidthPercent(100);
        MainMenu.HeightPercent(100);
        MessagesPopup = new Notifier(MainMenu);
        Routes = new Button(MainMenu);
        Routes.Text("Routes");
        Matches = new Button(MainMenu);
        Matches.Text("Matches");
        Conversations = new Button(MainMenu);
        Conversations.Text("Conversations");
        Settings = new Button(MainMenu);
        Settings.Text("Settings");
        Terms = new Button(MainMenu);
        Terms.Text("Terms & Conditions");
        MainMenu.AlignHorizontal(Component.ALIGNMENT_CENTER);
        hz=new ArrayList();

        button_CommonFormatting(Routes, Matches, Conversations, Settings, Terms);

        EventDispatcher.registerEventForDelegation(this, "", "BackPressed");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {

        dbg("dispatchEvent: " + formName + " "  + eventName);
        if (eventName.equals("BackPressed")) {
            return true;
        }
         else if (eventName.equals("Click")) {
             if (component.equals(Matches)) {
                dbg("Matches");
                switchForm("screen04_Matches");
                return true;
            }
            else if (component.equals(Conversations)) {
                dbg("Conversations");
                switchForm("screen05_Conversations");
                return true;
            }
            else if (component.equals(Routes)) {
                dbg("Routes");
                switchForm("screen07_Routes");
                return true;
            }
            else if (component.equals(Settings)) {
                dbg("Settings");
                switchForm("screen09_Settings");
                return true;
            }
            else if (component.equals(Terms)) {
                dbg("Terms");
                switchForm("screen10_TermsAndConditions");
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

        while ((i < len) && (b[i] != null)) {
            hz.add(new HorizontalArrangement(MainMenu));
            b[i].WidthPercent(50);
            b[i].BackgroundColor(Component.COLOR_BLACK);
            b[i].FontBold(true);
            b[i].TextColor(Component.COLOR_WHITE);
            b[i].Shape(BUTTON_SHAPE_ROUNDED);
            EventDispatcher.registerEventForDelegation(this.getDispatchDelegate(), b[i].toString(), "Click");
            i++;
        }
    }
}
