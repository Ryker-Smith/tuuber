package net.fachtnaroe.tuuber;
// http://thunkableblocks.blogspot.ie/2017/07/java-code-snippets-for-app-inventor.html

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.PasswordTextBox;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
//import gnu.lists.FString;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/


public class screen09_Settings extends Form implements HandlesEventDispatching {

    settingsOnline settings = new settingsOnline();
    Web detailsWeb, passwordWeb;
    Notifier messages;
    String version;
    TextBox versionBox, debugBox, phoneBox, eMailBox, userBox;
    PasswordTextBox oldPassBox, newPassBox, confirmPassBox;
    Button debugButton, submitDetails, submitPassword;
    Label phoneLabel, eMailLabel, userLabel, oldPassLabel, newPassLabel, confirmPassLabel;
    HorizontalArrangement userHz, phoneHz, eMailHz, oldPassHz,newPassHz, confirmHz;
    VerticalArrangement detailsVt, passwordVt;

    protected void $define() {

        VerticalArrangement Settings = new VerticalArrangement(this);

        settings.pID="4";
        detailsWeb = new Web(Settings);
        passwordWeb = new Web(Settings);

        detailsVt = new VerticalArrangement(Settings);
        userHz = new HorizontalArrangement(detailsVt);
        userLabel = new Label(userHz);
        userLabel.Text("Name:");
        userBox = new TextBox(userHz);
        userBox.Text("["+ this.startupValue+"]");
        userBox.Enabled(false);

        phoneHz = new HorizontalArrangement(detailsVt);
        phoneLabel=new Label(phoneHz);
        phoneLabel.Text("Phone:");
        phoneBox = new TextBox(phoneHz);

        eMailHz=new HorizontalArrangement(detailsVt);
        eMailLabel = new Label(eMailHz);
        eMailLabel.Text("eMail:");
        eMailBox = new TextBox(eMailHz);

        submitDetails = new Button(detailsVt);
        submitDetails.Text("Save changes");
//
        passwordVt = new VerticalArrangement(Settings);
        oldPassHz = new HorizontalArrangement(passwordVt);
        oldPassLabel = new Label(oldPassHz);
        oldPassLabel.Text("Old password:");
        oldPassBox = new PasswordTextBox(oldPassHz);

        newPassHz = new HorizontalArrangement(passwordVt);
        newPassLabel=new Label(newPassHz);
        newPassLabel.Text("New password:");
        newPassBox = new PasswordTextBox(newPassHz);

        confirmHz=new HorizontalArrangement(passwordVt);
        confirmPassLabel = new Label(confirmHz);
        confirmPassLabel.Text("Confirm new:");
        submitPassword = new Button(passwordVt);
        submitPassword.Text("Change now");

        versionBox = new TextBox(Settings);
        debugButton = new Button(Settings);
        debugButton.Text("Debug");
        debugBox = new TextBox(Settings);
        debugBox.MultiLine(true);
        debugBox.HeightPercent(100);
        debugBox.WidthPercent(100);

        try {
            version = settings.get("tuuber2019_version");
            dbg(settings.baseURL);
        }
        catch (MalformedURLException error) {
            error.getStackTrace();
            dbg("Error");
        }

        dbg("Starting");
        versionBox.Text(version);
        messages = new Notifier(Settings);

        EventDispatcher.registerEventForDelegation(this, "LoginButton", "Click");
        EventDispatcher.registerEventForDelegation(this, "debugButton", "Click");
        EventDispatcher.registerEventForDelegation(this, "detailsWeb", "GotText");
        EventDispatcher.registerEventForDelegation(this, "passwordWeb", "GotText");


        detailsWeb.Url(settings.baseURL
                + "?action=GET"
                + "&entity=person"
                + "&pID=" + settings.pID
                + "&sessionID=" + settings.sessionID
        );
        detailsWeb.Get();
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(debugButton) && eventName.equals("Click")) {
            versionBox.Text(version);
            dbg(settings.lastValue);
            return true;
        }
        else if (component.equals(debugButton) && eventName.equals("Click")) {
            versionBox.Text(version);
            dbg(settings.lastValue);
            return true;
        }
        else if (component.equals(detailsWeb) && eventName.equals("GotText")) {
            String status = params[1].toString();
            String textOfResponse = (String) params[3];
            detailsGotText(status, textOfResponse);
            return true;
        }
        else if (component.equals(passwordWeb) && eventName.equals("GotText")) {
            return true;
        }
        return false;
    }

    public void detailsGotText(String status, String textOfResponse) {
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (parser.getString("pID").equals(settings.pID)) { //using matching pID to check success
                // do something
                eMailBox.Text(parser.getString("email"));
                phoneBox.Text(parser.getString("phone"));
                userBox.Text(parser.getString("first")+" "+parser.getString("family"));
            } else {
                messages.ShowMessageDialog("Error getting details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messages.ShowMessageDialog("JSON Exception", "Information", "OK");
        }
        else {
            messages.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
    }

    void dbg (String debugMsg) {
        debugBox.Text( debugMsg + "\n" + debugBox.Text());
    }
}
