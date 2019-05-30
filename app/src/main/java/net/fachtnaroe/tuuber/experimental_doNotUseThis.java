package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import android.webkit.JavascriptInterface;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Clock;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.WebViewer;
import com.google.appinventor.components.runtime.util.YailList;
//import com.google.appinventor.components.runtime.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class experimental_doNotUseThis extends Form implements HandlesEventDispatching {

    // MUST READ:
    //  https://developer.android.com/reference/android/webkit/WebView#addJavascriptInterface(java.lang.Object,%20java.lang.String)
    //https://codevog.com/blog/2015-03-09-webview-interactions-with-javascript
    //https://developer.android.com/guide/webapps/webview#java

    tuuber_Settings applicationSettings;
    tuuberCommonSubroutines tools;

    HorizontalArrangement toolbarHz;
    Map<String, String> hm = new HashMap<String, String>();
    Clock ticker;
    ListView myList;
    Web testFancyList_Web;
    Label debug_FancyList, debug, label_pID;
    fachtnaWebViewer aiWebViewer;
//    String stringTestURL_1="https://fachtnaroe.net/test_list1.html";
    Integer count=0;
    Button button_MainMenu, button_Refresh, button_TestRegister;
    VerticalArrangement screenArrangement;

    Notifier messagesPopUp;

    @JavascriptInterface
    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            dbg(e.toString());
        }
        tools=new tuuberCommonSubroutines(this);
        screenArrangement = new VerticalArrangement(this);
        screenArrangement.WidthPercent(100);
        screenArrangement.HeightPercent(100);
        // The 'toolbar'
        toolbarHz = new HorizontalArrangement(screenArrangement);
        button_MainMenu = new Button(toolbarHz);
        button_MainMenu.Width(40);
        button_MainMenu.Height(40);
        button_MainMenu.Image(applicationSettings.ourLogo);
        label_pID = tools.fn_HeadingLabel(toolbarHz, label_pID, applicationSettings.pID,"Experimental Screen");
        button_Refresh = new Button(toolbarHz);
        button_Refresh.Width(40);
        button_Refresh.Height(40);
        button_Refresh.FontSize(8);
        button_Refresh.Image("buttonRefresh.png");

        debug_FancyList=new Label(screenArrangement);
        debug_FancyList.Text("Debug");
        testFancyList_Web = new Web(screenArrangement);
        testFancyList_Web.Url("https://fachtnaroe.net/tuuber-test?action=LIST&entity=chat&sessionID=a1b2c3d4&initiator_pID=15&respondent_pID=22&displaymode=fancy1");

        aiWebViewer = new fachtnaWebViewer(screenArrangement);
        aiWebViewer.HomeUrl(
                applicationSettings.baseURL +
                "?cmd=debug&sessionID=" +
                applicationSettings.sessionID
        );
        aiWebViewer.GoHome();
        aiWebViewer.HeightPercent(40);
        aiWebViewer.WebViewString("This is another sample.");

        ticker = new Clock(screenArrangement);
        ticker.TimerEnabled(false);
        ticker.TimerInterval(1000);
        button_TestRegister= new Button(screenArrangement);
        button_TestRegister.Text("Show the Register screen");
        messagesPopUp = new Notifier(screenArrangement);
        fancyListView(screenArrangement, myList, "pick one to see 2-question dialog", "one", "two", "three");
        debug=new Label(screenArrangement);
        debug.Text("Debug");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterPicking");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterChoosing");
        EventDispatcher.registerEventForDelegation(this, formName, "fachtnaWebViewStringChange");
        EventDispatcher.registerEventForDelegation(this, formName, "Timer");

        hm.put("es","Spain");
        hm.put("it","Italy");
        hm.put("po","Poland");
        hm.put("fr","France");
        debug_FancyList.Text(aiWebViewer.HomeUrl());

    }

    @JavascriptInterface
    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        String w;
        dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);

            if (eventName.contains("fachtnaWebViewStringChange")) {
                messagesPopUp.ShowAlert("You selected: " + hm.get(aiWebViewer.WebViewString()));
                debug_FancyList.Text(
                        aiWebViewer.WebViewString()
                );
                return true;
        }
        else if (eventName.equals("BackPressed")) {
            finish();
            return true;
        }
        else if (eventName.equals("AfterChoosing")) {
            if (component.equals(messagesPopUp)) {
                messagesPopUp.ShowMessageDialog("You chose: " + params[0], "Information", "Grand");
                debug.Text();
                return true;
            }
        }
        else if (eventName.equals("AfterPicking")) {
            messagesPopUp.ShowChooseDialog("Choose YES to fire Death-Star weapon, or NO self-destruct Death-Star","Selection made","YES","NO",false);
            return true;
        }
        else if (eventName.equals("Click")) {
            if (component.equals(button_MainMenu)) {
                finish();
                return true;
            }
            else if (component.equals(button_Refresh)) {
                aiWebViewer.GoToUrl( aiWebViewer.HomeUrl() );
                return true;
            }
            else if (component.equals(button_TestRegister)) {
                switchForm("screen06_Register");
                return true;
            }
        }
        return false;
    }

    void fancyListView(ComponentContainer container, ListView list, String... listData) {
        list = new ListView(container);
        list.HeightPercent(10);
        list.TextSize(applicationSettings.intListViewsize);
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
    void dbg(String debugMsg) { System.err.print("~~~> " + debugMsg + " <~~~\n"); }
}

