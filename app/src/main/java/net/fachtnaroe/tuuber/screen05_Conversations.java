package net.fachtnaroe.tuuber;

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
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.util.YailList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap; // import the HashMap class

public class screen05_Conversations extends Form implements HandlesEventDispatching {

    tuuber_Settings applicationSettings;
    tuuberCommonSubroutines tools;
    
    private VerticalScrollArrangement Conversations;
    private HorizontalArrangement OutboundInitiationButtonHZ, InboundButtonsHZ, ChatsScreenHZ, toolbarHz, PaddingHZOpenConversations,PaddingHZInConversations, PaddingHZOutConversations;
    private VerticalArrangement vt_Open, vt_In, vt_Out;
    private ListView listview_Open, listview_Out, listview_In;
    private Button button_OpenChatScreen, button_AcceptInbound, button_DeclineInbound, button_CancelOutbound, Refresh, MainMenu;
    private Label label_Open, label_Out, label_In, label_pID;
    private Web web_Open, web_Inbound, web_AcceptInbound, web_DeclineInbound, web_Outbound, web_OutboundCancel;
    private List<String> ListofContactWeb1, ListofInboundWeb, ListofOutboundWeb;
    private Notifier notifier_Messages;
    String string_InboundLineID, string_InboundpID, string_OutboundpID, string_OutboundLineID;
    HashMap<Integer, String> conversationsOpen = new HashMap<Integer, String>();

