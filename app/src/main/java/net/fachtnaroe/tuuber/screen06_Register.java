package net.fachtnaroe.tuuber;

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
    private Image Image1, Image2, Image3, Image4, Image5, Image6;
    private VerticalArrangement Register;
    private HorizontalArrangement TermsConditionsHZ, CreateHZ, PhoneHZ, eMailHZ, LastNameHZ, FirstNameHZ, PasswordHZ, ConfirmPasswordHZ;
    private CheckBox TCAgree;
    private Label TelephoneLabel, eMailLabel, LastNameLabel, FirstNameLabel, PasswordLabel, Password1Label;
    private TextBox Telephone,eMail, LastName, FirstName;
    private String baseURL ="https://fachtnaroe.net/tuuber-2019?";
    private Web Creation;
    private Notifier Creation_Notifier, Universal_Notifier, Web_Notifier;
    private PasswordTextBox Password, ConfirmPassword;
    private dd_aPerson User;

    protected void $define() {

        Register = new VerticalArrangement (this);
        Image1 = new Image (Register);
        PhoneHZ = new HorizontalArrangement (Register);
        eMailHZ = new HorizontalArrangement (Register);
        LastNameHZ = new HorizontalArrangement (Register);
        FirstNameHZ = new HorizontalArrangement (Register);
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
        LastNameLabel = new Label(LastNameHZ);
        LastNameLabel.Text ("LastName");
        LastName = new TextBox (LastNameHZ);
        LastName.Text ("John");
        FirstNameLabel = new Label (FirstNameHZ);
        FirstNameLabel.Text ("FirstName");
        FirstName = new TextBox (FirstNameHZ);
        FirstName.Text ("John");
        Image2 = new Image (Register);
        Password1Label = new Label (PasswordHZ);
        Password1Label.Text ("CreatePassword");
        Password = new PasswordTextBox (PasswordHZ);
        Password.Text ("abc");
        PasswordLabel = new Label (ConfirmPasswordHZ);
        PasswordLabel.Text ("ConfirmPassword");
        ConfirmPassword = new PasswordTextBox (ConfirmPasswordHZ);
        ConfirmPassword.Text ("abc");
        Image3 = new Image (Register);

        TermsConditionsHZ = new HorizontalArrangement(Register);
        TCAgree = new CheckBox(TermsConditionsHZ);
        TCAgree.Text ("Agree?");
        TermsConditions = new Button(TermsConditionsHZ);
        TermsConditions.Text ("Terms&Conditions");

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

    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(Create) && eventName.equals("Click")) {
            User.phone = Telephone.Text();
            User.eMail = eMail.Text();
            if (FirstName.Text().length() <1) {
                Universal_Notifier.ShowMessageDialog("Invalid FirstName", "Error", "Confirm");
                return true;
            }
            if (LastName.Text().length() <1){
                Universal_Notifier.ShowMessageDialog("Invalid LastName", "Error", "Confirm");
                return true;
            }
            if (!User.valid_eMail()) {
                Universal_Notifier.ShowMessageDialog("Invalid Email", "Error", "Confirm");
                return true;
            }
            if (!User.valid_phone()) {
                Universal_Notifier.ShowMessageDialog("Invalid Phone Number", "Error", "Confirm");
                return true;
            }
            if (!Password.Text().equals(ConfirmPassword.Text())) {
                Universal_Notifier.ShowMessageDialog("Passwords Don't Match", "Error", "Confirm");
                return true;
            }
            if (!TCAgree.Checked()) {
                Universal_Notifier.ShowMessageDialog("Agree to Terms and Conditions", "Error", "Confirm");
                return true;
            }

            Creation.Url(
                    baseURL +
                            "entity=person&action=POST&first=" +
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
    public void webGotText(String status, String textOfResponse) {

        String temp=new String();
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            temp = parser.getString("result");
            if (parser.getString("result").equals("OK")) {
                Creation_Notifier.ShowMessageDialog("User created", "Success!", "Confirm");
                switchForm("screen02_Login");
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
}
