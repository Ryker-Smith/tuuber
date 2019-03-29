package net.fachtnaroe.tuuber;

import android.graphics.Color;

import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.util.Ev3Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class screen01_Splash extends Form implements HandlesEventDispatching {

    tuuberCommonSubroutines t;
    Clock timerNextScreen;
    tuuber_Settings applicationSettings;
    VerticalArrangement SplashScreen;
    Image ourLogo;
    Label spacer;
    Web web_RequestLocalizedText;
    int thisIsA_Bad_Idea_SettingTheLogoSizeLikeThis = 320;
    int another_Bad_Idea_SettingTheTimerThisWay = 3000;
    Notifier notifier_Messages;
    boolean boolean_LocalizedText_OK=false;

    protected void $define(){

        applicationSettings=new tuuber_Settings(this);
        applicationSettings.get();
        t=new tuuberCommonSubroutines(this);
        web_RequestLocalizedText=new Web(this);
        notifier_Messages=new Notifier(this);
        notifier_Messages.BackgroundColor(Color.parseColor(applicationSettings.string_ColorBad));
        web_RequestLocalizedText.Url(
                applicationSettings.localisationBaseUrl +
                "?l=" +
                applicationSettings.string_PreferredLanguage +
                "&s=" +
                applicationSettings.default_sessionID +
                "&a=listall" +
                "&f=json"
        );
        web_RequestLocalizedText.Get();
        t.dbg(web_RequestLocalizedText.Url());
        t=new tuuberCommonSubroutines(this);
        this.BackgroundColor(Component.COLOR_BLACK);
        Integer screenHeight = this.Height();
        SplashScreen = new VerticalArrangement(this);
        SplashScreen.HeightPercent(100);
        SplashScreen.WidthPercent(100);
        SplashScreen.AlignHorizontal(Component.ALIGNMENT_CENTER);
        spacer = new Label(SplashScreen);
        ourLogo = new Image(SplashScreen);
        spacer.WidthPercent(100);
        ourLogo.Picture(applicationSettings.ourLogo);
        ourLogo.ScalePictureToFit(false);
        ourLogo.Height(thisIsA_Bad_Idea_SettingTheLogoSizeLikeThis);
        Integer spacerHeight= ( screenHeight - ourLogo.Height() );
        spacerHeight = screenHeight / 4 - ourLogo.Height() /2;
        spacer.Height( spacerHeight );
        spacer.TextColor(Component.COLOR_WHITE);
        spacer.FontBold(true);
        spacer.FontTypeface(Ev3Constants.FontType.NORMAL_FONT);
        spacer.TextAlignment(Component.ALIGNMENT_CENTER);

        spacer.Text(
                t.fn_téacs_aistriú("loading" + "..." )
        );
        timerNextScreen = new Clock(SplashScreen);
        timerNextScreen.TimerEnabled(false);
        timerNextScreen.TimerInterval(another_Bad_Idea_SettingTheTimerThisWay);
        timerNextScreen.TimerEnabled(true);

        EventDispatcher.registerEventForDelegation(this,formName,"Timer");
        EventDispatcher.registerEventForDelegation(this,formName,"Click");
        EventDispatcher.registerEventForDelegation(this,formName,"GotText");
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        t.dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
           if (eventName.equals("Timer")) {
               if (boolean_LocalizedText_OK) {
                   timerNextScreen.TimerEnabled(false);
                   switchForm("screen02_Login");
                   return true;
               }
               else {
                   t.dbg("Waiting");
               }
            }
            else if (eventName.equals("Click")) {
               startNewForm("screen02_Login",null);
               return true;
           }
           else if (eventName.equals("GotText")) {
            if (component.equals(web_RequestLocalizedText)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_LocalizeText(status, textOfResponse);
                boolean_LocalizedText_OK=true;
            }
            }
            return false; // event not handled
    }

    void fn_GotText_LocalizeText (String status, String textOfResponse) {

        t.dbg(status);
        t.dbg(textOfResponse);
        t.dbg("B "+applicationSettings.string_PreferredLanguage);
        t.dbg("DDD");
        //https://beginnersbook.com/2013/12/hashmap-in-java-with-example/

        if (status.equals("200" )) try {

            JSONObject parser = new JSONObject(textOfResponse);
            applicationSettings.txt=textOfResponse;
            t.dbg(applicationSettings.string_PreferredLanguage);
            t.dbg(parser.getString(applicationSettings.string_PreferredLanguage));
            if (!parser.getString(applicationSettings.string_PreferredLanguage ).equals("")) {
                JSONArray words_Array = parser.getJSONArray(applicationSettings.string_PreferredLanguage);
                for (int i = 0; i < words_Array.length(); i++) {
                    if (words_Array.getJSONObject(i).toString().equals("{}")) break;
                    applicationSettings.messages.put(
                            words_Array.getJSONObject(i).getString("keyC" ),
                            words_Array.getJSONObject(i).getString("value" )
                    );
                    t.dbg(words_Array.getJSONObject(i).getString("keyC" ));
                    t.dbg(words_Array.getJSONObject(i).getString("value" ));
                }
                applicationSettings.json_Words.put(applicationSettings.string_PreferredLanguage,parser.getJSONArray(applicationSettings.string_PreferredLanguage));
//                t.dbg(parser.getJSONArray("ZZ: "+applicationSettings.string_PreferredLanguage).toString());
                applicationSettings.set();
           }
            else {
                notifier_Messages.ShowAlert("Problem 1.137");
            }
        }
        catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowAlert("error 1.142 (json)");
        }
        else {
            notifier_Messages.ShowAlert("problem 1.145 (server)");
        }
    }

}