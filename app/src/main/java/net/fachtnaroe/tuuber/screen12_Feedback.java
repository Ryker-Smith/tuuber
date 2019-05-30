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

public class screen12_Feedback extends Form implements HandlesEventDispatching {

    tuuber_Settings applicationSettings;
    tuuberCommonSubroutines tools;

    private VerticalArrangement Feedback;
    private Button button_Done, button_MainMenu, button_Refresh;
    private Label label_pID;
    private fachtnaWebViewer webviewer_FeedbackForm;


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

        Feedback = new VerticalArrangement(this);
        Feedback.WidthPercent(100);
        Feedback.HeightPercent(100);

        HorizontalArrangement toolbarHz = new HorizontalArrangement(Feedback);
        button_MainMenu = new Button(toolbarHz);
        button_MainMenu.Width(40);
        button_MainMenu.Height(40);
        button_MainMenu.Image(applicationSettings.ourLogo);

        label_pID=tools.fn_HeadingLabel(toolbarHz, label_pID, applicationSettings.pID,(tools.fn_téacs_aistriú("feedback")));

        button_Refresh = new Button(toolbarHz);
        button_Refresh.Width(40);
        button_Refresh.Height(40);
        button_Refresh.FontSize(8);
        button_Refresh.Image("buttonRefresh.png");

        webviewer_FeedbackForm = new fachtnaWebViewer(Feedback);
        webviewer_FeedbackForm.HomeUrl(
                applicationSettings.Feedback_URL  +
                "?l=" + applicationSettings.string_PreferredLanguage +
                "&sessionID=" +applicationSettings.default_sessionID +
                "&f=embed");
        webviewer_FeedbackForm.HeightPercent(80);
        Label flogginpadalso=(Label)tools.padding(Feedback,1,1);
        HorizontalArrangement hz_AcceptDecline = new HorizontalArrangement(Feedback);
        button_Done = new Button(hz_AcceptDecline);
        button_Done.Text(tools.fn_téacs_aistriú("go_back"));

        button_CommonFormatting(button_Done);
        Feedback.AlignHorizontal(Component.ALIGNMENT_CENTER);

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        tools.dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);
        if (eventName.equals("BackPressed")) {
            finishActivityWithTextResult("");
            return true;
        }
        else if(eventName.equals("Click")){
            if (component.equals(button_MainMenu)) {
                finish();
                return true;
            }
            else if (component.equals(button_Done)){
                finishActivityWithTextResult("");
                return true;
            }
            if (component.equals(button_Refresh)) {
                webviewer_FeedbackForm.GoToUrl(webviewer_FeedbackForm.HomeUrl());
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