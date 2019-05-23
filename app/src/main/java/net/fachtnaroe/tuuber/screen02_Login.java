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

    private Web web_Login;
    private VerticalScrollArrangement Login;
    private Notifier notifier_MessagesPopUp;
    private HorizontalArrangement usernameHz, passwordHz;
    private Label UserNameLabel, PasswordLabel;
    private TextBox inputUsername;
    private PasswordTextBox inputPassword;
    private Image ourLogo;
    private CheckBox checkbox_IsDeveloperSession, checkbox_SavePassword, checkbox_IsAdminSession;
    Integer int_ColWidth;
    Integer int_ImageSize=250;

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
        Login.AlignHorizontal(Component.ALIGNMENT_CENTER);
        notifier_MessagesPopUp = new Notifier(Login);

        web_Login = new Web(Login);
        usernameHz = new HorizontalArrangement(Login);
        usernameHz.WidthPercent(100);
        UserNameLabel= new Label (usernameHz);
        UserNameLabel.Text(tools.fn_téacs_aistriú("username")+": ");
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
        PasswordLabel.Text(tools.fn_téacs_aistriú("password")+": ");
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
        button_Login.Text( tools.fn_téacs_aistriú("login"));
        button_Login.Column(1);
        button_Login.Row(0);

        Button button_Pad = new Button(menu);
        button_Pad.BackgroundColor(Component.COLOR_NONE);
        button_Pad.Text("");
        button_Pad.Height(5);
        button_Pad.Column(1);
        button_Pad.Row(1);

        button_Register = new Button(menu);
        button_Register.Text(tools.fn_téacs_aistriú("register"));
        button_Register.Column(1);
        button_Register.Row(2);

        tools.button_CommonFormatting(80, button_Login);
        tools.button_CommonFormatting(80, button_Register);
        ourLogo=new Image(Login);
        ourLogo.Picture(applicationSettings.ourLogo);
        ourLogo.ScalePictureToFit(false);
//        ourLogo.Height(int_ImageSize);
        Login.AlignHorizontal(Component.ALIGNMENT_CENTER);
        ourLogo.WidthPercent(100);

        checkbox_SavePassword = new CheckBox(Login);
        checkbox_SavePassword.Text(tools.fn_téacs_aistriú("save_password"));
        checkbox_SavePassword.Checked(applicationSettings.SavePassword);

        checkbox_IsDeveloperSession = new CheckBox(Login);
        checkbox_IsDeveloperSession.Text(tools.fn_téacs_aistriú("developer_session"));
        checkbox_IsDeveloperSession.Checked(applicationSettings.IsDeveloperSession);

        checkbox_IsAdminSession = new CheckBox(Login);
        checkbox_IsAdminSession.Text(tools.fn_téacs_aistriú("administrative_session"));
        checkbox_IsAdminSession.Checked(applicationSettings.IsAdminSession);

//        Button pdp=(Button)tools.padding(Login,10,15);

        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "Changed");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        tools.dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("BackPressed")) {
            // prevents return to splash screen
            this.moveTaskToBack(true);
            return true;
        }
        else if (eventName.equals("Changed")) {
            // for debugging session
            applicationSettings.IsDeveloperSession = checkbox_IsDeveloperSession.Checked();
            applicationSettings.IsAdminSession= checkbox_IsAdminSession.Checked();
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
        tools.dbg(textOfResponse);
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            temp = parser.getString("result");
            if (parser.getString("result").equals("OK")) {
                applicationSettings.pID= parser.getString("pID");
                applicationSettings.lastLogin= inputUsername.Text();
                applicationSettings.sessionID=parser.getString("sessionID");
                applicationSettings.IsDeveloperSession =
                        (checkbox_IsDeveloperSession.Checked() &&
                                tools.binary_same_as(parser.getInt("role"),tools.role_Developer)
                        );
                applicationSettings.IsAdminSession=
                        (checkbox_IsAdminSession.Checked() &&
                                tools.binary_same_as(parser.getInt("role"),tools.role_Administrator)
                        );
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
                notifier_MessagesPopUp.ShowMessageDialog("Error 2.242; Login failed, check details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_MessagesPopUp.ShowMessageDialog("Error 2.246; JSON Exception (check password) " + temp, "Information", "OK");
        }
        else {
            notifier_MessagesPopUp.ShowMessageDialog("Error 2.249; Problem connecting with server","Information", "OK");
        }
    }

}
