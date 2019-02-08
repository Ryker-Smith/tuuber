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

    private Button Routes, Matches, Conversations, Settings, Terms, experimental;

    tuuber_Settings applicationSettings;
    boolean form_made=false;
    VerticalArrangement MainMenu;
    Notifier MessagesPopup;
    ArrayList<HorizontalArrangement> hz;//= new Array ();

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
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
        MainMenu.AlignHorizontal(Component.ALIGNMENT_CENTER);
        hz=new ArrayList();

        button_CommonFormatting(Routes, Matches, Conversations, Settings, Terms, experimental);

        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {

//        dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("BackPressed")) {
            return true;
        }
        if (eventName.equals("Click")) {
            if (component.equals(Matches)) {
                // eg startActivity(new Intent().setClass(this, Screen2.class).putExtra("startValue", "2"));
                //
//                finishActivityWithResult("elbow");
//                finishActivityWithTextResult();

//                onActivityResult();
//                EventDispatcher.registerEventForDelegation(this, "OtherScreenClosedEvent", "OtherScreenClosed" );
                startActivity(new Intent().setClass(this, screen04_Matches.class));
                return true;
            }
            else if (component.equals(Conversations)) {
                startActivity(new Intent().setClass(this, screen05_Conversations.class));
                return true;
            }
            else if (component.equals(Routes)) {
                startActivity(new Intent().setClass(this, screen07_Routes.class));
                return true;
            }
            else if (component.equals(Settings)) {
                startActivity(new Intent().setClass(this, screen09_Settings.class));
                return true;
            }
            else if (component.equals(Terms)) {
                startActivity(new Intent().setClass(this, screen10_TermsAndConditions.class));
                return true;
            }
            else if (component.equals(experimental)) {
                startActivity(new Intent().setClass(this, experimental_doNotUseThis.class));
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
//            hz.add(new HorizontalArrangement(MainMenu));
            b[i].WidthPercent(50);
            b[i].BackgroundColor(Component.COLOR_BLACK);
            b[i].FontBold(true);
            b[i].TextColor(Component.COLOR_WHITE);
            b[i].Shape(BUTTON_SHAPE_ROUNDED);
//            b[i].Text(b[i].toString());
//            R.id();
//            String[] temp = b[i].toString().split("@");
//            String x = temp[1];
//            Integer a = Resources.getSystem().getIdentifier(b[i].toString(),"drawable","android");
//            Integer r=$context().getResources().getIdentifier(x,"drawable",$context().getPackageName());
//            dbg(a.toString());
//            dbg(r.toString());
//            android.app.Activity
//            $c
////            $context().findViewById()
//            dbg(x);
//            int x=getResourceId(b[i].toString(),"",getPackageName());
//            EventDispatcher.registerEventForDelegation(this.getDispatchDelegate(), b[i].toString(), "Click");
            i++;
        }
    }

}
