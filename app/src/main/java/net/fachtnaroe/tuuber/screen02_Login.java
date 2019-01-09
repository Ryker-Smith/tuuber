package net.fachtnaroe.tuuber;
// http://thunkableblocks.blogspot.ie/2017/07/java-code-snippets-for-app-inventor.html

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;
//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;
//import gnu.lists.FString;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/


public class screen02_Login extends Form implements HandlesEventDispatching {

    private Label PasswordLabel;
    private TextBox textMine, varName;
    private Button buttonMine, LoginButton;

    private Web LoginWeb;
    private VerticalScrollArrangement Login;

    private VerticalArrangement VerticalArrangement1;
    private HorizontalArrangement HorizontalArrangement1, HorizontalArrangement2, HorizontalArrangement3, HorizontalArrangement4;

    private Label UserNameLabel;
    private TextBox UserName;
    private String baseURL = "https://fachtnaroe.net/qnd?";
    private TinyDB localDB;

    protected void $define() {

        Login = new VerticalScrollArrangement(this);
        Login.WidthPercent(100);
        Login.HeightPercent(100);

        VerticalArrangement1 = new VerticalArrangement (Login);
        HorizontalArrangement1 = new HorizontalArrangement(VerticalArrangement1);
        HorizontalArrangement2 = new HorizontalArrangement(VerticalArrangement1);
        HorizontalArrangement3 = new HorizontalArrangement(VerticalArrangement1);
        HorizontalArrangement4 = new HorizontalArrangement(VerticalArrangement1);

        UserNameLabel= new Label (HorizontalArrangement2);
        UserName= new TextBox (HorizontalArrangement2);

        LoginButton = new Button (HorizontalArrangement3);
        localDB = new TinyDB(this);

        PasswordLabel = new Label(HorizontalArrangement4);
        PasswordLabel.Text("Password");

        varName = new TextBox(Login);
        varName.Text("theVariableName");
        textMine = new TextBox(Login);
        textMine.Text("Type a value here");
        buttonMine = new Button(Login);
        buttonMine.Text("Then click here to save");

        LoginWeb = new Web(Login);

        EventDispatcher.registerEventForDelegation(this, "buttonMine", "Click");
        EventDispatcher.registerEventForDelegation(this, "LoginWeb", "GotText");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(buttonMine) && eventName.equals("Click")) {

            LoginWeb.Url(
                    baseURL +
                    "name=" +
                    varName.Text() +
                    "&value=" +
                    textMine.Text() +
                    "&method=POST"
            );
            LoginWeb.Get();
            return true;
        }
        else if (component.equals(LoginWeb) && eventName.equals("GotText")) {

            String stringSent =  (String) params[0];
            Integer status = (Integer) params[1];
            String encoding = (String) params[2];
            String textOfResponse = (String) params[3];

            webGotText(status.toString());
            return true;
        }
        else {

        }
        // here is where you'd check for other events of your app...
        return false;
    }

    public void webGotText(String status) {

        try {
            JSONObject parser = new JSONObject(status);
            if (parser.getString("status").equals("OK"))
            {}
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
        }
    }


}
