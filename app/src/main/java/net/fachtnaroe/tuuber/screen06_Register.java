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
    private Image Image1, Image2, Image3, Image4, Image5, Image6;
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

        Image1 = new Image (Register);
        PhoneHZ = new HorizontalArrangement (Register);
        eMailHZ = new HorizontalArrangement (Register);
        FirstNameHZ = new HorizontalArrangement (Register);
        LastNameHZ = new HorizontalArrangement (Register);
        PasswordHZ = new HorizontalArrangement (Register);
        ConfirmPasswordHZ = new HorizontalArrangement (Register);
        TelephoneLabel = new Label(menu);
        TelephoneLabel.Text ("Phone Number");
        TelephoneLabel.Column(1);
        TelephoneLabel.Row(1);
        Telephone = new TextBox(menu);
        Telephone.Text ("");
        Telephone.Column(2);
        Telephone.Row(1);
        eMailLabel = new Label(eMailHZ);
        eMailLabel.Text ("Email");
        eMail = new TextBox(eMailHZ);
        eMail.Text ("");
        FirstNameLabel = new Label (FirstNameHZ);
        FirstNameLabel.Text ("FirstName");
        FirstName = new TextBox (FirstNameHZ);
        FirstName.Text ("");
        LastNameLabel = new Label(LastNameHZ);
        LastNameLabel.Text ("LastName");
        LastName = new TextBox (LastNameHZ);
        LastName.Text ("");
        Image2 = new Image (Register);
        PasswordLabel = new Label (PasswordHZ);
        PasswordLabel.Text ("CreatePassword");
        Password = new PasswordTextBox (PasswordHZ);
        Password.Text ("");
        ConfirmPasswordLabel = new Label (ConfirmPasswordHZ);
        ConfirmPasswordLabel.Text ("ConfirmPassword");
        ConfirmPassword = new PasswordTextBox (ConfirmPasswordHZ);
        ConfirmPassword.Text ("");
        Image3 = new Image (Register);

        TermsConditionsHZ = new HorizontalArrangement(Register);
        TermsConditions = new Button(TermsConditionsHZ);
        TermsConditions.Text ("Terms & Conditions");
        TCAgree = new CheckBox(TermsConditionsHZ);
        TCAgree.Text ("Agree?");
        TCAgree.Enabled(false);
        TCLabel = new Label(TermsConditionsHZ);
        TCLabel.BackgroundColor(Component.COLOR_BLUE);
        TCLabel.Visible(false);

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
