package net.fachtnaroe.tuuber;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;

import com.google.appinventor.components.runtime.Button;
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

    private Button SelectMyRout, MainMenu, Send ;
    private VerticalArrangement Matches, VerticalArrangment1, VerticalArrangment2;
    private HorizontalArrangement HorizontalArragment1,HorizontalArragment2,HorizontalArragment3;
    private ListPicker MyRouteList;
    private Label Label_1;
    private TextBox Chatbox1;



    protected void $define() {
        Matches = new VerticalArrangement(this);
        MainMenu = new Button(HorizontalArragment1);
        SelectMyRout = new Button (HorizontalArragment1);
        MyRouteList = new ListPicker(VerticalArrangment2);
        Label_1 = new Label(HorizontalArragment2);
        Chatbox1 = new TextBox(HorizontalArragment3);
        Send = new Button(HorizontalArragment3);
        Label_1.Text("Star Chat by Clicking on Send ");



    }
}
