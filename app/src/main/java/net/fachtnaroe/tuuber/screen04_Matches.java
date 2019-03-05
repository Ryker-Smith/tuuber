package net.fachtnaroe.tuuber;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;

import android.content.Intent;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListPicker;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.util.YailList;

import java.util.ArrayList;
import java.util.List;

public class screen04_Matches extends Form implements HandlesEventDispatching {

    private tuuber_Settings applicationSettings;
    private Button SelectMyRout, MainMenu, Send;
    private VerticalArrangement Matches, VerticalArrangment1, VerticalArrangment2;
    private HorizontalArrangement HorizontalArragment1, HorizontalArragment2, HorizontalArragment3;
    private ListView MyRouteList, MatchesMade;
    private String baseURL = "https://fachtnaroe.net/tuuber";


    protected void $define() {
        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        this.BackgroundImage(applicationSettings.backgroundImageName);

        Matches = new VerticalArrangement(this);
        Matches.HeightPercent(100);
        Matches.WidthPercent(100);
        MyRouteList = new ListView(Matches);
        MatchesMade = new ListView(Matches);

        EventDispatcher.registerEventForDelegation(this, "buttonMainMenu", "Click");
        EventDispatcher.registerEventForDelegation(this, "none", "BackPressed");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (eventName.equals("BackPressed")) {
//            if (this.BackPressed()) {
                // one of the above should be redundant
                closeForm(new Intent());
//            }
        }
        else if (component.equals(MainMenu) && eventName.equals("Click")) {
                switchForm("screen03_MainMenu");

                return true;
        }
        return true;
    }

}
