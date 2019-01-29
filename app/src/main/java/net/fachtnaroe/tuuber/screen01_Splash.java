package net.fachtnaroe.tuuber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;

public class screen01_Splash extends Form implements HandlesEventDispatching {

    Clock nextScreenTimer = new Clock();

    protected void $define(){

        this.BackgroundImage("tuuber_logo_001.png");
//        this.Width(800);
        nextScreenTimer.TimerEnabled(false);
        nextScreenTimer.TimerInterval(3);
        nextScreenTimer.TimerEnabled(true);
        EventDispatcher.registerEventForDelegation(this,"nextScreenTimer","Timer");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
           if (eventName.equals("Timer")) {
                nextScreenTimer.TimerEnabled(false);
                switchForm("screen02_Login");
                return true;

            }
            return false; // event not handled
    }
}