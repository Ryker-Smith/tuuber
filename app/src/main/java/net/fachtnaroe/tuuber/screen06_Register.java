package net.fachtnaroe.tuuber;

import android.content.Intent;
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
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
//import com.google.appinventor.components.runtime.ActivityStarter;

import org.json.JSONException;
import org.json.JSONObject;

public class screen06_Register extends Form implements HandlesEventDispatching {

    private Button Create, TermsConditions;
    private tuuber_Settings applicationSettings;
    private VerticalArrangement Register;
    private HorizontalArrangement TermsConditionsHZ, CreateHZ, PhoneHZ, eMailHZ, LastNameHZ, FirstNameHZ, PasswordHZ, ConfirmPasswordHZ;
    private CheckBox TCAgree;
    private Label TelephoneLabel, eMailLabel, LastNameLabel, FirstNameLabel, ConfirmPasswordLabel, PasswordLabel, TCLabel;
    private TextBox Telephone,eMail, LastName, FirstName;
//    private String baseURL ="https://fachtnaroe.net/tuuber-2019";
    private Web Creation;
    private Notifier Creation_Notifier, Universal_Notifier, Web_Notifier;
    private PasswordTextBox Password, ConfirmPassword;
    private dd_aPerson User;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
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
        menu.HeightPercent(100);

        TelephoneLabel = new Label(menu);
        TelephoneLabel.Text ("Phone Number");
        TelephoneLabel.Column(0);
        TelephoneLabel.Row(0);
        Telephone = new TextBox(menu);
        Telephone.Text ("");
        Telephone.Column(1);
        Telephone.Row(0);

        eMailLabel = new Label(menu);
        eMailLabel.Text ("Email");
        eMailLabel.Column(0);
        eMailLabel.Row(1);
        eMail = new TextBox(menu);
        eMail.Text ("");
        eMail.Column(1);
        eMail.Row(1);

        FirstNameLabel = new Label (menu);
        FirstNameLabel.Text ("FirstName");
        FirstNameLabel.Column(0);
        FirstNameLabel.Row(2);
        FirstName = new TextBox (menu);
        FirstName.Text ("");
        FirstName.Column(1);
        FirstName.Row(2);

        LastNameLabel = new Label(menu);
        LastNameLabel.Text ("LastName");
        LastNameLabel.Column(0);
        LastNameLabel.Row(3);
        LastName = new TextBox (menu);
        LastName.Text ("");
        LastName.Column(1);
        LastName.Row(3);

        PasswordLabel = new Label (menu);
        PasswordLabel.Text ("CreatePassword");
        PasswordLabel.Column(0);
        PasswordLabel.Row(4);
        Password = new PasswordTextBox (PasswordHZ);
        Password.Text ("");
        Password.Column(1);
        Password.Row(4);

        ConfirmPasswordLabel = new Label (ConfirmPasswordHZ);
        ConfirmPasswordLabel.Text ("ConfirmPassword");
        ConfirmPasswordLabel.Column(0);
        ConfirmPasswordLabel.Row(5);
        ConfirmPassword = new PasswordTextBox (ConfirmPasswordHZ);
        ConfirmPassword.Text ("");
        ConfirmPassword.Column(1);
        ConfirmPassword.Row(5);

        TermsConditions = new Button(menu);
        TermsConditions.Text ("Terms & Conditions");
        TermsConditions.Column(0);
        TermsConditions.Row(6);
        TCAgree = new CheckBox(menu);
        TCAgree.Text ("Agree?");
        TCAgree.Enabled(false);
        TCAgree.Column(1);
        TCAgree.Row(6);
        TCLabel = new Label(menu);
        TCLabel.BackgroundColor(Component.COLOR_BLUE);
        TCLabel.Visible(false);
        
        Create = new Button(menu);
        Create.Text ("Create");
        Create.Column(0);
        Create.Row(7);


        Creation = new Web(Register);
        Creation_Notifier = new Notifier(Register);
        Universal_Notifier = new Notifier(Register);
        Web_Notifier = new Notifier(Register);
        User = new dd_aPerson(Register);

        EventDispatcher.registerEventForDelegation(this, "notImportant", "GotText");
//        EventDispatcher.registerEventForDelegation(this, "notImportant", "OtherScreenClosedEvent" );
        EventDispatcher.registerEventForDelegation(this, "notImportant", "OtherScreenClosed" );
        EventDispatcher.registerEventForDelegation(this, "notImportant", "Click");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("events");

        dbg("GOT: "+formName+" "+eventName);
        if ((component.equals(this)) && (eventName.equals("OtherScreenClosed"))) {
            thisOtherScreenClosed((String) params[0], (Object) params[1]);
            if (TCLabel.Text().equals("screen10_TermsAndConditions EEEEE Good")) {
                TCAgree.Checked(true);
                return true;
            }
            return true;
        }
        else if (eventName.equals("Click")) {
            dbg("fail");
            if (component.equals(Create)){
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
                    Universal_Notifier.ShowMessageDialog("Invalid Phone Number", "Error", "OK");
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
                    Universal_Notifier.ShowMessageDialog("Invalid Email", "Error", "OK");
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
                    Universal_Notifier.ShowMessageDialog("Invalid FirstName", "Error", "OK");
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
                    Universal_Notifier.ShowMessageDialog("Invalid LastName", "Error", "OK");
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
                    Universal_Notifier.ShowMessageDialog("Invalid Password", "Error", "OK");
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
                    Universal_Notifier.ShowMessageDialog("Passwords Don't Match", "Error", "OK");
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
                    Universal_Notifier.ShowMessageDialog("Agree to Terms and Conditions", "Error", "OK");
                    return true;
                }

                Creation.Url(
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
                Creation.Get();

                return true;
            }
            if (component.equals(TermsConditions)) {
                dbg("error");
                startNewForm("screen10_TermsAndConditions","none");
                return true;
            }

        }
        else if (component.equals(Creation) && eventName.equals("GotText")){

            String stringSent =  (String) params[0];
            Integer status = (Integer) params[1];
            String encoding = (String) params[2];
            String textOfResponse = (String) params[3];

            webGotText(status.toString(), textOfResponse);
            return true;
        }
        return true;
    }
    void dbg (String debugMsg) {
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }

    public void webGotText(String status, String textOfResponse) {

        String temp=new String();
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (parser.getString("result").equals("OK")) {
                Creation_Notifier.ShowMessageDialog("User created", "Success!", "Grand");
                screen06_Register.finishActivity();
            } else {
                Web_Notifier.ShowMessageDialog("Creation failed, check details (1)(" + textOfResponse +")", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            Web_Notifier.ShowMessageDialog("Creation failed, check details (2)(" + textOfResponse +")", "Information", "OK");
        }
        else {
            Web_Notifier.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
    }
    public void thisOtherScreenClosed(String otherScreenName, Object result) {
//        TCLabel.Text(result.toString());
        TCLabel.Text(otherScreenName+" EEEEE " + result);

    }
}
