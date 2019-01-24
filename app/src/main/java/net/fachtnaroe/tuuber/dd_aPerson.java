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
        if (phone.length() <= 9) {
            return false;
        }
        else if (phone.length() >= 11) {
            return false;
        }
        return true;
    }
}
