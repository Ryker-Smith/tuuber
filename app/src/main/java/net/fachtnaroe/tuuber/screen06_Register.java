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
import com.google.appinventor.components.runtime.TableArrangement;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;

public class screen06_Register extends Form implements HandlesEventDispatching {

    private Button Create, TermsConditions;
    private Image Image1, Image2, Image3, Image4, Image5, Image6;
    private VerticalArrangement Register;
    private HorizontalArrangement TermsConditionsHZ, CreateHZ, PhoneHZ, eMailHZ, LastNameHZ, FirstNameHZ, PasswordHZ, ConfirmPasswordHZ;
    private TableArrangement TableArrangement1;
    private CheckBox TCAgree;
    private Label TelephoneLabel, eMailLabel, LastNameLabel, FirstNameLabel, PasswordReEnter, Password1Label;
    private TextBox Telephone,eMail, LastName, FirstName, Password1, Password2;
    private String baseURL ="https://fachtnaroe.net/tuuber-2019?";
    private Web Creation;
    private Notifier Terms_Conditions_Notifier, CheckedBox_Notifier;
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
        Telephone.Text ("Phone");
        eMailLabel = new Label(eMailHZ);
        eMailLabel.Text ("Email");
        eMail = new TextBox(eMailHZ);
        eMail.Text ("EmailDetail");
        LastNameLabel = new Label(LastNameHZ);
        LastNameLabel.Text ("LastName");
        LastName = new TextBox (LastNameHZ);
        LastName.Text ("Lastname");
        FirstNameLabel = new Label (FirstNameHZ);
        FirstNameLabel.Text ("FirstName");
        FirstName = new TextBox (FirstNameHZ);
        FirstName.Text ("Firstname");
        Image2 = new Image (Register);
        Password1Label = new Label (PasswordHZ);
        Password1Label.Text ("CreatePassword");
        Password1 = new TextBox (PasswordHZ);
        Password1.Text ("Password");
        PasswordReEnter = new Label (ConfirmPasswordHZ);
        PasswordReEnter.Text ("ConfirmPassword");
        Password2 = new TextBox (ConfirmPasswordHZ);
        Password2.Text ("Confirm");
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
        Terms_Conditions_Notifier = new Notifier(Register);
        CheckedBox_Notifier = new Notifier(Register);
        User = new dd_aPerson();

        EventDispatcher.registerEventForDelegation(this, "Create", "Click");
        EventDispatcher.registerEventForDelegation(this, "Creation", "GotText");

    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(Create) && eventName.equals("Click")) {
            User.phone = Telephone.Text();
            if (!User.valid_phone()) {
                return true;
            }

        }
        return true;
    }




}
