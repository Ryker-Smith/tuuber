package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.ActivityStarter;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.VerticalArrangement;

public class screen03_MainMenu extends Form implements HandlesEventDispatching {

    private Button Routes, Matches, Chats, Settings, Terms;

    VerticalArrangement MainMenu;
    Notifier MessagesPopup;
    Image Header;

    protected void $define() {

        MainMenu = new VerticalArrangement(this);
        MainMenu.WidthPercent(100);
        MainMenu.HeightPercent(100);
        MessagesPopup = new Notifier(MainMenu);
        Routes = new Button(MainMenu);
        Routes.Text("Routes");
        Matches = new Button(MainMenu);
        Matches.Text("Matches");
        Chats = new Button(MainMenu);
        Chats.Text("Chats");
        Settings = new Button(MainMenu);
        Settings.Text("Settings");
        Terms = new Button(MainMenu);
        Terms.Text("Terms & Conditions");
        Header = new Image(MainMenu);
        Header.Picture("img_carlogo.png");

        EventDispatcher.registerEventForDelegation(this, "Routes", "Click");
        EventDispatcher.registerEventForDelegation(this, "Matches", "Click");
        EventDispatcher.registerEventForDelegation(this, "Chats", "Click");
        EventDispatcher.registerEventForDelegation(this, "Settings", "Click");
        EventDispatcher.registerEventForDelegation(this, "Terms", "Click");
        EventDispatcher.registerEventForDelegation(this, "", "BackPressed");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);
        if (eventName.equals("BackPressed")) {
            return true;
        }
         else if (eventName.equals("Click")) {
             if (componentName.equals("Matches")) {
                dbg("Matches");
                switchForm("screen04_Matches");
                return true;
            }
            else if (componentName.equals("Chats")) {
                dbg("Chats");
                switchForm("screen05_Chats");
                return true;
            }
            else if (componentName.equals("Routes")) {
                dbg("Routes");
                switchForm("screen07_Routes");
                return true;
            }
            else if (componentName.equals("Settings")) {
                dbg("Settings");
                switchForm("screen09_Settings");
                return true;
            }
            else if (componentName.equals("Terms")) {
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
}
