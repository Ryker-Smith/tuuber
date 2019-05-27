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
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.util.YailList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap; // import the HashMap class

public class screen11_Pools extends Form implements HandlesEventDispatching {

    tuuber_Settings applicationSettings;
    tuuberCommonSubroutines tools;

    private VerticalArrangement Pools;
    private HorizontalArrangement OutboundInitiationButtonHZ, InboundButtonsHZ, ChatsScreenHZ, toolbarHz;
    private VerticalArrangement vt_Open, vt_In, vt_Out;
    private ListView listview_Open, listview_Out, listview_In;
    private Button button_OpenChatScreen, button_AcceptInbound, button_DeclineInbound, button_CancelOutbound, Refresh, MainMenu;
    private Label label_Open, label_Out, label_In, label_pID;
    private Web web_Open, web_Inbound, web_AcceptInbound, web_DeclineInbound, web_Outbound, web_OutboundCancel;
    private List<String> ListofContactWeb1, ListofInboundWeb, ListofOutboundWeb;
    private Notifier notifier_Messages;
    String string_InboundpID, string_OutboundpID;

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
        Pools = new VerticalArrangement(this);

        toolbarHz = new HorizontalArrangement(Pools);
        MainMenu = new Button(toolbarHz);
        MainMenu.Width(40);
        MainMenu.Height(40);
        MainMenu.Image(applicationSettings.ourLogo);
        label_pID =tools.fn_HeadingLabel(toolbarHz, label_pID, applicationSettings.pID,"Pools");
        Refresh = new Button(toolbarHz);
        Refresh.Width(40);
        Refresh.Height(40);
        Refresh.FontSize(8);
        Refresh.Image("buttonRefresh.png");

        vt_Open = new VerticalArrangement(Pools);
        vt_Open.HeightPercent(40);
        label_Open = new Label(vt_Open);
        label_Open.Text(tools.fn_téacs_aistriú("pools_im_in",tools.capitalize_none));
        listview_Open = new ListView(vt_Open);
        listview_Open.TextSize(applicationSettings.intListViewsize);
        listview_Open.HeightPercent(100);
        listview_Open.WidthPercent(100);
        listview_Open.SelectionColor(Component.COLOR_DKGRAY);
        listview_Open.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
        Label dghv=(Label)tools.padding(Pools,1,1);
        ChatsScreenHZ = new HorizontalArrangement(Pools);
        ChatsScreenHZ.WidthPercent(100);

        button_OpenChatScreen = new Button(ChatsScreenHZ);
        button_OpenChatScreen.Text(tools.fn_téacs_aistriú("chat",tools.capitalize_none));
        tools.buttonOnOff(button_OpenChatScreen,false);
        ChatsScreenHZ.AlignHorizontal(Component.ALIGNMENT_CENTER);

        vt_In = new VerticalArrangement(Pools);
        vt_In.HeightPercent(30);
        label_In = new Label(vt_In);
        label_In.Text("Pools pending");
        listview_In = new ListView(vt_In);
        listview_In.TextSize(applicationSettings.intListViewsize);
        listview_In.Height(100);
        listview_In.WidthPercent(100);
        listview_In.SelectionColor(Component.COLOR_DKGRAY);
        listview_In.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));

        Label dgh=(Label)tools.padding(Pools,1,1);
        InboundButtonsHZ = new HorizontalArrangement(Pools);

        button_AcceptInbound = new Button(InboundButtonsHZ);
        button_AcceptInbound.Text("Accept");
        tools.buttonOnOff(button_AcceptInbound,false);
        Label deggh=(Label)tools.padding(InboundButtonsHZ,1,1);
        button_DeclineInbound = new Button(InboundButtonsHZ);
        button_DeclineInbound.Text("Decline");
        tools.buttonOnOff(button_DeclineInbound,false);
        InboundButtonsHZ.AlignHorizontal(Component.ALIGNMENT_CENTER);
        InboundButtonsHZ.WidthPercent(100);

