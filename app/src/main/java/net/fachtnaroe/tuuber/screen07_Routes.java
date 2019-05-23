package net.fachtnaroe.tuuber;

import android.graphics.Color;

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
    tuuberCommonSubroutines tools;
    private Web web_SaveRoute, web_GetMyRoutes, web_GetTowns, web_DeleteRoute, web_GetOneRoute;
    private Notifier messagesPopUp;
    private ImagePicker img_DirectionArrow;
    //    private ArrayList RoutsList ;
    private HorizontalArrangement hz_JourneyDetails, hz_Days, toolbarHz;
    private Label myRoutes;
    private Label label_pID;
//    private Label test;
    private Label routesDescription;
    private Label routesAction;
//    ListPicker listpicker_TravelVia;
    private VerticalArrangement  RoutesScreen;
    private HorizontalArrangement hz_SaveDelete_ButtonHolder;
    private Button button_Save, button_Delete;
    ListPicker listpicker_To, listpicker_From;
    private Button button_MainMenu, buttonRefresh;
    private ListView list_MyRoutes;
    private CheckBox checkbox_Mon, checkbox_Tues, checkbox_Weds, checkbox_Thurs, checkbox_Fri, checkbox_IsDriver;
    private List<String> ListOfRoutesFromWeb, ListOfTownsFromWeb;
    Integer day = -1;// used to store the day selection of the user
    Integer saveNewIs0_saveEditIs1 = 0;
    String driver, destination, origin, action;
    Integer int_SelectedRoute;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        tools=new tuuberCommonSubroutines(this);
        RoutesScreen = new VerticalArrangement(this);

        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            tools.dbg(e.toString());
        }
        // The 'toolbar'
        toolbarHz = new HorizontalArrangement(RoutesScreen);
        button_MainMenu = new Button(toolbarHz);
        button_MainMenu.Width(40);
        button_MainMenu.Height(40);
        button_MainMenu.Image(applicationSettings.ourLogo);

        label_pID =tools.fn_HeadingLabel(toolbarHz, label_pID, applicationSettings.pID,(tools.fn_téacs_aistriú("routes")));

        buttonRefresh = new Button(toolbarHz);
        buttonRefresh.Width(40);
        buttonRefresh.Height(40);
        buttonRefresh.FontSize(8);
        buttonRefresh.Image("buttonRefresh.png");

        myRoutes = new Label(RoutesScreen);
        myRoutes.Text("My Routes:");

        list_MyRoutes = new ListView(RoutesScreen);
        list_MyRoutes.HeightPercent(30);
        list_MyRoutes.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));

        routesDescription = new Label(RoutesScreen);
        routesDescription.Text("Route description area:");

//        ListofDDT = new VerticalArrangement(RoutesScreen);
//        ListofDDT.BackgroundColor(Component.COLOR_WHITE);
        hz_JourneyDetails = new HorizontalArrangement(RoutesScreen);
        hz_JourneyDetails.WidthPercent(100);
        hz_JourneyDetails.AlignHorizontal(Component.ALIGNMENT_CENTER);
        hz_JourneyDetails.BackgroundColor(Component.COLOR_WHITE);
        Label paddy5=(Label)tools.padding(hz_JourneyDetails,2,1);

        listpicker_From = new ListPicker(hz_JourneyDetails);
        img_DirectionArrow = new ImagePicker(hz_JourneyDetails);
        listpicker_To = new ListPicker(hz_JourneyDetails);
        listpicker_From.Text("origin");
        listpicker_To.Text("destination");

        hz_Days = new HorizontalArrangement(RoutesScreen);
        checkbox_Mon = new CheckBox(hz_Days);
        checkbox_Tues = new CheckBox(hz_Days);
        checkbox_Weds = new CheckBox(hz_Days);
        checkbox_Thurs = new CheckBox(hz_Days);
        checkbox_Fri = new CheckBox(hz_Days);
        checkbox_IsDriver = new CheckBox(hz_Days);

        checkbox_Mon.Text("M");
        checkbox_Tues.Text("T");
        checkbox_Weds.Text("W");
        checkbox_Thurs.Text("Th");
        checkbox_Fri.Text("F");
        checkbox_IsDriver.Text("Press Yes if Driver");

        listpicker_From.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
        listpicker_From.TextColor(Component.COLOR_WHITE);
        listpicker_From.WidthPercent(40);
        listpicker_From.Height(40);
        listpicker_To.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
        listpicker_To.TextColor(Component.COLOR_WHITE);
        listpicker_To.WidthPercent(40);
        listpicker_To.Height(40);
