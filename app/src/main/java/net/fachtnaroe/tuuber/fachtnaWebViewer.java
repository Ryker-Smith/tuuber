package net.fachtnaroe.tuuber;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.EclairUtil;
import com.google.appinventor.components.runtime.util.FroyoUtil;
import com.google.appinventor.components.runtime.util.SdkLevel;

@DesignerComponent(
        version = 6,
        category = ComponentCategory.USERINTERFACE,
        description = "Component for viewing Web pages.  The Home URL can be specified in the Designer or in the Blocks Editor.  The view can be set to follow links when they are tapped, and users can fill in Web forms. Warning: This is not a full browser.  For example, pressing the phone's hardware Back key will exit the app, rather than move back in the browser history.<p />You can use the fachtnaWebViewer.WebViewString property to communicate between your app and Javascript code running in the Webviewer page. In the app, you get and set WebViewString.  In the fachtnaWebViewer, you include Javascript that references the window.AppInventor object, using the methoods </em getWebViewString()</em> and <em>frSetWebViewString(text)</em>.  <p />For example, if the fachtnaWebViewer opens to a page that contains the Javascript command <br /> <em>document.write(\"The answer is\" + window.AppInventor.getWebViewString());</em> <br />and if you set WebView.WebVewString to \"hello\", then the web page will show </br ><em>The answer is hello</em>.  <br />And if the Web page contains Javascript that executes the command <br /><em>window.AppInventor.frSetWebViewString(\"hello from Javascript\")</em>, <br />then the value of the WebViewString property will be <br /><em>hello from Javascript</em>. "
)
@SimpleObject
@UsesPermissions(
        permissionNames = "android.permission.INTERNET"
)
public final class fachtnaWebViewer extends AndroidViewComponent  {
    private final WebView webview;
    private String homeUrl;
    private boolean followLinks = true;
    private boolean prompt = true;
    private boolean ignoreSslErrors = false;
    fachtnaWebViewer.WebViewInterface wvInterface;

