package net.fachtnaroe.tuuber;

import android.preference.PreferenceActivity;

import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.VerticalArrangement;

public class screen03_MainMenu extends Form implements HandlesEventDispatching {

    VerticalArrangement MainMenu;
    Notifier Messages;
    Image Header;


    protected void $define() {

        MainMenu = new VerticalArrangement(this);
        MainMenu.WidthPercent(100);
        MainMenu.HeightPercent(100);
        MainMenu.Image("img_splashcanvas.png");
        Messages = new Notifier(MainMenu);

        Header = new Image(MainMenu);
        Header.Picture("img_carlogo.png");
    }

    }
