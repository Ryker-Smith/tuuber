package net.fachtnaroe.tuuber;

import android.graphics.Color;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.TextBox;

import static com.google.appinventor.components.runtime.Component.BUTTON_SHAPE_ROUNDED;
import static com.google.appinventor.components.runtime.Component.COLOR_NONE;
import static com.google.appinventor.components.runtime.Component.DEFAULT_VALUE_COLOR_NONE;

public class tuuberCommonSubroutines {

    tuuber_Settings applicationSettings;
    public final Integer role_Tester=2;
    public final Integer role_Developer=4;
    public final Integer role_Administrator=128;


    public tuuberCommonSubroutines(ComponentContainer screenName){
        applicationSettings = new tuuber_Settings(screenName);
        applicationSettings.get();
    }

    public Label fn_HeadingLabel(ComponentContainer parent, Label lbl, String pID, String heading) {
        lbl = new Label(parent);
        lbl.HTMLFormat(true);
        lbl.Text("<small>I am user: #" + pID + "</small><br><small><small>"+heading+"</small></small>");
        lbl.Height(40);
        lbl.FontSize(20);
        lbl.WidthPercent(70);
        lbl.TextAlignment(Component.ALIGNMENT_CENTER);
        return lbl;
    }

    public static void dbg (String debugMsg) {
        System.err.print( "~~~> " + debugMsg + " <~~~\n");
    }

    void button_CommonFormatting(Integer int_ColWidthPercent, Button... b) {
        // This function takes a list of TextBox'es and sets them to 100% width
        // Other common applicationSettings may be applied this way.
        int i=0;
        int len = b.length;
//        https://stackoverflow.com/questions/4427608/android-getting-resource-id-from-string
        while ((i < len) && (b[i] != null)) {
            if (int_ColWidthPercent != null) {
                b[i].WidthPercent(int_ColWidthPercent);
            }
            b[i].BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
            b[i].FontBold(true);
            b[i].TextColor(Component.COLOR_WHITE);
            b[i].Shape(BUTTON_SHAPE_ROUNDED);
            b[i].FontSize(applicationSettings.int_ButtonTextSize);
            b[i].Height(40);
            b[i].Column(1);
            i++;
        }
    }

    Object padding(ComponentContainer c, Integer w, Integer h) {
        Label tmp=new Label(c);
        tmp.Width(w);
        tmp.Height(h);
        tmp.BackgroundColor(Component.COLOR_NONE);
        tmp.Text("");
        return tmp;
    }

    void buttonOnOff(Button b, boolean On) {
        if (On) {
            //trurn button on
            b.TextColor(Color.parseColor(applicationSettings.string_TextColor));
            b.BackgroundColor(Color.parseColor(applicationSettings.string_ButtonColor));
            b.FontBold(true);
            b.Enabled(true);
        }
        else {
            // turn button off
            b.TextColor(Color.parseColor(applicationSettings.string_ButtonColor));
            b.BackgroundColor(Component.COLOR_NONE);
            b.FontBold(false);
            b.Enabled(false);
        }
    }

    void fn_SwapStrings (String first, String second) {
        String temp=first;
        first=second;
        second=temp;
    }

    String fn_téacs_aistriú(String t){
        // Purpose: takes a string, and matches it to text for
        // the corresponding language
        String translation;//=applicationSettings.messages.get(t.toLowerCase());
        if (applicationSettings.messages.containsKey(t.toLowerCase())) {
            translation=applicationSettings.messages.get(t.toLowerCase());
        }
        else {
            translation = applicationSettings.messages.get("error");
        }
        return translation;
    }

    String fn_téacs_aistriú(String t, Integer formatting){
        final Integer capitalize =2;
        final Integer capitalize_each =4;
        String translation=applicationSettings.messages.get(t);
        if (formatting == capitalize) {

        }
        else if (formatting == capitalize_each) {

        }
        return translation;
    }

    boolean binary_same_as(Integer first, Integer second) {
//        dbg("Compare: "+first.toString() + " " + second.toString());
        if ((first & second) == second) {
            return true;
        } else {
            //dbg("false" + Integer.toString(first & second));
            return false;
        }
    }
}
