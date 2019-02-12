package net.fachtnaroe.tuuber;

/* Research:

http://www.java2s.com/Tutorial/Java/0320__Network/ReadingAWebResourceOpeningaURLsstream.htm

*/

import android.os.AsyncTask;

import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.TinyDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class tuuber_Settings {

    public String baseURL = "";
    public String sessionID="";
    public String pID="";
    public String lastValue;
    public String backgroundImageName="img_splashcanvas.png";
    public String lastLogin="";
    public String textColor;
    public String TermsAndConditions = "You must use the app only in the way we intended, but even then there is no guarantee or warranty of any kind.\nUse this at your own risk.";
    public String ourLogo="MultiLayerLogo-002.png";
    public final String default_baseURL="https://fachtnaroe.net/tuuber-2019";
    public final String default_sessionID="a1b2c3d4";
    public String default_pID="-1";
    public String default_backgroundImageName="tuuberBackdrop-06.png";
    public String defaultLastLogin="";

    TinyDB localDB;

    public tuuber_Settings(ComponentContainer screenName){
        localDB= new TinyDB(screenName);
        baseURL=default_baseURL;
        sessionID=default_sessionID;
        backgroundImageName=default_backgroundImageName;
        pID=default_pID;
    }


    public String get () {

        baseURL=(String) localDB.GetValue("baseURL",default_baseURL);
        sessionID=(String) localDB.GetValue("sessionId",default_sessionID);
        backgroundImageName=(String) localDB.GetValue("backgroundImageName", default_backgroundImageName);
        lastLogin=(String) localDB.GetValue("lastLogin", defaultLastLogin);
        pID=(String) localDB.GetValue("pID",default_pID);
        return "OK";
    }

    public String set () {
        localDB.StoreValue("lastLogin", lastLogin);
        localDB.StoreValue("baseURL",baseURL);
        localDB.StoreValue("backgroundImageName", backgroundImageName);
        localDB.StoreValue("pID", pID);
        return "OK";
    }

}
