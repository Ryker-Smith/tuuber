package net.fachtnaroe.tuuber;

/* Research:

http://www.java2s.com/Tutorial/Java/0320__Network/ReadingAWebResourceOpeningaURLsstream.htm

*/

import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class tuuber_Settings {

    public final String versionCode=Integer.toString(BuildConfig.VERSION_CODE);
    public final String versionName=BuildConfig.VERSION_NAME;
    public final String appName="túber";
    public final String Endpoint="Templemore";
    public final String localisationBaseUrl="https://tuuber.fachtnaroe.net/aist";
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
    public boolean IsDeveloperSession =false;
    public boolean IsAdminSession=false;
    public boolean SavePassword=true;
    public String string_SavedPassword="";
    public String string_PreferredLanguage;
    public String string_ButtonColor="#113508";
    public String string_ColorGood="#30b102";
    public String string_ColorBad="#dd103a";
    public String string_TextColor="#FFFFFF";
    public Integer int_ButtonTextSize=12;
    public Integer minimimum_intListViewsize=5;
    public String rawtxt;

    final String default_baseURL="https://fachtnaroe.net/tuuber";
    final String debug_baseURL="https://tuuber.fachtnaroe.net/tuuber-test";
    final String Feedback_URL="https://tuuber.fachtnaroe.net/feedback";
    final String default_sessionID="a1b2c3d4";
    String default_pID="-1";
    String default_otherpIDforChat ="-1";
    String default_backgroundImageName="tuuberBackdrop-07.png";
    public String default_lastLogin="";
    String default_notificationSoundFileName="";
    Integer default_intListViewsize=40;

    boolean default_IsDeveloperSession =false;
    boolean default_IsAdminSession =false;
    boolean default_SavePassword=true;
    String default_PreferredLanguage="en";
    String default_ButtonColor="#113508";
    Integer default_ButtonTextSize=12;

    HashMap<String,String> messages;

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
        IsDeveloperSession = default_IsDeveloperSession;
        SavePassword=default_SavePassword;
        string_PreferredLanguage=default_PreferredLanguage;
        TermsAndConditions_URL=baseURL + "?cmd=TERMS&lang=";
        messages=new HashMap<String,String>();
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
        IsDeveloperSession=(boolean) localDB.GetValue("IsDeveloperSession", default_IsDeveloperSession);
        IsAdminSession=(boolean) localDB.GetValue("IsAdminSession",default_IsAdminSession);
        SavePassword=(boolean) localDB.GetValue("SavePassword",default_SavePassword);
        string_PreferredLanguage =(String) localDB.GetValue("string_PreferredLanguage", default_PreferredLanguage);
        string_ButtonColor=(String) localDB.GetValue("string_ButtonColor", default_ButtonColor);
        int_ButtonTextSize=(Integer) localDB.GetValue("int_ButtonTextSize", default_ButtonTextSize);
        rawtxt=(String)localDB.GetValue("rawtxt", null);
        if (IsDeveloperSession) {
            baseURL=debug_baseURL;
        }
        if (SavePassword) {
            string_SavedPassword=(String) localDB.GetValue("string_SavedPassword",string_SavedPassword);
        }
        TermsAndConditions_URL=baseURL + "?cmd=TERMS";

        if (rawtxt != null) {
            String raw = (String) localDB.GetValue("rawtxt", null);
            messages=fn_unpack_messages_from_string(string_PreferredLanguage,raw,null);
        }
        else {
           tuuberCommonSubroutines.dbg("NULL/language unpack");
        }
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
        localDB.StoreValue("IsDeveloperSession", IsDeveloperSession);
        localDB.StoreValue("IsAdminSession", IsAdminSession);
        localDB.StoreValue("SavePassword", SavePassword);
        localDB.StoreValue("string_PreferredLanguage", string_PreferredLanguage);
        localDB.StoreValue("string_ButtonColor", string_ButtonColor);
        localDB.StoreValue("int_ButtonTextSize", int_ButtonTextSize);
        localDB.StoreValue("rawtxt", rawtxt);
        if (SavePassword) {
            localDB.StoreValue("string_SavedPassword", string_SavedPassword);
        }
        return "OK";
    }

    HashMap<String, String> fn_unpack_messages_from_string(String language, String s, Notifier n) {
        // Purpose: Given a string known to contain a JSON array of terms, unpack
        // those terms in a more accessible HashMap
        HashMap<String,String> t = new HashMap<String, String>();
        try {
            JSONObject parser = new JSONObject(s);
            JSONArray words_Array = parser.getJSONArray(language);
            for (int i = 0; i < words_Array.length(); i++) {
                if (words_Array.getJSONObject(i).toString().equals("{}")) break;
                t.put(
                        words_Array.getJSONObject(i).getString("keyC"),
                        words_Array.getJSONObject(i).getString("value")
                );
            }
        }
        catch (JSONException e) {
            if (n != null) {
                // fail silently if null, otherwise:
                n.ShowAlert("error 0.176 (json/language unpack)");
            }
        }
        return t;
    }
}

