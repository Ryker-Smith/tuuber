package net.fachtnaroe.tuuber;

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
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
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

    tuuber_Settings applicationSettings;
    private Web saveRouteWeb, getRouteWeb, TownsWeb, DeleteRoute, GetTowns, getRoute;
    private Notifier messagesPopUp;
    private ImagePicker Templemore;
    private String baseURL = "https://fachtnaroe.net/tuuber-2019";
    //    private ArrayList RoutsList ;
    private HorizontalArrangement Direction, Days, toolbarHz;
    private Label myRoutes;
    private Label label_pID;
    private Label test;
    private Label Test2;
    private Label routesDescription;
    private Label routesAction;
    private String Test;
    private ListPicker TownsList, townsDisplay;
    private VerticalArrangement ListofDDT, RoutesScreen;
    private HorizontalArrangement ButtonHolder;
    private Button MainMenu, To, From, Save, Delete;
    private Button buttonMainMenu, buttonRefresh;
    TinyDB localDB;
    //    private CheckBox M, T, W, Th,F;
    private ListView routesDisplay;
    public String days;
    //    private TextBox TownSingle, TownsDecoded, DriverYN;
//    private ListPicker O, DestinationList, OriginList2, DestinationList2;
    private CheckBox mon, tues, weds, thurs, fri, DriverYoN;
    private List<String> ListOfRoutesFromWeb, ListOfTownsFromWeb, getDayFromWeb;
    String Specify = new String("to");
    String rID;
    Integer day = -1; // used to store the day selection of the user
    String driver, destination, origin, town;
    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        RoutesScreen = new VerticalArrangement(this);

        RoutesScreen.Image(applicationSettings.backgroundImageName);
        // The 'toolbar'
        toolbarHz = new HorizontalArrangement(RoutesScreen);
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
        test = new Label(RoutesScreen);
        buttonRefresh = new Button(toolbarHz);
        buttonRefresh.Width(40);
        buttonRefresh.Height(40);
        buttonRefresh.FontSize(8);
        buttonRefresh.Image("buttonRefresh.png");

        myRoutes = new Label(RoutesScreen);
        myRoutes.Text("My Routes:");
        routesDisplay = new ListView(RoutesScreen);
        routesDisplay.HeightPercent(35);
        routesDescription = new Label(RoutesScreen);
        routesDescription.Text("Route description area:");
        saveRouteWeb = new Web(RoutesScreen);
        messagesPopUp = new Notifier(RoutesScreen);
        getRouteWeb = new Web(RoutesScreen);
        TownsWeb = new Web(RoutesScreen);
        ListofDDT = new VerticalArrangement(RoutesScreen);
        ListofDDT.BackgroundColor(Component.COLOR_WHITE);
        Direction = new HorizontalArrangement(ListofDDT);
        Days = new HorizontalArrangement(RoutesScreen);
        mon = new CheckBox(Days);
        tues = new CheckBox(Days);
        weds = new CheckBox(Days);
        thurs = new CheckBox(Days);
        fri = new CheckBox(Days);
        DriverYoN = new CheckBox(Days);
        To = new Button(Direction);
        Templemore = new ImagePicker(Direction);
        From = new Button(Direction);
        saveRouteWeb = new Web(RoutesScreen);
        To.Text("To");
        From.Text("From");
        mon.Text("M");
        tues.Text("T");
        weds.Text("W");
        thurs.Text("Th");
        fri.Text("F");
        DriverYoN.Text("Press Yes if Driver");
        townsDisplay = new ListPicker(RoutesScreen);
        townsDisplay.Text("Press for list of towns");
        townsDisplay.Selection();
