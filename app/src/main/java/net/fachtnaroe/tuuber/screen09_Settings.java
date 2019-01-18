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
    fr_aPerson thisPersonsDetails = new fr_aPerson();
    Web detailsWeb, detailsWebSave, passwordWeb, passwordWebSave;
    Notifier messages;
    String version;
    TextBox versionBox, debugBox, phoneBox, eMailBox, userFirstBox, userFamilyBox, centralDebugBox;
    PasswordTextBox oldPassBox, newPassBox, confirmPassBox;
    Button debugButton, submitDetails, submitPassword, temp;
    Label phoneLabel, eMailLabel, userFirstLabel, userFamilyLabel, oldPassLabel, newPassLabel, confirmPassLabel;
    HorizontalArrangement userFirstHz, userFamilyHz, phoneHz, eMailHz, oldPassHz,newPassHz, confirmHz;
    VerticalArrangement detailsVt, passwordVt;

    protected void $define() {

        VerticalArrangement Settings = new VerticalArrangement(this);

        settings.pID="4";
        detailsWeb = new Web(Settings);
        detailsWebSave=new Web(Settings);
        passwordWeb = new Web(Settings);
        passwordWebSave = new Web(Settings);

        detailsVt = new VerticalArrangement(Settings);
        userFirstHz = new HorizontalArrangement(detailsVt);
        userFirstLabel = new Label(userFirstHz);
        userFirstLabel.Text("First name:");
        userFirstBox = new TextBox(userFirstHz);
        userFirstBox.Text("["+ this.startupValue+"]");

        userFamilyHz = new HorizontalArrangement(detailsVt);
        userFamilyLabel = new Label(userFamilyHz);
        userFamilyLabel.Text("Family name:");
        userFamilyBox = new TextBox(userFamilyHz);
        userFamilyBox.Text("["+ this.startupValue+"]");

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
        temp = new Button(detailsVt);
        temp.Text("Test this");
        centralDebugBox = new TextBox(detailsVt);
        centralDebugBox.WidthPercent(100);
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
        EventDispatcher.registerEventForDelegation(this, "detailsWebSave", "GotText");
        EventDispatcher.registerEventForDelegation(this, "passwordWeb", "GotText");
        EventDispatcher.registerEventForDelegation(this, "passwordWebSave", "GotText");
        EventDispatcher.registerEventForDelegation(this,"eMailBox","LostFocus");
        EventDispatcher.registerEventForDelegation(this,"phoneBox","LostFocus");
        EventDispatcher.registerEventForDelegation(this, "none", "BackPressed");
        EventDispatcher.registerEventForDelegation(this, "temp", "Click");

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
        else if (component.equals(temp) && eventName.equals("Click")) {
//            ak_aPerson myP = new ak_aPerson();
//            myP.email="me@here.ie";
////            eMailBox.toString("S");
//            myP.validEmail(centralDebugBox);
            return true;
        }
        else if (component.equals(submitDetails) && eventName.equals("Click")) {
            // copy from screen elements to data
            thisPersonsDetails.email=eMailBox.Text();
            thisPersonsDetails.first=userFirstBox.Text();
            thisPersonsDetails.family=userFamilyBox.Text();
            thisPersonsDetails.phone=phoneBox.Text();
            // prepare to pass to back end
            detailsWebSave.Url(settings.baseURL
                    + "?action=PUT"
                    + "&entity=person"
                    + "&pID=" + settings.pID
                    + "&sessionID=" + settings.sessionID
                    + "&first=" +thisPersonsDetails.first
                    + "&family=" + thisPersonsDetails.family
                    + "&email=" + thisPersonsDetails.email
                    + "&phone=" + thisPersonsDetails.phone
            );
            detailsWebSave.Get();
            return true;
        }
        else if (component.equals(eMailBox) && eventName.equals("LostFocus")) {
            fr_aPerson tempPerson=new fr_aPerson();
            tempPerson.email=eMailBox.Text();
            if (!tempPerson.validEmail()) {
                submitDetails.Enabled(false);
            }
        }
        else if (component.equals(phoneBox) && eventName.equals("LostFocus")) {
            fr_aPerson tempPerson=new fr_aPerson();
            tempPerson.phone=phoneBox.Text();
            if (!tempPerson.validPhone()) {
                submitDetails.Enabled(false);
            }
        }
        else if (component.equals(detailsWeb) && eventName.equals("GotText")) {
            String status = params[1].toString();
            String textOfResponse = (String) params[3];
            detailsGotText(status, textOfResponse);
            return true;
        }
        else if (component.equals(detailsWebSave) && eventName.equals("GotText")) {
            String status = params[1].toString();
            String textOfResponse = (String) params[3];
            detailsSaveGotText(status, textOfResponse);
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
            if (parser.getString("pID").equals(settings.pID)) {
                //using matching pID to check success
                // do something
                thisPersonsDetails.email=parser.getString("email");
                thisPersonsDetails.first=parser.getString("first");
                thisPersonsDetails.family=parser.getString("family");
                thisPersonsDetails.phone=parser.getString("phone");


                eMailBox.Text(thisPersonsDetails.email);
                phoneBox.Text(thisPersonsDetails.phone);
                userFirstBox.Text(thisPersonsDetails.first);
                userFamilyBox.Text(thisPersonsDetails.family);

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

    public void detailsSaveGotText(String status, String textOfResponse) {
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (parser.getString("result").equals("OK")) {
                // do something
//                <block type="controls_openAnotherScreen" id="0T9@Zj[!|+x=~y8V7Z7G">
                Form.finishActivity();
            } else {
                messages.ShowMessageDialog("Error saving details", "Information", "OK");
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
