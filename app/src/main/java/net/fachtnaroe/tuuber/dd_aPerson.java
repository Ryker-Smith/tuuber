package net.fachtnaroe.tuuber;

public class dd_aPerson {
    String First;
    String Family;
    String eMail;
    String phone;

    public boolean valid_eMail(char d) {
        int count = 0;

        for(int a=0; a < eMail.length(); a++) {
            if(eMail.charAt(a) == d)
                count++;
        }
        return false;
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
}
