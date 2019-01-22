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

    private Button Routes, Matches, Messages;

    VerticalArrangement MainMenu;
    Notifier MessagesPopup;
    Image Header;


    protected void $define() {


        MainMenu = new VerticalArrangement(this);
        MainMenu.WidthPercent(100);
        MainMenu.HeightPercent(100);
        MainMenu.Image("img_splashcanvas.png");
        MessagesPopup = new Notifier(MainMenu);

        Routes = new Button(MainMenu);
        Routes.Text("Routes");
        Matches = new Button(MainMenu);
        Matches.Text("Matches");
        Messages = new Button(MainMenu);
        Messages.Text("Messages");

        Header = new Image(MainMenu);
        Header.Picture("img_carlogo.png");

        EventDispatcher.registerEventForDelegation(this, "Routes", "Click");
        EventDispatcher.registerEventForDelegation(this, "Matches", "Click");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params){
        if (component.equals(Matches) && eventName.equals("Click")) {

            switchForm("screen04_Matches");

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
