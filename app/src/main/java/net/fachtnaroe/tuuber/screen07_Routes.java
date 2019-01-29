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
import com.google.appinventor.components.runtime.util.YailList;

//import com.google.appinventor.components.runtime.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//import gnu.lists.FString;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/

// I deleted most of the previous items,

public class screen07_Routes extends Form implements HandlesEventDispatching {


    settingsOnline settings = new settingsOnline();
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
    private List<String> ListOfRoutesFromWeb;

    protected void $define() {

        Routes = new VerticalArrangement(this);
        localDB = new TinyDB(Routes);
        String tempString= (String) localDB.GetValue("pID",-1);
        pID = Integer.valueOf(tempString);
        settings.pID=localDB.GetValue("pID",-1).toString();

        Routes.Image("img_splashcanvas.png");
        MainMenu =  new Button(Routes);
        MainMenu.Text("MainMenu");

        myRoutes = new Label(Routes);
        myRoutes.Text("My Routes");

        routesDisplay = new ListView(Routes);
        routesDisplay.WidthPercent(100);
        routesDisplay.HeightPercent(40);
        routesDisplay.TextColor(Component.COLOR_WHITE);

        saveRouteWeb = new Web(Routes);
        messagesPopUp = new Notifier(Routes);
        getRouteWeb = new Web(Routes);

        EventDispatcher.registerEventForDelegation( this, "MainMenu", "Click");
        EventDispatcher.registerEventForDelegation(this, "getRouteWeb", "GotText");

        getRouteWeb.Url(settings.baseURL
                + "?action=LIST"
                + "&entity=ROUTE"
                + "&pID=" + settings.pID
                + "&sessionID=" + settings.sessionID
        );
        getRouteWeb.Get();
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(MainMenu) && eventName.equals("Click")) {
            switchForm("screen03_MainMenu");
            return true;
        }
        else if (component.equals(getRouteWeb) && eventName.equals("GotText")) {
            dbg((String) params[0]);
            String status = params[1].toString();
            String textOfResponse = (String) params[3];
            getRouteWebGotText(status, textOfResponse);
            return true;
        }

        return true;
    }

    public void getRouteWebGotText(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        if (status.equals("200") ) try {

            ListOfRoutesFromWeb = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("routes").equals("")) {
                JSONArray routesArray = parser.getJSONArray("routes");
                for(int i = 0 ; i < routesArray.length() ; i++){
                    ListOfRoutesFromWeb.add(
                            "from "
                            + routesArray.getJSONObject(i).getString("origin")
                            + " to "
                            + routesArray.getJSONObject(i).getString("destination")
                    );
                }
                YailList tempData=YailList.makeList( ListOfRoutesFromWeb);
                routesDisplay.Elements(tempData);
            } else {
                messagesPopUp.ShowMessageDialog("Error getting details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK");
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
    }

    void dbg (String debugMsg) {
        System.err.print( debugMsg + "\n");
    }

}
