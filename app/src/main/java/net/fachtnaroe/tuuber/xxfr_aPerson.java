package net.fachtnaroe.tuuber;

//import android.support.v7.app.AppCompatActivity;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.WebViewer;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;


//import com.google.appinventor.components.runtime.util;


public class xxfr_aPerson extends Form implements HandlesEventDispatching {

    private tuuber_Settings applicationSettings;
    private TextBox chatText;
    private VerticalArrangement ChatWith;
    private HorizontalArrangement SendHZ, PoolHZ, pIDHZ, ChatsViewerHZ, toolbarHz;
    private Button Send, Refresh, Pool, MainMenu;
    private Label pID, OtherpIDLabel, DriverOrNavigatorLabel, PoolID;
    private Notifier Driver_Or_Navigator_ChoiceDialogNotifier, MessageError_Notifier;
    private Web ChatWeb, PoolWebDriver, PoolWebNavigator, PoolNoIDWeb, PoolIDWeb;
    private WebViewer ChatsViewer;


    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        ChatWith = new VerticalArrangement(this);
        ChatWith.WidthPercent(100);
        ChatWith.HeightPercent(100);

        toolbarHz = new HorizontalArrangement(ChatWith);
        MainMenu = new Button(toolbarHz);
        MainMenu.Width(40);
        MainMenu.Height(40);
        MainMenu.Image("buttonHome.png");

        pID = new Label(toolbarHz);
        pID.Text("I am user: #" + applicationSettings.pID);
        pID.Height(40);
        pID.FontSize(20);
        pID.WidthPercent(70);
        pID.TextAlignment(Component.ALIGNMENT_CENTER);

        Refresh = new Button(toolbarHz);
        Refresh.Width(40);
        Refresh.Height(40);
        Refresh.FontSize(8);
        Refresh.Image("buttonRefresh.png");

        this.BackgroundImage(applicationSettings.backgroundImageName);
        ChatsViewerHZ = new HorizontalArrangement(ChatWith);
        ChatsViewerHZ.AlignHorizontal(ALIGNMENT_CENTER);
        ChatsViewerHZ.WidthPercent(100);
        ChatsViewer = new WebViewer(ChatsViewerHZ);
        ChatsViewer.HeightPercent(40);
    }
}