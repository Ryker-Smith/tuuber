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
import com.google.appinventor.components.runtime.TableArrangement;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;
//import gnu.lists.FString;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/


public class screen02_Login extends Form implements HandlesEventDispatching {

//    private TextBox;
    private Button LoginButton, RegisterButton, DebugButton;
    private Image Header, Image2, Image3, Image4;

    private Web LoginWeb;
    private VerticalArrangement Login;
    private Notifier Notifier1;

    private HorizontalArrangement HorizontalArrangement1, usernameHz, loginHz, passwordHz;

    private Label UserNameLabel, PasswordLabel;
    private TextBox UserName;
    private String baseURL = "https://fachtnaroe.net/tuuber-2019?";
    private TinyDB localDB;
    private String pIDsend, LoginResult, LoginList_JSON;
    private PasswordTextBox Password;

    TableArrangement testTable;

    protected void $define() {

        this.BackgroundImage("img_splashcanvas.png");
        Login = new VerticalArrangement(this);
        Login.WidthPercent(100);
        Login.HeightPercent(100);
//        Login.Image("img_splashcanvas.png");
        Notifier1 = new Notifier(Login);

        Header = new Image (Login);
        Header.Picture("img_carlogo.png");

        DebugButton = new Button(Login);
        DebugButton.Text("Go straight to the settings screen");
        DebugButton.FontSize(8);

        RegisterButton = new Button(Login);
        RegisterButton.Text("Register");

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
//        Image2 = new Image(passwordHz);
//        Image2.Picture("img_icon_fast.png");
        LoginButton = new Button (loginHz);
        LoginButton.Shape(Component.BUTTON_SHAPE_ROUNDED);
        LoginButton.BackgroundColor(Component.COLOR_BLACK);
        LoginButton.FontBold(true);
        LoginButton.TextColor(Component.COLOR_LTGRAY);
        LoginButton.FontTypeface(Component.TYPEFACE_SANSSERIF);
        LoginButton.Text("Login");
        Image3 = new Image(passwordHz);
        HorizontalArrangement[] x = new HorizontalArrangement[] {HorizontalArrangement1, usernameHz};
//        for (HorizontalArrangement y : x) {
//
//        }

//        LoginButton= buttonPrepare ( new Button ());

        localDB = new TinyDB(Login);

        EventDispatcher.registerEventForDelegation(this, "LoginButton", "Click");
        EventDispatcher.registerEventForDelegation(this, "debugButton", "Click");
        EventDispatcher.registerEventForDelegation(this, "LoginWeb", "GotText");
        EventDispatcher.registerEventForDelegation(this, "RegisterButton", "Click");
        EventDispatcher.registerEventForDelegation(this, "none", "BackPressed");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (eventName.equals("BackPressed")) {
            return true;
        }
        else if (component.equals(LoginButton) && eventName.equals("Click")) {

                LoginWeb.Url(
                        baseURL +
                                "email=" +
                                UserName.Text() +
                                "&password=" +
                                Password.Text() +
                                "&cmd=LOGIN"
                );
                LoginWeb.Get();
                return true;
            }

            else if (component.equals(RegisterButton) && eventName.equals("Click")) {
                switchForm("screen06_Register");
                return true;
            }

            else if (component.equals(DebugButton) && eventName.equals("Click")) {

                switchForm("screen09_Settings");
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

        // here is where you'd check for other events of your app...

    }

    public void webGotText(String status, String textOfResponse) {
//        LoginButton.Text(status);
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (parser.getString("result").equals("OK")) {
                // do something
                localDB.StoreValue("pID", parser.getString("pID"));
                switchForm("screen03_MainMenu");
            } else {
                Notifier1.ShowMessageDialog("Login failed, check details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            Notifier1.ShowMessageDialog("JSON Exception", "Information", "OK");
        }
        else {
            Notifier1.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
    }


}
