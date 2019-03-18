package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Label;

public class tuuberCommonSubroutines {

    public tuuberCommonSubroutines(ComponentContainer screenName){
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
}