//        vt_Out = new VerticalArrangement(Pools);
//        vt_Out.HeightPercent(20);
//        label_Out = new Label(vt_Out);
//        label_Out.Text("Pools I'm making (Outbound)");
//        listview_Out = new ListView(vt_Out);
//        listview_Out.TextSize(applicationSettings.intListViewsize);
//        listview_Out.Height(100);
//        listview_Out.WidthPercent(100);
//        listview_Out.SelectionColor(Component.COLOR_DKGRAY);
//        listview_Out.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
//
//        Label dggh=(Label)tools.padding(Pools,1,1);
//        OutboundInitiationButtonHZ = new HorizontalArrangement(Pools);
//        OutboundInitiationButtonHZ.AlignHorizontal(Component.ALIGNMENT_CENTER);
//        OutboundInitiationButtonHZ.WidthPercent(100);
//
//        button_CancelOutbound = new Button(OutboundInitiationButtonHZ);
//        button_CancelOutbound.Text("Cancel Outbound");
//        tools.buttonOnOff(button_CancelOutbound,false);

        web_Open = new Web(this);
        web_Inbound = new Web(this);
        web_AcceptInbound = new Web(this);
        web_DeclineInbound = new Web(this);
        web_Outbound = new Web(this);
        web_OutboundCancel = new Web(this);
        notifier_Messages = new Notifier(Pools);

        tools.button_CommonFormatting(50,button_OpenChatScreen, button_CancelOutbound);
        tools.button_CommonFormatting(40,button_AcceptInbound, button_DeclineInbound);
        tools.buttonOnOff(button_OpenChatScreen, false);
        tools.buttonOnOff(button_AcceptInbound, false);
        tools.buttonOnOff(button_DeclineInbound, false);
