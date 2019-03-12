package net.fachtnaroe.tuuber;
// http://thunkableblocks.blogspot.ie/2017/07/java-code-snippets-for-app-inventor.html

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.CheckBox;
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

    tuuber_Settings applicationSettings;
    private Button LoginButton, RegisterButton;
//    private Image Header;

    private Web LoginWeb;
    private VerticalScrollArrangement Login;
    private Notifier ErrorNotifier;
    private HorizontalArrangement usernameHz, loginHz, passwordHz;
    private Label UserNameLabel, PasswordLabel;
    private TextBox inputUsername;
    private PasswordTextBox inputPassword;
    private Image ourLogo;
    private CheckBox IsDebugSession;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        this.BackgroundImage(applicationSettings.backgroundImageName);

        Login = new VerticalScrollArrangement(this);
        Login.WidthPercent(100);
        Login.HeightPercent(100);
        ErrorNotifier = new Notifier(Login);

        LoginWeb = new Web(Login);
        usernameHz = new HorizontalArrangement(Login);
        UserNameLabel= new Label (usernameHz);
        UserNameLabel.Text("Username:");
        UserNameLabel.FontBold(true);

        inputUsername = new TextBox (usernameHz);
        inputUsername.BackgroundColor(Component.COLOR_WHITE);
        if (applicationSettings.lastLogin == applicationSettings.default_lastLogin) {
            inputUsername.Text("testing.this@tcfe.ie");
        }
        else {
            inputUsername.Text(applicationSettings.lastLogin);
        }
        passwordHz = new HorizontalArrangement(Login);
        PasswordLabel = new Label(passwordHz);
        PasswordLabel.Text("Password");
        PasswordLabel.FontBold(true);
        inputPassword = new PasswordTextBox(passwordHz);
        inputPassword.Text("tcfetcfe");

        loginHz = new HorizontalArrangement(Login);
        LoginButton = new Button (loginHz);
        LoginButton.Text("Login");
        RegisterButton = new Button(Login);
        RegisterButton.Text("Register");
        button_CommonFormatting(LoginButton, RegisterButton);
        ourLogo=new Image(Login);
        ourLogo.Picture(applicationSettings.ourLogo);
        ourLogo.ScalePictureToFit(false);
        ourLogo.Height(320);
        Login.AlignHorizontal(Component.ALIGNMENT_CENTER);
        IsDebugSession = new CheckBox(Login);
        IsDebugSession.Text("This is a debugging session");
        IsDebugSession.Checked(true);

        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "Changed");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (eventName.equals("BackPressed")) {
            // prevents return to splash screen
            return true;
        }
        else if (eventName.equals("Changed")) {
            // for debugging session
            applicationSettings.IsDebugSession= IsDebugSession.Checked();
            return true;
        }
        else if (eventName.equals("Click")) {
            if (component.equals(LoginButton)) {
                LoginWeb.Url(
                        applicationSettings.baseURL
                                + "?cmd=LOGIN"
                                + "&email="
                                + inputUsername.Text()
                                + "&password="
                                + inputPassword.Text()
                );
                LoginWeb.Get();
                return true;
            }
            else if (component.equals(RegisterButton)) {
                startNewForm("screen06_Register",null);
                return true;
            }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(LoginWeb)) {
                Integer status = (Integer) params[1];
                String textOfResponse = (String) params[3];
                webGotText(status.toString(), textOfResponse);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void webGotText(String status, String textOfResponse) {
        String temp=new String();
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            temp = parser.getString("result");
            if (parser.getString("result").equals("OK")) {
                applicationSettings.pID= parser.getString("pID");
                applicationSettings.lastLogin= inputUsername.Text();
                applicationSettings.sessionID=parser.getString("sessionID");
                applicationSettings.IsDebugSession=true;
                applicationSettings.set();
                startNewForm("screen03_MainMenu",null);
            } else {
                ErrorNotifier.ShowMessageDialog("Login failed, check details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            ErrorNotifier.ShowMessageDialog("JSON Exception (check password)" + temp, "Information", "OK");
        }
        else {
            ErrorNotifier.ShowMessageDialog("Problem connecting with server","Information", "OK");
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
