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
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;

import org.json.JSONException;
import org.json.JSONObject;

public class screen06_Register extends Form implements HandlesEventDispatching {

    private Button Create, TermsConditions;
    private tuuber_Settings applicationSettings;
    private Image Image1, Image2, Image3, Image4, Image5, Image6;
    private VerticalArrangement Register;
    private HorizontalArrangement TermsConditionsHZ, CreateHZ, PhoneHZ, eMailHZ, LastNameHZ, FirstNameHZ, PasswordHZ, ConfirmPasswordHZ;
    private CheckBox TCAgree;
    private Label TelephoneLabel, eMailLabel, LastNameLabel, FirstNameLabel, ConfirmPasswordLabel, PasswordLabel, TCLabel;
    private TextBox Telephone,eMail, LastName, FirstName;
    private String baseURL ="https://fachtnaroe.net/tuuber-2019";
    private Web Creation;
    private Notifier Creation_Notifier, Universal_Notifier, Web_Notifier;
    private PasswordTextBox Password, ConfirmPassword;
    private dd_aPerson User;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        this.BackgroundImage(applicationSettings.backgroundImageName);
        Register = new VerticalArrangement (this);
        Image1 = new Image (Register);
        PhoneHZ = new HorizontalArrangement (Register);
        eMailHZ = new HorizontalArrangement (Register);
        FirstNameHZ = new HorizontalArrangement (Register);
        LastNameHZ = new HorizontalArrangement (Register);
        PasswordHZ = new HorizontalArrangement (Register);
        ConfirmPasswordHZ = new HorizontalArrangement (Register);
        TelephoneLabel = new Label(PhoneHZ);
        TelephoneLabel.Text ("Phone Number");
        Telephone = new TextBox(PhoneHZ);
        Telephone.Text ("0123456789");
        eMailLabel = new Label(eMailHZ);
        eMailLabel.Text ("Email");
        eMail = new TextBox(eMailHZ);
        eMail.Text ("");
        FirstNameLabel = new Label (FirstNameHZ);
        FirstNameLabel.Text ("FirstName");
        FirstName = new TextBox (FirstNameHZ);
        FirstName.Text ("John");
        LastNameLabel = new Label(LastNameHZ);
        LastNameLabel.Text ("LastName");
        LastName = new TextBox (LastNameHZ);
        LastName.Text ("John");
        Image2 = new Image (Register);
        PasswordLabel = new Label (PasswordHZ);
        PasswordLabel.Text ("CreatePassword");
        Password = new PasswordTextBox (PasswordHZ);
        Password.Text ("abc");
        ConfirmPasswordLabel = new Label (ConfirmPasswordHZ);
        ConfirmPasswordLabel.Text ("ConfirmPassword");
        ConfirmPassword = new PasswordTextBox (ConfirmPasswordHZ);
        ConfirmPassword.Text ("abc");
        Image3 = new Image (Register);

        TermsConditionsHZ = new HorizontalArrangement(Register);
        TermsConditions = new Button(TermsConditionsHZ);
        TermsConditions.Text ("Terms&Conditions");
        TCAgree = new CheckBox(TermsConditionsHZ);
        TCAgree.Text ("Agree?");
        TCLabel = new Label(TermsConditionsHZ);
        TCLabel.BackgroundColor(Component.COLOR_BLUE);

        Image4 = new Image (Register);

        CreateHZ = new HorizontalArrangement(Register);
        Image5 = new Image (CreateHZ);
        Create = new Button(CreateHZ);
        Create.Text ("Create");
        Image6 = new Image (CreateHZ);

        Creation = new Web(Register);
        Creation_Notifier = new Notifier(Register);
        Universal_Notifier = new Notifier(Register);
        Web_Notifier = new Notifier(Register);
        User = new dd_aPerson();

        EventDispatcher.registerEventForDelegation(this, "Create", "Click");
        EventDispatcher.registerEventForDelegation(this, "Creation", "GotText");
        EventDispatcher.registerEventForDelegation(this, "OtherScreenClosedEvent", "OtherScreenClosed" );
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("failure");
        if(eventName.equals("OtherScreenClosed") ) {
            thisOtherScreenClosed((String) params[0], (Object) params[1]);
            return true;
        }
        else if (eventName.equals("Click")) {
            dbg("fail");
            if (component.equals(Create)){
                User.phone = Telephone.Text();
                User.eMail = eMail.Text();
                if (!User.valid_phone()) {
                    TelephoneLabel.TextColor(Color.RED);
                    eMailLabel.TextColor(Color.BLACK);
                    FirstNameLabel.TextColor(Color.BLACK);
                    LastNameLabel.TextColor(Color.BLACK);
                    PasswordLabel.TextColor(Color.BLACK);
                    ConfirmPasswordLabel.TextColor(Color.BLACK);
                    TCAgree.TextColor(Color.BLACK);
                    Universal_Notifier.ShowMessageDialog("Invalid Phone Number", "Error", "Confirm");
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
                    Universal_Notifier.ShowMessageDialog("Invalid Email", "Error", "Confirm");
                    return true;
                }

                if (FirstName.Text().length() <1) {
                    FirstNameLabel.TextColor(Color.RED);
                    TelephoneLabel.TextColor(Color.BLACK);
                    eMailLabel.TextColor(Color.BLACK);
                    LastNameLabel.TextColor(Color.BLACK);
                    PasswordLabel.TextColor(Color.BLACK);
                    ConfirmPasswordLabel.TextColor(Color.BLACK);
                    TCAgree.TextColor(Color.BLACK);
                    Universal_Notifier.ShowMessageDialog("Invalid FirstName", "Error", "Confirm");
                    return true;
                }

                if (LastName.Text().length() <1){
                    LastNameLabel.TextColor(Color.RED);
                    TelephoneLabel.TextColor(Color.BLACK);
                    eMailLabel.TextColor(Color.BLACK);
                    FirstNameLabel.TextColor(Color.BLACK);
                    PasswordLabel.TextColor(Color.BLACK);
                    ConfirmPasswordLabel.TextColor(Color.BLACK);
                    TCAgree.TextColor(Color.BLACK);
                    Universal_Notifier.ShowMessageDialog("Invalid LastName", "Error", "Confirm");
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
                    Universal_Notifier.ShowMessageDialog("Passwords Don't Match", "Error", "Confirm");
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
                    Universal_Notifier.ShowMessageDialog("Agree to Terms and Conditions", "Error", "Confirm");
                    return true;
                }

                Creation.Url(
                        baseURL +
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
                startActivity(new Intent().setClass(this, screen10_TermsAndConditions.class));
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
            temp = parser.getString("result");
            if (parser.getString("result").equals("OK")) {
                Creation_Notifier.ShowMessageDialog("User created", "Success!", "Confirm");
            } else {
                Web_Notifier.ShowMessageDialog("Creation failed, check details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            Web_Notifier.ShowMessageDialog("Creation failed, check details" + temp, "Information", "OK");
        }
        else {
            Web_Notifier.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
    }
    public void thisOtherScreenClosed(String otherScreenName, Object result) {
//        TCLabel.Text(result.toString());
        TCLabel.Text("EEEEE");
    }
}
