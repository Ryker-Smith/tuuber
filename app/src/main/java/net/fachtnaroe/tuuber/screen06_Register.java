package net.fachtnaroe.tuuber;

import android.graphics.Color;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.CheckBox;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.PasswordTextBox;
import com.google.appinventor.components.runtime.TableArrangement;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
//import com.google.appinventor.components.runtime.ActivityStarter;

import org.json.JSONException;
import org.json.JSONObject;

public class screen06_Register extends Form implements HandlesEventDispatching {

    tuuber_Settings applicationSettings;
    tuuberCommonSubroutines tools;

    private Button button_Create, button_TermsConditions;
    private VerticalArrangement Register;
    private HorizontalArrangement TermsConditionsHZ, OverEighteenHZ, CreateHZ, PaddingHZ;
    private CheckBox TCAgree, OverEighteen;
    private Label TelephoneLabel, eMailLabel, LastNameLabel, FirstNameLabel, ConfirmPasswordLabel, PasswordLabel, TCLabel;
    private TextBox Telephone,eMail, LastName, FirstName;
    private Web web_CreateUser;
    private Notifier notifier_MessagesPopUp, notifier_EmailActivationPopUp;
    private PasswordTextBox Password, ConfirmPassword;
    private dd_aPerson User;
    private String flag_OverEighteen="N";

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        tools = new tuuberCommonSubroutines(this);
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            dbg(e.toString());
        }
        Register = new VerticalArrangement (this);
        Register.WidthPercent(100);
        Register.HeightPercent(100);

        TableArrangement menu = new TableArrangement(Register);

        menu.Columns(2);
        menu.Rows(8);

        menu.WidthPercent(100);
        menu.HeightPercent(50);

        TelephoneLabel = new Label(menu);
        TelephoneLabel.Text (tools.fn_téacs_aistriú("phone_number",tools.capitalize_first)+": ");
        TelephoneLabel.Column(0);
        TelephoneLabel.Row(2);
        Telephone = new TextBox(menu);
        Telephone.Text ("");
        Telephone.Column(1);
        Telephone.Row(2);

        eMailLabel = new Label(menu);
        eMailLabel.Text ( tools.fn_téacs_aistriú("email")+": ");
        eMailLabel.Column(0);
        eMailLabel.Row(3);
        eMail = new TextBox(menu);
        eMail.Text ("");
        eMail.Column(1);
        eMail.Row(3);

        FirstNameLabel = new Label (menu);
        FirstNameLabel.Text (tools.fn_téacs_aistriú("first_name",tools.capitalize_first)+": ");
        FirstNameLabel.Column(0);
        FirstNameLabel.Row(0);
        FirstName = new TextBox (menu);
        FirstName.Text ("");
        FirstName.Column(1);
        FirstName.Row(0);

        LastNameLabel = new Label(menu);
        LastNameLabel.Text (tools.fn_téacs_aistriú("family_name",tools.capitalize_first)+": ");
        LastNameLabel.Column(0);
        LastNameLabel.Row(1);
        LastName = new TextBox (menu);
        LastName.Text ("");
        LastName.Column(1);
        LastName.Row(1);

        PasswordLabel = new Label (menu);
        PasswordLabel.Text (tools.fn_téacs_aistriú("password",tools.capitalize_first)+": ");
        PasswordLabel.Column(0);
        PasswordLabel.Row(4);
        Password = new PasswordTextBox (menu);
        Password.Text ("");
        Password.Column(1);
        Password.Row(4);

        ConfirmPasswordLabel = new Label (menu);
        ConfirmPasswordLabel.Text (tools.fn_téacs_aistriú("confirm_password",tools.capitalize_first)+": ");
        ConfirmPasswordLabel.Column(0);
        ConfirmPasswordLabel.Row(5);
        ConfirmPassword = new PasswordTextBox (menu);
        ConfirmPassword.Text ("");
        ConfirmPassword.Column(1);
        ConfirmPassword.Row(5);

        TermsConditionsHZ = new HorizontalArrangement(Register);
        button_TermsConditions = new Button(TermsConditionsHZ);
        button_TermsConditions.Text (tools.fn_téacs_aistriú("terms_and_conditions"));
        TCAgree = new CheckBox(TermsConditionsHZ);
        TCAgree.Text (tools.fn_téacs_aistriú("agree")+"?");
        TCAgree.Enabled(false);
        TCLabel = new Label(TermsConditionsHZ);
        TCLabel.BackgroundColor(Component.COLOR_BLUE);
        TCLabel.Visible(false);

        OverEighteenHZ = new HorizontalArrangement(Register);
        OverEighteen = new CheckBox(OverEighteenHZ);
        OverEighteen.Checked(false);
        OverEighteen.Text (tools.fn_téacs_aistriú("am_over_eighteen"));

        PaddingHZ = new HorizontalArrangement(Register);
        Button button_Pad = new Button(PaddingHZ);
        button_Pad.BackgroundColor(Component.COLOR_NONE);
        button_Pad.Text("");
        button_Pad.HeightPercent(3);

        CreateHZ = new HorizontalArrangement(Register);
        button_Create = new Button(CreateHZ);
        button_Create.Text (tools.fn_téacs_aistriú("create"));
        // 2019-08-29 CHANGE THIS BACK????
        button_Create.Enabled(true);

        web_CreateUser = new Web(Register);
        notifier_MessagesPopUp = new Notifier(Register);
        notifier_EmailActivationPopUp = new Notifier(Register);
        User = new dd_aPerson(Register);
        tools.button_CommonFormatting(45, button_Create, button_TermsConditions);

        EventDispatcher.registerEventForDelegation(this, formName,"GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "OtherScreenClosed" );
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterChoosing");
        EventDispatcher.registerEventForDelegation(this, formName, "Changed");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);
        if ((component.equals(this)) && (eventName.equals("OtherScreenClosed"))) {
            thisOtherScreenClosed((String) params[0], (Object) params[1]);
            if (TCLabel.Text().equals("screen10_TermsAndConditions EEEEE Good")) {
                TCAgree.Checked(true);
                return true;
            }
        }
        else if (eventName.equals("Changed")) {
            if (component.equals(OverEighteen)) {
                button_Create.Enabled(OverEighteen.Checked());
                if (OverEighteen.Checked()) {
                    flag_OverEighteen="Y";
                }
                else {
                    flag_OverEighteen="N";
                }
                return true;
            }
        }
        else if (eventName.equals("AfterChoosing")) {
               if (component.equals(notifier_EmailActivationPopUp)) {
                    screen06_Register.finishActivity();
                }
        }
        else if (eventName.equals("Click")) {
                if (component.equals(button_Create)){
                    User.phone = Telephone.Text();
                    User.email = eMail.Text();
                    User.first = FirstName.Text();
                    User.family = LastName.Text();
                    User.password = Password.Text();
                    button_Create.Enabled(false);

                    if (!User.validPhone()) {
                        TelephoneLabel.TextColor(Color.RED);
                        eMailLabel.TextColor(Color.BLACK);
                        FirstNameLabel.TextColor(Color.BLACK);
                        LastNameLabel.TextColor(Color.BLACK);
                        PasswordLabel.TextColor(Color.BLACK);
                        ConfirmPasswordLabel.TextColor(Color.BLACK);
                        TCAgree.TextColor(Color.BLACK);
                        notifier_MessagesPopUp.ShowMessageDialog("Invalid Phone Number", "Error", "OK");
                        button_Create.Enabled(true);
                        return true;
                    }
                    if (!User.validEmail()) {
                        eMailLabel.TextColor(Color.RED);
                        TelephoneLabel.TextColor(Color.BLACK);
                        FirstNameLabel.TextColor(Color.BLACK);
                        LastNameLabel.TextColor(Color.BLACK);
                        PasswordLabel.TextColor(Color.BLACK);
                        ConfirmPasswordLabel.TextColor(Color.BLACK);
                        TCAgree.TextColor(Color.BLACK);
                        notifier_MessagesPopUp.ShowMessageDialog("Invalid Email", "Error", "OK");
                        button_Create.Enabled(true);
                        return true;
                    }

                    if (!User.valid_first()) {
                        FirstNameLabel.TextColor(Color.RED);
                        TelephoneLabel.TextColor(Color.BLACK);
                        eMailLabel.TextColor(Color.BLACK);
                        LastNameLabel.TextColor(Color.BLACK);
                        PasswordLabel.TextColor(Color.BLACK);
                        ConfirmPasswordLabel.TextColor(Color.BLACK);
                        TCAgree.TextColor(Color.BLACK);
                        notifier_MessagesPopUp.ShowMessageDialog("Invalid FirstName", "Error", "OK");
                        button_Create.Enabled(true);
                        return true;
                    }
                    if (!User.valid_family()) {
                        LastNameLabel.TextColor(Color.RED);
                        TelephoneLabel.TextColor(Color.BLACK);
                        eMailLabel.TextColor(Color.BLACK);
                        FirstNameLabel.TextColor(Color.BLACK);
                        PasswordLabel.TextColor(Color.BLACK);
                        ConfirmPasswordLabel.TextColor(Color.BLACK);
                        TCAgree.TextColor(Color.BLACK);
                        notifier_MessagesPopUp.ShowMessageDialog("Invalid LastName", "Error", "OK");
                        button_Create.Enabled(true);
                        return true;
                    }
                    if (!User.valid_password()) {
                        PasswordLabel.TextColor(Color.RED);
                        ConfirmPasswordLabel.TextColor(Color.BLACK);
                        TelephoneLabel.TextColor(Color.BLACK);
                        eMailLabel.TextColor(Color.BLACK);
                        FirstNameLabel.TextColor(Color.BLACK);
                        LastNameLabel.TextColor(Color.BLACK);
                        TCAgree.TextColor(Color.BLACK);
                        notifier_MessagesPopUp.ShowMessageDialog("Invalid Password", "Error", "OK");
                        button_Create.Enabled(true);
                        return true;
                    }
                    if (!Password.Text().equals(ConfirmPassword.Text())) {
                        PasswordLabel.TextColor(Color.RED);
                        ConfirmPasswordLabel.TextColor(Color.RED);
                        TelephoneLabel.TextColor(Color.BLACK);
                        eMailLabel.TextColor(Color.BLACK);
                        FirstNameLabel.TextColor(Color.BLACK);
                        LastNameLabel.TextColor(Color.BLACK);
                        TCAgree.TextColor(Color.BLACK);
                        notifier_MessagesPopUp.ShowMessageDialog("Passwords Don't Match", "Error", "OK");
                        button_Create.Enabled(true);
                        return true;
                    }
                    if (!TCAgree.Checked()) {
                        TCAgree.TextColor(Color.RED);
                        TelephoneLabel.TextColor(Color.BLACK);
                        eMailLabel.TextColor(Color.BLACK);
                        FirstNameLabel.TextColor(Color.BLACK);
                        LastNameLabel.TextColor(Color.BLACK);
                        PasswordLabel.TextColor(Color.BLACK);
                        ConfirmPasswordLabel.TextColor(Color.BLACK);
                        notifier_MessagesPopUp.ShowMessageDialog("Please agree to the Terms and Conditions, or cease using this App", "Error", "OK");
                        button_Create.Enabled(true);
                        return true;
                    }
                    notifier_MessagesPopUp.ShowAlert("This will take about 3 minutes...");
                    web_CreateUser.Url(
                            applicationSettings.baseURL +
                                    "?entity=person&action=POST&first=" +
                                    FirstName.Text() +
                                    "&family=" +
                                    LastName.Text() +
                                    "&phone=" +
                                    Telephone.Text() +
                                    "&email=" +
                                    eMail.Text() +
                                    "&pw=" +
                                    ConfirmPassword.Text() +
                                    "&adult=" +
                                    flag_OverEighteen
                    );
                    web_CreateUser.Get();

                    return true;
                }
                else if (component.equals(button_TermsConditions)) {
                    switchForm("screen10_TermsAndConditions");
                    return true;
                }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_CreateUser) ){
                Integer status = (Integer) params[1];
                String textOfResponse = (String) params[3];
                fn_GotText_CreateUser(status.toString(), textOfResponse);
                return true;
            }
        }

        return true;
    }
    void dbg (String debugMsg) {
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }

    public void fn_GotText_CreateUser(String status, String textOfResponse) {
        String temp=new String();
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (parser.getString("result").equals("OK")) {
//                notifier_EmailActivationPopUp.ShowMessageDialog("User created. Please check your email for an activation message *before* you can use " + applicationSettings.appName, "Success!", "Grand");
                notifier_EmailActivationPopUp.ShowChooseDialog("User created. Please check your email for an activation message *before* you can use " + applicationSettings.appName + " (but don't forget to look in your SPAM folder)", "Success!", "Grand","",false);
            } else {
                notifier_MessagesPopUp.ShowMessageDialog("Create failed, check details (1)(" + textOfResponse +")", "Information", "OK");
                button_Create.Enabled(true);
            }

        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_MessagesPopUp.ShowMessageDialog("Create failed, check details (2)(" + textOfResponse +")", "Information", "OK");
            button_Create.Enabled(true);
        }
        else {
            notifier_MessagesPopUp.ShowMessageDialog("Problem connecting with server","Information", "OK");
            button_Create.Enabled(true);
        }
    }
    public void thisOtherScreenClosed(String otherScreenName, Object result) {
        TCLabel.Text(otherScreenName+" EEEEE " + result);

    }
}
