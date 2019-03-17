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
    public String otherpIDforChat ="";
    public String backgroundImageName="img_splashcanvas.png";
    public String lastLogin="";
    public String TermsAndConditions = "You must use the app only in the manner intended, but even then there is no guarantee or warranty of any kind.\nUse this at your own risk.";
    public String ourLogo="MultiLayerLogo-002.png";
    public String notificationSoundFileName="";
    public Integer intListViewsize=0;
    public boolean IsDebugSession=false;

    public final String default_baseURL="https://fachtnaroe.net/tuuber";
    public final String debug_baseURL="https://fachtnaroe.net/tuuber-test";
    public final String default_sessionID="a1b2c3d4";
    public String default_pID="-1";
    public String default_otherpIDforChat ="-1";
    public String default_backgroundImageName="tuuberBackdrop-07.png";
    public String default_lastLogin="";
    public String default_notificationSoundFileName="";
    public Integer default_intListViewsize=40;
    public Integer minimimum_intListViewsize=5;
    public boolean default_IsDebugSession=false;

    TinyDB localDB;

    public tuuber_Settings(ComponentContainer screenName){
        localDB= new TinyDB(screenName);
        baseURL=default_baseURL;
        sessionID=default_sessionID;
        backgroundImageName=default_backgroundImageName;
        pID=default_pID;
        otherpIDforChat = default_otherpIDforChat;
        lastLogin=default_lastLogin;
        notificationSoundFileName=default_notificationSoundFileName;
        intListViewsize=default_intListViewsize;
        IsDebugSession=default_IsDebugSession;
    }

    public String get () {
        baseURL=(String) localDB.GetValue("baseURL",default_baseURL);
        sessionID=(String) localDB.GetValue("sessionId",default_sessionID);
        backgroundImageName=(String) localDB.GetValue("backgroundImageName", default_backgroundImageName);
        lastLogin=(String) localDB.GetValue("lastLogin", default_lastLogin);
        pID=(String) localDB.GetValue("pID",default_pID);
        otherpIDforChat =(String) localDB.GetValue("otherpIDforChat", otherpIDforChat);
        notificationSoundFileName=(String) localDB.GetValue("notificationSoundFileName",default_notificationSoundFileName);
        intListViewsize=(Integer) localDB.GetValue("intListViewsize",default_intListViewsize);
        IsDebugSession=(boolean) localDB.GetValue("IsDebugSession",default_IsDebugSession);
//        dbg("Backend: " + baseURL);
        if (IsDebugSession) {
            baseURL=debug_baseURL;
        }
        return "OK";
    }

    public String set () {
        localDB.StoreValue("lastLogin", lastLogin);
        localDB.StoreValue("baseURL",baseURL);
        localDB.StoreValue("sessionID",baseURL);
        localDB.StoreValue("backgroundImageName", backgroundImageName);
        localDB.StoreValue("pID", pID);
        localDB.StoreValue("otherpIDforChat", otherpIDforChat);
        localDB.StoreValue("notificationSoundFileName", notificationSoundFileName);
        localDB.StoreValue("intListViewsize", intListViewsize);
        localDB.StoreValue("IsDebugSession", IsDebugSession);
        return "OK";
    }

    void dbg(String debugMsg) { System.err.print("~~~> " + debugMsg + " <~~~\n");  }
}
