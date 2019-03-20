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
//    private String baseURL = "https://fachtnaroe.net/tuuber-2019";
    //    private ArrayList RoutsList ;
    private HorizontalArrangement Direction, Days, toolbarHz;
    private Label myRoutes;
    private Label label_pID;
//    private Label test;
    private Label routesDescription;
    private Label routesAction;
    private ListPicker listpicker_Towns, Town_Via;
    private VerticalArrangement ListofDDT, RoutesScreen;
    private HorizontalArrangement ButtonHolder;
    private Button button_Save, button_Delete;
    ListPicker button_To, button_From;
    private Button button_MainMenu, buttonRefresh;
    private ListView list_MyRoutes;
    private CheckBox checkbox_Mon, checkbox_Tues, checkbox_Weds, checkbox_Thurs, checkbox_Fri, checkbox_IsDriver;
    private List<String> ListOfRoutesFromWeb, ListOfTownsFromWeb;
    String Specify = new String("to");
    Integer day = -1;// used to store the day selection of the user
    Integer saveNewIs0_saveEditIs1 = 0;
    String driver, destination, origin, action, string_listpicker_Towns_InitialText;
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

        label_pID =tools.fn_HeadingLabel(toolbarHz, label_pID, applicationSettings.pID,"Routes");

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

        ListofDDT = new VerticalArrangement(RoutesScreen);
        ListofDDT.BackgroundColor(Component.COLOR_WHITE);
        Direction = new HorizontalArrangement(ListofDDT);
        Direction.WidthPercent(100);
        Direction.AlignHorizontal(Component.ALIGNMENT_CENTER);

        button_From = new ListPicker(Direction);
        img_DirectionArrow = new ImagePicker(Direction);
        button_To = new ListPicker(Direction);
        button_To.Text("destination");
        button_From.Text("origin");

        Days = new HorizontalArrangement(RoutesScreen);
        checkbox_Mon = new CheckBox(Days);
        checkbox_Tues = new CheckBox(Days);
        checkbox_Weds = new CheckBox(Days);
        checkbox_Thurs = new CheckBox(Days);
        checkbox_Fri = new CheckBox(Days);
        checkbox_IsDriver = new CheckBox(Days);

        checkbox_Mon.Text("M");
        checkbox_Tues.Text("T");
        checkbox_Weds.Text("W");
        checkbox_Thurs.Text("Th");
        checkbox_Fri.Text("F");
        checkbox_IsDriver.Text("Press Yes if Driver");

        listpicker_Towns = new ListPicker(RoutesScreen);
        string_listpicker_Towns_InitialText="Press for list of towns";
        listpicker_Towns.Text(string_listpicker_Towns_InitialText);
        listpicker_Towns.Selection();
        listpicker_Towns.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
        listpicker_Towns.TextColor(Component.COLOR_WHITE);

        Town_Via = new ListPicker(RoutesScreen);
        Town_Via.Text("Via");
        Town_Via.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
        Town_Via.TextColor(Component.COLOR_WHITE);

        routesAction = new Label(RoutesScreen);
        routesAction.Text("Route actions:");
        ButtonHolder = new HorizontalArrangement(RoutesScreen);
        ButtonHolder.AlignHorizontal(Component.ALIGNMENT_CENTER);

        button_Save = new Button(ButtonHolder);
        button_Save.Text("Save");
        button_Delete = new Button(ButtonHolder);
        button_Delete.Text("Delete");

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
        tools.button_CommonFormatting(40,button_Delete,button_Save );
