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
//import gnu.lists.FString;

// Research:  http://loopj.com/android-async-http/
// Research: https://hc.apache.org/httpcomponents-client-ga/


public class screen07_Routes extends Form implements HandlesEventDispatching {

    private Image TuberH, Image11, Image7, Image5, Image9, Image1, Image2;
    private Web NewRouteWeb, GetRouteWeb, Web3;
    private Notifier Notifier1, Notifier2;
    private Label pID, InfoLabel, DriverLabel, Route1Label, OriginLabel, Destination, CheckDaysLabel, Origin2Label, Destination2Label, MonYN, Mon2YN, TueYN, Tue2YN, WedYN, Wed2YN, ThursYN, Thurs2YN, FriYN, Fri2YN;
    private HorizontalArrangement MainMenuHArrangement, HorizontalArrangement8, HorizontalArrangement11, HorizontalArrangement7, HorizontalArrangement12, HorizontalArrangement9, HorizontalArrangement13, HorizontalArrangement5, SaveRouteHArrangement;
    private VerticalArrangement RoutesVArrangement, VerticalArrangement14, VerticalArrangement3, VerticalArrangement2, Routes;
    private Button MainMenu, SaveNew;
    private ListView TempList;
    private TextBox TownSingle, TownsDecoded, DriverYN;
    private ListPicker OriginList, DestinationList, OriginList2, DestinationList2;
    private CheckBox Mon, Tue, Wed, Thurs, Fri, Mon2, Tue2, Wed2, Thurs2, Fri2;

    protected void $define() {

        Routes = new VerticalArrangement(this);
        TuberH = new Image(Routes);
        MainMenuHArrangement = new HorizontalArrangement(Routes);
        pID = new Label(MainMenuHArrangement);
        MainMenu = new Button(MainMenuHArrangement);

    }
}
