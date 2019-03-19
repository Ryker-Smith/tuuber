package net.fachtnaroe.tuuber;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;

import android.content.Intent;
import android.graphics.Color;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.util.YailList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class screen04_Matches extends Form implements HandlesEventDispatching {

    private tuuber_Settings applicationSettings;
    tuuberCommonSubroutines tools;
    private Web web_MyRoutes, Routes, web_MatchesFound, web_InitiateChat;
    private Notifier messagesPopUp;
    private Button button_InitiateChat, MainMenu, Refresh, button_FindMatches;
    private VerticalArrangement Matches;
    private HorizontalArrangement MatchesButtons, MenuButtons, hz_Arrangement3;
    private ListView list_MyRoutes, list_MatchesFound;
    private Label User_ID,seperation;
    private List<String> ListOfRoutesFromWeb, ListOfMatchesFromWeb;

    private Integer int_SelectedRoute = -1;

    protected void $define() {
        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        tools=new tuuberCommonSubroutines(this);
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            tools.dbg(e.toString());
        }

        Matches = new VerticalArrangement(this);
        Matches.HeightPercent(100);
        Matches.WidthPercent(100);
        MenuButtons = new HorizontalArrangement(Matches);
        MainMenu = new Button(MenuButtons);
        MainMenu.Height(40);
        MainMenu.Width(40);
        MainMenu.Image(applicationSettings.ourLogo);

        User_ID =tools.fn_HeadingLabel(MenuButtons, User_ID, applicationSettings.pID,"Matches");

        Refresh = new Button(MenuButtons);
        Refresh.Image("buttonRefresh.png");

        list_MyRoutes = new ListView(Matches);
        list_MyRoutes.HeightPercent(35);
        list_MyRoutes.TextSize(applicationSettings.intListViewsize);
        list_MyRoutes.SelectionColor(Component.COLOR_DKGRAY);
        list_MyRoutes.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));

        Label pad=(Label)tools.fn_pad(Matches,5,1);

        MatchesButtons = new HorizontalArrangement(Matches);
        button_FindMatches = new Button(MatchesButtons);
        button_FindMatches.Text("Click to see Matches");
        button_FindMatches.Enabled(false);
        Label padalso=(Label)tools.fn_pad(Matches,5,1);

        list_MatchesFound = new ListView(Matches);
        list_MatchesFound.HeightPercent(35);
        list_MatchesFound.TextSize(applicationSettings.intListViewsize);
        list_MatchesFound.SelectionColor(COLOR_DKGRAY);
        list_MatchesFound.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));

        hz_Arrangement3 = new HorizontalArrangement(Matches);
        Label flogginpadalso=(Label)tools.fn_pad(hz_Arrangement3,5,5);
        button_InitiateChat = new Button(hz_Arrangement3);
        button_InitiateChat.Text("Initiate Chat");
        button_InitiateChat.Enabled(false);

        messagesPopUp = new Notifier(Matches);
        Routes = new Web(Matches);
        web_InitiateChat = new Web(Matches);
        web_MyRoutes = new Web(Matches);
        web_MatchesFound = new Web(Matches);
