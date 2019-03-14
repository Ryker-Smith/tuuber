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
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
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
    private HorizontalArrangement OutboundInitiationButtonHZ, InboundButtonsHZ, ChatsScreenHZ, toolbarHz;
    private VerticalArrangement vt_Open, vt_In, vt_Out;
    private ListView listview_Open, listview_Out, listview_In;
    private Button button_OpenChatScreen, button_AcceptInbound, button_DeclineInbound, button_CancelOutbound, Refresh, MainMenu;
    private Label label_Open, label_Out, label_In, pID;
    private Web web_Contact1, web_Contact2, web_Inbound, web_AcceptInbound, web_DeclineInbound, web_Outbound, web_OutboundCancel;
    private List<String> ListofContactWeb1, ListofContactWeb2, ListofInboundWeb, ListofOutboundWeb;
    private Notifier notifier_Messages;
    String string_InboundLineID, string_InboundpID, string_OutboundpID, string_OutboundLineID;


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
        Refresh = new Button(toolbarHz);
        Refresh.Width(40);
        Refresh.Height(40);
        Refresh.FontSize(8);
        Refresh.Image("buttonRefresh.png");

        vt_Open = new VerticalArrangement(Conversations);
        vt_Open.HeightPercent(35);
        label_Open = new Label(vt_Open);
        label_Open.Text("Open Conversations");
        listview_Open = new ListView(vt_Open);
        listview_Open.TextSize(applicationSettings.intListViewsize);
        listview_Open.HeightPercent(100);

        ChatsScreenHZ = new HorizontalArrangement(Conversations);
        button_OpenChatScreen = new Button(ChatsScreenHZ);
        button_OpenChatScreen.Text("Chat");
        button_OpenChatScreen.Enabled(false);

        vt_In = new VerticalArrangement(Conversations);
        vt_In.HeightPercent(15);
        label_In = new Label(vt_In);
        label_In.Text("Pending (Inbound)");
        listview_In = new ListView(vt_In);
        listview_In.TextSize(applicationSettings.intListViewsize);
        listview_In.HeightPercent(100);

        InboundButtonsHZ = new HorizontalArrangement(Conversations);
        button_AcceptInbound = new Button(InboundButtonsHZ);
        button_AcceptInbound.Text("Accept inbound");
        button_AcceptInbound.Enabled(false);
        button_DeclineInbound = new Button(InboundButtonsHZ);
        button_DeclineInbound.Text("Decline inbound");
        button_DeclineInbound.Enabled(false);

        vt_Out = new VerticalArrangement(Conversations);
        vt_Out.HeightPercent(15);
        label_Out = new Label(vt_Out);
        label_Out.Text("Pending (Outbound)");
        listview_Out = new ListView(vt_Out);
        listview_Out.TextSize(applicationSettings.intListViewsize);
        listview_Out.HeightPercent(100);

        OutboundInitiationButtonHZ = new HorizontalArrangement(Conversations);
        button_CancelOutbound = new Button(OutboundInitiationButtonHZ);
        button_CancelOutbound.Text("Cancel Outbound");
        button_CancelOutbound.Enabled(false);

        web_Contact1 = new Web(this);
        web_Contact2 = new Web(this);
        web_Inbound = new Web(this);
        web_AcceptInbound = new Web(this);
        web_DeclineInbound = new Web(this);
        web_Outbound = new Web(this);
        web_OutboundCancel = new Web(this);
        notifier_Messages = new Notifier(Conversations);

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
            if (component.equals(button_OpenChatScreen)) {
                startNewForm("screen08_ChatWith",null);
                return true;
            }
            if (component.equals(button_AcceptInbound)) {
                web_AcceptInbound.Url(
                        applicationSettings.baseURL +
                                "?action=POST&entity=CHAT&sessionID=" +
                                applicationSettings.sessionID +
                                "&respondent_pID=" +
                                string_InboundpID +
                                "&initiator_pID=" +
                                applicationSettings.pID +
                                "&status=open&text=" +
                                ""
                );
                web_AcceptInbound.Get();
                web_DeclineInbound.Url(
                        applicationSettings.baseURL +
                                "?action=DELETE&entity=CHAT&sessionID=" +
                                applicationSettings.sessionID +
                                "&respondent_pID=" +
                                applicationSettings.pID +
                                "&initiator_pID=" +
                                string_InboundpID +
                                "&status=init" +
                                "&line_ID=" +
                                string_InboundLineID
                );
                web_DeclineInbound.Get();
                return true;
                //startNewForm("screen08_ChatWith",null);
            }
            else if (component.equals(button_DeclineInbound)) {
                web_DeclineInbound.Url(
                        applicationSettings.baseURL +
                                "?action=DELETE&entity=CHAT&sessionID=" +
                                applicationSettings.sessionID +
                                "&respondent_pID=" +
                                applicationSettings.pID +
                                "&initiator_pID=" +
                                string_InboundpID +
                                "&status=init" +
                                "&line_ID=" +
                                string_InboundLineID
                );
                web_DeclineInbound.Get();
                return true;
            }
            else if (component.equals(button_CancelOutbound)) {
                web_OutboundCancel.Url(
                        applicationSettings.baseURL +
                                "?action=DELETE&entity=CHAT&sessionID=" +
                                applicationSettings.sessionID +
                                "&initiator_pID=" +
                                applicationSettings.pID +
                                "&respondent_pID=" +
                                string_OutboundpID +
                                "&status=init&line_ID=" +
                                string_OutboundLineID
                );
                web_OutboundCancel.Get();
                return true;
            }
            else if (component.equals(Refresh)) {
                callBackEnd();
            }
        }
        else if (eventName.equals("AfterPicking")) {
            if (component.equals(listview_Open)) {
                String currentString = listview_Open.Selection();
                String[] separated = currentString.split(":");
                applicationSettings.otherpIDforChat =separated[0];
                applicationSettings.set();
                button_OpenChatScreen.Enabled(true);
            }
            if (component.equals(listview_In)) {
                String currentString = listview_In.Selection();
                String[] seperated = currentString.split(":");
                String newString = listview_In.Selection();;
                String[] seperated2 = newString.split("=");
                string_InboundpID=seperated[0];
                string_InboundLineID=seperated2[1];
                button_AcceptInbound.Enabled(true);
                button_DeclineInbound.Enabled(true);
            }
            if (component.equals(listview_Out)) {
                String nextString = listview_Out.Selection();
                String[] seperated3 = nextString.split(":");
                String anotherString = listview_Out.Selection();
                String[] seperated4 = anotherString.split("=");
                string_OutboundpID=seperated3[0];
                string_OutboundLineID=seperated4[1];
                button_CancelOutbound.Enabled(true);
            }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_Contact1)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getContact1List(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_Contact2)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getContact2List(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_Inbound)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getInboundList(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_Outbound)) {
                dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                getOutboundList(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_DeclineInbound)) {
                callBackEnd();
            }
            else if (component.equals(web_OutboundCancel)) {
                callBackEnd();
            }
        }
        return true;
    }

    void callBackEnd() {
        web_Contact1.Url(
                applicationSettings.baseURL +
                        "?action=LIST&entity=chat&sessionID=" +
                        applicationSettings.sessionID +
                        "&respondent_pID=" +
                        applicationSettings.pID +
                        "&status=open"
        );
        web_Contact1.Get();
        web_Inbound.Url(
                applicationSettings.baseURL +
                        "?action=LIST&entity=chat&sessionID=" +
                        applicationSettings.sessionID +
                        "&respondent_pID=" +
                        applicationSettings.pID +
                        "&status=init"
        );
        web_Inbound.Get();
        web_Outbound.Url(
                applicationSettings.baseURL +
                        "?action=LIST&entity=chat&sessionID=" +
                        applicationSettings.sessionID +
                        "&initiator_pID=" +
                        applicationSettings.pID +
                        "&status=init"
        );
        web_Outbound.Get();
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

                YailList tempData = YailList.makeList(ListofContactWeb1.toArray());
//                ListofContactWeb1.add(listview_Open.Elements().toString());
                listview_Open.Elements(tempData);


            } else {
                notifier_Messages.ShowMessageDialog("Error getting Contact1 details", "Information", "OK" );
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog("JSON Exception (1)", "Information", "OK" );
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server", "Information", "OK" );
        }
        web_Contact2.Url(
                applicationSettings.baseURL +
                        "?action=LIST&entity=chat&sessionID=" +
                        applicationSettings.sessionID +
                        "&initiator_pID=" +
                        applicationSettings.pID +
                        "&status=open"
        );
        web_Contact2.Get();

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

                String[] temp= new String[listview_Open.Elements().toStringArray().length];
                temp= listview_Open.Elements().toStringArray();
                for (int i=0; i< temp.length; i++) {
                    ListofContactWeb2.add(temp[i]);
                }
                YailList tempData2 = YailList.makeList(ListofContactWeb1.toArray());
//                ListofContactWeb1.add(listview_Open.Elements().toString());
                tempData2=YailList.makeList( ListofContactWeb2 );
                listview_Open.Elements(tempData2);

            } else {
                notifier_Messages.ShowMessageDialog("Error getting Contact2 details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog("JSON Exception (2)", "Information", "OK");
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server","Information", "OK");
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
                listview_In.Elements(tempData3);

            } else {
                notifier_Messages.ShowMessageDialog("Error getting Inbound details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog("JSON Exception (3)", "Information", "OK");
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server","Information", "OK");
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
                listview_Out.Elements(tempData4);

            } else {
                notifier_Messages.ShowMessageDialog("Error getting Outbound details", "Information", "OK");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog("JSON Exception (4)", "Information", "OK");
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }

    }
    void dbg (String debugMsg) {
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }
}