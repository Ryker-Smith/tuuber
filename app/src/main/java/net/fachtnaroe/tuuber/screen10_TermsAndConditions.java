package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import android.graphics.Color;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.VerticalArrangement;
//import com.google.appinventor.components.runtime.util;


public class screen10_TermsAndConditions extends Form implements HandlesEventDispatching {
    private tuuber_Settings applicationSettings;
    private VerticalArrangement TermsAndConditions;
    private Button button_Accept, button_Decline, buttonMainMenu;
    private Label section_TextOfTermsAndConditions;


    protected void $define() {
        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            dbg(e.toString());
        }
        TermsAndConditions = new VerticalArrangement(this);
        TermsAndConditions.WidthPercent(100);
        TermsAndConditions.HeightPercent(100);

        HorizontalArrangement toolbarHz = new HorizontalArrangement(TermsAndConditions);
        buttonMainMenu = new Button(toolbarHz);
        buttonMainMenu.Width(40);
        buttonMainMenu.Height(40);
        buttonMainMenu.Image("buttonHome.png");
        Label label_pID = new Label(toolbarHz);
        label_pID.HTMLFormat(true);
        label_pID.Text("I am user: #" + applicationSettings.pID + "<br><small><small>Settings</small></small>");
        label_pID.Height(40);
        label_pID.FontSize(20);
        label_pID.WidthPercent(70);
        label_pID.TextAlignment(Component.ALIGNMENT_CENTER);
        Button buttonRefresh = new Button(toolbarHz);
        buttonRefresh.Width(40);
        buttonRefresh.Height(40);
        buttonRefresh.FontSize(8);
        buttonRefresh.Image("buttonRefresh.png");

        section_TextOfTermsAndConditions = new Label(TermsAndConditions);
        section_TextOfTermsAndConditions.Text(applicationSettings.TermsAndConditions);
        section_TextOfTermsAndConditions.TextColor(Component.COLOR_WHITE);
        section_TextOfTermsAndConditions.HTMLFormat(true);
        section_TextOfTermsAndConditions.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
        section_TextOfTermsAndConditions.HeightPercent(70);
        HorizontalArrangement hz_AcceptDecline = new HorizontalArrangement(TermsAndConditions);
        button_Accept = new Button(hz_AcceptDecline);
        button_Accept.Text("Accept");
        button_Decline = new Button(hz_AcceptDecline);
        button_Decline.Text("Decline");

        button_CommonFormatting(button_Accept, button_Decline);
        TermsAndConditions.AlignHorizontal(Component.ALIGNMENT_CENTER);

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);
        if (eventName.equals("BackPressed")) {
            finishActivityWithTextResult("Bad");
            return true;
        }
        else if(eventName.equals("Click")){
            if (component.equals(buttonMainMenu)) {
                finish();
                return true;
            }
            else if (component.equals(button_Accept)){
                finishActivityWithTextResult("Good");
                dbg("Good");
                return true;
            }
            else if(component.equals(button_Decline)){
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
    void button_CommonFormatting(Button... b) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i=0;
        int len = b.length;
//        https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
        while ((i < len) && (b[i] != null)) {
            b[i].WidthPercent(50);
            b[i].BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
            b[i].FontBold(true);
            b[i].TextColor(Component.COLOR_WHITE);
            b[i].Shape(BUTTON_SHAPE_ROUNDED);
            b[i].FontSize(12);
            b[i].Column(1);
//            b[i].Width(int_ColWidth);
            i++;
        }
    }
}