//        web_MatchesFound = new Web(Matches);
        tools.button_CommonFormatting(50, button_FindMatches,button_InitiateChat );

        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterPicking");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        Routes.Url(
                applicationSettings.baseURL
                        + "?entity=town&action=LIST"
                        + "&sessionID="
                        + applicationSettings.sessionID
        );
        Routes.Get();
        getRoutesFromBackEnd();
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        tools.dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);
        if (eventName.equals("BackPressed")) {
            closeForm(new Intent());

        } else if (eventName.equals("Click")) {
            if (component.equals(MainMenu)) {
                switchForm("screen03_MainMenu");
                return true;
            } else if (component.equals(Refresh)) {
                button_FindMatches.Enabled(false);
                button_InitiateChat.Enabled(false);
                YailList temp = new YailList();
                list_MatchesFound.Elements(temp);
                getRoutesFromBackEnd();
                return true;
            } else if (component.equals(button_InitiateChat)) {
                String selection = list_MatchesFound.Selection();
                String[] separated = selection.split("[(]"); // using [] to escape ( without )
                separated[1] = separated[1].replace(")", "");
                String[] bitsOfData = separated[1].split(";");
                Integer temp_pID = Integer.valueOf(bitsOfData[0].split("=")[1]);
                Integer temp_rID = Integer.valueOf(bitsOfData[1].split("=")[1]);
                Integer int_First = -1, int_Second = -1;
                if (Integer.valueOf(applicationSettings.pID) < temp_pID) {
                    int_First = Integer.valueOf(applicationSettings.pID);
                    int_Second = temp_pID;
                } else {
                    int_First = temp_pID;
                    int_Second = Integer.valueOf(applicationSettings.pID);
                }
                web_InitiateChat.Url(
                        applicationSettings.baseURL
                                + "?action=POST"
                                + "&entity=link"
                                + "&sessionID="
                                + applicationSettings.sessionID
                                + "&first="
                                + int_First
                                + "&second="
                                + int_Second
                                + "&status=init"
                                + "&rID="
                                + temp_rID
                                + "&openedBy="
                                + applicationSettings.pID
                );
                web_InitiateChat.Get();
                tools.dbg(web_InitiateChat.Url());
                return true;
            } else if (component.equals(button_FindMatches)) {
                if (component.equals(button_FindMatches)) {
                    web_MatchesFound.Url(
                            applicationSettings.baseURL
                                    + "?action=GET"
                                    + "&entity=Match"
                                    + "&sessionID="
                                    + applicationSettings.sessionID
                                    + "&iam="
                                    + applicationSettings.pID
                                    + "&rID="
                                    + int_SelectedRoute.toString()
                    );
                    tools.dbg(web_MatchesFound.Url());
                    web_MatchesFound.Get();
                    return true;
                }
            }
        } else if (eventName.equals("AfterPicking")) {
            if (component.equals(list_MyRoutes)) {
                String[] temp = list_MyRoutes.Selection().split("=");
                temp[1] = temp[1].replace(")", "");
                int_SelectedRoute = Integer.valueOf(temp[1]);
                tools.dbg(int_SelectedRoute.toString());
                button_FindMatches.Enabled(true);
                return true;
            } else if (component.equals(list_MatchesFound)) {
                button_InitiateChat.Enabled(true);
                return true;
            }
        } else if (eventName.equals("GotText")) {
            tools.dbg("RAW: " + (String) params[3]);
            if (component.equals(web_MyRoutes)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_MyRoutes(status, textOfResponse);
                return true;
            } else if (component.equals(web_MatchesFound)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String TextOfResponse = (String) params[3];
                fn_GotText_MatchesFound(status, TextOfResponse);
                return true;
            } else if (component.equals(web_InitiateChat)) {
                String status = params[1].toString();
                String TextOfResponse = (String) params[3];
                fn_GotText_InitiateChat(status, TextOfResponse);
                return true;
            }
        }
        return false;
    }

    public void fn_GotText_MyRoutes(String status, String textOfResponse) {
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
                    if ((routesArray.getJSONObject(i).getString("driver").equals(("Y")))) {
                        string_driverStatus = "Driving";
                    } else {
                        string_driverStatus = "Going";
                    }
                    ListOfRoutesFromWeb.add(
                            string_driverStatus
                                    + " from "
                                    + routesArray.getJSONObject(i).getString("origin")
                                    + " to "
                                    + routesArray.getJSONObject(i).getString("destination")
                                    + " on "
                                    + temp
                                    + " (rID=" + routesArray.getJSONObject(i).getString("rID") + ")"
                    );
                }
                YailList tempData = YailList.makeList(ListOfRoutesFromWeb);
                list_MyRoutes.Elements(tempData);
            } else {
                messagesPopUp.ShowMessageDialog("Error getting details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception(1)", "Information", "OK");
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server", "Information", "OK");
        }
    }

    public void fn_GotText_MatchesFound(String status, String textOfResponse) {
        if (status.equals("200")) try {
            ListOfMatchesFromWeb = new ArrayList<String>();
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("matches").equals("")) {
                JSONArray matchArray = parser.getJSONArray("matches");
                if (matchArray.length() == 0) {
                    messagesPopUp.ShowMessageDialog("No Matches Available", "Information", "OK");
                    YailList temp = new YailList();
                    list_MatchesFound.Elements(temp);
                } else {
                    // messagesPopUp.ShowMessageDialog("No Matches Available" +  matchArray.length(), "Information", "OK");
                    for (int I = 0; I < matchArray.length(); I++) {
                        ListOfMatchesFromWeb.add(
                                matchArray.getJSONObject(I).getString("realName") +
                                        " (" +
                                        "pID=" + matchArray.getJSONObject(I).getString("pID") +
                                        ";" +
                                        "rID=" + matchArray.getJSONObject(I).getString("rID") +
                                        ")"
                        );
                    }
                    YailList tempData = YailList.makeList(ListOfMatchesFromWeb);
                    list_MatchesFound.Elements(tempData);
                }
            } else {
                messagesPopUp.ShowMessageDialog("Error getting details", "Information", "OK");
            }
        } catch (JSONException a) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception (2)", "Information", "OK");
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server", "Information", "OK");
        }
    }

    public void fn_GotText_InitiateChat(String status, String textOfResponse) {
        String temp = new String();
        if (status.equals("200")) try {
            JSONObject parser = new JSONObject(textOfResponse);
            if (parser.getInt("link_ID") >= 1) {
                messagesPopUp.ShowMessageDialog("Chat has been initiated; now wait for the response", "Information", "OK");
            } else {
                messagesPopUp.ShowMessageDialog("Could not initiate chat", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Data Error" + temp, "Information", "OK");
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server", "Information", "OK");
        }
    }

    boolean binary_same_as(Integer first, Integer second) {
        if ((first & second) == second) {
            return true;
        } else {
            return false;
        }
    }

    void getRoutesFromBackEnd() {
        web_MyRoutes.Url(
                applicationSettings.baseURL
                        + "?action=LIST"
                        + "&entity=ROUTE"
                        + "&pID=" + applicationSettings.pID
                        + "&sessionID=" + applicationSettings.sessionID
        );
        web_MyRoutes.Get();
    }

}

