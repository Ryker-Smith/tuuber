package net.fachtnaroe.tuuber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;

public class screen01_Splash extends Form implements HandlesEventDispatching {

    protected void $define(){
        switchForm("screen02_Login");
    }
}