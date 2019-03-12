package net.fachtnaroe.tuuber;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;

import android.content.Intent;

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
    private Web getRouteWeb, Routes, MatchesAvailable;
    private Notifier messagesPopUp;
    private Button SelectMyRoute, MainMenu, DisplayDetails, Refresh , AddToMatches;
    private VerticalArrangement Matches, VerticalArrangment1, VerticalArrangment2;
    private HorizontalArrangement MatchesButtons, MenuButtons, HorizontalArragment3;
    private ListView MyRouteList, MatchesMade;
    private Label User_ID;
    private List<String> ListOfRoutesFromWeb, ListOfMatchesFromWeb;
    private String baseURL = "https://fachtnaroe.net/tuuber";


    protected void $define() {
        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        this.BackgroundImage(applicationSettings.backgroundImageName);

        Matches = new VerticalArrangement(this);
        Matches.HeightPercent(100);
        Matches.WidthPercent(100);
        MenuButtons = new HorizontalArrangement(Matches);
        MainMenu = new Button(MenuButtons);
        MainMenu.Height(40);
        MainMenu.Width(40);
        MainMenu.Image("buttonHome.png");
        User_ID = new Label(MenuButtons);
        User_ID.Text("I am user: #" + applicationSettings.pID);
        User_ID.FontSize(20);
        Routes=new Web(Matches);
        User_ID.Height(40);
        User_ID.WidthPercent(70);
        User_ID.TextAlignment(Component.ALIGNMENT_CENTER);
        Refresh = new Button(MenuButtons);
        Refresh.Image("buttonRefresh.png");
        MyRouteList = new ListView(Matches);
        MyRouteList.HeightPercent(35);
        MatchesButtons = new HorizontalArrangement(Matches);
        AddToMatches =new Button(MatchesButtons);
        AddToMatches.Text("Click to see Matches");
        getRouteWeb = new Web(Matches);
        MatchesAvailable = new Web(Matches);
        MatchesMade = new ListView(Matches);
        MatchesMade.HeightPercent(35);
        HorizontalArragment3 = new HorizontalArrangement(Matches);
        DisplayDetails= new Button(HorizontalArragment3);
        DisplayDetails.Text("Display Details");
        MatchesAvailable = new Web(Matches);
        SelectMyRoute =new Button(HorizontalArragment3);
        SelectMyRoute.Text("Send Chat");
        messagesPopUp = new Notifier(Matches);
        EventDispatcher.registerEventForDelegation(this, formName, "BackPressed");
        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        Routes.Url(
                baseURL + "?entity=town&action=LIST"
                        + "&"
                        + "sessionID="
                        + applicationSettings.sessionID
        );
        Routes.Get();
        getRoutesFromBackEnd();
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName + " " + componentName + " " + eventName);
        if (eventName.equals("BackPressed")) {
//            if (this.BackPressed()) {
                // one of the above should be redundant
                closeForm(new Intent());
//            }
        }
        else if (component.equals(MainMenu) && eventName.equals("Click")) {
                switchForm("screen03_MainMenu");}

       else if (component.equals(Refresh) && eventName.equals("Click")){
            getRoutesFromBackEnd();
            return true;
       }
        else if (component.equals(AddToMatches)&& eventName.equals("Click")) {
           if (component.equals(AddToMatches)) { MatchesAvailable.Url(
                      applicationSettings.baseURL
                              + "?action=GET"
                              + "&entity=Match"
                              +  "&sessionID="
                              + applicationSettings.sessionID
               );
                dbg(MatchesAvailable.Url());
                MatchesAvailable.Get();
                return true;
            }

        }
        else if (eventName.equals("GotText")) {
            if (component.equals(getRouteWeb)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];

                getRouteWebGotText(status, textOfResponse);
                return true;
            }
            else if (eventName.equals("GotText")) {
                if (component.equals(MatchesAvailable)){

                    dbg((String)params[0]);
                    String status = params[1].toString();
                    String TextOfResponse = (String)params[3];
                    MatchesAvailableGotText(status , TextOfResponse);

                }
            }
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
                        String temp="";
                        if (binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 2) ) {
                            temp="Monday";
                        }
                        else if (binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 4) ) {
                            temp="Tuesday";
                        }
                        else if (binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 8) ) {
                            temp="Wednesday";
                        }
                        else if (binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 16) ) {
                            temp="Thursday";
                        }
                        else if (binary_same_as(Integer.valueOf(routesArray.getJSONObject(i).getString("day")), 32) ) {
                            temp="Friday";
                        }
                        ListOfRoutesFromWeb.add(
                                routesArray.getJSONObject(i).getString("rID")
                                        +
                                        ":: "
                                        +
                                        "From "
                                        + routesArray.getJSONObject(i).getString("origin")
                                        + " to "
                                        + routesArray.getJSONObject(i).getString("destination" )
                                        + " on "
                                        + temp

                        );
                    }
                    YailList tempData=YailList.makeList( ListOfRoutesFromWeb);
                    MyRouteList.Elements(tempData);
                } else {
                    messagesPopUp.ShowMessageDialog("Error getting details", "Information", "OK");
                }
            } catch (JSONException e) {
                // if an exception occurs, code for it in here
                messagesPopUp.ShowMessageDialog("JSON Exception(1)", "Information", "OK");
            }
            else {
                messagesPopUp.ShowMessageDialog("Problem connecting with server","Information", "OK");
            }
        }
        public void MatchesAvailableGotText(String status, String TextOfResponse) {
            if (status.equals("200")) try {
                ListOfMatchesFromWeb = new ArrayList<String>();
                JSONObject parser = new JSONObject(TextOfResponse);
                if (!parser.getString("matches").equals("")) {
                    JSONArray matchArray = parser.getJSONArray("matches");
                    for (int I = 0; I < matchArray.length(); I++) {
                        if (binary_same_as(Integer.valueOf(matchArray.getJSONObject(I).getString("day")), 2) )
                        ListOfMatchesFromWeb.add(
                                matchArray.getJSONObject(I).getString("rID")
                                        +
                                        ":: "
                                        +
                                        "From "
                                        + matchArray.getJSONObject(I).getString("day")
                                        + " to "


                        );

                    }
                    YailList tempData=YailList.makeList( ListOfMatchesFromWeb);
                    MatchesMade.Elements(tempData);

                }
                else {
                    messagesPopUp.ShowMessageDialog("Error getting details", "Information", "OK");
                }
            }  catch (JSONException a) {
                // if an exception occurs, code for it in here
                messagesPopUp.ShowMessageDialog("JSON Exception (2)", "Information", "OK");
        }
        else {
                messagesPopUp.ShowMessageDialog("Problem connecting with server","Information", "OK");
            }
        }


    boolean binary_same_as(Integer first, Integer second) {
        if ((first & second) == second) {
            return true;
        }
        else {
            return false;
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

    void dbg (String debugMsg) {
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }
}

