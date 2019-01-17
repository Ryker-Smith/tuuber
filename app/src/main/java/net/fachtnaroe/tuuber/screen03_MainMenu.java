package net.fachtnaroe.tuuber;

import android.preference.PreferenceActivity;

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

    private Button Routes, Matches;

    VerticalArrangement MainMenu;
    Notifier Messages;
    Image Header;


    protected void $define() {

        Routes = new Button(this);
        Routes.Text("Routes");
        Matches = new Button(this);
        Matches.Text("Matches");

        MainMenu = new VerticalArrangement(this);
        MainMenu.WidthPercent(100);
        MainMenu.HeightPercent(100);
        MainMenu.Image("img_splashcanvas.png");
        Messages = new Notifier(MainMenu);

        Header = new Image(MainMenu);
        Header.Picture("img_carlogo.png");

        EventDispatcher.registerEventForDelegation(this, "Routes", "Click");
        EventDispatcher.registerEventForDelegation(this, "Matches", "Click");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params){
        if (component.equals(Matches) && eventName.equals("Click")) {

            ActivityStarter nextScreen = new ActivityStarter(this);
            nextScreen.ActivityClass("net.fachtnaroe.tuuber.screen04_Matches");
            nextScreen.ExtraValue("2");
            nextScreen.ActivityPackage("net.fachtnaroe.tuuber");
            nextScreen.StartActivity();
            return true;
        }

        else if (component.equals(Routes) && eventName.equals("Click")) {

            ActivityStarter nextScreen = new ActivityStarter(this);
            nextScreen.ActivityClass("net.fachtnaroe.tuuber.screen07_Routes");
            nextScreen.ActivityPackage("net.fachtnaroe.tuuber");
            nextScreen.StartActivity();
            return true;
        }

    return true;
    }
}
