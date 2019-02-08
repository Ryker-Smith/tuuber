package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.util.Ev3Constants;

public class screen01_Splash extends Form implements HandlesEventDispatching {

    Clock nextScreenTimer;
    tuuber_Settings applicationSettings;
    VerticalArrangement SplashScreen;
    Button tempButton;
    Image ourLogo;
    Label spacer;
    int thisIsA_Bad_Idea = 320;
    int another_Bad_Idea = 50;

    protected void $define(){

        applicationSettings=new tuuber_Settings(this);
        this.BackgroundColor(Component.COLOR_BLACK);
        Integer screenHeight = this.Height();
        SplashScreen = new VerticalArrangement(this);
        SplashScreen.HeightPercent(100);
        SplashScreen.WidthPercent(100);
        SplashScreen.AlignHorizontal(Component.ALIGNMENT_CENTER);
        spacer = new Label(SplashScreen);
        ourLogo = new Image(SplashScreen);
        spacer.WidthPercent(100);
        ourLogo.Picture(applicationSettings.ourLogo);
        ourLogo.ScalePictureToFit(false);
        ourLogo.Height(thisIsA_Bad_Idea);
        Integer spacerHeight= ( screenHeight - ourLogo.Height() );
        spacerHeight = screenHeight / 4 - ourLogo.Height() /2;
        spacer.Height( spacerHeight );
        spacer.TextColor(Component.COLOR_WHITE);
        spacer.FontBold(true);
        spacer.FontTypeface(Ev3Constants.FontType.NORMAL_FONT);
        spacer.TextAlignment(Component.ALIGNMENT_CENTER);
        spacer.Text("Ag fáil data...");
        nextScreenTimer = new Clock(SplashScreen);
        nextScreenTimer.TimerEnabled(false);
        nextScreenTimer.TimerInterval(another_Bad_Idea);
        nextScreenTimer.TimerEnabled(true);
//        tempButton = new Button(SplashScreen);

        EventDispatcher.registerEventForDelegation(this,"nextScreenTimer","Timer");
        EventDispatcher.registerEventForDelegation(this,"tempButton","Click");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
           if (eventName.equals("Timer")) {
                nextScreenTimer.TimerEnabled(false);
                switchForm("screen02_Login");
                return true;
            }
            else if (eventName.equals("Click")) {
               switchForm("screen02_Login");
           }
            return false; // event not handled
    }


}