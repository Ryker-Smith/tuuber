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
import com.google.appinventor.components.runtime.TinyDB;
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
    private HorizontalArrangement ContactsHZ, OutboundInitiationHZ, OutboundInitiationLabelHZ, OutboundInitiationButtonHZ, InboundInitiationHZ, InboundInitiationLabelHZ, InboundpIDHZ, InboundButtonsHZ, OutboundpIDHZ, OutboundLineHZ, ContactsLabelHZ, ChatsScreenHZ, pIDHZ, OtherpIDHZ, toolbarHz;
    private ListView Contacts, OutboundInitiation, InboundInitiation;
    private String baseURL = "https://fachtnaroe.net/tuuber-2019";
    private Button buttonGoToChatScreen, buttonInitiateNewChat, buttonRemoveChatRequestInbound, buttonRemoveChatRequestOutbound, Refresh, MainMenu;
    private Label ContactsLabel, OutboundInitiationLabel, InboundInitiationLabel, InboundLineID, OutboundLineID, pID, OtherpID, InboundpID, OutboundpID, test;
    private Web Contact1Web, Contact2Web, InboundWeb,InboundWebCreate, InboundWebRemove, OutboundWeb, OutboundWebRemove;
    private List<String> ListofContactWeb1, ListofContactWeb2, ListofInboundWeb, ListofOutboundWeb;
    private Notifier messagesPopUp;
    String Specify=new String("to");
