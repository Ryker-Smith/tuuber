package net.fachtnaroe.tuuber;
// http://thunkableblocks.blogspot.ie/2017/07/java-code-snippets-for-app-inventor.html

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;

import com.google.appinventor.components.runtime.CheckBox;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Button;
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
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;

import org.json.JSONException;
import org.json.JSONObject;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/


public class screen09_Settings extends Form implements HandlesEventDispatching {

    tuuber_Settings applicationSettings;
    tuuberCommonSubroutines tools;
    aPerson thisPersonsDetails = new aPerson();
    Web web_GetMyDetails, web_SaveMyDetails,  web_PasswordSave, web_RequestLocalizedText;
    Notifier notifier_Messages;
    TextBox textbox_ListViewSize, textbox_PhoneNumber, textbox_eMail, textbox_UserFirstName, textbox_UserFamilyName, backgroundImageTextBox;
    PasswordTextBox textbox_OldPassword, textbox_NewPassword, textbox_ConfirmPassword;
    Button button_SaveMyDetails, button_SubmitPassword, button_MainMenu, button_Refresh, button_SubmitCustomisation;
    Label listViewSizeLabel, label_PhoneNumber, label_eMail, label_UserFirstName, label_UserFamilyName, label_OldPassword, label_NewPassword, label_ConfirmPassword, label_pID;
    HorizontalArrangement listViewSizeHz, oldPassHz, newPassHz, confirmHz, hz_toolbar;
    VerticalArrangement detailsVt, vt_Password, vt_Customisation;
    fachtnaImagePicker myImagePicker;
    fachtnaSlider slider_FontSize;
    CheckBox checkbox_GA, checkbox_EN, checkbox_PO, checkbox_FR, checkbox_ES;
    VerticalScrollArrangement Settings;
    Label versionCode;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            tools.dbg(e.toString());
        }
        tools = new tuuberCommonSubroutines(this);
        // The 'toolbar'
        hz_toolbar = new HorizontalArrangement(this);
        button_MainMenu = new Button(hz_toolbar);
        button_MainMenu.Width(40);
        button_MainMenu.Height(40);
        button_MainMenu.Image(applicationSettings.ourLogo);
        button_Refresh = new Button(hz_toolbar);
        button_Refresh.Width(40);
        button_Refresh.Height(40);
        button_Refresh.FontSize(8);
        button_Refresh.Image("buttonRefresh.png");
        Settings = new VerticalScrollArrangement(this);
        Settings.WidthPercent(100);
        Settings.HeightPercent(100);
        versionCode=new Label(Settings);
        versionCode.FontSize(10);
        versionCode.TextColor(Color.parseColor(applicationSettings.string_ButtonColor));
        versionCode.BackgroundColor(Component.COLOR_NONE);
        versionCode.HTMLFormat(true);
        vt_Customisation = new VerticalArrangement(Settings);
        vt_Customisation.BackgroundColor(Component.COLOR_NONE);
        vt_Customisation.WidthPercent(100);
        HorizontalArrangement hz_ImageTextBox=new HorizontalArrangement(vt_Customisation);
        myImagePicker = new fachtnaImagePicker(hz_ImageTextBox);
        myImagePicker.FontBold(true);
        myImagePicker.FontSize(12);
        myImagePicker.Height(40);
        myImagePicker.WidthPercent(20);
        backgroundImageTextBox = new TextBox(hz_ImageTextBox);
        backgroundImageTextBox.WidthPercent(80);
        listViewSizeHz=new HorizontalArrangement(vt_Customisation);
        listViewSizeLabel=new Label(listViewSizeHz);
        listViewSizeLabel.HTMLFormat(true);
        listViewSizeLabel.WidthPercent(15);
        textbox_ListViewSize = new TextBox(listViewSizeHz);
        textbox_ListViewSize.NumbersOnly(true);
        textbox_ListViewSize.WidthPercent(15);
        textbox_ListViewSize.TextAlignment(Component.ALIGNMENT_CENTER);
        slider_FontSize= new fachtnaSlider(listViewSizeHz);
        slider_FontSize.WidthPercent(70);
        slider_FontSize.MinValue(1);
        slider_FontSize.MaxValue(100);
        slider_FontSize.ColorLeft(Component.COLOR_RED);
        slider_FontSize.ColorRight(Component.COLOR_RED);
        slider_FontSize.ThumbEnabled(true);
        HorizontalArrangement hz_Languages1 = new HorizontalArrangement(vt_Customisation);
        HorizontalArrangement hz_Languages2 = new HorizontalArrangement(vt_Customisation);
        checkbox_GA = new CheckBox(hz_Languages1);
        checkbox_GA.Text("Gaeilge");
        checkbox_EN = new CheckBox(hz_Languages1);
        checkbox_EN.Text("English");
        checkbox_PO = new CheckBox(hz_Languages1);
        checkbox_PO.Text("Polski");
        checkbox_FR = new CheckBox(hz_Languages2);
        checkbox_FR.Text("Français");
        checkbox_ES = new CheckBox(hz_Languages2);
        checkbox_ES.Text("Español");
        button_SubmitCustomisation = new Button(vt_Customisation);
        detailsVt = new VerticalArrangement(Settings);
        detailsVt.BackgroundColor(Component.COLOR_NONE);
        TableArrangement tabla_Sonraí = new TableArrangement(detailsVt);
        tabla_Sonraí.WidthPercent(100);
        tabla_Sonraí.Columns(2);
        tabla_Sonraí.Rows(4);
        label_UserFirstName = new Label(tabla_Sonraí);
        label_UserFirstName.HTMLFormat(true);
        label_UserFirstName.Row(0);
        label_UserFirstName.Column(0);
        textbox_UserFirstName = new TextBox(tabla_Sonraí);
        textbox_UserFirstName.Row(0);
        textbox_UserFirstName.Column(1);
        label_UserFamilyName = new Label(tabla_Sonraí);
        label_UserFamilyName.HTMLFormat(true);
        label_UserFamilyName.Row(1);
        label_UserFamilyName.Column(0);
        textbox_UserFamilyName = new TextBox(tabla_Sonraí);
        textbox_UserFamilyName.Row(1);
        textbox_UserFamilyName.Column(1);
        label_PhoneNumber = new Label(tabla_Sonraí);
        label_PhoneNumber.Row(2);
        label_PhoneNumber.Column(0);
        textbox_PhoneNumber = new TextBox(tabla_Sonraí);
        textbox_PhoneNumber.Row(2);
        textbox_PhoneNumber.Column(1);
        label_eMail = new Label(tabla_Sonraí);
        label_eMail.Row(3);
        label_eMail.Column(0);
        textbox_eMail = new TextBox(tabla_Sonraí);
        textbox_eMail.Row(3);
        textbox_eMail.Column(1);
        button_SaveMyDetails = new Button(detailsVt);
        vt_Password = new VerticalArrangement(Settings);
        vt_Password.BackgroundColor(Component.COLOR_NONE);
        oldPassHz = new HorizontalArrangement(vt_Password);
        label_OldPassword = new Label(oldPassHz);
        textbox_OldPassword = new PasswordTextBox(oldPassHz);
        newPassHz = new HorizontalArrangement(vt_Password);
        label_NewPassword = new Label(newPassHz);
        textbox_NewPassword = new PasswordTextBox(newPassHz);
        confirmHz = new HorizontalArrangement(vt_Password);
        label_ConfirmPassword = new Label(confirmHz);
        textbox_ConfirmPassword = new PasswordTextBox(confirmHz);
        button_SubmitPassword = new Button(vt_Password);
        Label pad_Bottom = new Label(Settings);
        pad_Bottom.Height(50);
        pad_Bottom.Text("");
        pad_Bottom.BackgroundColor(Component.COLOR_NONE);
        notifier_Messages = new Notifier(Settings);

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "LostFocus");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterPicking");
        EventDispatcher.registerEventForDelegation(this, formName, "BeforePicking");
        EventDispatcher.registerEventForDelegation(this, formName, "ActivityResult");
        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "PositionChanged");
        EventDispatcher.registerEventForDelegation(this, formName, "Changed");

        web_GetMyDetails = new Web(this);
        web_SaveMyDetails = new Web(this);
        web_RequestLocalizedText = new Web(this);
        web_PasswordSave = new Web(this);

        password_CommonFormatting(textbox_OldPassword, textbox_NewPassword, textbox_ConfirmPassword);
        textbox_CommonFormatting(textbox_eMail, textbox_PhoneNumber, textbox_UserFamilyName, textbox_UserFirstName);
        label_CommonFormatting(label_eMail, label_PhoneNumber, label_UserFamilyName, label_UserFirstName);
        button_CommonFormatting(button_SubmitCustomisation, button_SaveMyDetails, button_SubmitPassword);
        checkbox_CommonFormatting(checkbox_GA, checkbox_EN, checkbox_PO);