//        listpicker_TravelVia = new ListPicker(RoutesScreen);
//        listpicker_TravelVia.Text("Via");
//        listpicker_TravelVia.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
//        listpicker_TravelVia.TextColor(Component.COLOR_WHITE);

        routesAction = new Label(RoutesScreen);
        routesAction.Text("Route actions:");
        hz_SaveDelete_ButtonHolder = new HorizontalArrangement(RoutesScreen);
//        hz_SaveDelete_ButtonHolder.AlignHorizontal(Component.ALIGNMENT_CENTER);
        hz_SaveDelete_ButtonHolder.WidthPercent(100);

        Label paddy1=(Label)tools.padding(hz_SaveDelete_ButtonHolder,2,1);
        button_Save = new Button(hz_SaveDelete_ButtonHolder);
        button_Save.Text("Save");
        Label paddy2=(Label)tools.padding(hz_SaveDelete_ButtonHolder,6,1);
        button_Delete = new Button(hz_SaveDelete_ButtonHolder);
        button_Delete.Text("Delete");
        Label paddy3=(Label)tools.padding(hz_SaveDelete_ButtonHolder,1,1);

        list_MyRoutes.TextSize(applicationSettings.intListViewsize);
        list_MyRoutes.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
        list_MyRoutes.TextColor(Component.COLOR_WHITE);
        list_MyRoutes.SelectionColor(COLOR_DKGRAY);

        img_DirectionArrow.Image("Arrow_Right_Templemore.png");
        img_DirectionArrow.Width(50);
        img_DirectionArrow.Height(50);

        web_SaveRoute = new Web(RoutesScreen);
        web_GetMyRoutes = new Web(RoutesScreen);
        web_GetTowns = new Web(RoutesScreen);
        web_DeleteRoute = new Web(RoutesScreen);
        web_GetOneRoute = new Web(RoutesScreen);
        web_SaveRoute = new Web(RoutesScreen);

        messagesPopUp = new Notifier(RoutesScreen);
        tools.button_CommonFormatting(46,button_Delete,button_Save );

        tools.buttonOnOff(button_Delete,false);
        tools.buttonOnOff(button_Save,false);

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterPicking");
        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "Changed");

        fn_GetTowns();
        fn_GetMyRoutesFromBackEnd();
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        tools.dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);
        if (eventName.equals("BackPressed")) {
            this.finish();
            return true;
        } else if (eventName.equals("Changed")) {  // 'radio' buttons
            if (component.equals(checkbox_Mon)) {
                if (checkbox_Mon.Checked() == true) {
                    day = 2;
                    checkbox_Tues.Checked(false);
                    checkbox_Weds.Checked(false);
                    checkbox_Thurs.Checked(false);
                    checkbox_Fri.Checked(false);
                    tools.buttonOnOff(button_Save,true);
                } else {
                    tools.buttonOnOff(button_Save,false);
                }
                return true;
            } else if (component.equals(checkbox_Tues)) {
                if (checkbox_Tues.Checked() == true) {
                    day = 4;
                    checkbox_Mon.Checked(false);
                    checkbox_Weds.Checked(false);
                    checkbox_Thurs.Checked(false);
                    checkbox_Fri.Checked(false);
                    tools.buttonOnOff(button_Save,true);
                } else {
                    tools.buttonOnOff(button_Save,false);
                }
                return true;
            } else if (component.equals(checkbox_Weds)) {
                if (checkbox_Weds.Checked() == true) {
                    day = 8;
                    checkbox_Tues.Checked(false);
                    checkbox_Mon.Checked(false);
                    checkbox_Thurs.Checked(false);
                    checkbox_Fri.Checked(false);
                    tools.buttonOnOff(button_Save,true);
                } else {
                    tools.buttonOnOff(button_Save,false);
                }
                return true;
            } else if (component.equals(checkbox_Thurs)) {
                if (checkbox_Thurs.Checked() == true) {
                    day = 16;
                    checkbox_Tues.Checked(false);
                    checkbox_Weds.Checked(false);
                    checkbox_Mon.Checked(false);
                    checkbox_Fri.Checked(false);
                    tools.buttonOnOff(button_Save,true);
                } else {
                    tools.buttonOnOff(button_Save,false);
                }
                return true;
            } else if (component.equals(checkbox_Fri)) {
                if (checkbox_Fri.Checked() == true) {
                    day = 32;
                    checkbox_Tues.Checked(false);
                    checkbox_Weds.Checked(false);
                    checkbox_Thurs.Checked(false);
                    checkbox_Mon.Checked(false);
                    tools.buttonOnOff(button_Save,true);
                } else {
                    tools.buttonOnOff(button_Save,false);
                }
                return true;
            }
        } else if (eventName.equals("AfterPicking")) {
            if (component.equals(listpicker_To)) {
                img_DirectionArrow.Image("Arrow_Right_Templemore.png");
                listpicker_To.Text( listpicker_To.Selection() );
                if (!listpicker_To.Text().equals(applicationSettings.Endpoint) && (!listpicker_From.Text().equals(applicationSettings.Endpoint))) {
                    listpicker_From.Text(applicationSettings.Endpoint);
                }
            }
            else if (component.equals(listpicker_From)) {
                img_DirectionArrow.Image("Arrow_Left_Templemore.png");
                listpicker_From.Text( listpicker_From.Selection() );
                if (!listpicker_To.Text().equals(applicationSettings.Endpoint) && (!listpicker_From.Text().equals(applicationSettings.Endpoint))) {
                    listpicker_To.Text(applicationSettings.Endpoint);
                }
            }
            else if (component.equals(list_MyRoutes)) {
                String[] temp = list_MyRoutes.Selection().split("=");
                temp[1] = temp[1].replace(")", "");
                int_SelectedRoute = Integer.valueOf(temp[1]);
                web_GetOneRoute.Url(
                        applicationSettings.baseURL
                                + "?action=GET"
                                + "&entity=ROUTE"
                                + "&rID=" + int_SelectedRoute
                                + "&sessionID=" + applicationSettings.sessionID
                );
                saveNewIs0_saveEditIs1 = 1;
                tools.buttonOnOff(button_Delete,true);
                web_GetOneRoute.Get();
                return true;
            }
//            else if (component.equals(listpicker_TravelVia)){
//                listpicker_TravelVia.Text (listpicker_TravelVia.Selection());
//                return true;
//            }

        }

        else if (eventName.equals("Click")) {
            if (component.equals(button_MainMenu)) {
                switchForm("screen03_MainMenu");
                return true;
            }
            else if (component.equals(buttonRefresh)) {
                saveNewIs0_saveEditIs1 =0;
                checkbox_Mon.Checked(false);
                checkbox_Tues.Checked(false);
                checkbox_Weds.Checked(false);
                checkbox_Thurs.Checked(false);
                checkbox_Fri.Checked(false);
                tools.buttonOnOff(button_Save,false);
                tools.buttonOnOff(button_Delete,false);
                checkbox_IsDriver.Checked(false);
                listpicker_To.Text("destination");
                listpicker_From.Text("origin");
                fn_GetTowns();
                fn_GetMyRoutesFromBackEnd();
                return true;
            }
            else if (component.equals(button_Delete)) {
                String[] route=list_MyRoutes.Selection().split("=");
                String my_rID=route[1].replace(")","");
                web_DeleteRoute.Url(
                        applicationSettings.baseURL
                                + "?action=DELETE"
                                + "&entity=ROUTE"
                                + "&rID=" + my_rID
                                + "&sessionID=" + applicationSettings.sessionID
                );
                web_DeleteRoute.Get();
                buttonRefresh.Click();
                saveNewIs0_saveEditIs1=0;
                fn_GetMyRoutesFromBackEnd();
                return true;
            }
            else if (component.equals(button_Save)) {
                if (listpicker_To.Text().equals("origin") || listpicker_To.Text().equals("destination")
                    || listpicker_From.Text().equals("origin") || listpicker_From.Text().equals("destination")
                ) {
                    // if for any reason the town names aren't properly populated, don't try to save them
                    return true;
                }
                String my_rID="";
                if(saveNewIs0_saveEditIs1.equals(0)){
                    action="POST";
                }
                else{
                    action="PUT";
                    String[] route=list_MyRoutes.Selection().split("=");
                    my_rID=route[1].replace(")","");
                }
                if ((!checkbox_Mon.Checked()) && (!checkbox_Tues.Checked()) && (!checkbox_Weds.Checked()) && (!checkbox_Thurs.Checked()) && (!checkbox_Fri.Checked()) && (!checkbox_IsDriver.Checked())) {
                    return true;
                }
                String string_DayAndIfDriver = new String("");
                string_DayAndIfDriver = string_DayAndIfDriver + "day=" + day;
                string_DayAndIfDriver = string_DayAndIfDriver + "&";
                if (checkbox_IsDriver.Checked()) {
                    string_DayAndIfDriver = string_DayAndIfDriver + "driver=Y";
                }
                else {
                    string_DayAndIfDriver = string_DayAndIfDriver + "driver=N";
                }
                String string_DirectionOfTravel = new String();
                string_DirectionOfTravel = "&destination="+ listpicker_To.Text()+"&origin=" + listpicker_From.Text();
                web_SaveRoute.Url(
                        applicationSettings.baseURL
                                + "?action="
                                + action
                                + "&entity=ROUTE"
                                + string_DirectionOfTravel + "&"
                                + string_DayAndIfDriver + "&"
                                + "pID=" + applicationSettings.pID + "&"
                                + "sessionID=" + applicationSettings.sessionID
                                + "&rID=" + my_rID
                );
                web_SaveRoute.Get();
                buttonRefresh.Click();
                tools.dbg(web_SaveRoute.Url());
                return true;
            }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_GetMyRoutes)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_GetRoute(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_GetTowns)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_GetTowns(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_SaveRoute)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_SaveRoute(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_DeleteRoute)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_DeleteRoute(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_GetOneRoute)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getDayFromResponse(status, textOfResponse);
                getDriver(status, textOfResponse);
                getTownsFromResponse(status, textOfResponse);
                return true;
            }
        }
        return false;
    }

    public void fn_GetTowns() {
        web_GetTowns.Url(
                applicationSettings.baseURL + "?entity=town&action=LIST"
                        + "&sessionID=" + applicationSettings.sessionID
        );
        web_GetTowns.Get();
    }

    public void fn_GotText_GetRoute(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        if (status.equals("200")) try {
            ListOfRoutesFromWeb = new ArrayList<String>();
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("routes").equals("")) {
                JSONArray routesArray = parser.getJSONArray("routes");
                for (int i = 0; i < routesArray.length(); i++) {
                    String temp = "";
                    if (tools.binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 2)) {
                        temp = "Monday";
                    } else if (tools.binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 4)) {
                        temp = "Tuesday";
                    } else if (tools.binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 8)) {
                        temp = "Wednesday";
                    } else if (tools.binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 16)) {
                        temp = "Thursday";
                    } else if (tools.binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 32)) {
                        temp = "Friday";
                    }
                    String string_driverStatus;
                    if ( (routesArray.getJSONObject(i).getString("driver").equals(("Y")))) {
                        string_driverStatus="Driving";
                    }
                    else {
                        string_driverStatus="Going";
                    }
                    ListOfRoutesFromWeb.add(
                            string_driverStatus
                                    + " from "
                                    + routesArray.getJSONObject(i).getString("origin")
                                    + " to "
                                    + routesArray.getJSONObject(i).getString("destination" )
                                    + " on "
                                    + temp
                                    + " (rID="  + routesArray.getJSONObject(i).getString("rID") + ")"

                    );
                }
                YailList tempData = YailList.makeList(ListOfRoutesFromWeb);
                list_MyRoutes.Elements(tempData);
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

    public void fn_GotText_GetTowns(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        if (status.equals("200")) try {
            ListOfTownsFromWeb = new ArrayList<String>();
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("towns").equals("")) {

                JSONArray townsArray = parser.getJSONArray("towns");
                for (int i = 0; i < townsArray.length(); i++) {
                    ListOfTownsFromWeb.add(
                            townsArray.getJSONObject(i).getString("name")
//                            + " (" + townsArray.getJSONObject(i).getString("location") + ")"
                    );
                }
                YailList tempData = YailList.makeList(ListOfTownsFromWeb);
                listpicker_To.Elements(tempData);
                listpicker_From.Elements(tempData);
//                listpicker_TravelVia.Elements(tempData);

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

    public void fn_GetMyRoutesFromBackEnd() {
        web_GetMyRoutes.Url(applicationSettings.baseURL
                + "?action=LIST"
                + "&entity=ROUTE"
                + "&pID=" + applicationSettings.pID
                + "&sessionID=" + applicationSettings.sessionID
        );
        web_GetMyRoutes.Get();
    }

    public void fn_GotText_SaveRoute(String status, String textOfResponse) {
        if (status.equals("200") ) {
            try {
                JSONObject parser = new JSONObject(textOfResponse);
                if (parser.getString("result").equals("OK")) {
                    messagesPopUp.ShowAlert("Saved");
                    web_GetMyRoutes.Get();
                }
                else {
                    messagesPopUp.ShowMessageDialog("Error saving changes (" + textOfResponse + ")", "Information", "OK");
                }
            } catch (JSONException e) {
                // if an exception occurs, code for it in here
                messagesPopUp.ShowMessageDialog("JSON Exception (" + textOfResponse + ")", "Information", "OK");
            }
        }
    }

    public void fn_GotText_DeleteRoute(String status, String textOfResponse) {
        if (status.equals("200") ) {
            try {
                JSONObject parser = new JSONObject(textOfResponse);
                if (parser.getString("result").equals("OK")) {
                    messagesPopUp.ShowAlert("Deleted");
                    web_GetMyRoutes.Get();
                }
                else {
                    messagesPopUp.ShowMessageDialog("Error saving changes (" + textOfResponse + ")", "Information", "OK");
                }
            } catch (JSONException e) {
                // if an exception occurs, code for it in here
                messagesPopUp.ShowMessageDialog("JSON Exception (" + textOfResponse + ")", "Information", "OK");
            }
        }
    }

    public void getDayFromResponse(String status, String textOfResponse) {
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

    public void getTownsFromResponse(String status, String textOfResponse) {
        if (status.equals("200") ) {
            try {
                JSONObject parser = new JSONObject(textOfResponse);
                origin=(parser.getString("origin"));
                destination=(parser.getString("destination"));
                listpicker_From.Text(origin);
                listpicker_To.Text(destination);
            }
            catch (JSONException e) {
                // if an exception occurs, code for it in here
                messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK");
            }
        }
    }

    public void setday(Integer days){
        if (tools.binary_same_as(days, 2)) {
            checkbox_Mon.Checked(true);
        }
        else if (tools.binary_same_as(days, 4)){
            checkbox_Tues.Checked(true);
        }
        else if (tools.binary_same_as(days, 8)){
            checkbox_Weds.Checked(true);
        }
        else if (tools.binary_same_as(days, 16)){
            checkbox_Thurs.Checked(true);
        }
        else if (tools.binary_same_as(days, 32)){
            checkbox_Fri.Checked(true);
        }
    }

    public void setdriver(String driver) {
        if (driver.equals("Y")) {
            checkbox_IsDriver.Checked(true);
        }
        else {
            checkbox_IsDriver.Checked(false);
        }
    }

}