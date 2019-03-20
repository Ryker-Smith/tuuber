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
    private HorizontalArrangement TermsConditionsHZ, CreateHZ, PaddingHZ;
    private CheckBox TCAgree;
    private Label TelephoneLabel, eMailLabel, LastNameLabel, FirstNameLabel, ConfirmPasswordLabel, PasswordLabel, TCLabel;
    private TextBox Telephone,eMail, LastName, FirstName;
    private Web web_CreateUser;
    private Notifier notifier_MessagesPopUp;
    private PasswordTextBox Password, ConfirmPassword;
    private dd_aPerson User;

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
        TelephoneLabel.Text ("Phone number: ");
        TelephoneLabel.Column(0);
        TelephoneLabel.Row(2);
        Telephone = new TextBox(menu);
        Telephone.Text ("");
        Telephone.Column(1);
        Telephone.Row(2);

        eMailLabel = new Label(menu);
        eMailLabel.Text ("Email: ");
        eMailLabel.Column(0);
        eMailLabel.Row(3);
        eMail = new TextBox(menu);
        eMail.Text ("");
        eMail.Column(1);
        eMail.Row(3);

        FirstNameLabel = new Label (menu);
        FirstNameLabel.Text ("First name: ");
        FirstNameLabel.Column(0);
        FirstNameLabel.Row(0);
        FirstName = new TextBox (menu);
        FirstName.Text ("");
        FirstName.Column(1);
        FirstName.Row(0);

        LastNameLabel = new Label(menu);
        LastNameLabel.Text ("Family name: ");
        LastNameLabel.Column(0);
        LastNameLabel.Row(1);
        LastName = new TextBox (menu);
        LastName.Text ("");
        LastName.Column(1);
        LastName.Row(1);

        PasswordLabel = new Label (menu);
        PasswordLabel.Text ("Password: ");
        PasswordLabel.Column(0);
        PasswordLabel.Row(4);
        Password = new PasswordTextBox (menu);
        Password.Text ("");
        Password.Column(1);
        Password.Row(4);

        ConfirmPasswordLabel = new Label (menu);
        ConfirmPasswordLabel.Text ("Confirm password: ");
        ConfirmPasswordLabel.Column(0);
        ConfirmPasswordLabel.Row(5);
        ConfirmPassword = new PasswordTextBox (menu);
        ConfirmPassword.Text ("");
        ConfirmPassword.Column(1);
        ConfirmPassword.Row(5);

        TermsConditionsHZ = new HorizontalArrangement(Register);
        button_TermsConditions = new Button(TermsConditionsHZ);
        button_TermsConditions.Text ("Terms & Conditions");
        TCAgree = new CheckBox(TermsConditionsHZ);
        TCAgree.Text ("Agree?");
        TCAgree.Enabled(false);
        TCLabel = new Label(TermsConditionsHZ);
        TCLabel.BackgroundColor(Component.COLOR_BLUE);
        TCLabel.Visible(false);

        PaddingHZ = new HorizontalArrangement(Register);
        Button button_Pad = new Button(PaddingHZ);
        button_Pad.BackgroundColor(Component.COLOR_NONE);
        button_Pad.Text("");
        button_Pad.HeightPercent(3);

        CreateHZ = new HorizontalArrangement(Register);
        button_Create = new Button(CreateHZ);
        button_Create.Text ("Create");

        web_CreateUser = new Web(Register);
        notifier_MessagesPopUp = new Notifier(Register);
        User = new dd_aPerson(Register);
        tools.button_CommonFormatting(45, button_Create, button_TermsConditions);

        EventDispatcher.registerEventForDelegation(this, formName,"GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "OtherScreenClosed" );
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
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
        else if (eventName.equals("Click")) {
            if (component.equals(button_Create)){
                User.phone = Telephone.Text();
                User.eMail = eMail.Text();
                User.First = FirstName.Text();
                User.Family = LastName.Text();
                User.password = Password.Text();
                User.set();
                if (!User.valid_phone()) {
                    TelephoneLabel.TextColor(Color.RED);
                    eMailLabel.TextColor(Color.BLACK);
                    FirstNameLabel.TextColor(Color.BLACK);
                    LastNameLabel.TextColor(Color.BLACK);
                    PasswordLabel.TextColor(Color.BLACK);
                    ConfirmPasswordLabel.TextColor(Color.BLACK);
                    TCAgree.TextColor(Color.BLACK);
                    notifier_MessagesPopUp.ShowMessageDialog("Invalid Phone Number", "Error", "OK");
                    return true;
                }
                if (!User.valid_eMail()) {
                    eMailLabel.TextColor(Color.RED);
                    TelephoneLabel.TextColor(Color.BLACK);
                    FirstNameLabel.TextColor(Color.BLACK);
                    LastNameLabel.TextColor(Color.BLACK);
                    PasswordLabel.TextColor(Color.BLACK);
                    ConfirmPasswordLabel.TextColor(Color.BLACK);
                    TCAgree.TextColor(Color.BLACK);
                    notifier_MessagesPopUp.ShowMessageDialog("Invalid Email", "Error", "OK");
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
                    return true;
                }
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
                                ConfirmPassword.Text()
                );
                web_CreateUser.Get();

                return true;
            }
            if (component.equals(button_TermsConditions)) {
                dbg("error");
                switchForm("screen10_TermsAndConditions");
                return true;
            }
        }
        else if (component.equals(web_CreateUser) && eventName.equals("GotText")){
            Integer status = (Integer) params[1];
            String textOfResponse = (String) params[3];
            fn_GotText_CreateUser(status.toString(), textOfResponse);
            return true;
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
                notifier_MessagesPopUp.ShowMessageDialog("User created", "Success!", "Grand");
                screen06_Register.finishActivity();
            } else {
                notifier_MessagesPopUp.ShowMessageDialog("Create failed, check details (1)(" + textOfResponse +")", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_MessagesPopUp.ShowMessageDialog("Create failed, check details (2)(" + textOfResponse +")", "Information", "OK");
        }
        else {
            notifier_MessagesPopUp.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
    }
    public void thisOtherScreenClosed(String otherScreenName, Object result) {
//        TCLabel.Text(result.toString());
        TCLabel.Text(otherScreenName+" EEEEE " + result);

    }
}
