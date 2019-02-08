package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.util.YailList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class screen05_Conversations extends Form implements HandlesEventDispatching {

    private tuuber_Settings applicationSettings;
    private VerticalArrangement Conversations;
    private HorizontalArrangement ContactsHZ, OutboundInitiationHZ, OutboundInitiationLabelHZ, InboundInitiationHZ, InboundInitiationLabelHZ, ContactsLabelHZ, ChatsScreenHZ, pIDHZ;
    private ListView Contacts, OutboundInitiation, InboundInitiation;
    private String baseURL = "https://fachtnaroe.net/tuuber-2019";
    private Button ChatsScreen;
    private Label ContactsLabel, OutboundInitiationLabel, InboundInitiationLabel, pID;
    private Web Contact1Web, Contact2Web, InboundWeb, OutboundWeb;
    private List<String> ListofContactWeb1, ListofContactWeb2, ListofInboundWeb, ListofOutboundWeb;
    private Notifier messagesPopUp;
    String Specify=new String("to");

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        this.BackgroundImage(applicationSettings.backgroundImageName);
        Conversations = new VerticalArrangement(this);
        pIDHZ = new HorizontalArrangement(Conversations);
        pID = new Label(pIDHZ);
        pID.Text(applicationSettings.pID);
        ContactsLabelHZ = new HorizontalArrangement(Conversations);
        ContactsLabel = new Label(ContactsLabelHZ);
        ContactsLabel.Text("Open Conversation");
        ContactsHZ = new HorizontalArrangement(Conversations);
        Contacts = new ListView(ContactsHZ);
        Contacts.HeightPercent(20);

        InboundInitiationLabelHZ = new HorizontalArrangement(Conversations);
        InboundInitiationLabel = new Label(InboundInitiationLabelHZ);
        InboundInitiationLabel.Text("PendingInbound");
        InboundInitiationHZ = new HorizontalArrangement(Conversations);
        InboundInitiation = new ListView(InboundInitiationHZ);
        InboundInitiation.HeightPercent(20);

        OutboundInitiationLabelHZ = new HorizontalArrangement(Conversations);
        OutboundInitiationLabel = new Label(OutboundInitiationLabelHZ);
        OutboundInitiationLabel.Text("PendingOutbound");
        OutboundInitiationHZ = new HorizontalArrangement(Conversations);
        OutboundInitiation = new ListView(OutboundInitiationHZ);
        OutboundInitiation.HeightPercent(20);

        ChatsScreenHZ = new HorizontalArrangement(Conversations);
        ChatsScreen = new Button(ChatsScreenHZ);
        ChatsScreen.Text("Chat");

        Contact1Web = new Web(this);
        Contact2Web = new Web(this);
        InboundWeb = new Web(this);
        OutboundWeb = new Web(this);

        EventDispatcher.registerEventForDelegation(this, "ChatsScreen", "Click");
        EventDispatcher.registerEventForDelegation(this, "Contact1Web", "GotText");

    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(ChatsScreen) && eventName.equals("Click")) {
            switchForm("screen08_ChatWith");
            return true;
        }
        Contact1Web.Url(
                baseURL +
                        "?action=LIST&entity=chat&sessionID=" +
                        applicationSettings.sessionID +
                        "&respondent_pID=" +
                        applicationSettings.pID +
                        "&status=open"
        );
        Contact1Web.Get();
        Contact2Web.Url(
                baseURL +
                        "?action=LIST&entity=chat&sessionID=" +
                        applicationSettings.sessionID +
                        "&initiator_pID=" +
                        applicationSettings.pID +
                        "&status=open"
        );
        Contact2Web.Get();
        InboundWeb.Url(
                baseURL +
                        "?action=LIST&entity=chat&sessionID=" +
                        applicationSettings.sessionID +
                        "&respondent_pID=" +
                        applicationSettings.pID +
                        "&status=init"
        );
        InboundWeb.Get();
        OutboundWeb.Url(
                baseURL +
                        "?action=LIST&entity=chat&sessionID=" +
                        applicationSettings.sessionID +
                        "&initiator_pID=" +
                        applicationSettings.pID +
                        "&status=init"
        );
        OutboundWeb.Get();

        if (component.equals(Contact1Web) && eventName.equals("GotText")) {
            dbg((String) params[0]);
            String status = params[1].toString();
            String textOfResponse = (String) params[3];
            getContact1List(status, textOfResponse);
            return true;
        }
        else if (component.equals(Contact2Web) && eventName.equals("GotText")) {
            dbg((String) params[0]);
            String status = params[1].toString();
            String textOfResponse = (String) params[3];
            getContact2List(status, textOfResponse);
            return true;
        }
        else if (component.equals(InboundWeb) && eventName.equals("GotText")) {
            dbg((String) params[0]);
            String status = params[1].toString();
            String textOfResponse = (String) params[3];
            getInboundList(status, textOfResponse);
            return true;
        }
        else if (component.equals(OutboundWeb) && eventName.equals("GotText")) {
            dbg((String) params[0]);
            String status = params[1].toString();
            String textOfResponse = (String) params[3];
            getOutboundList(status, textOfResponse);
            return true;
        }
        return true;
    }
    public void getContact1List (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        if (status.equals("200" )) try {

            ListofContactWeb1 = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("towns" ).equals("" )) {

                JSONArray contacts1Array = parser.getJSONArray("Contacts1" );
                for (int i = 0; i < contacts1Array.length(); i++) {
                    ListofContactWeb1.add(
                            contacts1Array.getJSONObject(i).getString("name" )
                    );
                }
                YailList tempData = YailList.makeList(ListofContactWeb1);
                Contacts.Elements(tempData);

            } else {
                messagesPopUp.ShowMessageDialog("Error getting Contact1 details", "Information", "OK" );
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK" );
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server", "Information", "OK" );
        }
    }
    public void getContact2List (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        if (status.equals("200") ) try {

            ListofContactWeb2 = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("Contacts2").equals("")) {

                JSONArray contacts2 = parser.getJSONArray("Contacts2");
                for(int i = 0 ; i < contacts2.length() ; i++){
                    ListofContactWeb2.add(
                            contacts2.getJSONObject(i).getString("names")
                    );
                }
                YailList tempData2=YailList.makeList( ListofContactWeb2 );
                Contacts.Elements(tempData2);

            } else {
                messagesPopUp.ShowMessageDialog("Error getting Contact2 details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK");
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }

    }
    public void getInboundList (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        if (status.equals("200") ) try {

            ListofInboundWeb = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("Inbound").equals("")) {

                JSONArray Inbound = parser.getJSONArray("Inbound");
                for(int i = 0 ; i < Inbound.length() ; i++){
                    ListofInboundWeb.add(
                            Inbound.getJSONObject(i).getString("names")
                    );
                }
                YailList tempData3=YailList.makeList( ListofInboundWeb );
                InboundInitiation.Elements(tempData3);

            } else {
                messagesPopUp.ShowMessageDialog("Error getting Inbound details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception", "Information", "OK");
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }

    }
    public void getOutboundList (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        if (status.equals("200") ) try {

            ListofOutboundWeb = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("Outbound").equals("")) {

                JSONArray Outbound = parser.getJSONArray("Outbound");
                for(int i = 0 ; i < Outbound.length() ; i++){
                    ListofOutboundWeb.add(
                            Outbound.getJSONObject(i).getString("names")
                    );
                }
                YailList tempData4=YailList.makeList( ListofOutboundWeb );
                OutboundInitiation.Elements(tempData4);

            } else {
                messagesPopUp.ShowMessageDialog("Error getting Outbound details", "Information", "OK");
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
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }
}