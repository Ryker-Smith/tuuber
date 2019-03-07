package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.TinyDB;

public class dd_aPerson {

    public String First = "";
    public String Family = "";
    public String eMail = "";
    public String phone = "";
    public String password = "";

    public String default_First = "";
    public String default_Family = "";
    public String default_eMail = "";
    public String default_phone = "";
    public String default_password = "";

    TinyDB localDB;

    public boolean valid_eMail() {
        int atSymbolCount = 0;
        int dotCount = 0;

        String theEmailAddressToTest= new String();
        theEmailAddressToTest=this.eMail;
        String currentString = theEmailAddressToTest;
        //string is split
        String[] separated = currentString.split("@");
        if (separated.length < 2) {
            return false;
        }
        // string checked for length
        if (separated[0].length() <1) {
            return false;
        }
        //string checked for length
        if (separated[1].length() <3) {
            return false;
        }
        //loop created
        for (int b = 0; b < theEmailAddressToTest.length(); b++) {
            //character presence checked
            if (theEmailAddressToTest.charAt(b) == '@'){
                atSymbolCount++;
            }
        }
        //loop created
        for (int a = 0; a < separated[1].length(); a++) {
//            System.out.print(theEmailAddressToTest.toCharArray()[a]);
            // character presence checked
            if (separated[1].charAt(a) == '.'){
                dotCount++;
            }
        }
        // specific character count checked
        if (dotCount <1) {
            return false;
        }
        // specific character count checked
        if (atSymbolCount <1) {
            return false;
        }
        return true;
    }

    public boolean valid_phone() {
        // checked if too short
        if (phone.length() <= 8) {
            return false;
        }
        // checked if too long
        else if (phone.length() >= 20) {
            return false;
        }
        int i;
        // loop created
        for(i=0;i<=phone.length()-1;i++){
            //character checked individually
            char c=phone.charAt(i);
            //character checked if non-numeric
            if ((((int) c) <48) || (((int)c) >57)) {
                return false;

            }
        }
        //phone is valid here
        return true;
    }
    public boolean valid_first() {
        // checks length
        if (First.length() <= 1) {
            return false;
        }
        int i;
        // loop created
        for(i=0;i<=First.length()-1;i++){
            //character checked individually
            char c=First.charAt(i);
            //character checked if numeric
            if ((((int) c) >48) || (((int)c) <57)) {
                return false;
            }
        }
        return true;
    }
    public boolean valid_family() {
        if (Family.length() <= 1) {
            return false;
        }
        int i;
        // loop created
        for(i=0;i<=Family.length()-1;i++) {
            //character checked individually
            char c = Family.charAt(i);
            //character checked if numeric
            if ((((int) c) > 48) || (((int) c) < 57)) {
                return false;
            }
        }
        return true;
    }
    public boolean valid_password() {
        if (password.length() <= 1) {
            return false;
        }
        return true;
    }

    public dd_aPerson(ComponentContainer screenName) {
        localDB= new TinyDB(screenName);
        First=default_First;
        Family=default_Family;
        eMail=default_eMail;
        phone=default_phone;
        password=default_password;
    }
    
    public String get () {
        First=(String) localDB.GetValue("First",default_First);
        Family=(String) localDB.GetValue("Family",default_Family);
        eMail=(String) localDB.GetValue("eMail", default_eMail);
        phone=(String) localDB.GetValue("phone", default_phone);
        password=(String) localDB.GetValue("pID",default_password);
        return "OK";
    }
    //stores the values
    public String set () {
        localDB.StoreValue("First", First);
        localDB.StoreValue("Family",Family);
        localDB.StoreValue("eMail",eMail);
        localDB.StoreValue("phone", phone);
        localDB.StoreValue("password", password);
        return "OK";
    }

    void dbg (String debugMsg) { System.err.print( debugMsg + "\n"); }
}
