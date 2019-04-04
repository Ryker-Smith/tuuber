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

    tuuber_Settings applicationSettings;
    tuuberCommonSubroutines tools;

    private VerticalArrangement TermsAndConditions;
    private Button button_Accept, button_Decline, button_MainMenu, button_Refresh;
    private Label label_pID;
    private fachtnaWebViewer webviewer_TextOfTermsAndConditions;


    protected void $define() {
        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            tools.dbg(e.toString());
        }
        tools= new tuuberCommonSubroutines(this);

        TermsAndConditions = new VerticalArrangement(this);
        TermsAndConditions.WidthPercent(100);
        TermsAndConditions.HeightPercent(100);

        HorizontalArrangement toolbarHz = new HorizontalArrangement(TermsAndConditions);
        button_MainMenu = new Button(toolbarHz);
        button_MainMenu.Width(40);
        button_MainMenu.Height(40);
        button_MainMenu.Image(applicationSettings.ourLogo);

        label_pID=tools.fn_HeadingLabel(toolbarHz, label_pID, applicationSettings.pID,"Terms & Conditions");

        button_Refresh = new Button(toolbarHz);
        button_Refresh.Width(40);
        button_Refresh.Height(40);
        button_Refresh.FontSize(8);
        button_Refresh.Image("buttonRefresh.png");

        webviewer_TextOfTermsAndConditions = new fachtnaWebViewer(TermsAndConditions);
        webviewer_TextOfTermsAndConditions.HomeUrl(applicationSettings.TermsAndConditions_URL);
        webviewer_TextOfTermsAndConditions.HeightPercent(70);
        Label flogginpadalso=(Label)tools.padding(TermsAndConditions,1,1);
        HorizontalArrangement hz_AcceptDecline = new HorizontalArrangement(TermsAndConditions);
        button_Accept = new Button(hz_AcceptDecline);
        button_Accept.Text(tools.fn_téacs_aistriú("accept"));
        if (!this.startupValue.toString().equals("\"1\"")){ // apparently the quotes are part of what's passed!
            button_Decline = new Button(hz_AcceptDecline);
            button_Decline.Text(tools.fn_téacs_aistriú("decline"));
        }
        tools.dbg("Start Value ["+this.startupValue.toString()+"]");

        button_CommonFormatting(button_Accept, button_Decline);
        TermsAndConditions.AlignHorizontal(Component.ALIGNMENT_CENTER);

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        tools.dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);
        if (eventName.equals("BackPressed")) {
            finishActivityWithTextResult("Bad");
            return true;
        }
        else if(eventName.equals("Click")){
            if (component.equals(button_MainMenu)) {
                finish();
                return true;
            }
            else if (component.equals(button_Accept)){
                finishActivityWithTextResult("Good");
                tools.dbg("Good");
                return true;
            }
            else if(component.equals(button_Decline)){
                tools.dbg("Bad");
                finishActivityWithTextResult("Bad");
                return true;
            }
            if (component.equals(button_Refresh)) {
                webviewer_TextOfTermsAndConditions.GoToUrl(applicationSettings.TermsAndConditions_URL);
                return true;
            }
        }
        return false;
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
            b[i].FontSize(applicationSettings.int_ButtonTextSize);
            b[i].Column(1);
            i++;
        }
    }
}