//        tools.buttonOnOff(button_CancelOutbound,false);
        
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
                switchForm("screen08_ChatWith");
                return true;
            }
            else if (component.equals(button_AcceptInbound)) {
                String[] temp=listview_In.Selection().split("=");
                String pool_ID=temp[1].replace(")","");
                web_AcceptInbound.Url(
                        applicationSettings.baseURL +
                                "?action=PUT&entity=POOL&sessionID=" +
                                applicationSettings.sessionID +
                                "&iam=" +
                                applicationSettings.pID +
                                "&pool_ID=" +
                                pool_ID +
                                "&status=open"
                );
                web_AcceptInbound.Get();
                callBackEnd();
                return true;
            }
            else if (component.equals(button_DeclineInbound)) {
                String[] temp=listview_In.Selection().split("=");
                String pool_ID=temp[1].replace(")","");
                web_DeclineInbound.Url(
                        applicationSettings.baseURL +
                                "?action=DELETE&entity=POOL&sessionID=" +
                                applicationSettings.sessionID +
                                "&iam=" +
                                applicationSettings.pID +
                                "&pool_ID=" +
                                pool_ID
                );
                web_DeclineInbound.Get();
                tools.dbg(web_DeclineInbound.Url());
                return true;
            }
            else if (component.equals(button_CancelOutbound)) {
                String[] temp=listview_Out.Selection().split("=");
                String pool_ID=temp[1].replace(")","");
                web_OutboundCancel.Url(
                        applicationSettings.baseURL +
                                "?action=DELETE&entity=POOL&sessionID=" +
                                applicationSettings.sessionID +
                                "&iam=" +
                                applicationSettings.pID +
                                "&pool_ID=" +
                                pool_ID
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
                String selection = listview_Open.Selection();
                String[] separated = selection.split("[(]"); // using [] to escape ( without )
                separated[1] = separated[1].replace(")", "");
                String[] bitsOfData = separated[1].split(";");
                String link_ID = bitsOfData[1].split("=")[1];
                applicationSettings.CurrentLinkId = link_ID;
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
                tools.dbg("Inbound selection");
                return true;
            }
            else if (component.equals(listview_Out)) {
                String nextString = listview_Out.Selection();
                String[] separated = nextString.split(":");
                string_OutboundpID=separated[0];
                tools.buttonOnOff(button_CancelOutbound,true);
                tools.dbg("Outbound selection");
                return true;
            }
        }
        else if (eventName.equals("GotText")) {
            if (component.equals(web_Open)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_ExistingPools(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_Inbound)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                fn_GotText_PendingPool_Inbound(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_Outbound)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
//                fn_GotText_PendingPool_Outbound(status, textOfResponse);
                return true;
            }
            else if (component.equals(web_DeclineInbound)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                callBackEnd();
                return true;
            }
            else if (component.equals(web_OutboundCancel)) {
                String status = params[1].toString();
                String textOfResponse = (String) params[3];
                callBackEnd();
                return true;
            }
        }
        return false;
    }

    void callBackEnd() {
        web_Open.Url(
                applicationSettings.baseURL +
                        "?action=LIST&entity=POOL&sessionID=" +
                        applicationSettings.sessionID +
                        "&pID=" +
                        applicationSettings.pID +
                        "&status=open"
        );
        web_Open.Get();
        web_Inbound.Url(
                applicationSettings.baseURL +
                        "?action=LIST&entity=POOL&sessionID=" +
                        applicationSettings.sessionID +
                        "&pID=" +
                        applicationSettings.pID +
                        "&status=init"
        );
        web_Inbound.Get();
//        web_Outbound.Url(
//                applicationSettings.baseURL +
//                        "?action=LIST&entity=POOL&sessionID=" +
//                        applicationSettings.sessionID +
//                        "&iam=" +
//                        applicationSettings.pID +
//                        "&status=init" +
//                        "&direction=out"
//        );
//        web_Outbound.Get();
    }

    public void fn_GotText_ExistingPools(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
//        tools.dbg(status);
//        tools.dbg(textOfResponse);
        if (status.equals("200" )) try {
            ListofContactWeb1 = new ArrayList<String>();
            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("pool" ).equals("" )) {
                JSONArray contacts1Array = parser.getJSONArray("pool" );
                for (int i = 0; i < contacts1Array.length(); i++) {
                    if (contacts1Array.getJSONObject(i).toString().equals("{}")) break;
                    ListofContactWeb1.add(
                            "Currently pooling with " +
                                    contacts1Array.getJSONObject(i).getString("realName" ) +
                                    " (pool_ID=" +
                                    contacts1Array.getJSONObject(i).getString("pool_ID" ) +
                                    ";link_ID=" +
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

    public void fn_GotText_PendingPool_Inbound(String status, String textOfResponse) {
        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
//        tools.dbg(status);
//        tools.dbg("INBOUND: " + textOfResponse);
        if (status.equals("200") ) try {

            ListofInboundWeb = new ArrayList<String>();

            JSONObject parser = new JSONObject(textOfResponse);
            if (!parser.getString("pool").equals("")) {

                JSONArray Inbound = parser.getJSONArray("pool");
                for(int i = 0 ; i < Inbound.length() ; i++){
                    if (Inbound.getJSONObject(i).toString().equals("{}")) break;
                    String anItem = "Connection pending with " +
                            Inbound.getJSONObject(i).getString("realName") +
                            " (pool_ID=" +
                            Inbound.getJSONObject(i).getString("pool_ID")
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

//    public void fn_GotText_PendingPool_Outbound(String status, String textOfResponse) {
//        // See:  https://stackoverflow.com/questions/5015844/parsing-json-object-in-java
////        tools.dbg(status);
////        tools.dbg("OUTBOUND: " + textOfResponse);
//        if (status.equals("200") ) try {
//
//            ListofOutboundWeb = new ArrayList<String>();
//            JSONObject parser = new JSONObject(textOfResponse);
//            if (!parser.getString("pool").equals("")) {
//                JSONArray Outbound = parser.getJSONArray("pool");
//                for(int i = 0 ; i < Outbound.length() ; i++){
//                    if (Outbound.getJSONObject(i).toString().equals("{}")) break;
//                    String anItem = "Connecting with " +
////                            Outbound.getJSONObject(i).getString("realName") +
//                            " (pool_ID=" +
//                            Outbound.getJSONObject(i).getString("pool_ID")
//                            +")";
//                    ListofOutboundWeb.add( anItem );
//                    tools.dbg(anItem);
//                }
//                YailList tempData=YailList.makeList( ListofOutboundWeb );
//                listview_Out.Elements(YailList.makeList(ListofOutboundWeb));
//                tools.dbg(tempData.toString());
//            }
//            else {
//                notifier_Messages.ShowMessageDialog("Error getting Outbound details", "Information", "OK");
//            }
//        }
//        catch (JSONException e) {
//            // if an exception occurs, code for it in here
//            notifier_Messages.ShowMessageDialog("JSON Exception (4)", "Information", "OK");
//        }
//        else {
//            notifier_Messages.ShowMessageDialog("Problem connecting with server","Information", "OK");
//        }
//        tools.buttonOnOff(button_CancelOutbound,false);
//    }
}