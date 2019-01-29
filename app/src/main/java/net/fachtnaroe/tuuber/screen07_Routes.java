package net.fachtnaroe.tuuber;

import android.content.SharedPreferences;
import android.renderscript.Sampler;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.CheckBox;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.ImagePicker;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListPicker;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.util.YailList;

//import com.google.appinventor.components.runtime.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
//import gnu.lists.FString;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/

// I deleted most of the previous items,

public class screen07_Routes extends Form implements HandlesEventDispatching {


    settingsOnline settings = new settingsOnline();
    private Web saveRouteWeb, getRouteWeb, TownsWeb;
    private Notifier messagesPopUp;
    private ImagePicker Templemore;
    private String baseURL = "https://fachtnaroe.net/tuuber-2019?";
    private ArrayList RoutsList ;
    private HorizontalArrangement Direction, Days;
    private Label myRoutes;
    private ListPicker TownsList,townsDisplay;
    private VerticalArrangement ListofDDT, RoutesScreen;
    private Button MainMenu, To, From, Save, Delete;
    TinyDB localDB;
    private CheckBox M, T, W, Th,F;
    private ListView routesDisplay;
    private TextBox TownSingle, TownsDecoded, DriverYN;
    private ListPicker OriginList, DestinationList, OriginList2, DestinationList2;
    private CheckBox Mon, Tue, Wed, Thurs, Fri;;
    Integer pID;
    private List<String> ListOfRoutesFromWeb, ListOfTownsFromWeb;

    protected void $define() {

        RoutesScreen = new VerticalArrangement(this);
        localDB = new TinyDB(RoutesScreen);
        String tempString= (String) localDB.GetValue("pID",-1);
        pID = Integer.valueOf(tempString);
        settings.pID=localDB.GetValue("pID",-1).toString();

        RoutesScreen.Image("img_splashcanvas.png");
        MainMenu =  new Button(RoutesScreen);
        MainMenu.Text("MainMenu");


        myRoutes = new Label(RoutesScreen);
        myRoutes.Text("My RoutesScreen");

        routesDisplay = new ListView(RoutesScreen);
        routesDisplay.WidthPercent(100);
        routesDisplay.HeightPercent(40);
        routesDisplay.TextColor(Component.COLOR_WHITE);

        saveRouteWeb = new Web(RoutesScreen);
        messagesPopUp = new Notifier(RoutesScreen);
        getRouteWeb = new Web(RoutesScreen);
        TownsWeb = new Web(RoutesScreen);

        ListofDDT = new VerticalArrangement(RoutesScreen);
        Direction = new HorizontalArrangement(ListofDDT);
        Days = new HorizontalArrangement(RoutesScreen);
        M = new CheckBox(Days);
        T = new CheckBox(Days);
        W = new CheckBox(Days);
        Th = new CheckBox(Days);
        F = new CheckBox(Days);
        To = new Button(Direction);
        Templemore = new ImagePicker(Direction);
        From = new Button(Direction);
        saveRouteWeb = new Web(RoutesScreen);
        To.Text("To");
        From.Text("From");
        M.Text("M");
        T.Text("T");
        W.Text("W");
        Th.Text("Th");
        F.Text("F");
        townsDisplay = new ListPicker(RoutesScreen);
        townsDisplay.Text("Press for list of towns");
//        RoutsList = new ArrayList();

//        YailList tempData=YailList.makeList(RoutsList);
//        townsDisplay.Elements(tempData);
        Save = new Button(RoutesScreen);
        Delete = new Button(RoutesScreen);
        Save.Text("Save");
        Delete.Text("Delete");

        EventDispatcher.registerEventForDelegation(this, "RoutsList", "Click");
        EventDispatcher.registerEventForDelegation( this, "MainMenu", "Click");
        EventDispatcher.registerEventForDelegation(this, "getRouteWeb", "GotText");
        EventDispatcher.registerEventForDelegation(this, "TownsWeb", "GotText");
        EventDispatcher.registerEventForDelegation(this, "townsDisplay", "AfterPicking");
        EventDispatcher.registerEventForDelegation(this, "Save", "Click");
        EventDispatcher.registerEventForDelegation(this, "From", "Click");
        EventDispatcher.registerEventForDelegation(this, "To", "Click");

        TownsWeb.Url(
                baseURL + "&entity=town&action=LIST"
        );
        TownsWeb.Get();
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


        else if (component.equals(TownsWeb) && eventName.equals("GotText")) {
            dbg((String) params[0]);
            String status = params[1].toString();
            String textOfResponse = (String) params[3];
            getTownList(status, textOfResponse);
            return true;
        }

        else if (component.equals(townsDisplay) && eventName.equals("Click")) {

//            TownsWeb.Url(
//                    baseURL + "&entity=town&action=LIST"
//            );
            return true;
        }

        else if (component.equals(townsDisplay)&& eventName.equals("AfterPicking")){
            townsDisplay.Text(townsDisplay.Selection());

        }
        else if (component.equals(Save)&& eventName.equals("Click")){
                saveRouteWeb.Url(

                        baseURL
                                + "?action=POST"
                                + "&entity=ROUTE"
                                + "&orgin=" + townsDisplay.Text()
                );

                saveRouteWeb.Get();
        }
            else if (component.equals(To)&& eventName.equals("Click")){
            



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

    public void getTownList (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        if (status.equals("200") ) try {

            ListOfTownsFromWeb = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("towns").equals("")) {

                JSONArray townsArray = parser.getJSONArray("towns");
                for(int i = 0 ; i < townsArray.length() ; i++){
                    ListOfTownsFromWeb.add(
                            townsArray.getJSONObject(i).getString("name")
                    );
                }
                YailList tempData=YailList.makeList( ListOfTownsFromWeb );
                townsDisplay.Elements(tempData);

            } else {
                messagesPopUp.ShowMessageDialog("Error getting town details", "Information", "OK");
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

