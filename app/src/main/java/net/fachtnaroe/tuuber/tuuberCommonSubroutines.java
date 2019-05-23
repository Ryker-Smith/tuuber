package net.fachtnaroe.tuuber;

import android.graphics.Color;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.TextBox;

import static com.google.appinventor.components.runtime.Component.BUTTON_SHAPE_ROUNDED;
import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.text.WordUtils;

public class tuuberCommonSubroutines {

    tuuber_Settings applicationSettings;
    public final Integer role_Tester=2;
    public final Integer role_Developer=4;
    public final Integer role_Administrator=128;
    public final Integer capitalize_none =0; // shouldn't do anything
    public final Integer capitalize_first =2;
    public final Integer capitalize_each =4;
    public final Integer capitalize_all =8;


    public tuuberCommonSubroutines(ComponentContainer screenName){
        applicationSettings = new tuuber_Settings(screenName);
        applicationSettings.get();
    }

    public Label fn_HeadingLabel(ComponentContainer parent, Label lbl, String pID, String heading) {
        lbl = new Label(parent);
        lbl.HTMLFormat(true);
        lbl.Text("<small>"+ fn_téacs_aistriú("i_am_user", capitalize_first)+": #" + pID + "</small><br><small><small>"+heading+"</small></small>");
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
        String translation;//=applicationSettings.notifier_Messages.get(t.toLowerCase());
        if (applicationSettings.messages.containsKey(t.toLowerCase())) {
            translation=applicationSettings.messages.get(t.toLowerCase());
        }
        else {
            translation = applicationSettings.messages.get("error");
        }
        return translation;
    }

    String fn_téacs_aistriú(String t, Integer formatting){
        // first get the text from other language
        String translation=fn_téacs_aistriú(t); // call 'tother version
        // then decide the capitalization action, as all translations are in lowercase
        if (binary_same_as(capitalize_first,formatting )) {
            return StringUtils.capitalize(translation);
        }
        else if (binary_same_as(capitalize_each,formatting )) {
            return WordUtils.capitalizeFully(translation);
        }
        else if (binary_same_as(capitalize_all,formatting )) {
            return translation.toUpperCase();
        }
        else if (binary_same_as(capitalize_none,formatting )) {
            return translation.toLowerCase();
        }
        return translation;
    }

    boolean binary_same_as(Integer first, Integer second) {
        if ((first & second) == second) {
            return true;
        } else {
            return false;
        }
    }
}
