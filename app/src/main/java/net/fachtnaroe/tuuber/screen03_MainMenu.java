package net.fachtnaroe.tuuber;

import android.content.Intent;
import android.content.res.Resources;

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

import static android.support.v4.content.res.TypedArrayUtils.getResourceId;

public class screen03_MainMenu extends Form implements HandlesEventDispatching {

    private Button Routes, Matches, Conversations, Settings, Terms, experimental, buttonLogOut;

    tuuber_Settings applicationSettings;
    boolean form_made=false;
    VerticalArrangement MainMenu;
    Notifier MessagesPopup;
    ArrayList<HorizontalArrangement> hz;//= new Array ();

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        MainMenu = new VerticalArrangement(this);
        MainMenu.Image(applicationSettings.backgroundImageName);
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
        experimental= new Button(MainMenu);
        experimental.Text("Experimental Stuff");
        buttonLogOut = new Button(MainMenu);
        buttonLogOut.Text("Log Out");
        MainMenu.AlignHorizontal(Component.ALIGNMENT_CENTER);
        hz=new ArrayList();

        button_CommonFormatting(Routes, Matches, Conversations, Settings, Terms, experimental,buttonLogOut);

        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "OtherScreenClosed" );
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {

//        dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("BackPressed")) {
            return true;
        }
        else if (eventName.equals("OtherScreenClosed")) {
            if (params[0].equals("screen09_Settings")) {
                applicationSettings.get();
//                this.recreate();
                return true;
            }
        }
        else if (eventName.equals("Click")) {
            if (component.equals(Matches)) {
                // eg startActivity(new Intent().setClass(this, Screen2.class).putExtra("startValue", "2"));
                // finishActivityWithResult("elbow");
                // finishActivityWithTextResult();
                startNewForm("screen04_Matches",null   );
                return true;
            }
            else if (component.equals(Conversations)) {
                startNewForm("screen05_Conversations",null );
                return true;
            }
            else if (component.equals(Routes)) {
                startNewForm("screen07_Routes",null);
                return true;
            }
            else if (component.equals(Settings)) {
                startNewForm("screen09_Settings",null);
                return true;
            }
            else if (component.equals(Terms)) {
                startNewForm("screen10_TermsAndConditions",null);
                return true;
            }
            else if (component.equals(experimental)) {
                startNewForm("experimental_doNotUseThis",null);
                return true;
            }
            else if (component.equals(buttonLogOut)) {
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
            b[i].FontBold(true);
            b[i].TextColor(Component.COLOR_WHITE);
            b[i].Shape(BUTTON_SHAPE_ROUNDED);
            i++;
        }
    }

}
