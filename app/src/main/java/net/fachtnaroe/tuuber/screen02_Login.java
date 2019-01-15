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
import com.google.appinventor.components.runtime.TableLayout;
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

//    private TextBox;
    private Button LoginButton, RegisterButton, DebugButton;
    private Image Header, Image2, Image3, Image4;

    private Web LoginWeb;
    private VerticalArrangement Login;
    private Notifier Notifier1;

    private VerticalArrangement VerticalArrangement1;
    private HorizontalArrangement HorizontalArrangement1, HorizontalArrangement2, HorizontalArrangement3, HorizontalArrangement4;

    private Label UserNameLabel, PasswordLabel;
    private TextBox UserName;
    private String baseURL = "https://fachtnaroe.net/tuuber-2019?";
    private TinyDB localDB;
    private String pIDsend, LoginResult, LoginList_JSON;
    private PasswordTextBox Password;

    TableArrangement testTable;

    protected void $define() {

        Login = new VerticalArrangement(this);
        Login.WidthPercent(100);
        Login.HeightPercent(100);
        Login.Image("img_splashcanvas.png");
        Notifier1 = new Notifier(Login);

        Header = new Image (Login);
        Header.Picture("img_carlogo.png");

        DebugButton = new Button(Login);
        DebugButton.Text("Debug");

        RegisterButton = new Button(Login);
        RegisterButton.Text("Register");

        VerticalArrangement1 = new VerticalArrangement (Login);
        HorizontalArrangement1 = new HorizontalArrangement(VerticalArrangement1);
        LoginWeb = new Web(Login);
//        Image4 = new Image(VerticalArrangement1);

        HorizontalArrangement2 = new HorizontalArrangement(VerticalArrangement1);
//        testTable = new TableArrangement(this);
//        testTable.Columns(2);
//        testTable.Rows(8);
//        testTable.$add(testTable.Da);
//        testTable.getView(testTable.Row(1),testTable.Column(1));
////        Tabl
//        testTable.$add(Header);
//
//
//        testTable.$add( testTable.Row(1), testTable.Column(1),);
//        testTable.
        UserNameLabel= new Label (Login);
        UserNameLabel.Text("Username:");
        UserNameLabel.FontBold(true);

        UserName= new TextBox (HorizontalArrangement2);
        UserName.BackgroundColor(Component.COLOR_WHITE);
        UserName.Text("testing.this@tcfe.ie");

        HorizontalArrangement4 = new HorizontalArrangement(VerticalArrangement1);
        PasswordLabel = new Label(HorizontalArrangement4);

        PasswordLabel.Text("Password");
        PasswordLabel.FontBold(true);
        Password = new PasswordTextBox(HorizontalArrangement4);
        Password.Text("tcfetcfe");

        HorizontalArrangement3 = new HorizontalArrangement(VerticalArrangement1);
        Image2 = new Image(HorizontalArrangement4);
        Image2.Picture("img_icon_fast.png");
        LoginButton = new Button (HorizontalArrangement3);
        LoginButton.Shape(Component.BUTTON_SHAPE_ROUNDED);
        LoginButton.BackgroundColor(Component.COLOR_BLACK);
        LoginButton.FontBold(true);
        LoginButton.TextColor(Component.COLOR_LTGRAY);
        LoginButton.FontTypeface(Component.TYPEFACE_SANSSERIF);
        LoginButton.Text("Login");
        Image3 = new Image(HorizontalArrangement4);
        HorizontalArrangement[] x = new HorizontalArrangement[] {HorizontalArrangement1,HorizontalArrangement2};
//        for (HorizontalArrangement y : x) {
//
//        }

//        LoginButton= buttonPrepare ( new Button ());

//        localDB = new TinyDB(Login);
//        if (localDB != null) try {
//
////            localDB.StoreValue("TestTag", "67890");
//        } catch (Exception e) {
////            e.printStackTrace();
//            messages.ShowMessageDialog("JSON Exception", "Information", "OK");
//        }
//        String prevName = (String) localDB.GetValue("Username","-1");
//        if ( !prevName.equals("-1")) {
//            UserName.Text(prevName);
//        }
//        localDB.ClearTag("TestTag");
//        localDB.GetTags();

        EventDispatcher.registerEventForDelegation(this, "LoginButton", "Click");
        EventDispatcher.registerEventForDelegation(this, "debugButton", "Click");
        EventDispatcher.registerEventForDelegation(this, "LoginWeb", "GotText");
        EventDispatcher.registerEventForDelegation(this, "RegisterButton", "Click");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(LoginButton) && eventName.equals("Click")) {

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

            ActivityStarter nextScreen = new ActivityStarter(this);
            nextScreen.ActivityClass("net.fachtnaroe.tuuber.screen06_Register");
            nextScreen.ActivityPackage("net.fachtnaroe.tuuber");
            nextScreen.StartActivity();
            return true;
        }

        else if (component.equals(DebugButton) && eventName.equals("Click")) {

            ActivityStarter nextScreen = new ActivityStarter(this);
            nextScreen.ExtraValue( "2"); // for testing

            //nextScreen.
            nextScreen.ActivityClass("net.fachtnaroe.tuuber.screen09_Settings");
            nextScreen.ActivityPackage("net.fachtnaroe.tuuber");
            nextScreen.StartActivity();
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

                ActivityStarter nextScreen = new ActivityStarter(this);
                nextScreen.ActivityClass("net.fachtnaroe.tuuber.screen03_MainMenu");
                nextScreen.ActivityPackage("net.fachtnaroe.tuuber");
                nextScreen.StartActivity();

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

//    Button buttonPrepare (Button item) {
//        return item;
//    }

}
