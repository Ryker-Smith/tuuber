package net.fachtnaroe.tuuber;
// http://thunkableblocks.blogspot.ie/2017/07/java-code-snippets-for-app-inventor.html

import android.graphics.Color;

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
import com.google.appinventor.components.runtime.TableArrangement;
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
    tuuberCommonSubroutines tools;
    private Button button_Login, button_Register;
//    private Image Header;

    private Web web_Login;
    private VerticalScrollArrangement Login;
    private Notifier ErrorNotifier;
    private HorizontalArrangement usernameHz, loginHz, passwordHz;
    private Label UserNameLabel, PasswordLabel;
    private TextBox inputUsername;
    private PasswordTextBox inputPassword;
    private Image ourLogo;
    private CheckBox checkbox_IsDebugSession, checkbox_SavePassword;
    Integer int_ColWidth;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        tools=new tuuberCommonSubroutines(this);
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            tools.dbg(e.toString());
        }

        Login = new VerticalScrollArrangement(this);
        Login.WidthPercent(100);
        Login.HeightPercent(100);
        ErrorNotifier = new Notifier(Login);

        web_Login = new Web(Login);
        usernameHz = new HorizontalArrangement(Login);
        usernameHz.WidthPercent(100);
        UserNameLabel= new Label (usernameHz);
        UserNameLabel.Text("Username:");
        UserNameLabel.FontBold(true);
        UserNameLabel.WidthPercent(25);
        UserNameLabel.TextAlignment(Component.ALIGNMENT_OPPOSITE);

        inputUsername = new TextBox (usernameHz);
        inputUsername.BackgroundColor(Component.COLOR_NONE);
        inputUsername.WidthPercent(75);
        if (applicationSettings.lastLogin == applicationSettings.default_lastLogin) {
            inputUsername.Text("testing.this@tcfe.ie");
        }
        else {
            inputUsername.Text(applicationSettings.lastLogin);
        }

        passwordHz = new HorizontalArrangement(Login);
        passwordHz.WidthPercent(100);
        PasswordLabel = new Label(passwordHz);
        PasswordLabel.Text("Password:");
        PasswordLabel.FontBold(true);
        PasswordLabel.WidthPercent(25);
        PasswordLabel.TextAlignment(Component.ALIGNMENT_OPPOSITE);
        inputPassword = new PasswordTextBox(passwordHz);
        if (applicationSettings.SavePassword) {
            inputPassword.Text(applicationSettings.string_SavedPassword);
        }
        else {
            inputPassword.Text("");
        }
        inputPassword.WidthPercent(70);

        TableArrangement menu=new TableArrangement(Login);
        int_ColWidth = 200;

        menu.Columns(3);
        menu.Rows(3);
        menu.WidthPercent(100);
//        menu.HeightPercent(100);

        Integer int_SideOffset=15;
        Button button_PadLeft=new Button(menu);
        button_PadLeft.Column(0);
        button_PadLeft.Row(0);
        button_PadLeft.Text("");
        button_PadLeft.Visible(true);
        button_PadLeft.BackgroundColor(Component.COLOR_NONE);
        button_PadLeft.WidthPercent(10);

        Button button_PadRight=new Button(menu);
        button_PadRight.Column(2);
        button_PadRight.Row(0);
        button_PadRight.Text("");
        button_PadRight.Visible(true);
        button_PadRight.BackgroundColor(Component.COLOR_NONE);
        button_PadRight.WidthPercent(10);

//        loginHz = new HorizontalArrangement(Login);
        button_Login = new Button (menu);
        button_Login.Text("Login");
        button_Login.Column(1);
        button_Login.Row(0);

        Button button_Pad = new Button(menu);
        button_Pad.BackgroundColor(Component.COLOR_NONE);
        button_Pad.Text("");
        button_Pad.Height(5);
        button_Pad.Column(1);
        button_Pad.Row(1);

        button_Register = new Button(menu);
        button_Register.Text("Register");
        button_Register.Column(1);
        button_Register.Row(2);

        button_CommonFormatting(button_Login, button_Register);
        ourLogo=new Image(Login);
        ourLogo.Picture(applicationSettings.ourLogo);
        ourLogo.ScalePictureToFit(false);
        ourLogo.Height(320);
        Login.AlignHorizontal(Component.ALIGNMENT_CENTER);

        checkbox_SavePassword = new CheckBox(Login);
        checkbox_SavePassword.Text("Save password");
        checkbox_SavePassword.Checked(applicationSettings.SavePassword);

        checkbox_IsDebugSession = new CheckBox(Login);
        checkbox_IsDebugSession.Text("This is a developer session");
        checkbox_IsDebugSession.Checked(applicationSettings.IsDebugSession);

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
            applicationSettings.IsDebugSession= checkbox_IsDebugSession.Checked();
            return true;
        }
        else if (eventName.equals("Click")) {
            if (component.equals(button_Login)) {
                web_Login.Url(
                        applicationSettings.baseURL
                                + "?cmd=LOGIN"
                                + "&email="
                                + inputUsername.Text()
                                + "&password="
                                + inputPassword.Text()
                );
                web_Login.Get();
                return true;
            }
            else if (component.equals(button_Register)) {
                switchForm("screen06_Register");
                return true;
            }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_Login)) {
                Integer status = (Integer) params[1];
                String textOfResponse = (String) params[3];
                webGotText(status.toString(), textOfResponse);
                return true;
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
                applicationSettings.IsDebugSession= checkbox_IsDebugSession.Checked();
                applicationSettings.SavePassword=checkbox_SavePassword.Checked();
                if (applicationSettings.SavePassword) {
                    applicationSettings.string_SavedPassword=inputPassword.Text();
                }
                else {
                    applicationSettings.string_SavedPassword="NotSaved";
                }
                applicationSettings.set();
                switchForm("screen03_MainMenu");
            } else {
                ErrorNotifier.ShowMessageDialog("Login failed, check details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            ErrorNotifier.ShowMessageDialog("JSON Exception (check password) " + temp, "Information", "OK");
        }
        else {
            ErrorNotifier.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
    }

    void button_CommonFormatting (Button... b) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i=0;
        int len = b.length;
        while ((i < len) && (b[i] != null)) {
            b[i].WidthPercent(80);
            b[i].BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
//            b[i].BackgroundColor(Component.COLOR_BLACK);
            b[i].FontBold(true);
            b[i].Shape(Component.BUTTON_SHAPE_ROUNDED);
            b[i].TextColor(Component.COLOR_WHITE);
            b[i].FontTypeface(Component.TYPEFACE_SANSSERIF);
            b[i].FontSize(applicationSettings.int_ButtonTextSize);
            b[i].Height(40);
            i++;
        }
    }

}