//    int intListViewsize=40;
    TinyDB LocalDB;

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        this.BackgroundImage(applicationSettings.backgroundImageName);
        Conversations = new VerticalArrangement(this);

        toolbarHz = new HorizontalArrangement(Conversations);
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
        test = new Label(Conversations);
        Refresh = new Button(toolbarHz);
        Refresh.Width(40);
        Refresh.Height(40);
        Refresh.FontSize(8);
        Refresh.Image("buttonRefresh.png");


        ContactsLabelHZ = new HorizontalArrangement(Conversations);
        ContactsLabel = new Label(ContactsLabelHZ);
        ContactsLabel.Text("Open Conversations");
        ContactsHZ = new HorizontalArrangement(Conversations);
        Contacts = new ListView(ContactsHZ);
        Contacts.HeightPercent(15);
        Contacts.TextSize(applicationSettings.intListViewsize);
        Contacts.Visible(true);
        OtherpIDHZ = new HorizontalArrangement(Conversations);
        OtherpID = new Label(OtherpIDHZ);
        OtherpID.Visible(true);

        ChatsScreenHZ = new HorizontalArrangement(Conversations);
        buttonGoToChatScreen = new Button(ChatsScreenHZ);
        buttonGoToChatScreen.Text("Chat");
        buttonGoToChatScreen.Enabled(false);

        InboundInitiationLabelHZ = new HorizontalArrangement(Conversations);
        InboundInitiationLabel = new Label(InboundInitiationLabelHZ);
        InboundInitiationLabel.Text("Pending (Inbound)");
        InboundInitiationHZ = new HorizontalArrangement(Conversations);
        InboundInitiation = new ListView(InboundInitiationHZ);
        InboundInitiation.HeightPercent(15);
        InboundInitiation.TextSize(applicationSettings.intListViewsize);
        InboundInitiation.Visible(true);
        InboundpIDHZ = new HorizontalArrangement(Conversations);
        InboundpID = new Label(InboundpIDHZ);
        InboundLineID = new Label(InboundpIDHZ);

        InboundButtonsHZ = new HorizontalArrangement(Conversations);
        buttonInitiateNewChat = new Button(InboundButtonsHZ);
        buttonInitiateNewChat.Text("Accept inbound");
        buttonInitiateNewChat.Enabled(false);
        buttonRemoveChatRequestInbound = new Button(InboundButtonsHZ);
        buttonRemoveChatRequestInbound.Text("Decline inbound");
        buttonRemoveChatRequestInbound.Enabled(false);

        OutboundInitiationLabelHZ = new HorizontalArrangement(Conversations);
        OutboundInitiationLabel = new Label(OutboundInitiationLabelHZ);
        OutboundInitiationLabel.Text("Pending (Outbound)");
        OutboundInitiationHZ = new HorizontalArrangement(Conversations);
        OutboundInitiation = new ListView(OutboundInitiationHZ);
        OutboundInitiation.HeightPercent(15);
        OutboundInitiation.TextSize(applicationSettings.intListViewsize);
        OutboundpIDHZ = new HorizontalArrangement(Conversations);
        OutboundpID = new Label(OutboundpIDHZ);
        OutboundLineHZ = new HorizontalArrangement(Conversations);
        OutboundLineID = new Label (OutboundLineHZ);
        OutboundInitiationButtonHZ = new HorizontalArrangement(Conversations);
        buttonRemoveChatRequestOutbound = new Button(OutboundInitiationButtonHZ);
        buttonRemoveChatRequestOutbound.Text("Decline Outbound");
        buttonRemoveChatRequestOutbound.Enabled(false);



        Contact1Web = new Web(this);
        Contact2Web = new Web(this);
        InboundWeb = new Web(this);
        InboundWebCreate = new Web(this);
        InboundWebRemove = new Web(this);
        OutboundWeb = new Web(this);
        OutboundWebRemove = new Web(this);
        messagesPopUp = new Notifier(Conversations);

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterPicking");
        callBackEnd();
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("Click")) {
            if (component.equals(MainMenu))  {
                finish();
            }
            if (component.equals(buttonGoToChatScreen)) {
                startNewForm("screen08_ChatWith",null);
                return true;
            }
            if (component.equals(buttonInitiateNewChat)) {
                InboundWebCreate.Url(
                        applicationSettings.baseURL +
                                "?action=POST&entity=CHAT&sessionID=" +
                                applicationSettings.sessionID +
                                "&respondent_pID=" +
                                InboundpID.Text() +
                                "&initiator_pID=" +
                                applicationSettings.pID +
                                "&status=open&text=" +
                                ""
                );
                InboundWebCreate.Get();
                InboundWebRemove.Url(
                        applicationSettings.baseURL +
                                "?action=DELETE&entity=CHAT&sessionID=" +
                                applicationSettings.sessionID +
                                "&respondent_pID=" +
                                applicationSettings.pID +
                                "&initiator_pID=" +
                                InboundpID.Text() +
                                "&status=init" +
                                "&line_ID=" +
                                InboundLineID.Text()
                );
                InboundWebRemove.Get();
                return true;
                //startNewForm("screen08_ChatWith",null);
            }
            else if (component.equals(buttonRemoveChatRequestInbound)) {
                InboundWebRemove.Url(
                        applicationSettings.baseURL +
                                "?action=DELETE&entity=CHAT&sessionID=" +
                                applicationSettings.sessionID +
                                "&respondent_pID=" +
                                applicationSettings.pID +
                                "&initiator_pID=" +
                                InboundpID.Text() +
                                "&status=init" +
                                "&line_ID=" +
                                InboundLineID.Text()
                );
                InboundWebRemove.Get();
                return true;
            }
            else if (component.equals(buttonRemoveChatRequestOutbound)) {
                OutboundWebRemove.Url(
                        applicationSettings.baseURL +
                                "?action=DELETE&entity=CHAT&sessionID=" +
                                applicationSettings.sessionID +
                                "&initiator_pID=" +
                                applicationSettings.pID +
                                "&respondent_pID=" +
                                OutboundpID.Text() +
                                "&status=init&line_ID=" +
                                OutboundLineID.Text()
                );
                OutboundWebRemove.Get();
                return true;
            }
            else if (component.equals(Refresh)) {
                callBackEnd();
            }
        }
        else if (eventName.equals("AfterPicking")) {
            if (component.equals(Contacts)) {
                String OtherpIDText = new String();
                OtherpIDText = Contacts.Selection();
                String currentString = OtherpIDText;
                String[] separated = currentString.split(":");
                applicationSettings.otherpIDforChat =separated[0];
                applicationSettings.set();
                OtherpID.Text(separated[0]);
                buttonGoToChatScreen.Enabled(true);
            }
            if (component.equals(InboundInitiation)) {
                String InboundpIDText = new String();
                InboundpIDText = InboundInitiation.Selection();
                String currentString = InboundpIDText;
                String[] seperated = currentString.split(":");
                String newString = InboundpIDText;
                String[] seperated2 = newString.split("=");
                InboundpID.Text(seperated[0]);
                InboundLineID.Text(seperated2[1]);
                buttonInitiateNewChat.Enabled(true);
                buttonRemoveChatRequestInbound.Enabled(true);
            }
            if (component.equals(OutboundInitiation)) {
                String OutboundpIDText = new String();
                OutboundpIDText = OutboundInitiation.Selection();
                String nextString = OutboundpIDText;
                String[] seperated3 = nextString.split(":");
                String anotherString = OutboundpIDText;
                String[] seperated4 = anotherString.split("=");
                OutboundpID.Text(seperated3[0]);
                OutboundLineID.Text(seperated4[1]);
                buttonRemoveChatRequestOutbound.Enabled(true);
            }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(Contact1Web)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getContact1List(status, textOfResponse);
                return true;
            }
            else if (component.equals(Contact2Web)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getContact2List(status, textOfResponse);
                return true;
            }
            else if (component.equals(InboundWeb)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getInboundList(status, textOfResponse);
                return true;
            }
            else if (component.equals(OutboundWeb)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getOutboundList(status, textOfResponse);
                return true;
            }
            else if (component.equals(InboundWebRemove)) {
                callBackEnd();
            }
            else if (component.equals(OutboundWebRemove)) {
                callBackEnd();
            }
        }
        return true;
    }

    void callBackEnd() {
        Contact1Web.Url(
                baseURL +
                        "?action=LIST&entity=chat&sessionID=" +
                        applicationSettings.sessionID +
                        "&respondent_pID=" +
                        applicationSettings.pID +
                        "&status=open"
        );
        Contact1Web.Get();
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
    }

    public void getContact1List (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        dbg(status);
        dbg(textOfResponse);
        if (status.equals("200" )) try {

            ListofContactWeb1 = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("chat" ).equals("" )) {
                JSONArray contacts1Array = parser.getJSONArray("chat" );
                for (int i = 0; i < contacts1Array.length(); i++) {
                    if (contacts1Array.getJSONObject(i).toString().equals("{}")) break;
                    ListofContactWeb1.add(
                            contacts1Array.getJSONObject(i).getString("initiator_pID" ) +
                                    ":: " +
                                    contacts1Array.getJSONObject(i).getString("first" ) +
                                    " " +
                                    contacts1Array.getJSONObject(i).getString("family")

                    );
                }
//                String[] temp= new String[Contacts.Elements().toStringArray().length];
//                temp=Contacts.Elements().toStringArray();
//                for (int i=0; i< temp.length; i++) {
//                    ListofContactWeb1.add(temp[i]);
//                }
                YailList tempData = YailList.makeList(ListofContactWeb1.toArray());
//                ListofContactWeb1.add(Contacts.Elements().toString());
                Contacts.Elements(tempData);


            } else {
                messagesPopUp.ShowMessageDialog("Error getting Contact1 details", "Information", "OK" );
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception (1)", "Information", "OK" );
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server", "Information", "OK" );
        }
        Contact2Web.Url(
                baseURL +
                        "?action=LIST&entity=chat&sessionID=" +
                        applicationSettings.sessionID +
                        "&initiator_pID=" +
                        applicationSettings.pID +
                        "&status=open"
        );
        Contact2Web.Get();

    }


    public void getContact2List (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        dbg(status);
        dbg(textOfResponse);
        if (status.equals("200") ) try {

            ListofContactWeb2 = new ArrayList<String>();
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("chat").equals("")) {

                JSONArray contacts2 = parser.getJSONArray("chat");

                for(int i = 0 ; i < contacts2.length() ; i++){
                    if (contacts2.getJSONObject(i).toString().equals("{}")) break;
                    ListofContactWeb2.add(
                            contacts2.getJSONObject(i).getString("respondent_pID") +
                                    ":: " +
                                    contacts2.getJSONObject(i).getString("first") +
                                    " " +
                                    contacts2.getJSONObject(i).getString("family")

                    );
                }

                String[] temp= new String[Contacts.Elements().toStringArray().length];
                temp=Contacts.Elements().toStringArray();
                for (int i=0; i< temp.length; i++) {
                    ListofContactWeb2.add(temp[i]);
                }
                YailList tempData2 = YailList.makeList(ListofContactWeb1.toArray());
//                ListofContactWeb1.add(Contacts.Elements().toString());
                tempData2=YailList.makeList( ListofContactWeb2 );
                Contacts.Elements(tempData2);

            } else {
                messagesPopUp.ShowMessageDialog("Error getting Contact2 details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception (2)", "Information", "OK");
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }

    }
    public void getInboundList (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        dbg(status);
        dbg("INBOUND: " + textOfResponse);
        if (status.equals("200") ) try {

            ListofInboundWeb = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("chat").equals("")) {

                JSONArray Inbound = parser.getJSONArray("chat");
                for(int i = 0 ; i < Inbound.length() ; i++){
                    if (Inbound.getJSONObject(i).toString().equals("{}")) break;
                    ListofInboundWeb.add(
                            Inbound.getJSONObject(i).getString("initiator_pID") +
                                    "::" +
                                    Inbound.getJSONObject(i).getString("first") +
                                    " " +
                                    Inbound.getJSONObject(i).getString("family") +
                                    "=" +
                                    Inbound.getJSONObject(i).getString("line_ID")

                    );
                }
                YailList tempData3=YailList.makeList( ListofInboundWeb );
                InboundInitiation.Elements(tempData3);

            } else {
                messagesPopUp.ShowMessageDialog("Error getting Inbound details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception (3)", "Information", "OK");
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }

    }
    public void getOutboundList (String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        dbg(status);
        dbg(textOfResponse);
        if (status.equals("200") ) try {

            ListofOutboundWeb = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("chat").equals("")) {

                JSONArray Outbound = parser.getJSONArray("chat");
                for(int i = 0 ; i < Outbound.length() ; i++){
                    if (Outbound.getJSONObject(i).toString().equals("{}")) break;
                    ListofOutboundWeb.add(
                            Outbound.getJSONObject(i).getString("respondent_pID") +
                                    "::" +
                                    Outbound.getJSONObject(i).getString("first") +
                                    " " +
                                    Outbound.getJSONObject(i).getString("family") +
                                    "=" +
                                    Outbound.getJSONObject(i).getString("line_ID")



                    );
                }
                YailList tempData4=YailList.makeList( ListofOutboundWeb );
                OutboundInitiation.Elements(tempData4);

            } else {
                messagesPopUp.ShowMessageDialog("Error getting Outbound details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            messagesPopUp.ShowMessageDialog("JSON Exception (4)", "Information", "OK");
        }
        else {
            messagesPopUp.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }

    }
    void dbg (String debugMsg) {
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }
}