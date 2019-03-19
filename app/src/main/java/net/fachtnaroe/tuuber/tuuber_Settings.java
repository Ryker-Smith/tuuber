package net.fachtnaroe.tuuber;

/* Research:

http://www.java2s.com/Tutorial/Java/0320__Network/ReadingAWebResourceOpeningaURLsstream.htm

*/

import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.TinyDB;

public class tuuber_Settings {

    public String baseURL = "";
    public String sessionID="";
    public String pID="";
    public String CurrentLinkId ="";
    public String backgroundImageName="img_splashcanvas.png";
    public String lastLogin="";
    public String TermsAndConditions = "<h2>Use this <u>at your own risk</u></h2><p>You must use this App only in the manner intended.</p><p>Even then there is no guarantee  the App will work as expected, if at all. In fact, no guarantee or warranty of <u>any</u> kind is provided. Neither will any liability be accepted for any result of the use of the App, even if used as intended.</p><p>This is <b>experimental software</b>; use this <u>at your own risk</u>.<p>Please be <i>very</i> cautious about your physical safety, including &mdash; but not limited to &mdash; whether any travel is safe or is required to be undertaken.</p><p>There is no guarantee that any other user of this App is reliable or trustworthy; neither should their use of this App be seen as implying that they are.</p><h2>Use this <u>at your own risk</u></h2>";
    public String TermsAndConditions_URL="";
    public String ourLogo="MultiLayerLogo-002.png";
    public String notificationSoundFileName="";
    public Integer intListViewsize=0;
    public boolean IsDebugSession=false;
    public boolean SavePassword=true;
    public String string_SavedPassword="";
    public String string_PreferredLanguage;
    public String string_ButtonColor="#113508";
    public String string_TextColor="#FFFFFF";
    public Integer int_ButtonTextSize=12;
    public Integer minimimum_intListViewsize=5;

    private final String default_baseURL="https://fachtnaroe.net/tuuber";
    private final String debug_baseURL="https://fachtnaroe.net/tuuber-test";
    private final String default_sessionID="a1b2c3d4";
    private String default_pID="-1";
    private String default_otherpIDforChat ="-1";
    private String default_backgroundImageName="tuuberBackdrop-07.png";
    public String default_lastLogin="";
    private String default_notificationSoundFileName="";
    private Integer default_intListViewsize=40;

    private boolean default_IsDebugSession=false;
    private boolean default_SavePassword=true;
    private String default_PreferredLanguage="ga";
    private String default_ButtonColor="#113508";
    private Integer default_ButtonTextSize=12;

    TinyDB localDB;

    public tuuber_Settings(ComponentContainer screenName){
        localDB= new TinyDB(screenName);
        baseURL=default_baseURL;
        sessionID=default_sessionID;
        backgroundImageName=default_backgroundImageName;
        pID=default_pID;
        CurrentLinkId = default_otherpIDforChat;
        lastLogin=default_lastLogin;
        notificationSoundFileName=default_notificationSoundFileName;
        intListViewsize=default_intListViewsize;
        IsDebugSession=default_IsDebugSession;
        SavePassword=default_SavePassword;
        string_PreferredLanguage=default_PreferredLanguage;
        TermsAndConditions_URL=baseURL + "?cmd=TERMS";
    }

    public String get () {
        baseURL=(String) localDB.GetValue("baseURL",default_baseURL);
        sessionID=(String) localDB.GetValue("sessionId",default_sessionID);
        backgroundImageName=(String) localDB.GetValue("backgroundImageName", default_backgroundImageName);
        lastLogin=(String) localDB.GetValue("lastLogin", default_lastLogin);
        pID=(String) localDB.GetValue("label_pID",default_pID);
        CurrentLinkId =(String) localDB.GetValue("CurrentLinkId", CurrentLinkId);
        notificationSoundFileName=(String) localDB.GetValue("notificationSoundFileName",default_notificationSoundFileName);
        intListViewsize=(Integer) localDB.GetValue("intListViewsize",default_intListViewsize);
        IsDebugSession=(boolean) localDB.GetValue("IsDebugSession",default_IsDebugSession);
        SavePassword=(boolean) localDB.GetValue("SavePassword",default_SavePassword);
        string_PreferredLanguage =(String) localDB.GetValue("string_PreferredLanguage", default_PreferredLanguage);
        string_ButtonColor=(String) localDB.GetValue("string_ButtonColor", default_ButtonColor);
        int_ButtonTextSize=(Integer) localDB.GetValue("int_ButtonTextSize", default_ButtonTextSize);

        if (IsDebugSession) {
            baseURL=debug_baseURL;
        }
        if (SavePassword) {
            string_SavedPassword=(String) localDB.GetValue("string_SavedPassword",string_SavedPassword);
        }
        TermsAndConditions_URL=baseURL + "?cmd=TERMS";
        return "OK";
    }

    public String set () {
        localDB.StoreValue("lastLogin", lastLogin);
        localDB.StoreValue("baseURL",baseURL);
        localDB.StoreValue("sessionID",baseURL);
        localDB.StoreValue("backgroundImageName", backgroundImageName);
        localDB.StoreValue("label_pID", pID);
        localDB.StoreValue("CurrentLinkId", CurrentLinkId);
        localDB.StoreValue("notificationSoundFileName", notificationSoundFileName);
        localDB.StoreValue("intListViewsize", intListViewsize);
        localDB.StoreValue("IsDebugSession", IsDebugSession);
        localDB.StoreValue("SavePassword", SavePassword);
        localDB.StoreValue("string_PreferredLanguage", string_PreferredLanguage);
        localDB.StoreValue("string_ButtonColor", string_ButtonColor);
        localDB.StoreValue("int_ButtonTextSize", int_ButtonTextSize);
        if (SavePassword) {
            localDB.StoreValue("string_SavedPassword", string_SavedPassword);
        }
        return "OK";
    }

}