    public fachtnaWebViewer(ComponentContainer container) {
        super(container);
        this.webview = new WebView(container.$context());
        this.resetWebViewClient();
        this.webview.getSettings().setJavaScriptEnabled(true);
        this.webview.setFocusable(true);
        this.wvInterface = new fachtnaWebViewer.WebViewInterface(this.webview.getContext());
        this.webview.addJavascriptInterface(this.wvInterface, "AppInventor");
        this.webview.getSettings().setBuiltInZoomControls(true);
        if (SdkLevel.getLevel() >= 5) {
//            EclairUtil.setupWebViewGeoLoc((com.google.appinventor.components.runtime.WebViewer)this, this.webview, container.$context());
        }

        container.$add(this);
        this.webview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case 0:
                    case 1:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                    default:
                        return false;
                }
            }
        });
        this.HomeUrl("");
        this.Width(-2);
        this.Height(-2);
    }

    @SimpleProperty(
            description = "Gets the WebView's String, which is viewable through Javascript in the WebView as the window.AppInventor object",
            category = PropertyCategory.BEHAVIOR
    )
    public String WebViewString() {
        return this.wvInterface.getWebViewString();
    }

    @SimpleProperty(
            category = PropertyCategory.BEHAVIOR
    )
    public void WebViewString(String newString) {
        this.wvInterface.fachtnaSetWebViewString(newString);
    }

    public View getView() {
        return this.webview;
    }

    @SimpleProperty
    public void Width(int width) {
        if (width == -1) {
            width = -2;
        }

        super.Width(width);
    }

    @SimpleProperty
    public void Height(int height) {
        if (height == -1) {
            height = -2;
        }

        super.Height(height);
    }

    @SimpleProperty(
            description = "URL of the page the fachtnaWebViewer should initially open to.  Setting this will load the page.",
            category = PropertyCategory.BEHAVIOR
    )
    public String HomeUrl() {
        return this.homeUrl;
    }

    @DesignerProperty(
            editorType = "string",
            defaultValue = ""
    )
    @SimpleProperty
    public void HomeUrl(String url) {
        this.homeUrl = url;
        this.webview.clearHistory();
        this.webview.loadUrl(this.homeUrl);
    }

    @SimpleProperty(
            description = "URL of the page currently viewed.   This could be different from the Home URL if new pages were visited by following links.",
            category = PropertyCategory.BEHAVIOR
    )
    public String CurrentUrl() {
        return this.webview.getUrl() == null ? "" : this.webview.getUrl();
    }

    @SimpleProperty(
            description = "Title of the page currently viewed",
            category = PropertyCategory.BEHAVIOR
    )
    public String CurrentPageTitle() {
        return this.webview.getTitle() == null ? "" : this.webview.getTitle();
    }

    @SimpleProperty(
            description = "Determines whether to follow links when they are tapped in the fachtnaWebViewer.  If you follow links, you can use GoBack and GoForward to navigate the browser history. ",
            category = PropertyCategory.BEHAVIOR
    )
    public boolean FollowLinks() {
        return this.followLinks;
    }

    @DesignerProperty(
            editorType = "boolean",
            defaultValue = "True"
    )
    @SimpleProperty
    public void FollowLinks(boolean follow) {
        this.followLinks = follow;
        this.resetWebViewClient();
    }

    @SimpleProperty(
            description = "Determine whether or not to ignore SSL errors. Set to true to ignore errors. Use this to accept self signed certificates from websites.",
            category = PropertyCategory.BEHAVIOR
    )
    public boolean IgnoreSslErrors() {
        return this.ignoreSslErrors;
    }

    @DesignerProperty(
            editorType = "boolean",
            defaultValue = "False"
    )
    @SimpleProperty
    public void IgnoreSslErrors(boolean ignoreSslErrors) {
        this.ignoreSslErrors = ignoreSslErrors;
        this.resetWebViewClient();
    }

    @SimpleFunction(
            description = "Loads the home URL page.  This happens automatically when the home URL is changed."
    )
    public void GoHome() {
        this.webview.loadUrl(this.homeUrl);
    }

    @SimpleFunction(
            description = "Go back to the previous page in the history list.  Does nothing if there is no previous page."
    )
    public void GoBack() {
        if (this.webview.canGoBack()) {
            this.webview.goBack();
        }

    }

    @SimpleFunction(
            description = "Go forward to the next page in the history list.   Does nothing if there is no next page."
    )
    public void GoForward() {
        if (this.webview.canGoForward()) {
            this.webview.goForward();
        }

    }

    @SimpleFunction(
            description = "Returns true if the fachtnaWebViewer can go forward in the history list."
    )
    public boolean CanGoForward() {
        return this.webview.canGoForward();
    }

    @SimpleFunction(
            description = "Returns true if the fachtnaWebViewer can go back in the history list."
    )
    public boolean CanGoBack() {
        return this.webview.canGoBack();
    }

    @SimpleFunction(
            description = "Load the page at the given URL."
    )
    public void GoToUrl(String url) {
        this.webview.loadUrl(url);
    }

    @DesignerProperty(
            editorType = "boolean",
            defaultValue = "False"
    )
    @SimpleProperty(
            userVisible = false,
            description = "Whether or not to give the application permission to use the Javascript geolocation API. This property is available only in the designer."
    )
    public void UsesLocation(boolean uses) {
    }

    @SimpleProperty(
            description = "If True, then prompt the user of the WebView to give permission to access the geolocation API. If False, then assume permission is granted."
    )
    public boolean PromptforPermission() {
        return this.prompt;
    }

    @DesignerProperty(
            editorType = "boolean",
            defaultValue = "True"
    )
    @SimpleProperty(
            userVisible = true
    )
    public void PromptforPermission(boolean prompt) {
        this.prompt = prompt;
    }

    @SimpleFunction(
            description = "Clear stored location permissions."
    )
    public void ClearLocations() {
        if (SdkLevel.getLevel() >= 5) {
            EclairUtil.clearWebViewGeoLoc();
        }

    }

    private void resetWebViewClient() {
        if (SdkLevel.getLevel() >= 8) {
            this.webview.setWebViewClient(FroyoUtil.getWebViewClient(this.ignoreSslErrors, this.followLinks, this.container.$form(), this));
        } else {
            this.webview.setWebViewClient(new fachtnaWebViewer.WebViewerClient());
        }

    }

    @SimpleFunction(
            description = "Clear WebView caches."
    )
    public void ClearCaches() {
        this.webview.clearCache(true);
    }

    public class WebViewInterface {
        Context mContext;
        String webViewString;

        WebViewInterface(Context c) {
            this.mContext = c;
            this.webViewString = " ";
        }

        @JavascriptInterface
        public String getWebViewString() {
            return this.webViewString;
        }

        /* This next line added 20190215 by Fachtna; the omission of this
            was preventing the JavaScript in the web-page
            from communicating with the Java of the host App.
            PROBLEM:    JavascriptInterface not working (@JavascriptInterface omitted)
            SOLUTION:   Add @JavascriptInterface before each function to be accessible
        */
        @JavascriptInterface
        public void fachtnaSetWebViewString(String newString) {
            this.webViewString = newString;
            /* This next line added 20190216 by Fachtna; no event was being raised as documentation
                indicated would be raised; this function-call is part of bringing code inline with
                embedded documentation.
            */
            fachtnaRaiseEvent("fachtnaWebViewStringChange");
        }
    }
    /*
        This fachtnaRaiseEvent routine added 20190215 by Fachtna;
        PROBLEM: no event was being raised as documentation indicated would be raised;
        SOLUTION: raise an event using the provided EventDispatcher
     */
    @SimpleEvent
    public void fachtnaRaiseEvent(String customMessage) {
        EventDispatcher.dispatchEvent(this, customMessage, new Object[]{});
    }
    // End Fachtna-added bits

    private class WebViewerClient extends WebViewClient {
        private WebViewerClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return !fachtnaWebViewer.this.followLinks;
        }
    }
}
