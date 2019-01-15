package net.fachtnaroe.tuuber;

import android.support.v7.app.AppCompatActivity;
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
import android.os.Bundle;

public class screen06_Register extends Form implements HandlesEventDispatching {

    private Button Create, TermsConditions;
    private Image Image1, Image2, Image3, Image4, Image5, Image6;
    private VerticalArrangement Register, VerticalArrangement1;
    private HorizontalArrangement HorizontalArrangement1, HorizontalArrangement2, HorizontalArrangement3, HorizontalArrangement4, HorizontalArrangement5, HorizontalArrangement6, HorizontalArrangement7, HorizontalArrangement8;
    private TableArrangement TableArrangement1;
    private CheckBox TCAgree;
    private Label TelephoneLabel, eMailLabel, LastNameLabel, FirstNameLabel, PasswordReEnter, Password1Label;
    private TextBox Telephone,eMail, LastName, FirstName, Password1, Password2;
    private String baseURL ="https://fachtnaroe.net/tuuber-2019?";
    private Web Web1;
    private Notifier Terms_Conditions_Notifier, CheckedBox_Notifier;

    protected void $define() {

        Register = new VerticalArrangement (this);
        Image1 = new Image (Register);
        VerticalArrangement1 = new VerticalArrangement (Register);
        HorizontalArrangement3 = new HorizontalArrangement (VerticalArrangement1);
        HorizontalArrangement4 = new HorizontalArrangement (VerticalArrangement1);
        HorizontalArrangement5 = new HorizontalArrangement (VerticalArrangement1);
        HorizontalArrangement6 = new HorizontalArrangement (VerticalArrangement1);
        HorizontalArrangement7 = new HorizontalArrangement (VerticalArrangement1);
        HorizontalArrangement8 = new HorizontalArrangement (VerticalArrangement1);
        TelephoneLabel = new Label(HorizontalArrangement3);
        TelephoneLabel.Text ("Phone Number");
        Telephone = new TextBox(HorizontalArrangement3);
        Telephone.Text ("Phone");
        eMailLabel = new Label(HorizontalArrangement4);
        eMailLabel.Text ("Email");
        eMail = new TextBox(HorizontalArrangement4);
        eMail.Text ("EmailDetail");
        LastNameLabel = new Label(HorizontalArrangement5);
        LastNameLabel.Text ("LastName");
        LastName = new TextBox (HorizontalArrangement5);
        LastName.Text ("Lastname");
        FirstNameLabel = new Label (HorizontalArrangement6);
        FirstNameLabel.Text ("FirstName");
        FirstName = new TextBox (HorizontalArrangement6);
        FirstName.Text ("Firstname");
        Image2 = new Image (Register);
        Password1Label = new Label (HorizontalArrangement7);
        Password1Label.Text ("CreatePassword");
        Password1 = new TextBox (HorizontalArrangement7);
        Password1.Text ("Password");
        PasswordReEnter = new Label (HorizontalArrangement8);
        PasswordReEnter.Text ("ConfirmPassword");
        Password2 = new TextBox (HorizontalArrangement8);
        Password2.Text ("Confirm");
        Image3 = new Image (Register);

        HorizontalArrangement1 = new HorizontalArrangement(Register);
        TCAgree = new CheckBox(HorizontalArrangement1);
        TCAgree.Text ("Agree?");
        TermsConditions = new Button(HorizontalArrangement1);
        TermsConditions.Text ("Terms&Conditions");

        Image4 = new Image (Register);

        HorizontalArrangement2 = new HorizontalArrangement(Register);
        Image5 = new Image (HorizontalArrangement2);
        Create = new Button(HorizontalArrangement2);
        Create.Text ("Create");
        Image6 = new Image (HorizontalArrangement2);

        Web1 = new Web(Register);
        Terms_Conditions_Notifier = new Notifier(Register);
        CheckedBox_Notifier = new Notifier(Register);

        EventDispatcher.registerEventForDelegation(this, "Create", "Click");
        EventDispatcher.registerEventForDelegation(this, "Web1", "GotText");

    }

   // public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
       // if (component.equals(Create) && eventName.equals("Click"))
   // }



}
