package net.fachtnaroe.tuuber;
// http://thunkableblocks.blogspot.ie/2017/07/java-code-snippets-for-app-inventor.html

import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.ImagePicker;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.PasswordTextBox;
import com.google.appinventor.components.runtime.Slider;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.WebViewer;
import com.google.appinventor.components.runtime.util.YailList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/


public class screen09_Settings extends Form implements HandlesEventDispatching {

    tuuber_Settings applicationSettings;
    fr_aPerson thisPersonsDetails = new fr_aPerson();
    Web detailsWeb, detailsWebSave, passwordWeb, passwordWebSave;
    Notifier messages;
    TextBox listViewSizeBox, phoneBox, eMailBox, userFirstBox, userFamilyBox, backgroundImageTextBox;
    PasswordTextBox oldPassBox, newPassBox, confirmPassBox;
    Button submitDetails, submitPassword, buttonMainMenu, buttonRefresh, submitCustomisation;
    Label listViewSizeLabel, phoneLabel, eMailLabel, userFirstLabel, userFamilyLabel, oldPassLabel, newPassLabel, confirmPassLabel, label_pID;
    HorizontalArrangement listViewSizeHz, userFirstHz, userFamilyHz, phoneHz, eMailHz, oldPassHz, newPassHz, confirmHz, toolbarHz;
    VerticalArrangement detailsVt, passwordVt, customisationVt;
    Notifier messagesPopUp;
    ImagePicker myImagePicker;
    fachtnaSlider slider_FontSize;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            dbg(e.toString());
        }
        VerticalScrollArrangement screen09_Settings = new VerticalScrollArrangement(this);
        screen09_Settings.WidthPercent(100);
        screen09_Settings.HeightPercent(100);

        // The 'toolbar'
        toolbarHz = new HorizontalArrangement(screen09_Settings);
        buttonMainMenu = new Button(toolbarHz);
        buttonMainMenu.Width(40);
        buttonMainMenu.Height(40);
        buttonMainMenu.Image("buttonHome.png");
        label_pID = new Label(toolbarHz);
        label_pID.HTMLFormat(true);
        label_pID.Text("I am user: #" + applicationSettings.pID + "<br><small><small>Settings</small></small>");
        label_pID.Height(40);
        label_pID.FontSize(20);
        label_pID.WidthPercent(70);
        label_pID.TextAlignment(Component.ALIGNMENT_CENTER);
        buttonRefresh = new Button(toolbarHz);
        buttonRefresh.Width(40);
        buttonRefresh.Height(40);
        buttonRefresh.FontSize(8);
        buttonRefresh.Image("buttonRefresh.png");

        customisationVt = new VerticalArrangement(screen09_Settings);
        customisationVt.BackgroundColor(Component.COLOR_NONE);
        customisationVt.WidthPercent(100);
        backgroundImageTextBox = new TextBox(customisationVt);
        backgroundImageTextBox.Text(applicationSettings.backgroundImageName);

        myImagePicker = new ImagePicker(customisationVt);
        myImagePicker.Text("Crash Program");
        myImagePicker.FontSize(8);
        listViewSizeHz=new HorizontalArrangement(customisationVt);
        listViewSizeLabel=new Label(listViewSizeHz);
        listViewSizeLabel.Text("List<br>font size:");
        listViewSizeLabel.HTMLFormat(true);
        listViewSizeBox = new TextBox(listViewSizeHz);
        listViewSizeBox.NumbersOnly(true);
        listViewSizeBox.Text(applicationSettings.intListViewsize.toString());
        listViewSizeBox.Width(50);
        listViewSizeBox.TextAlignment(Component.ALIGNMENT_CENTER);
        slider_FontSize= new fachtnaSlider(customisationVt);
        slider_FontSize.MinValue(1);
        slider_FontSize.MaxValue(100);
        slider_FontSize.ColorLeft(Component.COLOR_RED);
        slider_FontSize.ColorRight(Component.COLOR_RED);
        slider_FontSize.ThumbEnabled(true);
        slider_FontSize.WidthPercent(100);
        slider_FontSize.Height(10);
        slider_FontSize.ThumbPosition(Integer.valueOf(listViewSizeBox.Text()));

        submitCustomisation = new Button(customisationVt);
        submitCustomisation.Text("Save program settings");

        detailsVt = new VerticalArrangement(screen09_Settings);
        detailsVt.BackgroundColor(Component.COLOR_NONE);
        userFirstHz = new HorizontalArrangement(detailsVt);
        userFirstLabel = new Label(userFirstHz);
        userFirstLabel.HTMLFormat(true);
        userFirstLabel.Text("First<br>name:");
        userFirstBox = new TextBox(userFirstHz);
        userFamilyHz = new HorizontalArrangement(detailsVt);
        userFamilyLabel = new Label(userFamilyHz);
        userFamilyLabel.Text("Family<br>name:");
        userFamilyLabel.HTMLFormat(true);
        userFamilyBox = new TextBox(userFamilyHz);
        phoneHz = new HorizontalArrangement(detailsVt);
        phoneLabel = new Label(phoneHz);
        phoneLabel.Text("Phone:");
        phoneBox = new TextBox(phoneHz);
        eMailHz = new HorizontalArrangement(detailsVt);
        eMailLabel = new Label(eMailHz);
        eMailLabel.Text("eMail:");
        eMailBox = new TextBox(eMailHz);

        submitDetails = new Button(detailsVt);
        submitDetails.Text("Save detail changes");

        passwordVt = new VerticalArrangement(screen09_Settings);
        passwordVt.BackgroundColor(Component.COLOR_NONE);
        oldPassHz = new HorizontalArrangement(passwordVt);
        oldPassLabel = new Label(oldPassHz);
        oldPassLabel.Text("Old password:");
        oldPassBox = new PasswordTextBox(oldPassHz);
        newPassHz = new HorizontalArrangement(passwordVt);
        newPassLabel = new Label(newPassHz);
        newPassLabel.Text("New password:");
        newPassBox = new PasswordTextBox(newPassHz);
        confirmHz = new HorizontalArrangement(passwordVt);
        confirmPassLabel = new Label(confirmHz);
        confirmPassLabel.Text("Confirm new:");
        confirmPassBox = new PasswordTextBox(confirmHz);

        submitPassword = new Button(passwordVt);
        submitPassword.Text("Change password now");

        w100listPTB(oldPassBox, newPassBox, confirmPassBox);
        w100listTB(eMailBox, phoneBox, userFamilyBox, userFirstBox);

        messages = new Notifier(screen09_Settings);
        messagesPopUp = new Notifier(screen09_Settings);

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "LostFocus");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterPicking");
        EventDispatcher.registerEventForDelegation(this, formName, "BeforePicking");
        EventDispatcher.registerEventForDelegation(this, formName, "ActivityResult");
        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "PositionChanged");

        detailsWeb = new Web(this);
        detailsWebSave = new Web(this);
        passwordWeb = new Web(this);
        passwordWebSave = new Web(this);

        detailsWeb.Url(applicationSettings.baseURL
                + "?action=GET"
                + "&entity=person"
                + "&pID=" + applicationSettings.pID
                + "&sessionID=" + applicationSettings.sessionID
        );
        detailsWeb.Get();
        dbg("End $define " + formName);
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);


        if (eventName.equals("BackPressed")) {
            finish();
            return true;
        }
        else if (eventName.equals("PositionChanged")) {
            listViewSizeBox.Text( Integer.toString( Math.round( slider_FontSize.ThumbPosition() ) ) );
            return true;
        }
        else if (eventName.equals("ActivityResult")) {
//            finish();
            return true;
        }
        else if (eventName.equals("BeforePicking")) {
            if (component.equals(myImagePicker)) {
                this.BackgroundImage(myImagePicker.Selection());
                return true;
            }
            return false;
        }
        else if (eventName.equals("AfterPicking")) {
            if (component.equals(myImagePicker)) {
                this.BackgroundImage(myImagePicker.Selection());
//                backgroundImageTextBox.Text( myImagePicker.Selection() );
                return true;
            }
            return false;
        }
        else if (eventName.equals("Click")) {
            if (component.equals(buttonMainMenu)) {
                finish();
                return true;
            }
            else if (component.equals(submitCustomisation) ) {
                applicationSettings.backgroundImageName=backgroundImageTextBox.Text();
                if (Integer.valueOf(listViewSizeBox.Text()) >= applicationSettings.minimimum_intListViewsize) {
                    applicationSettings.intListViewsize = Integer.valueOf(listViewSizeBox.Text());
                }
                applicationSettings.set();
                finish();
                return true;
            }
            else if (component.equals(submitDetails) ) {
                // copy from screen elements to data
                thisPersonsDetails.email = eMailBox.Text();
                thisPersonsDetails.first = userFirstBox.Text();
                thisPersonsDetails.family = userFamilyBox.Text();
                thisPersonsDetails.phone = phoneBox.Text();
                // prepare to pass to back end
                detailsWebSave.Url(applicationSettings.baseURL
                        + "?action=PUT"
                        + "&entity=person"
                        + "&pID=" + applicationSettings.pID
                        + "&sessionID=" + applicationSettings.sessionID
                        + "&first=" + thisPersonsDetails.first
                        + "&family=" + thisPersonsDetails.family
                        + "&email=" + thisPersonsDetails.email
                        + "&phone=" + thisPersonsDetails.phone
                );
                detailsWebSave.Get();
                return true;
            }
            else if (component.equals(submitPassword) ) {
                // prepare to pass to back end
                passwordWebSave.Url(applicationSettings.baseURL
                        + "?cmd=CHPWD"
                        + "&pID=" + applicationSettings.pID
                        + "&sessionID=" + applicationSettings.sessionID
                        + "&op=" + oldPassBox.Text()
                        + "&np=" + newPassBox.Text()
                        + "&cp=" + confirmPassBox.Text()
                );
                passwordWebSave.Get();
                return true;
            }
        }
        else if (eventName.equals("LostFocus")) {
            if (component.equals(eMailBox)) {
                fr_aPerson tempPerson = new fr_aPerson();
                tempPerson.email = eMailBox.Text();
                if (!tempPerson.validEmail()) {
                    submitDetails.Enabled(false);
                }
                return true;
            }
            if (component.equals(phoneBox) ) {
                fr_aPerson tempPerson = new fr_aPerson();
                tempPerson.phone = phoneBox.Text();
                if (!tempPerson.validPhone()) {
                    submitDetails.Enabled(false);
                }
                return true;
            }
        }
        else if (eventName.equals("GotText") ) {
            if (component.equals(detailsWeb)) {
                dbg((String) params[0]);
                dbg(this.formName + " detailsWeb");
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                detailsGotText(status, textOfResponse);
                return true;
            }
            else if (component.equals(detailsWebSave)) {
                dbg((String) params[0]);
                dbg(this.formName + " detailsWebSave");
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                detailsSaveGotText(status, textOfResponse);
                return true;
            }
            else if (component.equals(passwordWebSave)) {
                dbg((String) params[0]);
                dbg(this.formName + " passwordWebSave");
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                passwordSaveGotText(status, textOfResponse);
                return true;
            }
            return false;
        }
        return false;
    }

    public void detailsGotText(String status, String textOfResponse) {
        String temp2 = "";
        if (status.equals("200")) try {
            JSONObject parser = new JSONObject(textOfResponse);
            temp2 = parser.getString("pID");
            if (parser.getString("pID").equals(applicationSettings.pID)) {
                //using matching pID to check success
                // do something
                thisPersonsDetails.email = parser.getString("email");
                thisPersonsDetails.first = parser.getString("first");
                thisPersonsDetails.family = parser.getString("family");
                thisPersonsDetails.phone = parser.getString("phone");

                eMailBox.Text(thisPersonsDetails.email);
                phoneBox.Text(thisPersonsDetails.phone);
                userFirstBox.Text(thisPersonsDetails.first);
                userFamilyBox.Text(thisPersonsDetails.family);

            } else {
                messages.ShowMessageDialog("Error getting details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messages.ShowMessageDialog(textOfResponse + "JSON Exception [pID=" + applicationSettings.pID + "/" + temp2 + "]", "Information", "OK");
        }
        else {
            messages.ShowMessageDialog("Problem connecting with server", "Information", "OK");
        }
    }

    public void detailsSaveGotText(String status, String textOfResponse) {
        if (status.equals("200")) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (parser.getString("result").equals("OK")) {
                // do something
                finish();
            } else {
                messages.ShowMessageDialog("Error saving details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messages.ShowMessageDialog("JSON Exception", "Information", "OK");
        }
        else {
            messages.ShowMessageDialog("Problem connecting with server", "Information", "OK");
        }
    }

    public void passwordSaveGotText(String status, String textOfResponse) {
        JSONObject parser;
        if (status.equals("200")) try {
            parser = new JSONObject(textOfResponse);
            if (parser.getString("result").equals("OK")) {
                // do something
                finish();
            } else {
                messages.ShowMessageDialog("Error changing password", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messages.ShowMessageDialog("Server says: password not changed", "Information", "OK");
        }
        else {
            messages.ShowMessageDialog("Problem connecting with server", "Information", "OK");
        }
    }

    void dbg(String debugMsg) { System.err.print("~~~> " + debugMsg + " <~~~\n");  }

    void w100listTB(TextBox... t) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i = 0;
        int len = t.length;
        while ((i < len) && (t[i] != null)) {
            t[i].WidthPercent(100);
            t[i].BackgroundColor(Component.COLOR_NONE);
            t[i].FontBold(true);
            i++;
        }
    }

    void w100listPTB(PasswordTextBox... t) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i = 0;
        int len = t.length;
        while ((i < len) && (t[i] != null)) {
            t[i].WidthPercent(100);
            t[i].BackgroundColor(Component.COLOR_NONE);
            t[i].FontBold(true);
            i++;
        }
    }

    void fancyListView(ComponentContainer container, ListView list, String... listData) {
        list = new ListView(container);
        list.HeightPercent(10);
        list.TextSize(40);

        list = getRouteWebGotText(list, listData);
    }

    public ListView getRouteWebGotText(ListView listDisplay, String... items) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        ArrayList<String> data = new ArrayList<String>();
        WebViewer html = new WebViewer(this);

        for (int i = 0; i < items.length; i++) {
            html.WebViewString(items[i]);
            data.add(
                    html.WebViewString()
            );
        }
        YailList tempData = YailList.makeList(data);
        listDisplay.Elements(tempData);
        return listDisplay;
    }

}

