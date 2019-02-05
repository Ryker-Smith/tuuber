package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.appinventor.components.runtime.ActivityStarter;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.PasswordTextBox;
import com.google.appinventor.components.runtime.TableArrangement;
import com.google.appinventor.components.runtime.TableLayout;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.WebViewer;
import com.google.appinventor.components.runtime.util.YailList;
//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class experimental_doNotUseThis extends Form implements HandlesEventDispatching {

    //https://codevog.com/blog/2015-03-09-webview-interactions-with-javascript
    //https://developer.android.com/guide/webapps/webview#java
    tuuber_Settings applicationSettings;
    ListView myList;
    Web testFancyList_Web;
    Label debug_FancyList;
    WebViewer testFancyList_Web_Viewer;
    Button MainMenu;
    VerticalArrangement screenArrangement;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);

        this.BackgroundImage(applicationSettings.backgroundImageName);
        VerticalArrangement screenArrangement = new VerticalArrangement(this);
        screenArrangement.WidthPercent(100);
        screenArrangement.HeightPercent(100);
        MainMenu = new Button(screenArrangement);
        MainMenu.Text("MainMenu");

        debug_FancyList=new Label(screenArrangement);
        debug_FancyList.Text("Debug");
        testFancyList_Web = new Web(screenArrangement);
        testFancyList_Web.Url("https://fachtnaroe.net/tuuber-test?action=LIST&entity=chat&sessionID=a1b2c3d4&initiator_pID=15&respondent_pID=22&displaymode=fancy1");

        testFancyList_Web_Viewer= new WebViewer(screenArrangement);
        testFancyList_Web_Viewer.GoToUrl("https://fachtnaroe.net/test_list1.html");
        testFancyList_Web_Viewer.HeightPercent(25);
// MUST READ:
        //  https://developer.android.com/reference/android/webkit/WebView#addJavascriptInterface(java.lang.Object,%20java.lang.String)

//        WebView webView = testFancyList_Web_Viewer;
//        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        fancyListView(screenArrangement, myList, "one", "two", "three");

        EventDispatcher.registerEventForDelegation(this, "ClickEvent", "Click");
        EventDispatcher.registerEventForDelegation(this, "WebEvent", "GotText");
        EventDispatcher.registerEventForDelegation(this, "WebEvent", "WebViewStringChange");

    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);

        if (eventName.equals("WebViewStringChange")) {
            debug_FancyList.Text(testFancyList_Web_Viewer.WebViewString());
            return true;
        }
        if (eventName.equals("BackPressed")) {
            finish();
            return true;
        }
        if (eventName.equals("Click")) {
            if (component.equals(MainMenu)) {
                finish();
                return true;
            }
        }
        return true;
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

    void dbg(String debugMsg) {
        System.err.print("~~~> " + debugMsg + " <~~~\n");
    }
}

 class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}