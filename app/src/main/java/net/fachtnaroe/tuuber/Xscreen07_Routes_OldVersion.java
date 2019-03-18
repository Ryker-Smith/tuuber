package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.ActivityStarter;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.CheckBox;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListPicker;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.Notifier;
import com.google.appinventor.components.runtime.PasswordTextBox;
import com.google.appinventor.components.runtime.TableArrangement;
import com.google.appinventor.components.runtime.TableLayout;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;

//import com.google.appinventor.components.runtime.util;

import org.json.JSONException;
import org.json.JSONObject;

// DO NOT USE OR CHANGE THIS


public class Xscreen07_Routes_OldVersion extends Form implements HandlesEventDispatching {

    private Image TuberH, Image11, Image7, Image5, Image9, Image1, Image2;
    private Web NewRouteWeb, GetRouteWeb, Web3;
    private Notifier Notifier1, Notifier2;
    private Label InfoLabel, DriverLabel, Route1Label, OriginLabel, DestinationLabel, CheckDaysLabel, Origin2Label, Destination2Label, MonYN, Mon2YN, TueYN, Tue2YN, WedYN, Wed2YN, ThursYN, Thurs2YN, FriYN, Fri2YN;
    private HorizontalArrangement MainMenuHArrangement, HorizontalArrangement8, HorizontalArrangement11, HorizontalArrangement7, HorizontalArrangement12, HorizontalArrangement9, HorizontalArrangement13, HorizontalArrangement5, SaveRouteHArrangement;
    private VerticalArrangement RoutesVArrangement, VerticalArrangement4, VerticalArrangement3, VerticalArrangement2, Routes;
    private Button MainMenu, SaveNew;
    TinyDB localDB;
    private ListView TempList;
    private TextBox TownSingle, TownsDecoded, DriverYN;
    private ListPicker OriginList, DestinationList, OriginList2, DestinationList2;
    private CheckBox Mon, Tue, Wed, Thurs, Fri, Mon2, Tue2, Wed2, Thurs2, Fri2;
    Integer pID;

