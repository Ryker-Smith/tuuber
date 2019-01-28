package net.fachtnaroe.tuuber;

public class dd_aPerson {
    String First;
    String Family;
    String eMail;
    String phone;


    public boolean valid_eMail() {
        dbg("In Routine");
        int atSymbolCount = 0;
        int dotCount = 0;

        String theEmailAddressToTest= new String();
        theEmailAddressToTest=this.eMail;
        String currentString = theEmailAddressToTest;
        String[] separated = currentString.split("@");
        if (separated[0].length() <1) {
            return false;
        }
        for (int a = 0; a < theEmailAddressToTest.length(); a++) {
//            System.out.print(theEmailAddressToTest.toCharArray()[a]);
            // character presence checked
            if (theEmailAddressToTest.charAt(a) == '@'){
                atSymbolCount++;
            }
            if (theEmailAddressToTest.charAt(a) == '.'){
                dotCount++;
            }
        }
        if (atSymbolCount !=1) {
//            dbg("In OK");
            return false;
        }
        if (dotCount <1) {
//            dbg("In OK");
            return false;
        }
        return true;
    }

    public boolean valid_phone() {
        if (phone.length() <= 8) {
            return false;
        }
        else if (phone.length() >= 20) {
            return false;
        }
        int i;
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

    void dbg (String debugMsg) { System.err.print( debugMsg + "\n"); }
}
