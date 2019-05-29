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
import com.google.appinventor.components.runtime.ListPicker;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.VerticalArrangement;
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
    
    private HorizontalArrangement OutboundInitiationButtonHZ, InboundButtonsHZ, ChatsScreenHZ, toolbarHz, PaddingHZOpenConversations,PaddingHZInConversations, PaddingHZOutConversations;
    private VerticalArrangement Conversations, vt_Open, vt_In, vt_Out;
    private ListView listview_Open, listview_Out, listview_In;
    private Button button_OpenChatScreen, button_AcceptInbound, button_DeclineInbound, button_CancelOutbound, Refresh, MainMenu;
    private Label label_Open, label_Out, label_In, label_pID;
    private ListPicker listpicker_UserDirectory;
    private Web web_Open, web_Inbound, web_AcceptInbound, web_DeclineInbound, web_Outbound, web_UserDirectory, web_OutboundCancel, web_InitiateChat;
    private List<String> ListofContactWeb1, ListofInboundWeb, ListofOutboundWeb, ListofUsersWeb;
    private Notifier notifier_Messages;
    String string_InboundpID, string_OutboundpID, string_UserDirectory_pID;
//    HashMap<Integer, String> conversationsOpen = new HashMap<Integer, String>();

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
        Conversations = new VerticalArrangement(this);

        toolbarHz = new HorizontalArrangement(Conversations);
        MainMenu = new Button(toolbarHz);
        MainMenu.Width(40);
        MainMenu.Height(40);
        MainMenu.Image(applicationSettings.ourLogo);


        label_pID =tools.fn_HeadingLabel(toolbarHz, label_pID, applicationSettings.pID, (tools.fn_téacs_aistriú("conversations")));

        Refresh = new Button(toolbarHz);
        Refresh.Width(40);
        Refresh.Height(40);
        Refresh.FontSize(8);
        Refresh.Image("buttonRefresh.png");

        vt_Open = new VerticalArrangement(Conversations);
        vt_Open.HeightPercent(20);
        label_Open = new Label(vt_Open);
        label_Open.Text(tools.fn_téacs_aistriú("open_conversations"));
        listview_Open = new ListView(vt_Open);
        listview_Open.TextSize(applicationSettings.intListViewsize);
        listview_Open.HeightPercent(20);
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
        button_OpenChatScreen.Text(tools.fn_téacs_aistriú("chat"));
        tools.buttonOnOff(button_OpenChatScreen,false);

        vt_In = new VerticalArrangement(Conversations);
        vt_In.HeightPercent(20);
        label_In = new Label(vt_In);

        // not translated yet
        label_In.Text(tools.fn_téacs_aistriú("pending_inbound",tools.capitalize_none));
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

        //not translated yet
        button_AcceptInbound.Text(tools.fn_téacs_aistriú("accept_inbound",tools.capitalize_none));
        tools.buttonOnOff(button_AcceptInbound,false);

        Button button_Pad_Separate_Accept_Decline = new Button (InboundButtonsHZ);
        button_Pad_Separate_Accept_Decline.BackgroundColor(Component.COLOR_NONE);
        button_Pad_Separate_Accept_Decline.Text("");
        button_Pad_Separate_Accept_Decline.Height(5);
        button_Pad_Separate_Accept_Decline.Width(5);

        button_DeclineInbound = new Button(InboundButtonsHZ);

        //not translated yet
        button_DeclineInbound.Text(tools.fn_téacs_aistriú("decline_inbound",tools.capitalize_none));
        tools.buttonOnOff(button_DeclineInbound,false);

        vt_Out = new VerticalArrangement(Conversations);
        vt_Out.HeightPercent(20);
        label_Out = new Label(vt_Out);
        label_Out.Text(tools.fn_téacs_aistriú("pending_outbound",tools.capitalize_none));
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
        button_CancelOutbound.Text(tools.fn_téacs_aistriú("cancel_outbound",tools.capitalize_none));
        tools.buttonOnOff(button_CancelOutbound,false);
        button_CancelOutbound.Enabled(false);
        listpicker_UserDirectory = new ListPicker(OutboundInitiationButtonHZ);

        //not translated yet
        listpicker_UserDirectory.Text(tools.fn_téacs_aistriú("user_directory",tools.capitalize_none));
        listpicker_UserDirectory.Selection();
        listpicker_UserDirectory.Shape(BUTTON_SHAPE_ROUNDED);
        listpicker_UserDirectory.TextColor(Component.COLOR_WHITE);
        listpicker_UserDirectory.FontSize(applicationSettings.int_ButtonTextSize);
        listpicker_UserDirectory.Height(40);
        listpicker_UserDirectory.WidthPercent(40);
        listpicker_UserDirectory.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
        listpicker_UserDirectory.Enabled(false);

        tools.button_CommonFormatting(40, button_OpenChatScreen, button_AcceptInbound, button_DeclineInbound, button_CancelOutbound);
        tools.buttonOnOff(button_OpenChatScreen, false);
        tools.buttonOnOff(button_AcceptInbound, false);
        tools.buttonOnOff(button_DeclineInbound, false);
        tools.buttonOnOff(button_CancelOutbound,false);

        web_Open = new Web(this);
        web_Inbound = new Web(this);
        web_AcceptInbound = new Web(this);
        web_DeclineInbound = new Web(this);
        web_Outbound = new Web(this);
        web_UserDirectory = new Web(this);
        web_OutboundCancel = new Web(this);
        web_InitiateChat = new Web(this);
        notifier_Messages = new Notifier(Conversations);

        EventDispatcher.registerEventForDelegation(this, formName, "Click");
        EventDispatcher.registerEventForDelegation(this, formName, "GotText");
        EventDispatcher.registerEventForDelegation(this, formName, "AfterPicking");
        callBackEnd();
        fn_GotText_UserDirectory();
    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        tools.dbg("dispatchEvent: " + formName + " [" +component.toString() + "] [" + componentName + "] " + eventName);
        if (eventName.equals("Click")) {
            if (component.equals(MainMenu))  {
                finish();
                return true;
            }
            else if (component.equals(button_OpenChatScreen)) {
                switchForm("screen08_ChatWith");
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
//                tools.dbg(web_DeclineInbound.Url());
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
//                tools.dbg(web_OutboundCancel.Url());
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
                tools.buttonOnOff(button_OpenChatScreen,true);
                return true;
            }
            else if (component.equals(listview_In)) {
                String currentString = listview_In.Selection();
                String[] separated = currentString.split(":");
                string_InboundpID=separated[0];
                tools.buttonOnOff(button_AcceptInbound,true);
                tools.buttonOnOff(button_DeclineInbound,true);
//                tools.dbg("Inbound selection");
                return true;
            }
            else if (component.equals(listview_Out)) {
                String nextString = listview_Out.Selection();
                String[] separated = nextString.split(":");
                string_OutboundpID=separated[0];
                tools.buttonOnOff(button_CancelOutbound,true);
//                tools.dbg("Outbound selection");
                return true;
            }
            else if (component.equals(listpicker_UserDirectory)) {
                String[] separated = listpicker_UserDirectory.Selection().split("=");
                separated[1]=separated[1].replace(")","");
                Integer temp_pID = Integer.valueOf(separated[1]);
                Integer temp_rID = -1;//Integer.valueOf(bitsOfData[1].split("=")[1]);
                applicationSettings.set();
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
                return true;
            }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_Open)) {
//                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_OpenConversations(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_Inbound)) {
//                tools.dbg((String) params[0]);
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
            else if (component.equals(web_UserDirectory)) {
                tools.dbg((String) params[0]);
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_UserDirectory(status, textOfResponse);
                listpicker_UserDirectory.Enabled(true);
                return true;
            }
            else if (component.equals(web_DeclineInbound)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
//                tools.dbg(status);
//                tools.dbg(textOfResponse);
                callBackEnd();
                return true;
            }
            else if (component.equals(web_OutboundCancel)) {
                    String status = params[1].toString();
                    String textOfResponse = (String) params[3];
//                    tools.dbg(status);
//                    tools.dbg(textOfResponse);
                    callBackEnd();
                    return true;
            }
            else if (component.equals(web_InitiateChat)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_InitiateChate(status, textOfResponse);
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

    void fn_GotText_UserDirectory() {
        web_UserDirectory.Url(
                applicationSettings.baseURL +
                        "?action=LIST&entity=directory&sessionID=" +
                        applicationSettings.sessionID +
                        "&pID=" +
                        applicationSettings.pID
        );
        web_UserDirectory.Get();
    }
    public void fn_GotText_OpenConversations(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
//        tools.dbg(status);
//        tools.dbg(textOfResponse);
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
        tools.buttonOnOff(button_OpenChatScreen,false);
    }

    public void fn_GotText_InboundConversations(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
//        tools.dbg(status);
//        tools.dbg("INBOUND: " + textOfResponse);
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
        tools.buttonOnOff(button_AcceptInbound,false);
        tools.buttonOnOff(button_DeclineInbound,false);
    }

    public void fn_GotText_OutboundConversations(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
//        tools.dbg(status);
//        tools.dbg("OUTBOUND: " + textOfResponse);
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
        tools.buttonOnOff(button_CancelOutbound,false);
    }
    public void fn_GotText_UserDirectory(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
//        tools.dbg(status);
//        tools.dbg("OUTBOUND: " + textOfResponse);
        if (status.equals("200") ) try {

            ListofUsersWeb = new ArrayList<String>();
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("directory").equals("")) {
                JSONArray User = parser.getJSONArray("directory");
                for(int i = 0 ; i < User.length() ; i++){
                    if (User.getJSONObject(i).toString().equals("{}")) break;
                    String anItem =
                            User.getJSONObject(i).getString("realName") +
                                    " " +
//                                    User.getJSONObject(i).getString("nickname") +
                                    "(pID=" +
                                    User.getJSONObject(i).getString("pID") +
                                    ")";

                    ListofUsersWeb.add( anItem );
                    tools.dbg(anItem);
                }
                YailList tempData=YailList.makeList(ListofUsersWeb);
                listpicker_UserDirectory.Elements(YailList.makeList(ListofUsersWeb));
                tools.dbg(tempData.toString());
            }
            else {
                notifier_Messages.ShowMessageDialog("Error getting User Directory", "Information", "OK");
            }
        }
        catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowMessageDialog("JSON Exception (5)", "Information", "OK");
        }
        else {
            notifier_Messages.ShowMessageDialog("Problem connecting with server","Information", "OK");
        }
        tools.buttonOnOff(button_CancelOutbound,false);
    }

    public void fn_GotText_InitiateChate(String status, String textOfResponse) {
        String temp=new String();
        if (status.equals("200") ) try {
            JSONObject parser = new JSONObject(textOfResponse);
            temp = parser.getString("result");
            if (parser.getString("result").equals("OK")) {
                notifier_Messages.ShowAlert("An invitation to chat has been sent");
            } else {
                notifier_Messages.ShowAlert("Could not initiate chat");
            }
        } catch (JSONException e) {
            // if an exception occurs, code for it in here
            notifier_Messages.ShowAlert("JSON Exception");
        }
        else {
            notifier_Messages.ShowAlert("Problem connecting with server");
        }
    }
}