//        tools.button_CommonFormatting(40, button_To, button_From);

        tools.buttonOnOff(button_Delete,false);
        tools.buttonOnOff(button_Save,false);

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterPicking");
        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "Changed");

        fn_GetTownsFromBackEnd();
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
                    tools.buttonOnOff(button_Delete,false);
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

            if (component.equals(list_MyRoutes)) {
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
            } else if (component.equals(listpicker_Towns)) {
                listpicker_Towns.Text(listpicker_Towns.Selection());
                button_To.Text(listpicker_Towns.Selection());
                return true;
            }
            else if (component.equals(Town_Via)){
                Town_Via.Text (Town_Via.Selection());
                return true;
            }

        }

        else if (eventName.equals("Click")) {
            if (component.equals(button_MainMenu)) {
                switchForm("screen03_MainMenu");
                return true;
            }
            else if (component.equals(listpicker_Towns)) {
                fn_GetTownsFromBackEnd();
            }
            else if (component.equals(Town_Via)) {
                fn_GetTownsFromBackEnd();
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
                fn_GetTownsFromBackEnd();
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
            }
            else if (component.equals(button_Save)) {
                tools.dbg("Saving");
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
                if (Specify.equals("to")) {
                    string_DirectionOfTravel = "&destination=Templemore&origin=" + listpicker_Towns.Text();
                }
                else {
                    string_DirectionOfTravel = "&origin=Templemore&destination=" + listpicker_Towns.Text();
                }
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
            else if (component.equals(button_To)) {
                img_DirectionArrow.Image("Arrow_Right_Templemore.png");
                String temp=button_From.Text();
                button_From.Text( button_To.Text() );
                button_To.Text(temp);
            }
            else if (component.equals(button_From)) {
                img_DirectionArrow.Image("Arrow_Left_Templemore.png");
                String temp=button_From.Text();
                button_From.Text( button_To.Text() );
                button_To.Text(temp);
            }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_GetMyRoutes)) {
//                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_GetRoute(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_GetTowns)) {
//                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_GetTowns(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_SaveRoute)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_SaveRoute(status, textOfResponse);
            }
            else if (component.equals(web_DeleteRoute)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_DeleteRoute(status, textOfResponse);
            }
            else if (component.equals(web_GetOneRoute)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getDayFromResponse(status, textOfResponse);
//                getDirectionFromResponse(status, textOfResponse);
                getDriver(status, textOfResponse);
                getTownsFromResponse(status, textOfResponse);
                return true;
            }
        }
        return true;
    }

    public void fn_GetTownsFromBackEnd () {
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
                listpicker_Towns.Elements(tempData);
                Town_Via.Elements(tempData);

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
                + "&label_pID=" + applicationSettings.pID
                + "&sessionID=" + applicationSettings.sessionID
        );
        web_GetMyRoutes.Get();
    }

    boolean binary_same_as(Integer first, Integer second) {
        if ((first & second) == second) {
            return true;
        } else {
            return false;
        }
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
                button_From.Text(origin);
                button_To.Text(destination);
//                setdestination(origin);
//                settown(origin);
            }
            catch (JSONException e) {
                // if an exception occurs, code for it in here
                messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK");
            }
        }
    }

    public void setday(Integer days){
        if (binary_same_as(days, 2)) {
            checkbox_Mon.Checked(true);
        }
        else if (binary_same_as(days, 4)){
            checkbox_Tues.Checked(true);
        }
        else if (binary_same_as(days, 8)){
            checkbox_Weds.Checked(true);
        }
        else if (binary_same_as(days, 16)){
            checkbox_Thurs.Checked(true);
        }
        else if (binary_same_as(days, 32)){
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
//
//    public void setdestination(String destination) {
//        if (destination.equals("Templemore")) {
//            img_DirectionArrow.Image("Arrow_Left_Templemore.png");
//            button_To.Text("Templemore");
//            button_From.Text(origin);
////            listpicker_Towns.Text(origin);
//        }
//        else {
//            img_DirectionArrow.Image("Arrow_Right_Templemore.png");
//            button_To.Text(origin);
//            button_From.Text("Templemore");
//        }
//    }
//
//    public void settown(String origin) {
//        if (destination.equals("Templemore")) {
//            listpicker_Towns.Text(origin);
//        }
//        else{
//            listpicker_Towns.Text(destination);
//        }
//    }

}