//        RoutsList = new ArrayList();
//        YailList tempData=YailList.makeList(RoutsList);
//        townsDisplay.Elements(tempData);
        routesAction = new Label(RoutesScreen);
        routesAction.Text("Route actions:");
        ButtonHolder = new HorizontalArrangement(RoutesScreen);
        Save = new Button(ButtonHolder);
        Save.Text("Save");
        Delete = new Button(ButtonHolder);
        Delete.Text("Delete");
        routesDisplay.TextSize(40);
        //Save = new Button(RoutesScreen);
        //Delete = new Button(RoutesScreen);
        //Save.Text("Save");
        //Delete.Text("Delete");
        Templemore.Image("Arrow_Right_Templemore.png");
        Templemore.Width(50);
        Templemore.Height(50);
        Test2 = new Label(RoutesScreen);
        Test2.BackgroundColor(COLOR_WHITE);


        DeleteRoute = new Web(RoutesScreen);
        GetTowns = new Web(RoutesScreen);
        getRoute = new Web(RoutesScreen);
        Save.Enabled(false);



        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterPicking");
        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "Changed");
        EventDispatcher.registerEventForDelegation(this, formName, "ErrorNotifier");
        TownsWeb.Url(
                baseURL + "?entity=town&action=LIST"
                        + "&"
                        + "sessionID="
                        + applicationSettings.sessionID
        );
        TownsWeb.Get();
        getRoutesFromBackEnd();

    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);
        if (eventName.equals("BackPressed")) {
            this.finish();
            return true;
        } else if (eventName.equals("Changed")) {  // 'radio' buttons
            if (component.equals(mon)) {
                if (mon.Checked() == true) {
                    day = 2;
                    tues.Checked(false);
                    weds.Checked(false);
                    thurs.Checked(false);
                    fri.Checked(false);
                    Save.Enabled(true);
                } else {
                    Save.Enabled(false);
                }
                return true;
            } else if (component.equals(tues)) {
                if (tues.Checked() == true) {
                    day = 4;
                    mon.Checked(false);
                    weds.Checked(false);
                    thurs.Checked(false);
                    fri.Checked(false);
                    Save.Enabled(true);
                } else {
                    Save.Enabled(false);
                }
                return true;
            } else if (component.equals(weds)) {
                if (weds.Checked() == true) {
                    day = 8;
                    tues.Checked(false);
                    mon.Checked(false);
                    thurs.Checked(false);
                    fri.Checked(false);
                    Save.Enabled(true);
                } else {
                    Save.Enabled(false);
                }
                return true;
            } else if (component.equals(thurs)) {
                if (thurs.Checked() == true) {
                    day = 16;
                    tues.Checked(false);
                    weds.Checked(false);
                    mon.Checked(false);
                    fri.Checked(false);
                    Save.Enabled(true);
                } else {
                    Save.Enabled(false);
                }
                return true;
            } else if (component.equals(fri)) {
                if (fri.Checked() == true) {
                    day = 32;
                    tues.Checked(false);
                    weds.Checked(false);
                    thurs.Checked(false);
                    mon.Checked(false);
                    Save.Enabled(true);
                } else {
                    Save.Enabled(false);
                }
                return true;
            }
        } else if (eventName.equals("AfterPicking")) {

            if (component.equals(routesDisplay)) {
                String CheckrID = new String();
                CheckrID = routesDisplay.Selection();
                String currentstring = CheckrID;
                String[] separated = currentstring.split(":");
                getRoute.Url(
                        baseURL
                                + "?action=GET"
                                + "&entity=ROUTE"
                                + "&" + "rID=" + separated[0]
                                + "&sessionID=" + applicationSettings.sessionID
                );

                getRoute.Get();
                return true;
            } else if (component.equals(townsDisplay)) {
                townsDisplay.Text(townsDisplay.Selection());
                return true;
            }

        } else if (eventName.equals("Click")) {
            if (component.equals(buttonMainMenu)) {
                finish();
                return true;
            } else if (component.equals(townsDisplay)) {
                GetTowns.Url(
                        baseURL
                                + "?action=LIST"
                                + "&entity=TOWN"
                                + "&"
                                + "sessionID=" + applicationSettings.sessionID
                );
                dbg(GetTowns.Url());
                GetTowns.Get();
            } else if (component.equals(buttonRefresh)) {
                getRoutesFromBackEnd();
                return true;
            } else if (component.equals(Delete)) {
                String RouteID = townsDisplay.Selection();
                DeleteRoute.Url(
                        baseURL
                                + "?action=DELETE"
                                + "&entity=ROUTE"
                                + "&"
                                + "rID=" + test.Text() + "&"
                                + "sessionID=" + applicationSettings.sessionID
                );
                DeleteRoute.Get();
                getRoutesFromBackEnd();

            } else if (component.equals(Save)) {
                dbg("Saving");
                if ((!mon.Checked()) && (!tues.Checked()) && (!weds.Checked()) && (!thurs.Checked()) && (!fri.Checked()) && (!DriverYoN.Checked())) {
                    return true;
                }
                String temp = new String("");
                temp = temp + "day=" + day;
                temp = temp + "&";
                if (DriverYoN.Checked()) {
                    temp = temp + "driver=Y";
                } else {
                    temp = temp + "driver=N";
                }
                String Directions = new String();
                if (Specify.equals("to")) {
                    Directions = "&destination=Templemore&origin=" + townsDisplay.Selection();
                } else {
                    Directions = "&origin=Templemore&destination=" + townsDisplay.Selection();
                }
                saveRouteWeb.Url(
                        baseURL
                                + "?action=POST"
                                + "&entity=ROUTE"
                                + Directions + "&"
                                + temp + "&"
                                + "pID=" + applicationSettings.pID + "&"
                                + "sessionID=" + applicationSettings.sessionID
                );
                saveRouteWeb.Get();
                dbg(saveRouteWeb.Url());
                return true;
            } else if (component.equals(To)) {
                Templemore.Image("Arrow_Right_Templemore.png");
                Specify = "to";
            } else if (component.equals(From)) {
                Templemore.Image("Arrow_Left_Templemore.png");
                Specify = "from";
            } else if (component.equals(Delete)) {
            }
        } else if (eventName.equals("GotText")) {
            if (component.equals(getRouteWeb)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getRouteWebGotText(status, textOfResponse);
                return true;
            } else if (component.equals(TownsWeb)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getTownList(status, textOfResponse);
                return true;
            } else if (component.equals(saveRouteWeb)) {
                getRouteWeb.Get();
            } else if (component.equals(getRoute)) {
//                dbg((String) params[3]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getDay(status, textOfResponse);
                getDirection(status, textOfResponse);
                getDriver(status, textOfResponse);
                getTown(status, textOfResponse);
                return true;
            }
        }
        return true;
    }

    public void getRouteWebGotText(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        if (status.equals("200")) try {
            ListOfRoutesFromWeb = new ArrayList<String>();
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("routes").equals("")) {
                JSONArray routesArray = parser.getJSONArray("routes");
                for (int i = 0; i < routesArray.length(); i++) {
                    String temp = "";
                    if (binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 2)) {
                        temp = "Monday";
                    } else if (binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 4)) {
                        temp = "Tuesday";
                    } else if (binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 8)) {
                        temp = "Wednesday";
                    } else if (binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 16)) {
                        temp = "Thursday";
                    } else if (binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 32)) {
                        temp = "Friday";
                    }
                    ListOfRoutesFromWeb.add(
                            routesArray.getJSONObject(i).getString("rID")
                                    +
                                    ":: "
                                    +
                                    "From "
                                    + routesArray.getJSONObject(i).getString("origin")
                                    + " to "
                                    + routesArray.getJSONObject(i).getString("destination")
                                    + " on "
                                    + temp

                    );
                }
                YailList tempData = YailList.makeList(ListOfRoutesFromWeb);
                routesDisplay.Elements(tempData);
            } else {
                messagesPopUp.ShowMessageDialog("Error getting details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK");
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server", "Information", "OK");
        }
    }

    public void getTownList(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        if (status.equals("200")) try {
            ListOfTownsFromWeb = new ArrayList<String>();
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("towns").equals("")) {

                JSONArray townsArray = parser.getJSONArray("towns");
                for (int i = 0; i < townsArray.length(); i++) {
                    ListOfTownsFromWeb.add(
                            townsArray.getJSONObject(i).getString("name")
                    );
                }
                YailList tempData = YailList.makeList(ListOfTownsFromWeb);
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

    void getRoutesFromBackEnd() {
        getRouteWeb.Url(applicationSettings.baseURL
                + "?action=LIST"
                + "&entity=ROUTE"
                + "&pID=" + applicationSettings.pID
                + "&sessionID=" + applicationSettings.sessionID
        );
        getRouteWeb.Get();

    }

    boolean binary_same_as(Integer first, Integer second) {
        if ((first & second) == second) {
            return true;
        } else {
            return false;
        }
    }

    void dbg(String debugMsg) {
        System.err.print("~~~> " + debugMsg + " <~~~\n");
    }

    public void getDay(String status, String textOfResponse) {
        if (status.equals("200") ) {
            try {
                JSONObject parser = new JSONObject(textOfResponse);
                day=Integer.valueOf(parser.getString("day"));
                setday(day);
            } catch (JSONException e) {
                // if an exception occurs, code for it in here
                messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK");
            }
        }
    }
    public void getDriver(String status, String textOfResponse) {
        if (status.equals("200") ) {
            try {
                JSONObject parser = new JSONObject(textOfResponse);
                driver=(parser.getString("driver"));
                setdriver(driver);
            } catch (JSONException e) {
                // if an exception occurs, code for it in here
                messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK");
            }
        }
    }
    public void getDirection(String status, String textOfResponse) {
        if (status.equals("200") ) {
            try {
                JSONObject parser = new JSONObject(textOfResponse);
                destination=(parser.getString("destination"));
                setdestination(destination);
                settown(destination);

            } catch (JSONException e) {
                // if an exception occurs, code for it in here
                messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK");
            }
        }
    }
    public void getTown(String status, String textOfResponse) {
        if (status.equals("200") ) {
            try {
                JSONObject parser = new JSONObject(textOfResponse);
                origin=(parser.getString("origin"));
                setdestination(origin);
                settown(origin);
            } catch (JSONException e) {
                // if an exception occurs, code for it in here
                messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK");
            }
        }
    }



    public void setday(Integer days){
        if (binary_same_as(days, 2)) {
            mon.Checked(true);
        }
        else if (binary_same_as(days, 4)){
            tues.Checked(true);
        }
        else if (binary_same_as(days, 8)){
            weds.Checked(true);
        }
        else if (binary_same_as(days, 16)){
            thurs.Checked(true);

        }
        else if (binary_same_as(days, 32)){
            fri.Checked(true);
        }
    }
    public void setdriver(String driver) {
        if (driver.equals("Y")) {
            DriverYoN.Checked(true);
        }
        else {
            DriverYoN.Checked(false);
        }

    }
    public void setdestination(String destination) {
        if (destination.equals("Templemore")) {
            Templemore.Image("Arrow_Left_Templemore.png");
            townsDisplay.Text(origin);
        }
        else {
            Templemore.Image("Arrow_Right_Templemore.png");

        }

    }
    public void settown(String origin) {
        if (origin.equals("Templemore")) {
            townsDisplay.Text(destination);
        }
        else{
            townsDisplay.Text(origin);
        }


    }

}