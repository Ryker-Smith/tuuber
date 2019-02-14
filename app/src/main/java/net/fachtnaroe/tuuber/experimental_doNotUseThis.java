package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import android.content.res.Resources;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.appinventor.components.runtime.AndroidViewComponent;
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
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.WebViewActivity;
import com.google.appinventor.components.runtime.WebViewer;
import com.google.appinventor.components.runtime.util.YailList;
//import com.google.appinventor.components.runtime.util;

import java.util.ArrayList;

public class experimental_doNotUseThis extends Form implements HandlesEventDispatching {

    //https://codevog.com/blog/2015-03-09-webview-interactions-with-javascript
    //https://developer.android.com/guide/webapps/webview#java
    tuuber_Settings applicationSettings;
    HorizontalArrangement toolbarHz;
    Clock ticker;
    ListView myList;
    Web testFancyList_Web;
    Label debug_FancyList, head1, head2, label_pID;
    frWebViewer aiWebViewer;
//    WebViewActivity aiWebViewActivity;
//    frWebViewer.WebViewInterface wv;
    String stringTestURL_1="https://fachtnaroe.net/test_list1.html";
    Integer count=0;
    //=====
//    WebView androidWebView;
    //====
    Button buttonMainMenu, buttonRefresh;
    VerticalArrangement screenArrangement;

    @JavascriptInterface
    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        this.BackgroundImage(applicationSettings.backgroundImageName);

        VerticalArrangement screenArrangement = new VerticalArrangement(this);
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

        aiWebViewer = new frWebViewer(screenArrangement);
        aiWebViewer.GoToUrl(stringTestURL_1);
        aiWebViewer.HeightPercent(40);
        aiWebViewer.WebViewString("This is another sample.");

        ticker = new Clock(screenArrangement);
        ticker.TimerEnabled(true);
        ticker.TimerInterval(1000);
        head1 = new Label(screenArrangement);
        head1.Text("Above");
        dbg("In");
//        frWebViewer.WebViewInterface a= new frWebViewer(this.aiWebViewer.WebViewInterface());
        dbg("Out");
        head2 = new Label(screenArrangement);
        head2.Text("Below");

        fancyListView(screenArrangement, myList, "one", "two", "three");

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterPicking");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterChoosing");
        EventDispatcher.registerEventForDelegation(this, formName, "WebViewStringChange");
        EventDispatcher.registerEventForDelegation(this, formName, "WebViewString");
        EventDispatcher.registerEventForDelegation(this, formName, "getWebViewString");
        EventDispatcher.registerEventForDelegation(this, formName, "setWebViewString");
        EventDispatcher.registerEventForDelegation(this, formName, "WebViewActivity");
        EventDispatcher.registerEventForDelegation(this, formName, "Create");
        EventDispatcher.registerEventForDelegation(this, formName, "Timer");
        EventDispatcher.registerEventForDelegation(this, formName, "Finish");
        EventDispatcher.registerEventForDelegation(this, formName, "WebViewStringChanged");
    }

    @JavascriptInterface
    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);
//        debug_FancyList.Text(aiWebViewer.WebViewInterface().getWebViewString());
//        debug_FancyList.Text(frWebViewer.WebViewInterface.getWebViewString());
//aiWebViewer.WebViewString()
//        aiWebViewer.WebViewInterface.getWebViewString()=new frWebViewer(this).new WebViewInterface();
        if (eventName.equalsIgnoreCase("Timer")) {
            count++;
//            aiWebViewer.WebViewString(count.toString());
            head1.Text( aiWebViewer.WebViewString());
            dbg(aiWebViewer.WebViewString());
            tickety();
            return true;
        }
        else if (eventName.contains("web") || eventName.contains("Web")) { //.equals("WebViewStringChange")
            dbg("SUCCESS SUCCESS SUCCESS SUCCESS SUCCESS \nSUCCESS SUCCESS SUCCESS SUCCESS ");
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
                aiWebViewer.GoToUrl(stringTestURL_1);
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
    @JavascriptInterface
    void tickety(){
        debug_FancyList.Text(aiWebViewer.WebViewString());
    }

    void dbg(String debugMsg) { System.err.print("~~~> " + debugMsg + " <~~~\n"); }
}



//        aiWebViewActivity = new WebViewActivity(screenArrangement);
//        aiWebViewActivity. GoToUrl("https://fachtnaroe.net/test_list1.html");
//        aiWebViewActivity.HeightPercent(25);

//        androidWebView = new Component() {
//            @Override
//            public HandlesEventDispatching getDispatchDelegate() {
//                return null;
//            }
//        };
//        androidWebView=new WebView();
//        WebView(this);
//        this.$form().$add((AndroidViewComponent) androidWebView);
//        androidWebView= (WebView)findViewById(androidWebView.$context().getComponentName());
//        androidWebView = (frWebViewer) aiWebViewer.getView().getId();
//        //findViewById(experimental_doNotUseThis);
//        Resources temp=getResources();
//        dbg(temp.toString());

//      androidWebView.getId();

//        androidWebView.loadUrl("https://fachtnaroe.net/test_list1.html");
//        androidWebView.setBackgroundColor(COLOR_RED);
//        androidWebView.setMinimumHeight(100);

// MUST READ:
//  https://developer.android.com/reference/android/webkit/WebView#addJavascriptInterface(java.lang.Object,%20java.lang.String)

//        WebView webView = (WebView)aiWebViewer;
//        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
//        $context().getResources().getIdentifier("*","drawable",$context().getPackageName());