    protected void $define() {

        applicationSettings = new tuuber_Settings(this);
        applicationSettings.get();
        tools=new tuuberCommonSubroutines(this);
        tools.dbg(applicationSettings.backgroundImageName);
        try {
            this.BackgroundImage(applicationSettings.backgroundImageName);
        }
        catch (Exception e) {
            tools.dbg(e.toString());
        }
        Conversations = new VerticalScrollArrangement(this);

        toolbarHz = new HorizontalArrangement(Conversations);
        MainMenu = new Button(toolbarHz);
        MainMenu.Width(40);
        MainMenu.Height(40);
        MainMenu.Image(applicationSettings.ourLogo);


        label_pID =tools.fn_HeadingLabel(toolbarHz, label_pID, applicationSettings.pID, "Conversations");

        Refresh = new Button(toolbarHz);
        Refresh.Width(40);
        Refresh.Height(40);
        Refresh.FontSize(8);
        Refresh.Image("buttonRefresh.png");

        vt_Open = new VerticalArrangement(Conversations);
        vt_Open.HeightPercent(20);
        label_Open = new Label(vt_Open);
        label_Open.Text("Open Conversations");
        listview_Open = new ListView(vt_Open);
        listview_Open.TextSize(applicationSettings.intListViewsize);
        listview_Open.HeightPercent(100);
        listview_Open.WidthPercent(100);
        listview_Open.SelectionColor(Component.COLOR_DKGRAY);
        listview_Open.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));

        PaddingHZOpenConversations = new HorizontalArrangement(Conversations);
        Button button_Pad_OpenConversations = new Button(PaddingHZOpenConversations);
        button_Pad_OpenConversations.BackgroundColor(Component.COLOR_NONE);
        button_Pad_OpenConversations.Text("");
        button_Pad_OpenConversations.Height(5);

        ChatsScreenHZ = new HorizontalArrangement(Conversations);
        button_OpenChatScreen = new Button(ChatsScreenHZ);
        button_OpenChatScreen.Text("Chat");
        button_OpenChatScreen.Enabled(false);

        vt_In = new VerticalArrangement(Conversations);
        vt_In.HeightPercent(20);
        label_In = new Label(vt_In);
        label_In.Text("Pending (Inbound)");
        listview_In = new ListView(vt_In);
        listview_In.TextSize(applicationSettings.intListViewsize);
        listview_In.Height(100);
        listview_In.WidthPercent(100);
        listview_In.SelectionColor(Component.COLOR_DKGRAY);
        listview_In.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));

        PaddingHZInConversations = new HorizontalArrangement(Conversations);
        Button button_Pad_InvitationsToChat = new Button(PaddingHZInConversations);
        button_Pad_InvitationsToChat.BackgroundColor(Component.COLOR_NONE);
        button_Pad_InvitationsToChat.Text("");
        button_Pad_InvitationsToChat.Height(5);

        InboundButtonsHZ = new HorizontalArrangement(Conversations);
        button_AcceptInbound = new Button(InboundButtonsHZ);
        button_AcceptInbound.Text("Accept inbound");
        button_AcceptInbound.Enabled(false);

        Button button_Pad_Separate_Accept_Decline = new Button (InboundButtonsHZ);
        button_Pad_Separate_Accept_Decline.BackgroundColor(Component.COLOR_NONE);
        button_Pad_Separate_Accept_Decline.Text("");
        button_Pad_Separate_Accept_Decline.Height(5);
        button_Pad_Separate_Accept_Decline.Width(5);

        button_DeclineInbound = new Button(InboundButtonsHZ);
        button_DeclineInbound.Text("Decline inbound");
        button_DeclineInbound.Enabled(false);

        vt_Out = new VerticalArrangement(Conversations);
        vt_Out.HeightPercent(20);
        label_Out = new Label(vt_Out);
        label_Out.Text("Pending (Outbound)");
        listview_Out = new ListView(vt_Out);
        listview_Out.TextSize(applicationSettings.intListViewsize);
        listview_Out.Height(100);
        listview_Out.WidthPercent(100);
        listview_Out.SelectionColor(Component.COLOR_DKGRAY);
        listview_Out.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));

        PaddingHZOutConversations = new HorizontalArrangement(Conversations);
        Button button_Pad_SentInvitationsToChat = new Button(PaddingHZOutConversations);
        button_Pad_SentInvitationsToChat.BackgroundColor(Component.COLOR_NONE);
        button_Pad_SentInvitationsToChat.Text("");
        button_Pad_SentInvitationsToChat.Height(5);

        OutboundInitiationButtonHZ = new HorizontalArrangement(Conversations);
        button_CancelOutbound = new Button(OutboundInitiationButtonHZ);
        button_CancelOutbound.Text("Cancel Outbound");
        button_CancelOutbound.Enabled(false);

        tools.button_CommonFormatting(40, button_OpenChatScreen, button_AcceptInbound, button_DeclineInbound, button_CancelOutbound);

        web_Open = new Web(this);
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
        tools.dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("Click")) {
            if (component.equals(MainMenu))  {
                finish();
                return true;
            }
            else if (component.equals(button_OpenChatScreen)) {
                startNewForm("screen08_ChatWith",null);
                return true;
            }
            else if (component.equals(button_AcceptInbound)) {
                String[] temp=listview_In.Selection().split("=");
                String link_ID=temp[1].replace(")","");
                web_AcceptInbound.Url(
                        applicationSettings.baseURL +
                                "?action=PUT&entity=LINK&sessionID=" +
                                applicationSettings.sessionID +
                                "&iam=" +
                                applicationSettings.pID +
                                "&link_ID=" +
                                link_ID +
                                "&status=open"
                );
                web_AcceptInbound.Get();
                callBackEnd();
                return true;
            }
            else if (component.equals(button_DeclineInbound)) {
                String[] temp=listview_In.Selection().split("=");
                String link_ID=temp[1].replace(")","");
                web_DeclineInbound.Url(
                        applicationSettings.baseURL +
                                "?action=DELETE&entity=LINK&sessionID=" +
                                applicationSettings.sessionID +
                                "&iam=" +
                                applicationSettings.pID +
                                "&link_ID=" +
                                link_ID
                );
                web_DeclineInbound.Get();
                tools.dbg(web_DeclineInbound.Url());
                return true;
            }
            else if (component.equals(button_CancelOutbound)) {
                String[] temp=listview_Out.Selection().split("=");
                String link_ID=temp[1].replace(")","");
                web_OutboundCancel.Url(
                        applicationSettings.baseURL +
                                "?action=DELETE&entity=LINK&sessionID=" +
                                applicationSettings.sessionID +
                                "&iam=" +
                                applicationSettings.pID +
                                "&link_ID=" +
                                link_ID
                );
                tools.dbg(web_OutboundCancel.Url());
                web_OutboundCancel.Get();
                return true;
            }
            else if (component.equals(Refresh)) {
                callBackEnd();
                return true;
            }
        }
        else if (eventName.equals("AfterPicking")) {
            if (component.equals(listview_Open)) {
                String[] separated = listview_Open.Selection().split("=");
                separated[1]=separated[1].replace(")","");
                applicationSettings.CurrentLinkId =separated[1];
                applicationSettings.set();
                button_OpenChatScreen.Enabled(true);
                return true;
            }
            else if (component.equals(listview_In)) {
                String currentString = listview_In.Selection();
                String[] separated = currentString.split(":");
                string_InboundpID=separated[0];
                button_AcceptInbound.Enabled(true);
                button_DeclineInbound.Enabled(true);
                tools.dbg("Inbound selection");
                return true;
            }
            else if (component.equals(listview_Out)) {
                String nextString = listview_Out.Selection();
                String[] separated = nextString.split(":");
                string_OutboundpID=separated[0];
                button_CancelOutbound.Enabled(true);
                tools.dbg("Outbound selection");
                return true;
            }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_Open)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_OpenConversations(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_Inbound)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_InboundConversations(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_Outbound)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_OutboundConversations(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_DeclineInbound)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                tools.dbg(status);
                tools.dbg(textOfResponse);
                callBackEnd();
                return true;
            }
            else if (component.equals(web_OutboundCancel)) {
                    String status = params[1].toString();
                    String textOfResponse = (String) params[3];
                    tools.dbg(status);
                    tools.dbg(textOfResponse);
                    callBackEnd();
                    return true;
            }
        }
        return false;
    }

    void callBackEnd() {
        web_Open.Url(
                        applicationSettings.baseURL +
                        "?action=LIST&entity=link&sessionID=" +
                        applicationSettings.sessionID +
                        "&iam=" +
                        applicationSettings.pID +
                        "&status=open"
        );
        web_Open.Get();
        web_Inbound.Url(
                        applicationSettings.baseURL +
                        "?action=LIST&entity=link&sessionID=" +
                        applicationSettings.sessionID +
                        "&iam=" +
                        applicationSettings.pID +
                        "&status=init" +
                        "&direction=in"
        );
        web_Inbound.Get();
        web_Outbound.Url(
                        applicationSettings.baseURL +
                        "?action=LIST&entity=link&sessionID=" +
                        applicationSettings.sessionID +
                        "&iam=" +
                        applicationSettings.pID +
                        "&status=init" +
                        "&direction=out"
        );
        web_Outbound.Get();
    }

    public void fn_GotText_OpenConversations(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        tools.dbg(status);
        tools.dbg(textOfResponse);
        if (status.equals("200" )) try {

            ListofContactWeb1 = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("link" ).equals("" )) {
                JSONArray contacts1Array = parser.getJSONArray("link" );
                for (int i = 0; i < contacts1Array.length(); i++) {
                    if (contacts1Array.getJSONObject(i).toString().equals("{}")) break;
                    ListofContactWeb1.add(
                                    "Chatting with " +
                                    contacts1Array.getJSONObject(i).getString("realName" ) +
                                    " (link_ID=" +
                                    contacts1Array.getJSONObject(i).getString("link_ID" )
                                    + ")"
                    );
                }
                YailList tempData = YailList.makeList(ListofContactWeb1.toArray());
                ListofContactWeb1.add(listview_Open.Elements().toString());
                listview_Open.Elements(tempData);
            }
            else {
                notifier_Messages.ShowMessageDialog("Error getting Contact1 details", "Information", "OK" );
            }
        }
        catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog("JSON Exception (Open 1)", "Information", "OK" );
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server", "Information", "OK" );
        }
        button_OpenChatScreen.Enabled(false);
    }

    public void fn_GotText_InboundConversations(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        tools.dbg(status);
        tools.dbg("INBOUND: " + textOfResponse);
        if (status.equals("200") ) try {

            ListofInboundWeb = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("link").equals("")) {

                JSONArray Inbound = parser.getJSONArray("link");
                for(int i = 0 ; i < Inbound.length() ; i++){
                    if (Inbound.getJSONObject(i).toString().equals("{}")) break;
                    String anItem = "Contact request from " +
                            Inbound.getJSONObject(i).getString("realName") +
                            " (link_ID=" +
                            Inbound.getJSONObject(i).getString("link_ID")
                            + ")";
                    ListofInboundWeb.add( anItem );
                }
                listview_In.Elements(YailList.makeList( ListofInboundWeb ));
            }
            else {
                notifier_Messages.ShowMessageDialog("Error getting Inbound details", "Information", "OK");
            }
        }
        catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog("JSON Exception (3)", "Information", "OK");
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
        button_AcceptInbound.Enabled(false);
        button_DeclineInbound.Enabled(false);
    }

    public void fn_GotText_OutboundConversations(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
        tools.dbg(status);
        tools.dbg("OUTBOUND: " + textOfResponse);
        if (status.equals("200") ) try {

            ListofOutboundWeb = new ArrayList<String>();
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("link").equals("")) {
                JSONArray Outbound = parser.getJSONArray("link");
                for(int i = 0 ; i < Outbound.length() ; i++){
                    if (Outbound.getJSONObject(i).toString().equals("{}")) break;
                    String anItem = "Connecting to " +
                            Outbound.getJSONObject(i).getString("realName") +
                            " (link_ID=" +
                            Outbound.getJSONObject(i).getString("link_ID")
                            +")";
                    ListofOutboundWeb.add( anItem );
                    tools.dbg(anItem);
                }
                YailList tempData=YailList.makeList( ListofOutboundWeb );
                listview_Out.Elements(YailList.makeList(ListofOutboundWeb));
                tools.dbg(tempData.toString());
            }
            else {
                notifier_Messages.ShowMessageDialog("Error getting Outbound details", "Information", "OK");
            }
        }
        catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog("JSON Exception (4)", "Information", "OK");
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
        button_CancelOutbound.Enabled(false);
    }
}