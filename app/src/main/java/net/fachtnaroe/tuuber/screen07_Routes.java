package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.ActivityStarter;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.CheckBox;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListPicker;
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

//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;
//import gnu.lists.FString;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/

// I deleted most of the previous items,

public class screen07_Routes extends Form implements HandlesEventDispatching {


    private Web saveRouteWeb, getRouteWeb, deleteRouteWeb;
    private Notifier messagesPopUp;
    private Label myRoutes;
    private HorizontalArrangement MainMenuHArrangement, HorizontalArrangement8, HorizontalArrangement11, HorizontalArrangement7, HorizontalArrangement12, HorizontalArrangement9, HorizontalArrangement13, HorizontalArrangement5, SaveRouteHArrangement;
    private VerticalArrangement RoutesVArrangement, VerticalArrangement4, VerticalArrangement3, VerticalArrangement2, Routes;
    private Button MainMenu, SaveNew;
    TinyDB localDB;
    private ListView routesDisplay;
    private TextBox TownSingle, TownsDecoded, DriverYN;
    private ListPicker OriginList, DestinationList, OriginList2, DestinationList2;
    private CheckBox Mon, Tue, Wed, Thurs, Fri;;
    Integer pID;

    protected void $define() {

        Routes = new VerticalArrangement(this);
        localDB = new TinyDB(Routes);
        String tempString= (String) localDB.GetValue("pID",-1);
        pID = Integer.valueOf(tempString);
        Routes.Image("img_splashcanvas.png");
        MainMenu =  new Button(Routes);
        MainMenu.Text("MainMenu");

        myRoutes = new Label(Routes);
        myRoutes.Text("My Routes");

        routesDisplay = new ListView(Routes);
        routesDisplay.WidthPercent(100);
        routesDisplay.HeightPercent(40);

        saveRouteWeb = new Web(Routes);
        messagesPopUp = new Notifier(Routes);
        getRouteWeb = new Web(Routes);

        EventDispatcher.registerEventForDelegation( this, "MainMenu", "Click");

    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(MainMenu) && eventName.equals("Click")) {
            switchForm("screen03_MainMenu");
            return true;
        }
        return true;
    }
}
