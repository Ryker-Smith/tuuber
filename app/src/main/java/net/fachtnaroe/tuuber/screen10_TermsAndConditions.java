package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import com.google.appinventor.components.runtime.ActivityStarter;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.PasswordTextBox;
import com.google.appinventor.components.runtime.TableArrangement;
import com.google.appinventor.components.runtime.TableLayout;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;
//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;

public class screen10_TermsAndConditions extends Form implements HandlesEventDispatching {
    private tuuber_Settings applicationSettings;
    private VerticalArrangement tacvz;
    private Button accept, decline;
    private Label TandC;


    protected void $define() {
        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            dbg(e.toString());
        }
        tacvz = new VerticalArrangement(this);
        tacvz.WidthPercent(100);
        tacvz.HeightPercent(100);
        TandC = new Label(tacvz);
        TandC.Text(applicationSettings.TermsAndConditions);
        TandC.BackgroundColor(COLOR_WHITE);
        accept = new Button(tacvz);
        accept.Text("Accept");
        decline = new Button(tacvz);
        decline.Text("Decline");

        EventDispatcher.registerEventForDelegation(this, formName, "Click");

    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("GOT: "+formName+" "+eventName);
        if (eventName.equals("BackPressed")) {
            finishActivityWithTextResult("Bad");
            return true;
        }
        else if(eventName.equals("Click")){
            if (component.equals(accept)){
                finishActivityWithTextResult("Good");
                dbg("Good");
                return true;
            }
            else if(component.equals(decline)){
                dbg("Bad");
                finishActivityWithTextResult("Bad");
                return true;
            }

        }



        return false;


    }
    void dbg(String debugMsg) {
        System.err.print("~~~> " + debugMsg + " <~~~\n");
    }
}