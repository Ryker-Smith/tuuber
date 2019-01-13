package net.fachtnaroe.tuuber;
// http://thunkableblocks.blogspot.ie/2017/07/java-code-snippets-for-app-inventor.html

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
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;
//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
//import gnu.lists.FString;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/


public class screen09_Settings extends Form implements HandlesEventDispatching {

    settingsOnline settings = new settingsOnline();
    String version;

    protected void $define() {

        VerticalArrangement Settings = new VerticalArrangement(this);
        TextBox versionBox = new TextBox(Settings);
        versionBox.Text(version);

        try {
            version = settings.get("tuuber2019_version");
        }
        catch (MalformedURLException error) {
            error.getStackTrace();
        }

        EventDispatcher.registerEventForDelegation(this, "LoginButton", "Click");
        EventDispatcher.registerEventForDelegation(this, "LoginWeb", "GotText");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
return true;
    }


//    Button buttonPrepare (Button item) {
//        return item;
//    }

}
