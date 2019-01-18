package net.fachtnaroe.tuuber;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;

import android.content.Intent;
import android.util.Log;

import com.google.appinventor.components.runtime.ActivityStarter;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListPicker;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;

import java.net.URL;
import java.text.Normalizer;

public class screen04_Matches extends Form implements HandlesEventDispatching {

    private Button SelectMyRout, MainMenu, Send;
    private VerticalArrangement Matches, VerticalArrangment1, VerticalArrangment2;
    private HorizontalArrangement HorizontalArragment1, HorizontalArragment2, HorizontalArragment3;
    private ListPicker MyRouteList;
    private Label Label_1;
    private TextBox Chatbox1;


    protected void $define() {
        Matches = new VerticalArrangement(this);
        HorizontalArragment1 = new HorizontalArrangement(Matches);
        VerticalArrangment1 = new VerticalArrangement(Matches);
        VerticalArrangment2 = new VerticalArrangement(Matches);
        MainMenu = new Button(HorizontalArragment1);
        HorizontalArragment1 = new HorizontalArrangement(Matches);
        HorizontalArragment2 = new HorizontalArrangement(Matches);
        HorizontalArragment3 = new HorizontalArrangement(Matches);
        SelectMyRout = new Button(HorizontalArragment1);
        MyRouteList = new ListPicker(VerticalArrangment2);
        Label_1 = new Label(HorizontalArragment2);
        Chatbox1 = new TextBox(HorizontalArragment3);
        Send = new Button(HorizontalArragment3);
        Label_1.Text("Star Chat by Clicking on Send ");
        MainMenu.Text("MainMenu");
        SelectMyRout.Text("Select Rout");
        Send.Text("SendMessege");
        MyRouteList.Text("Routs List");
        EventDispatcher.registerEventForDelegation(this, "MainMenu", "Click");
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
