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
    HorizontalArrangement toolbarHz;
    Map<String, String> hm = new HashMap<String, String>();
    Clock ticker;
    ListView myList;
    Web testFancyList_Web;
    Label debug_FancyList, head1, head2, label_pID;
    fachtnaWebViewer aiWebViewer;
    String stringTestURL_1="https://fachtnaroe.net/test_list1.html";
    Integer count=0;
    Button buttonMainMenu, buttonRefresh;
    VerticalArrangement screenArrangement;

    Notifier messagesPopUp;

    @JavascriptInterface
    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        this.BackgroundImage(applicationSettings.backgroundImageName);

        screenArrangement = new VerticalArrangement(this);
        screenArrangement.WidthPercent(100);
        screenArrangement.HeightPercent(100);
        // The 'toolbar'
        toolbarHz = new HorizontalArrangement(screenArrangement);
        buttonMainMenu = new Button(toolbarHz);
        buttonMainMenu.Width(40);
        buttonMainMenu.Height(40);
        buttonMainMenu.Image("buttonHome.png");
        label_pID = new Label(toolbarHz);
        label_pID.Text("I am user: #" + applicationSettings.pID);
        label_pID.Height(40);
        label_pID.FontSize(20);
        label_pID.WidthPercent(70);
        label_pID.TextAlignment(Component.ALIGNMENT_CENTER);
        buttonRefresh = new Button(toolbarHz);
        buttonRefresh.Width(40);
        buttonRefresh.Height(40);
        buttonRefresh.FontSize(8);
        buttonRefresh.Image("buttonRefresh.png");

        debug_FancyList=new Label(screenArrangement);
        debug_FancyList.Text("Debug");
        testFancyList_Web = new Web(screenArrangement);
        //testFancyList_Web.Url("https://fachtnaroe.net/tuuber-test?action=LIST&entity=chat&sessionID=a1b2c3d4&initiator_pID=15&respondent_pID=22&displaymode=fancy1");

        aiWebViewer = new fachtnaWebViewer(screenArrangement);
        aiWebViewer.HomeUrl(
                applicationSettings.default_baseURL +
                "?action=LIST&entity=chat&sessionID=" +
                applicationSettings.sessionID +
                "&initiator_pID=" +
                applicationSettings.pID +
                "&respondent_pID=" +
                applicationSettings.OtherpIDforChat +
                "&showHtml=1" +
                "&iam=" + applicationSettings.pID
        );
        aiWebViewer.GoHome();
        aiWebViewer.HeightPercent(40);
        aiWebViewer.WebViewString("This is another sample.");

        ticker = new Clock(screenArrangement);
        ticker.TimerEnabled(false);
        ticker.TimerInterval(1000);
//        head1 = new Label(screenArrangement);
//        head1.Text("Above");
//        dbg("In");
//        dbg("Out");
//        head2 = new Label(screenArrangement);
//        head2.Text("Below");

        messagesPopUp = new Notifier(screenArrangement);
        fancyListView(screenArrangement, myList, "one", "two", "three");

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
        dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);

            if (eventName.contains("WebViewStringChange")) {
                //aiWebViewer.WebViewString()
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
        else if (eventName.equals("Click")) {
            if (component.equals(buttonMainMenu)) {
                finish();
                return true;
            }
            else if (component.equals(buttonRefresh)) {
                aiWebViewer.GoHome();
                return true;
            }
        }
        return false;
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
    void dbg(String debugMsg) { System.err.print("~~~> " + debugMsg + " <~~~\n"); }
}

