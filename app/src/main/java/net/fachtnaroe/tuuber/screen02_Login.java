package net.fachtnaroe.tuuber;
// http://thunkableblocks.blogspot.ie/2017/07/java-code-snippets-for-app-inventor.html

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
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;
//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;
//import gnu.lists.FString;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/


public class screen02_Login extends Form implements HandlesEventDispatching {

//    private TextBox;
    tuuber_Settings applicationSettings;
    private Button LoginButton, RegisterButton;
//    private Image Header;

    private Web LoginWeb;
    private VerticalScrollArrangement Login;
    private Notifier messages;

    private HorizontalArrangement usernameHz, loginHz, passwordHz;

    private Label UserNameLabel, PasswordLabel;
    private TextBox UserName;
    private PasswordTextBox Password;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        this.BackgroundImage(applicationSettings.backgroundImageName);

        Login = new VerticalScrollArrangement(this);
        Login.WidthPercent(100);
        Login.HeightPercent(100);
        messages = new Notifier(Login);

        LoginWeb = new Web(Login);
        usernameHz = new HorizontalArrangement(Login);
        UserNameLabel= new Label (usernameHz);
        UserNameLabel.Text("Username:");
        UserNameLabel.FontBold(true);

        UserName= new TextBox (usernameHz);
        UserName.BackgroundColor(Component.COLOR_WHITE);
        UserName.Text("testing.this@tcfe.ie");

        passwordHz = new HorizontalArrangement(Login);
        PasswordLabel = new Label(passwordHz);
        PasswordLabel.Text("Password");
        PasswordLabel.FontBold(true);
        Password = new PasswordTextBox(passwordHz);
        Password.Text("tcfetcfe");

        loginHz = new HorizontalArrangement(Login);
        LoginButton = new Button (loginHz);
        LoginButton.Text("Login");
        RegisterButton = new Button(Login);
        RegisterButton.Text("Register");
        button_CommonFormatting(LoginButton, RegisterButton);

        EventDispatcher.registerEventForDelegation(this, "none", "BackPressed");
        EventDispatcher.registerEventForDelegation(this, "LoginWeb", "GotText");
//        EventDispatcher.registerEventForDelegation(this, "RegisterButton", "Click");
//        EventDispatcher.registerEventForDelegation(this, "LoginButton", "Click");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName +  " " + eventName);
        if (eventName.equals("BackPressed")) {
            // prevents return to splash screen
            return true;
        }
        else if (component.equals(LoginButton) && eventName.equals("Click")) {
                LoginWeb.Url(
                        applicationSettings.baseURL
                            +    "?cmd=LOGIN"
                            +   "&email="
                            +    UserName.Text()
                            +    "&password="
                            +   Password.Text()
                );
                LoginWeb.Get();
                return true;
            }
            else if (component.equals(RegisterButton) && eventName.equals("Click")) {
                switchForm("screen06_Register");
                return true;
            }
            else if (component.equals(LoginWeb) && eventName.equals("GotText")) {
                String stringSent =  (String) params[0];
                Integer status = (Integer) params[1];
                String encoding = (String) params[2];
                String textOfResponse = (String) params[3];
                webGotText(status.toString(), textOfResponse);
                return true;
            }
            else {
                // do something
                return false;
            }
    }

    public void webGotText(String status, String textOfResponse) {
        String temp=new String();
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            temp = parser.getString("result");
            if (parser.getString("result").equals("OK")) {
                applicationSettings.pID= parser.getString("pID");
                switchForm("screen03_MainMenu");
            } else {
                messages.ShowMessageDialog("Login failed, check details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messages.ShowMessageDialog("JSON Exception (check password)" + temp, "Information", "OK");
        }
        else {
            messages.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
    }

    void dbg (String debugMsg) {
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }

    void button_CommonFormatting (Button... b) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i=0;
        int len = b.length;
        while ((i < len) && (b[i] != null)) {
            b[i].WidthPercent(100);
            b[i].BackgroundColor(Component.COLOR_BLACK);
            b[i].FontBold(true);
            b[i].Shape(Component.BUTTON_SHAPE_ROUNDED);
            b[i].TextColor(Component.COLOR_LTGRAY);
            b[i].FontTypeface(Component.TYPEFACE_SANSSERIF);
            EventDispatcher.registerEventForDelegation(this, b[i].toString(), "Click");
            i++;

        }
    }
}
