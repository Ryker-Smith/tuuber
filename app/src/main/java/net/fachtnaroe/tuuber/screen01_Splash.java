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
    VerticalArrangement SplashScreen;
    Button tempButton;
    Image logo;
    Label spacer;
    int thisIsA_Bad_Idea = 320;
    int another_Bad_Idea = 3000;

    protected void $define(){

        this.BackgroundColor(Component.COLOR_BLACK);
        Integer screenHeight = this.Height();
        SplashScreen = new VerticalArrangement(this);
        SplashScreen.HeightPercent(100);
        SplashScreen.WidthPercent(100);
        SplashScreen.AlignHorizontal(Component.ALIGNMENT_CENTER);
        spacer = new Label(SplashScreen);
        logo = new Image(SplashScreen);
        spacer.WidthPercent(100);
        logo.Picture("tuuber_logo_002.png");
        logo.ScalePictureToFit(false);
        logo.Height(thisIsA_Bad_Idea);
        Integer spacerHeight= ( screenHeight - logo.Height() );
        spacerHeight = screenHeight / 4 - logo.Height() /2;
        spacer.Height( spacerHeight );
        spacer.TextColor(Component.COLOR_WHITE);
        spacer.FontBold(true);
        spacer.FontTypeface(Ev3Constants.FontType.NORMAL_FONT);
        spacer.TextAlignment(Component.ALIGNMENT_CENTER);
        spacer.Text("Loading...");
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