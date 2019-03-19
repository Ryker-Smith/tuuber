package net.fachtnaroe.tuuber;

import android.graphics.Color;

import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Label;

import static com.google.appinventor.components.runtime.Component.BUTTON_SHAPE_ROUNDED;

public class tuuberCommonSubroutines {

    tuuber_Settings applicationSettings;


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

    public void dbg (String debugMsg) {
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

}