//        checkbox_PO.Enabled(false);
        if (applicationSettings.string_PreferredLanguage.equals("ga")) {
            checkbox_GA.Checked(true);
        }
        else if (applicationSettings.string_PreferredLanguage.equals("en")) {
            checkbox_EN.Checked(true);
        }
        else if (applicationSettings.string_PreferredLanguage.equals("po")) {
            checkbox_PO.Checked(true);
        }
        else if (applicationSettings.string_PreferredLanguage.equals("fr")) {
            checkbox_FR.Checked(true);
        }
        else if (applicationSettings.string_PreferredLanguage.equals("es")) {
            checkbox_ES.Checked(true);
        }
        myImagePicker.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
        myImagePicker.TextColor(Component.COLOR_WHITE);
        myImagePicker.Shape(BUTTON_SHAPE_ROUNDED);
        myImagePicker.FontSize(12);

        fn_UI_Text();
        fn_GetMyDetails();
        fn_GetProgramSettings();
    }

    void fn_UI_Text() {
        label_pID =tools.fn_HeadingLabel(
                hz_toolbar, label_pID, applicationSettings.pID,
                tools.fn_téacs_aistriú("settings",tools.capitalize_first)
        );
        versionCode.Text(tools.fn_téacs_aistriú("version")+": <b>" + applicationSettings.versionCode + "</b>");
        myImagePicker.Text(tools.fn_téacs_aistriú("picker",tools.capitalize_first));
        listViewSizeLabel.Text(tools.fn_téacs_aistriú("font_size_in_lists",tools.capitalize_each)+":");
        button_SubmitCustomisation.Text(tools.fn_téacs_aistriú("save_program_settings",tools.capitalize_first));
        label_UserFirstName.Text(tools.fn_téacs_aistriú("first_name",tools.capitalize_first)+":");
        label_UserFamilyName.Text(tools.fn_téacs_aistriú("family_name",tools.capitalize_first)+":");
        label_PhoneNumber.Text(tools.fn_téacs_aistriú("phone",tools.capitalize_first)+":");
        label_eMail.Text(tools.fn_téacs_aistriú("email",tools.capitalize_none)+":");
        button_SaveMyDetails.Text(tools.fn_téacs_aistriú("save_detail_changes",tools.capitalize_none));
        label_OldPassword.Text(tools.fn_téacs_aistriú("old_password",tools.capitalize_none)+":");
        label_NewPassword.Text(tools.fn_téacs_aistriú("new_password",tools.capitalize_none)+":");
        label_ConfirmPassword.Text(tools.fn_téacs_aistriú("confirm_password",tools.capitalize_none)+":");
        button_SubmitPassword.Text(tools.fn_téacs_aistriú("change_password_now",tools.capitalize_none));
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        tools.dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("BackPressed")) {
            finish();
            return true;
        }
        else if (eventName.equals("PositionChanged")) {
            textbox_ListViewSize.Text( Integer.toString( Math.round( slider_FontSize.ThumbPosition() ) ) );
            return true;
        }
        else if (eventName.equals("ActivityResult")) {
            return true;
        }
        else if (eventName.equals("BeforePicking")) {
            tools.dbg(Environment.getExternalStorageDirectory().toString());
            myImagePicker.Image( backgroundImageTextBox.Text() );
            if (component.equals(myImagePicker)) {
//                myImagePicker.
                this.BackgroundImage(myImagePicker.Selection());
                return true;
            }
            return false;
        }
        else if (eventName.equals("Changed")) {  // 'radio' buttons
            if (component.equals(checkbox_GA)) {
                if (checkbox_GA.Checked() == true) {
                    checkbox_EN.Checked(false);
                    checkbox_PO.Checked(false);
                    checkbox_FR.Checked(false);
                    checkbox_ES.Checked(false);
                    applicationSettings.string_PreferredLanguage="ga";
                    applicationSettings.set();
                    web_RequestLocalizedText.Url(
                            applicationSettings.localisationBaseUrl +
                                    "?s=" +
                                    applicationSettings.default_sessionID +
                                    "&app=túber"+
                                    "&f=json" +
                                    "&a=gettext" +
                                    "&l=" +
                                    applicationSettings.string_PreferredLanguage
                    );
                    //applicationSettings.set(); // save the new preferred language
                    web_RequestLocalizedText.Get();
//                    tools.dbg("Sent: " + web_RequestLocalizedText.Url());
                    return true;
                }
            }
            else if (component.equals(checkbox_EN)) {
                if (checkbox_EN.Checked() == true) {
                    checkbox_GA.Checked(false);
                    checkbox_PO.Checked(false);
                    checkbox_FR.Checked(false);
                    checkbox_ES.Checked(false);
                    applicationSettings.string_PreferredLanguage="en";
                    applicationSettings.set();
                    web_RequestLocalizedText.Url(
                            applicationSettings.localisationBaseUrl +
                                    "?s=" +
                                    applicationSettings.default_sessionID +
                                    "&app=túber"+
                                    "&f=json" +
                                    "&a=gettext" +
                                    "&l=" +
                                    applicationSettings.string_PreferredLanguage
                    );
                    web_RequestLocalizedText.Get();
//                    tools.dbg("Sent: " + web_RequestLocalizedText.Url());
                    return true;
                }
            }
            else if (component.equals(checkbox_PO)) {
                if (checkbox_PO.Checked() == true) {
                    checkbox_GA.Checked(false);
                    checkbox_EN.Checked(false);
                    checkbox_FR.Checked(false);
                    checkbox_ES.Checked(false);
                    applicationSettings.string_PreferredLanguage="po";
                    applicationSettings.set();
                    web_RequestLocalizedText.Url(
                            applicationSettings.localisationBaseUrl +
                                    "?s=" +
                                    applicationSettings.default_sessionID +
                                    "&app=túber"+
                                    "&f=json" +
                                    "&a=gettext" +
                                    "&l=" +
                                    applicationSettings.string_PreferredLanguage
                    );
                    //applicationSettings.set(); // save the new preferred language
                    web_RequestLocalizedText.Get();
//                    tools.dbg("Sent: " + web_RequestLocalizedText.Url());
                    return true;
                }
            }
            else if (component.equals(checkbox_FR)) {
                if (checkbox_FR.Checked() == true) {
                    checkbox_GA.Checked(false);
                    checkbox_EN.Checked(false);
                    checkbox_PO.Checked(false);
                    checkbox_ES.Checked(false);
                    applicationSettings.string_PreferredLanguage="fr";
                    applicationSettings.set();
                    web_RequestLocalizedText.Url(
                            applicationSettings.localisationBaseUrl +
                                    "?s=" +
                                    applicationSettings.default_sessionID +
                                    "&app=túber"+
                                    "&f=json" +
                                    "&a=gettext" +
                                    "&l=" +
                                    applicationSettings.string_PreferredLanguage
                    );
                    //applicationSettings.set(); // save the new preferred language
                    web_RequestLocalizedText.Get();
//                    tools.dbg("Sent: " + web_RequestLocalizedText.Url());
                    return true;
                }
            }
            else if (component.equals(checkbox_ES)) {
                if (checkbox_ES.Checked() == true) {
                    checkbox_GA.Checked(false);
                    checkbox_EN.Checked(false);
                    checkbox_FR.Checked(false);
                    checkbox_PO.Checked(false);
                    applicationSettings.string_PreferredLanguage="es";
                    applicationSettings.set();
                    web_RequestLocalizedText.Url(
                            applicationSettings.localisationBaseUrl +
                                    "?s=" +
                                    applicationSettings.default_sessionID +
                                    "&app=túber"+
                                    "&f=json" +
                                    "&a=gettext" +
                                    "&l=" +
                                    applicationSettings.string_PreferredLanguage
                    );
                    //applicationSettings.set(); // save the new preferred language
                    web_RequestLocalizedText.Get();
//                    tools.dbg("Sent: " + web_RequestLocalizedText.Url());
                    return true;
                }
            }

            return true;
        }
        else if (eventName.equals("AfterPicking")) {
            if (component.equals(myImagePicker)) {
                this.BackgroundImage(myImagePicker.Selection());
                backgroundImageTextBox.Text( myImagePicker.Selection() );
                Integer request=0,result=0;
                Intent i;
                i=this.getIntent();
                myImagePicker.resultReturned(request,result,i);
//                myImagePicker.r;
                return true;
            }
            return false;
        }
        else if (eventName.equals("Click")) {
            if (component.equals(button_MainMenu)) {
                finish();
                return true;
            }
            else if (component.equals(button_SubmitCustomisation) ) {
                applicationSettings.backgroundImageName=backgroundImageTextBox.Text();
                if (Integer.valueOf(textbox_ListViewSize.Text()) >= applicationSettings.minimimum_intListViewsize) {
                    applicationSettings.intListViewsize = Integer.valueOf(textbox_ListViewSize.Text());
                }
                applicationSettings.set();
                finish();
                return true;
            }
            else if (component.equals(button_SaveMyDetails) ) {
                // copy from screen elements to data
                thisPersonsDetails.email = textbox_eMail.Text();
                thisPersonsDetails.first = textbox_UserFirstName.Text();
                thisPersonsDetails.family = textbox_UserFamilyName.Text();
                thisPersonsDetails.phone = textbox_PhoneNumber.Text();
                // prepare to pass to back end
                web_SaveMyDetails.Url(applicationSettings.baseURL
                        + "?action=PUT"
                        + "&entity=person"
                        + "&pID=" + applicationSettings.pID
                        + "&sessionID=" + applicationSettings.sessionID
                        + "&first=" + thisPersonsDetails.first
                        + "&family=" + thisPersonsDetails.family
                        + "&email=" + thisPersonsDetails.email
                        + "&phone=" + thisPersonsDetails.phone
                );
                web_SaveMyDetails.Get();
                return true;
            }
            else if (component.equals(button_SubmitPassword) ) {
                // prepare to pass to back end
                web_PasswordSave.Url(applicationSettings.baseURL
                        + "?cmd=CHPWD"
                        + "&pID=" + applicationSettings.pID
                        + "&sessionID=" + applicationSettings.sessionID
                        + "&op=" + textbox_OldPassword.Text()
                        + "&np=" + textbox_NewPassword.Text()
                        + "&cp=" + textbox_ConfirmPassword.Text()
                );
                web_PasswordSave.Get();
                return true;
            }
            else if (component.equals(button_Refresh)) {
                fn_GetMyDetails();
                fn_GetProgramSettings();
            }
        }
        else if (eventName.equals("LostFocus")) {
            if (component.equals(textbox_eMail)) {
                dd_aPerson tempPerson = new dd_aPerson(this);
                tempPerson.email = textbox_eMail.Text();
                if (!tempPerson.validEmail()) {
                    button_SaveMyDetails.Enabled(false);
                }
                return true;
            }
            if (component.equals(textbox_PhoneNumber) ) {
                dd_aPerson tempPerson = new dd_aPerson(this);
                tempPerson.phone = textbox_PhoneNumber.Text();
                if (!tempPerson.validPhone()) {
                    button_SaveMyDetails.Enabled(false);
                }
                return true;
            }
        }
        else if (eventName.equals("GotText") ) {
            if (component.equals(web_GetMyDetails)) {
                tools.dbg((String) params[0]);
                tools.dbg(this.formName + " web_GetMyDetails");
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                detailsGotText(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_SaveMyDetails)) {
                tools.dbg((String) params[0]);
                tools.dbg(this.formName + " web_SaveMyDetails");
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                detailsSaveGotText(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_PasswordSave)) {
                tools.dbg((String) params[0]);
                tools.dbg(this.formName + " web_PasswordSave");
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                passwordSaveGotText(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_RequestLocalizedText)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_LocalizeText(status, textOfResponse);
                return false;
            }
        }
        return false;
    }

    void fn_GotText_LocalizeText (String status, String textOfResponse) {

        tools.dbg("GOT: "+textOfResponse);
        if (status.equals("200" )) try {

            JSONObject parser = new JSONObject(textOfResponse);
            applicationSettings.rawtxt=textOfResponse;
            if (!parser.getString(applicationSettings.string_PreferredLanguage ).equals("")) {
                applicationSettings.messages=applicationSettings.fn_unpack_messages_from_string(applicationSettings.string_PreferredLanguage,applicationSettings.rawtxt,notifier_Messages);
                tools.dbg("LANG: "+applicationSettings.string_PreferredLanguage );
                fn_UI_Text();
                applicationSettings.set();
                this.recreate();
            }
            else {
                notifier_Messages.ShowAlert("Problem 9.409");
            }
        }
        catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowAlert("error 9.414 (json)("+applicationSettings.string_PreferredLanguage+")"+applicationSettings.rawtxt);
        }
        else {
            notifier_Messages.ShowAlert("problem 9.417 (server)");
        }
    }

    void fn_GetMyDetails() {
        web_GetMyDetails.Url(applicationSettings.baseURL
                + "?action=GET"
                + "&entity=person"
                + "&pID=" + applicationSettings.pID
                + "&sessionID=" + applicationSettings.sessionID
        );
        web_GetMyDetails.Get();
    }

    void fn_GetProgramSettings(){
        backgroundImageTextBox.Text(applicationSettings.backgroundImageName);
        textbox_ListViewSize.Text(applicationSettings.intListViewsize.toString());
        slider_FontSize.ThumbPosition(Integer.valueOf(textbox_ListViewSize.Text()));
//        fn_preferredLanguage(applicationSettings.string_PreferredLanguage, checkbox_GA, checkbox_EN, checkbox_PO);
    }

