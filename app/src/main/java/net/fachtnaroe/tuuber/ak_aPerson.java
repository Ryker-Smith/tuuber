package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.TextBox;

import static java.lang.System.out;

public class ak_aPerson {
    public String First;
    public String Family;
    public String email;
    public String phone;
    public Integer pID;

    public ak_aPerson() {
        // The constructor
    }

    public boolean validEmail (TextBox DebugBox) {
        String temp;
        temp = email;
        Integer atCount=0;
        Integer spaceCount=0;
        Integer loop;
        for (loop = 0; loop <= email.length() -1; loop++) {
            Character a=temp.toCharArray()[loop];
            DebugBox.Text( DebugBox.Text().concat(" "+a.toString()));
            if (temp.toCharArray()[loop] == '@') {
                atCount++;
            }
            if (temp.toCharArray()[loop] == ' ') {
                spaceCount++;
            }
        }
        if (atCount != 1) {
            return false;
        }
        if (spaceCount != 0) {
            return false;
        }
        return true;
    }

    public  boolean validPhone () {
        return true;
    }
}

