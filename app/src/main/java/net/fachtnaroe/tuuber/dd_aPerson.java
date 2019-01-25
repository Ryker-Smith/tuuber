package net.fachtnaroe.tuuber;

public class dd_aPerson {
    String First;
    String Family;
    String eMail;
    String phone;

    public boolean valid_eMail() {
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
            char c=phone.charAt(i);
            //character checked if non-numeric
            if ((((int) c) <48) || (((int)c) >57)) {
                return false;

            }
        }
        return true;
    }
}