//    void fn_preferredLanguage (String lang, CheckBox... cb){
//
//    }

    public void detailsGotText(String status, String textOfResponse) {
        if (status.equals("200")) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (parser.getString("pID").equals(applicationSettings.pID)) {
                //using matching label_pID to check success
                // do something
                thisPersonsDetails.email = parser.getString("email");
                thisPersonsDetails.first = parser.getString("first");
                thisPersonsDetails.family = parser.getString("family");
                thisPersonsDetails.phone = parser.getString("phone");
                textbox_eMail.Text(thisPersonsDetails.email);
                textbox_PhoneNumber.Text(thisPersonsDetails.phone);
                textbox_UserFirstName.Text(thisPersonsDetails.first);
                textbox_UserFamilyName.Text(thisPersonsDetails.family);

            } else {
                notifier_Messages.ShowMessageDialog("Error getting details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog(textOfResponse + "JSON Exception [pID=" + applicationSettings.pID + "]", "Information", "OK");
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server", "Information", "OK");
        }
    }

    public void detailsSaveGotText(String status, String textOfResponse) {
        if (status.equals("200")) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (parser.getString("result").equals("OK")) {
                finish();
            } else {
                notifier_Messages.ShowMessageDialog("Error saving details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog("JSON Exception", "Information", "OK");
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server", "Information", "OK");
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
                notifier_Messages.ShowMessageDialog("Error changing password", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog("Server says: password not changed", "Information", "OK");
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server", "Information", "OK");
        }
    }


    void label_CommonFormatting(Label... lbl) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i = 0;
        int len = lbl.length;
        while ((i < len) && (lbl[i] != null)) {
            lbl[i].WidthPercent(30);
            lbl[i].BackgroundColor(Component.COLOR_NONE);
            lbl[i].FontBold(false);
            i++;
        }
    }

    void textbox_CommonFormatting(TextBox... tb) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i = 0;
        int len = tb.length;
        while ((i < len) && (tb[i] != null)) {
            tb[i].WidthPercent(75);
            tb[i].BackgroundColor(Component.COLOR_NONE);
            tb[i].FontBold(true);
            i++;
        }
    }

    void password_CommonFormatting(PasswordTextBox... t) {
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

    void button_CommonFormatting(Button... b) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i=0;
        int len = b.length;
//        https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
        while ((i < len) && (b[i] != null)) {
            b[i].WidthPercent(55);
            b[i].BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
            b[i].FontBold(true);
            b[i].TextColor(Component.COLOR_WHITE);
            b[i].Shape(BUTTON_SHAPE_ROUNDED);
            b[i].TextAlignment(Component.ALIGNMENT_CENTER);
            b[i].FontSize(12);
            b[i].Height(40);
            i++;
        }

    }

    void checkbox_CommonFormatting(CheckBox... cb) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i=0;
        int len = cb.length;
//        https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
        while ((i < len) && (cb[i] != null)) {
            cb[i].BackgroundColor(Component.COLOR_NONE);
            cb[i].FontBold(true);
            cb[i].TextColor(Color.parseColor(applicationSettings.string_ButtonColor));
            cb[i].FontSize(12);
            cb[i].Height(40);
            cb[i].WidthPercent(33);
            cb[i].Checked(false);
            cb[i].Enabled(true);
            i++;
        }

    }

}

