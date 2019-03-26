package net.fachtnaroe.tuuber;

import static java.lang.System.out;

public class ak_aPerson {
    public String First;
    public String Family;
    public String email;
    public String phone;
    public Integer pID;
    private final Integer minPhoneSize = 8;

    public ak_aPerson() {
        // The constructor
    }

    public boolean validEmail() {
        int symbol= 0;
        String temp = this.email;
        Integer loop;
        for (loop = 0; loop <= email.length(); loop++) {
            System.out.print(temp.toCharArray()[loop]);
            if (temp.toCharArray()[loop] == '@'){
                symbol = symbol+1;
            }
        }
        if (symbol != 1 ){
            return false;
        }
        return true;
    }

    boolean validPhone() {
        String temp = this.phone;
        if (temp.length() <= minPhoneSize) {
            return false;
        }
        int loop;
        for (loop = 0; loop <= temp.length() - 1; loop++) {
            if ((temp.toCharArray()[loop] < 48) || (temp.toCharArray()[loop] > 57)) {
                return false;
            }
        }
        return true;
    }
}