    protected void $define() {

        Routes = new VerticalArrangement(this);
        TuberH = new Image(Routes);
        localDB = new TinyDB(Routes);
        String tempString= (String) localDB.GetValue("label_pID",-1);
        pID = Integer.valueOf(tempString);
        MainMenuHArrangement = new HorizontalArrangement(Routes);
        MainMenu = new Button(MainMenuHArrangement);
        MainMenu.Text("MAIN MENU");
        HorizontalArrangement8 = new HorizontalArrangement(Routes);
        TempList = new ListView(HorizontalArrangement8);
        TownSingle = new TextBox(HorizontalArrangement8);
        TownsDecoded = new TextBox(HorizontalArrangement8);
        Image11 = new Image(Routes);
        RoutesVArrangement = new VerticalArrangement(Routes);
        VerticalArrangement4 = new VerticalArrangement(RoutesVArrangement);
        HorizontalArrangement11 = new HorizontalArrangement(VerticalArrangement4);
        InfoLabel = new Label(HorizontalArrangement11);
        InfoLabel.Text("Please fill in route information below");
        Image7 = new Image(HorizontalArrangement11);
        DriverLabel = new Label(HorizontalArrangement11);
        DriverLabel.Text("Driver Y / N");
        DriverYN = new TextBox(HorizontalArrangement11);
        DriverYN.Text("Y/N");
        Image5 = new Image(HorizontalArrangement11);
        VerticalArrangement3 = new VerticalArrangement(VerticalArrangement4);
        Route1Label = new Label(VerticalArrangement3);
        Route1Label.Text("ROUTE 1");
        HorizontalArrangement7 = new HorizontalArrangement(VerticalArrangement3);
        OriginLabel = new Label(HorizontalArrangement7);
        OriginLabel.Text("ORIGIN");
        OriginList = new ListPicker(HorizontalArrangement7);
        OriginList.Text("Select Origin");
        DestinationLabel = new Label(HorizontalArrangement7);
        DestinationLabel.Text("Destination");
        DestinationList = new ListPicker(HorizontalArrangement7);
        DestinationList.Text("Templemore");
        HorizontalArrangement12 = new HorizontalArrangement(VerticalArrangement3);
        Mon = new CheckBox(HorizontalArrangement12);
        Mon.Text("Mon");
        Tue = new CheckBox(HorizontalArrangement12);
        Tue.Text("Tue");
        Wed = new CheckBox(HorizontalArrangement12);
        Wed.Text("Wed");
        Thurs = new CheckBox(HorizontalArrangement12);
        Thurs.Text("Thurs");
        Fri = new CheckBox(HorizontalArrangement12);
        Fri.Text("Fri");
        Image9 = new Image(VerticalArrangement3);
        VerticalArrangement2 = new VerticalArrangement(VerticalArrangement3);
        CheckDaysLabel = new Label(VerticalArrangement2);
        CheckDaysLabel.Text("RETURN ROUTE");
        HorizontalArrangement9 = new HorizontalArrangement(VerticalArrangement2);
        Origin2Label = new Label(HorizontalArrangement9);
        Origin2Label.Text("ORIGIN");
        OriginList2 = new ListPicker(HorizontalArrangement9);
        OriginList2.Text("Templemore");
        Destination2Label = new Label(HorizontalArrangement9);
        Destination2Label.Text("DESTINATION");
        DestinationList2 = new ListPicker(HorizontalArrangement9);
        DestinationList2.Text("Select");
        HorizontalArrangement13 = new HorizontalArrangement(VerticalArrangement2);
        Mon2 = new CheckBox(HorizontalArrangement13);
        Mon2.Text("Mon");
        Tue2 = new CheckBox(HorizontalArrangement13);
        Tue2.Text("Tue");
        Wed2 = new CheckBox(HorizontalArrangement13);
        Wed2.Text("Wed");
        Thurs2 = new CheckBox(HorizontalArrangement13);
        Thurs2.Text("Thurs");
        Fri2 = new CheckBox(HorizontalArrangement13);
        Fri2.Text("Fri");
        HorizontalArrangement5 = new HorizontalArrangement(RoutesVArrangement);
        MonYN = new Label (HorizontalArrangement5);
        MonYN.Text("n");
        Mon2YN = new Label (HorizontalArrangement5);
        Mon2YN.Text("n");
        TueYN = new Label (HorizontalArrangement5);
        TueYN.Text("n");
        Tue2YN = new Label (HorizontalArrangement5);
        Tue2YN.Text("n");
        WedYN = new Label (HorizontalArrangement5);
        WedYN.Text("n");
        Wed2YN = new Label (HorizontalArrangement5);
        Wed2YN.Text("n");
        ThursYN = new Label (HorizontalArrangement5);
        ThursYN.Text("n");
        Thurs2YN = new Label (HorizontalArrangement5);
        Thurs2YN.Text("n");
        FriYN = new Label (HorizontalArrangement5);
        FriYN.Text("n");
        Fri2YN = new Label (HorizontalArrangement5);
        Fri2YN.Text("n");
        SaveRouteHArrangement = new HorizontalArrangement(Routes);
        Image1 = new Image(SaveRouteHArrangement);
        SaveNew = new Button(SaveRouteHArrangement);
        SaveNew.Text("SAVE");
        Image2 = new Image(SaveRouteHArrangement);
        NewRouteWeb = new Web(Routes);
        Notifier1 = new Notifier(Routes);
        Notifier2 = new Notifier(Routes);
        GetRouteWeb = new Web(Routes);
        Web3 = new Web(Routes);

        EventDispatcher.registerEventForDelegation( this, "button_MainMenu", "Click");

    }

    public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] params) {
        if (component.equals(MainMenu) && eventName.equals("Click")) {

            ActivityStarter nextScreen = new ActivityStarter(this);
            nextScreen.ActivityClass("net.fachtnaroe.tuuber.screen03_MainMenu");
            nextScreen.ActivityPackage("net.fachtnaroe.tuuber");
            nextScreen.StartActivity();
            return true;
        }
        return true;
    }
}
